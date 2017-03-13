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
    # if (find_node(node.id).nil?)
    #   # TODO: Consider merge node information
    #   return
    # end
    @nodes << node
    node.graph = self
    return node
  end

  # @!method Add a new edge to the graph
  # @param [Node] node on one end of the edge
  # @param [Node] node on another end of the edge
  # @param [Fixnum] weight of the edge
  def add_edge(from, to, weight=0)
    edges << Edge.new(from, to, weight)
    from.adjacents.push(to)
    to.adjacents.push(from)
  end


  # @!method Find node with the matching node_name
  # @param [String] id of the node object
  def find_node(node_id)
    @nodes.each do |n|
      return n if n.id == (node_id) # TODO! consider if should improve
    end
    return nil
  end

  def remove_node(node_id)
    @nodes.delete_if{|n| n.id == node_id}
  end

  def find_node_idx(node_id)
    @nodes.each_with_index do |n, i|
      return i if n.id == (node_id) # TODO! consider if should improve
    end
    return nil
  end

  # @!method Print the instance
  def print
    puts('Nodes of size ' + @nodes.size.to_s)
    @nodes.each do |n|
      n.content.print
    end
    @edges.each do |e|
      puts e.to_s
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

end