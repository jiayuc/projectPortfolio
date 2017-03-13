require 'test/unit'
require_relative '../src/scrapper/Actor'


class ActorTest < Test::Unit::TestCase

  # Called before every test method runs. Can be used
  # to set up fixture information.
  def setup
    @actor = Actor.new
  end

  # Test init method functions
  def test_init
    assert_equal(@actor.name, 'unnamed')
    assert_equal(@actor.age, -1)
    @actor = Actor.new('randomName', 89)
    assert_equal(@actor.name, 'randomName')
    assert_equal(@actor.age, 89)
  end

  # Test add film works correctly
  def test_add_film
    @actor.add_film('someFilm', 999)
    assert_equal(@actor.filmToGrossing['someFilm'], 999)
    @actor.add_film('someFilm2', 888)
    assert_equal(@actor.filmToGrossing['someFilm2'], 888)
    @actor.print
  end

end