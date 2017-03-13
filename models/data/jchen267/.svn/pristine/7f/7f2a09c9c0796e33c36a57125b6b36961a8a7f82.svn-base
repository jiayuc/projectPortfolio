require 'json'
class Graph
  attr_accessor :nodes, :edges, :visited, :edge_to

  # @!method Convert edge to string representation
  def initialize
    @nodes = []
    @edges = []
    @visited = Hash.new
    @edge_to = {}
  end

  # @!method Add a new node to the graph
  #  while making sure node is not pre-existing
  # @param [Node] node to add to the graph
  def add_node(node)
    if (find_node(node.id).nil? == false)
      # TODO: Consider merge node information
      return
    end
    @nodes << node
    node.graph = self
  end

  # @!method Add a new edge to the graph
  # @param [Node] node on one end of the edge
  # @param [Node] node on another end of the edge
  # @param [Fixnum] weight of the edge
  def add_edge(from, to, weight)
    edges << Edge.new(from, to, weight)
    from.adjacents << to
    to.adjacents << from
  end

  # @!method perform BFS on connected component with given node
  # @param [Node] node used as source node to start
  def bfs_component(node)
    # put the source node into a queue, mark it as visited
    queue = []
    queue << node
    @visited[node.id] = true

    # repeat until the queue is empty:
    # traverse the next level
    while queue.any?
      current_node = queue.shift # remove first element
      current_node.adjacents.each do |adjacent_node|
        next if @visited.include?(adjacent_node.id)
        queue << adjacent_node
        begin
          @visited[adjacent_node.id] = true
        rescue RuntimeError
          puts('Adjacent node: ' + adjacent_node.id)
        end
        @edge_to[adjacent_node] = current_node
      end
    end

  end

  # @!method Traverse the whole graph using BFS
  def bfs
    #  mark all nodes unvisited
    @visited = Hash.new
    @nodes.each do |n|
      @visited[n.id] = false
    end
    # bfs until
    @nodes.each do |n|
      if @visited[n.id] == false
        bfs_component(n)
      end
    end

  end

  # @!method Find node with the matching node_name
  # @param [String] id of the node object
  def find_node(node_id)
    @nodes.each do |n|
      return n if n.id.<=> (node_id) # TODO! consider if should improve
    end
    return nil
  end

  # @!method Print the instance
  def print
    puts('Nodes of size ' + @nodes.size.to_s )
    @nodes.each do |n|
      n.content.print
    end
  end

  # @!method Return string representation of the instance
  def to_s
    ret = 'In graph.to_s'
    @nodes.each do |n|
      ret = ret + n.content.to_s
    end
    return ret
  end

  def store_json_to_file(filename='graph.json')
    json = JSON.dump self
    puts JSON.pretty_generate(json)
    File.open(filename, 'w') {
        |file| file.write(json)
    }
  end

  def load_json_as_object(filename='graph.json')
    data = JSON.parse(json_data)
    residents = data['Resident'].map { |rd| Resident.new(rd['phone'], rd['addr']) }
  end


end