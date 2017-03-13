require 'test/unit'
require_relative '../src/graph/graph.rb'
require_relative '../src/graph/edge.rb'
require_relative '../src/graph/node.rb'
require_relative '../src/scrapper/Actor'
require_relative '../src/scrapper/Film'

class GraphTest < Test::Unit::TestCase

  # Called before every test method runs. Can be used
  # to set up fixture information.
  def setup
    # Do nothing
    @graph = Graph.new
  end

  # Test init method works correctly
  def test_init
    @graph.add_node(node0 = Node.new(Film.new('testFilm')))
    pp(@graph.nodes)
  end

  # Test bfs works correctly
  def test_bfs
    @graph.add_node(node0 = Node.new(Film.new('testFilm1')))
    @graph.add_node(node1 = Node.new(Film.new('testFilm2')))
    @graph.add_node(node2 = Node.new(Film.new('testFilm3')))
    @graph.add_node(node3 = Node.new(Film.new('testFilm4')))
    @graph.add_node(node4 = Node.new(Film.new('testFilm5')))
    @graph.add_node(node5 = Node.new(Film.new('testFilm6')))
    @graph.add_node(node6 = Node.new(Film.new('testFilm7')))
    @graph.add_node(node7 = Node.new(Film.new('testFilm8')))

    @graph.add_edge(node0, node1, 6)
    @graph.add_edge(node1, node3, 7)
    @graph.add_edge(node2, node4, 8)
    @graph.add_edge(node4, node5, 9)
    @graph.add_edge(node2, node5, 10)
    @graph.add_edge(node4, node6, 11)
    @graph.add_edge(node6, node7, 12)
    @graph.bfs
  end

  def test_to_s
    puts('Require manual testing for to_s')
    @graph.add_node(node0 = Node.new(Film.new("someFilm")))
    puts(@graph.to_s)
  end

end