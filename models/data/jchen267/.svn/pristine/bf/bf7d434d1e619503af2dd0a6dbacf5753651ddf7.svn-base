require 'test/unit'
require_relative '../src/scrapper/Film'


class FilmTest < Test::Unit::TestCase

  # Called before every test method runs. Can be used
  # to set up fixture information.
  def setup
    @film = Film.new
  end

  # Test init method functions
  def test_init
    assert_equal(@film.name, 'unnamed')
    @film = Film.new('randomName', 2017)
    assert_equal(@film.name, 'randomName')
    assert_equal(@film.year, 2017)
  end

  # Test add actor works correctly
  def test_add_actor
    @film.add_actor('actor1', 999)
    assert_equal(@film.actor_to_grossing['actor1'], 999)
    @film.add_actor('actor2', 888)
    assert_equal(@film.actor_to_grossing['actor2'], 888)
    @film.print
  end

  # Test print works correctly
  def test_print
    @film.add_actor('actor1', 999)
    @film.add_actor('actor2', 888)
    puts('Require manual testing for to_s')
    puts(@film.actor_to_grossing.size)
    @film.print
  end

end