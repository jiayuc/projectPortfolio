package membership

import (
	"bufio"
	"fmt"
	"github.com/jiayuc/util"
	"io/ioutil"
	"math/rand"
	"net"
	"strconv"
	"strings"
	"sync"
	"unsafe"
)

// messageType: used integer to represent different type
type msgType uint8

const (
	//udp
	pingMsg msgType = iota
	pingReqMsg
	ackMsg
	ackReqMsg

	//tcp
	listReqMsg
	dieMsg
	joinMsg

	udpBSize = 4080
	kMachine = 2
)

// get updated list from contactor, append into nodes
func (m *MembershipList) syncList(contactIP string, contactPort string) {
	conn, err := net.Dial("tcp", contactIP+":"+contactPort)
	util.Check(err, "tcp")
	defer conn.Close()
	str := m.encodeSelfMsg(listReqMsg)
	fmt.Fprintf(conn, str)
	msg, err := ioutil.ReadAll(conn)
	if err != nil {
		fmt.Println("error while reading result from server")
	} else if len(msg) > 0 {
		// fmt.Println("* Message from server: " + string(msg))
		m.parseListStr(string(msg))
	}
	m.Logger.Println("[ Sync list ] received listStr", msg)
}

func (m *MembershipList) UdpPingThread(wg *sync.WaitGroup) {
	m.timeline.Lock()
	m.timeline.InsertEvent(&Event{IP: "", PortNumTCP: "", PortNumUDP: "", triedTime: 0, nodeTimestamp: ""})
	m.timeline.Unlock()
	for {
		//get current time
		beginTime := util.GetTimeMilli()
		m.Logger.Println("UdpPingThread wake up at " + strconv.Itoa(int(beginTime)) + "ms")

		//pop from timeline, as long as we are past due
		m.timeline.Lock()
		it := m.timeline.line.Front()
		// fmt.Println((m.timeline.line.Len()))
		//if we are earlier than due, let's wait
		if it.Value.(*Event).due > beginTime {
			due := m.timeline.line.Front().Value.(*Event).due
			m.timeline.Unlock()
			util.SleepMilli(due - util.GetTimeMilli())
			continue
		}
		event := m.timeline.line.Remove(it).(*Event) //the front event
		m.timeline.Unlock()

		//if triedTime == 0:
		//	if node ip not empty, ping it, increment triedTime, and insert new due in timeline
		//	no matter what, randomly select a machinee, schedule next ping
		if event.triedTime == 0 {
			if event.IP != "" {
				//prepare msg for UDP
				msg := strconv.Itoa(int(pingMsg)) + ":" + event.IP + ":" +
					event.PortNumTCP + ":" + event.PortNumUDP + ":" +
					m.selfNode.IP + ":" + m.selfNode.PortNumTCP + ":" +
					m.selfNode.PortNumUDP

				buf := append(util.IntToByte(beginTime), msg...)

				//send UDP out
				LocalAddr, err := net.ResolveUDPAddr("udp",
					m.selfNode.IP+":"+"0")
				util.Check(err, "udp local resolve"+m.selfNode.IP+":"+"0")

				ServerAddr, err := net.ResolveUDPAddr("udp",
					event.IP+":"+event.PortNumUDP)
				util.Check(err, "udp server resolve "+event.IP+":"+event.PortNumUDP)

				conn, err := net.DialUDP("udp", LocalAddr, ServerAddr)
				util.Check(err, "udp failed ")

				conn.Write(buf)
				conn.Close()

				//increment and insert
				event.triedTime = event.triedTime + 1
				event.pingTimestamp = beginTime
				event.due = beginTime + pingTimeout
				m.timeline.Lock()
				m.timeline.InsertEvent(event)
				m.timeline.Unlock()
			}
			//randomly select a machine, prepare a future ping Event
			//careful about the lock order. Prevent deadlock
			m.nodesLock.Lock()
			m.timeline.Lock()

			pingEvent := &Event{}
			length := len(m.nodes)
			if length != 0 {
				node := m.nodes[rand.Int()%length]
				pingEvent = pingEventGen(node, beginTime, beginTime+roundTime, 0)
			} else {
				pingEvent = pingEventGen(nil, beginTime, beginTime+roundTime, 0)
			}

			m.timeline.InsertEvent(pingEvent)

			m.timeline.Unlock()
			m.nodesLock.Unlock()
		} else {

			//if triedTime != 0:
			//  if triedTime < MAX, pingReq it, increment triedTime, and insert new due in timeline
			//  remove the Event, and remove the machine, it is dead
			if event.triedTime >= tryTimeMax { // it is dead

				m.Logger.Println("[ UDP Sender ] Found dead node!!!")
				fmt.Println("[ UDP Sender ] Found dead node!!!")
				node := &NodeInfo{IP: event.IP, PortNumTCP: event.PortNumTCP,
					PortNumUDP: event.PortNumUDP, Timestamp: event.nodeTimestamp}
				fmt.Println("removing node: " + m.showNode(node))
				success := m.removeNode(node)
				if success == 1 { //first time removal, broadcast found failure
					m.broadcastMsg(m.encodeDieMsg(node))
				}
			} else { //pingReq it
				m.Logger.Println("[ UDP Sender ] Suspect node!!!")
				fmt.Println("[ UDP Sender ] Suspect node!!!")
				targetNode := &NodeInfo{IP: event.IP, PortNumTCP: event.PortNumTCP,
					PortNumUDP: event.PortNumUDP, Timestamp: ""}

				m.nodesLock.RLock()
				nodes := m.selectKNodes(kMachine)
				for _, node := range nodes {
					msg := m.encodeMsg3(pingReqMsg, targetNode, m.selfNode, node)
					m.sendUdpMsg(node, msg, util.IntToByte(beginTime))
					m.Logger.Println("[ UDP Sender ] Pingreq sent to " + m.showNode(node))
				}
				m.nodesLock.RUnlock()

				event.triedTime = event.triedTime + 1
				event.pingTimestamp = beginTime
				event.due = beginTime + pingTimeout*4

				m.timeline.Lock()
				m.timeline.InsertEvent(event)
				m.timeline.Unlock()
			}
		}

		//sleep until next Event in timeline
		m.timeline.RLock()
		due := m.timeline.line.Front().Value.(*Event).due
		m.timeline.RUnlock()
		util.SleepMilli(due - util.GetTimeMilli())
	}
}

func pingEventGen(node *NodeInfo, pingTimestamp int64, due int64, triedTime int) *Event {
	pingEvent := &Event{due: due, IP: "",
		PortNumTCP: "", PortNumUDP: "",
		pingTimestamp: pingTimestamp,
		triedTime:     triedTime, nodeTimestamp: ""}
	if node != nil {
		pingEvent.IP = node.IP
		pingEvent.PortNumTCP = node.PortNumTCP
		pingEvent.PortNumUDP = node.PortNumUDP
		pingEvent.nodeTimestamp = node.Timestamp
	}
	return pingEvent
}

func (m *MembershipList) UdpListenThread(wg *sync.WaitGroup) {
	if wg != nil {
		defer wg.Done()
	}
	m.udpListener = &net.UDPConn{}
	portN, _ := strconv.Atoi(m.selfNode.PortNumUDP)
	udpAddr := &net.UDPAddr{IP: net.ParseIP(m.selfNode.IP), Port: portN}
	udpLn, err := net.ListenUDP("udp", udpAddr)
	if err != nil {
		fmt.Println("Failed to start UDP listener\n", err)
		return
	}
	defer udpLn.Close()
	fmt.Println("Listening udp === ")
	buf := make([]byte, udpBSize)
	for {
		n, addr, err := udpLn.ReadFromUDP(buf)
		var tmp int64 = 9
		pingTimeStampSize := unsafe.Sizeof(tmp) //Event.pingTimestamp

		m.Logger.Println(" [[ UDP Received ]]", string(buf[pingTimeStampSize:n]), " from ", addr)
		m.handleUdpMsg(string(buf[pingTimeStampSize:n]), buf[0:pingTimeStampSize], addr)
		if err != nil {
			fmt.Println("Error: ", err)
		}
	}
}

// handle single udp msg

func (m *MembershipList) handleUdpMsg(msg string, msgTimeStamp []byte, addr *net.UDPAddr) {
	//parse/decode request message
	split := strings.SplitAfterN(msg, ":", 2)
	typestr := split[0]
	typestr = typestr[:len(typestr)-1] //remove ':'
	content := split[1]
	// no newline char jiayu thinks
	node1, node2, node3 := m.parseUdpMsg(content)
	m.Logger.Println(node1, node2, node3)

	//cast string into uint8
	msgtype, err := strconv.ParseUint(typestr, 10, 8)
	msgtype8 := msgType(msgtype)
	util.Check(err, "cannot parse "+msg)

	switch msgtype8 {

	case pingMsg:
		m.Logger.Println("[ UDP pingMsg received ] : " + content)
		// respond ack to node2
		msg := m.encodeMsg(ackMsg, node1, node2)
		m.sendUdpMsg(node2, msg, msgTimeStamp)

		//m.replyUdpMsg(addr, msg, msgTimeStamp)
		m.Logger.Println("[ UDP ackMsg sent ] on receiving pingMsg")

	case pingReqMsg:
		m.Logger.Println("[ UDP pingReqMsg received ] : " + content)
		//if node1 matches myself, return ackReqMsg
		m.nodesLock.RLock()
		if node1.IP == m.selfNode.IP && node1.PortNumTCP == m.selfNode.PortNumTCP &&
			node1.PortNumUDP == m.selfNode.PortNumUDP {
			m.nodesLock.RUnlock()
			ackReqReply := m.encodeMsg(ackReqMsg, node1, node2)
			//m.replyUdpMsg(addr, ackReqReply, msgTimeStamp)
			m.sendUdpMsg(node3, ackReqReply, msgTimeStamp)
		} else {
			m.nodesLock.RUnlock()
			m.sendUdpMsg(node1, msg, msgTimeStamp)
		}

	case ackMsg:
		m.Logger.Println("[ UDP ackMsg received ] : " + content)
		m.nodeAcked(node1, msgTimeStamp)

	case ackReqMsg:
		m.Logger.Println("[ UDP ackReqMsg received ] : " + content)
		//if I am the final receiver
		if node2.IP == m.selfNode.IP && node2.PortNumTCP == m.selfNode.PortNumTCP &&
			node2.PortNumUDP == m.selfNode.PortNumUDP {
			m.nodeAcked(node1, msgTimeStamp)
		} else {
			m.sendUdpMsg(node2, msg, msgTimeStamp)
		}

	default:
		m.Logger.Println("[ UDP unknown msg received ] not a UDP type: " + content)
	}

}

func (m *MembershipList) nodeAcked(node *NodeInfo, msgTimeStamp []byte) {
	ackEvent := &Event{IP: node.IP, PortNumTCP: node.PortNumTCP,
		PortNumUDP: node.PortNumUDP, pingTimestamp: util.ByteToInt(msgTimeStamp)}

	m.timeline.Lock()
	m.timeline.removePingedNode(ackEvent)
	m.timeline.Unlock()
}

func (m *MembershipList) replyUdpMsg(addr *net.UDPAddr, msg string, msgTimeStamp []byte) {

	buf := append(msgTimeStamp, msg...)

	//send UDP out
	LocalAddr, err := net.ResolveUDPAddr("udp",
		m.selfNode.IP+":"+"0")
	util.Check(err, "udp local resolve"+m.selfNode.IP+":"+"0")

	ServerAddr := addr

	conn, err := net.DialUDP("udp", LocalAddr, ServerAddr)
	util.Check(err, "udp failed ")

	conn.Write(buf)
	conn.Close()
}

func (m *MembershipList) sendUdpMsg(targetNode *NodeInfo, msg string, msgTimeStamp []byte) {
	buf := append(msgTimeStamp, msg...)

	//send UDP out
	LocalAddr, err := net.ResolveUDPAddr("udp",
		m.selfNode.IP+":"+"0")
	util.Check(err, "udp local resolve"+m.selfNode.IP+":"+"0")

	ServerAddr, err := net.ResolveUDPAddr("udp",
		targetNode.IP+":"+targetNode.PortNumUDP)
	util.Check(err, "udp server resolve "+targetNode.IP+":"+targetNode.PortNumUDP)

	conn, err := net.DialUDP("udp", LocalAddr, ServerAddr)
	util.Check(err, "udp failed ")

	conn.Write(buf)
	conn.Close()
}

// msg is in form  pongip:pongtcp:pongudp : pingip:pingTcp:pingUdp
func (m *MembershipList) parseUdpMsg(msg string) (*NodeInfo, *NodeInfo, *NodeInfo) {
	strs := strings.Split(msg, ":")
	if len(strs) < 6 {
		print("lens not 6, is ", len(strs), "\n")
	}
	node1 := NodeInfo{}
	node2 := NodeInfo{}
	node3 := NodeInfo{}

	node1.IP = strs[0]
	node1.PortNumTCP = strs[1]
	node1.PortNumUDP = strs[2]
	node2.IP = strs[3]
	node2.PortNumTCP = strs[4]
	node2.PortNumUDP = strs[5]
	if len(strs) == 9 {
		node3.IP = strs[6]
		node3.PortNumTCP = strs[7]
		node3.PortNumUDP = strs[8]
	}

	return &node1, &node2, &node3
}

func (m *MembershipList) TcpListenThread(wg *sync.WaitGroup) {
	fmt.Println("Firing TCPlistening thread---")
	if wg != nil {
		defer wg.Done()

	}

	tcpAddr, err := net.ResolveTCPAddr("tcp", m.selfNode.IP+":"+m.selfNode.PortNumTCP)
	util.Check(err, "CreateMemList: bind to local")
	m.tcpListener, err = net.ListenTCP("tcp", tcpAddr)
	util.Check(err, "CreateMemList: TCP error")

	for {
		//get a TCP connection
		conn, _ := m.tcpListener.Accept()
		reader := bufio.NewReader(conn)
		writer := bufio.NewWriter(conn)

		//read request message
		input, err := reader.ReadString('\n')
		if err != nil || input == "" {
			break
		}

		//parse/decode request message
		split := strings.SplitAfterN(input, ":", 2)
		typestr := split[0]
		typestr = typestr[:len(typestr)-1] //remove ':'
		content := split[1]
		content = content[:len(content)-1] //remove '\n'

		//cast string into uint8
		msgtype, err := strconv.ParseUint(typestr, 10, 8)
		msgtype8 := msgType(msgtype)
		util.Check(err, "cannot parse "+input)

		//do the real work
		switch msgtype8 {

		case listReqMsg:
			m.Logger.Println("listReqMsg: " + input)
			writer.WriteString(m.getListStr())

		case dieMsg:
			m.Logger.Println("dieMsg: " + input)
			node := decodeMsg(content)
			print("try remove node: " + content)
			m.removeNode(&node)

		case joinMsg:
			m.Logger.Println("joinMsg: " + input)
			node := decodeMsg(content)
			m.Logger.Println("try to join: " + content)
			m.addNode(&node)

		default:
			m.Logger.Println("not a TCP type: " + input)
		}

		writer.Flush()
		conn.Close()
	}
}

func (m *MembershipList) getListStr() string {
	ret := ""
	m.nodesLock.RLock()
	for _, node := range m.nodes {
		ret += node.IP + ":" + node.PortNumTCP + ":"
		ret += node.PortNumUDP + ":" + node.Timestamp + ";"
	}
	m.nodesLock.RUnlock()
	return ret
}

// parse incoming and ADD TO NODES
func (m *MembershipList) parseListStr(list string) {
	split := strings.Split(list, ";")
	for _, entry := range split {
		if entry != "" {
			newN := decodeMsg(entry)
			m.addNode(&newN)
		}
	}
}

func (m *MembershipList) encodeMsg(mt msgType, first *NodeInfo, second *NodeInfo) string {
	return strconv.Itoa(int(mt)) + ":" +
		first.IP + ":" + first.PortNumTCP + ":" + first.PortNumUDP + ":" +
		second.IP + ":" + second.PortNumTCP + ":" + second.PortNumUDP
}

func (m *MembershipList) encodeMsg3(mt msgType, first *NodeInfo, second *NodeInfo, third *NodeInfo) string {
	return strconv.Itoa(int(mt)) + ":" +
		first.IP + ":" + first.PortNumTCP + ":" + first.PortNumUDP + ":" +
		second.IP + ":" + second.PortNumTCP + ":" + second.PortNumUDP + ":" +
		third.IP + ":" + third.PortNumTCP + ":" + third.PortNumUDP
}

func (m *MembershipList) encodeSelfMsg(mt msgType) string {
	return strconv.Itoa(int(mt)) + ":" +
		m.selfNode.IP + ":" + m.selfNode.PortNumTCP + ":" +
		m.selfNode.PortNumUDP + ":" + m.selfNode.Timestamp + "\n"
}

func (m *MembershipList) encodeDieMsg(node *NodeInfo) string {
	return strconv.Itoa(int(dieMsg)) + ":" + node.IP + ":" +
		node.PortNumTCP + ":" + node.PortNumUDP + ":" +
		m.selfNode.Timestamp + "\n"
}

func decodeMsg(msg string) NodeInfo {
	split := strings.SplitAfterN(msg, ":", 4)
	node := NodeInfo{}
	node.IP = split[0][:len(split[0])-1]
	node.PortNumTCP = split[1][:len(split[1])-1]
	node.PortNumUDP = split[2][:len(split[2])-1]
	node.Timestamp = split[3][:len(split[3])]
	return node
}