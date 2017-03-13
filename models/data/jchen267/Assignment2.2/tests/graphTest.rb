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


  def test_to_s
    puts('Require manual testing for to_s')
    @graph.add_node(node0 = Node.new(Film.new("someFilm")))
    puts(@graph.to_s)
  end

end