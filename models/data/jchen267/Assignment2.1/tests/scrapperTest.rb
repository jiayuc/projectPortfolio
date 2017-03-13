require 'test/unit'
require_relative '../src/scrapper/web_scrapper'

class ScrapperTest < Test::Unit::TestCase

  # set up fixture information.
  def setup
    @s = WebScrapper.new
  end

  # Test scrapper works correctly for film wiki page with cast list
  def test_scrap_film_with_cast_list
    film_name = 'Demolition (2015 film)'
    url_film = 'https://en.wikipedia.org/wiki/Demolition_(2015_film)'
    urls = @s.scrap(url_film)
    film = @s.get_film_by_name(film_name)
    assert_not_nil(film)
    assert_equal(film_name, film.name)
    cast_members = film.get_cast_members
    assert_equal(9, cast_members.length)
  end

  # Test scrapper works correctly for film wiki page without cast list
  def test_scrap_film_no_cast_list
    film_name = 'Gone with the Wind (film)'
    url_film = 'https://en.wikipedia.org/wiki/Gone_with_the_Wind_(film)#Casting'
    urls = @s.scrap(url_film)
    film = @s.get_film_by_name(film_name)
    assert_not_nil(film)
    assert_equal(film_name, film.name)
    cast_members = film.get_cast_members
    # assert_equal(9, cast_members.length)
  end

  # Test scrapper works correctly for actor wiki page with film table
  def test_scrap_actor_with_film_table
    actor_name = 'Keanu Reeves'
    url_actor = 'https://en.wikipedia.org/wiki/Keanu_Reeves#Film'
    urls = @s.scrap(url_actor)
    actor = @s.get_actor_by_name(actor_name)
    @s.print_all
    assert_not_nil(actor)
    assert_equal(actor_name, actor.name)
    films_in = actor.get_films_in
    # assert_equal(9, films_in.length)
  end

  # Test scrapper works correctly for actor wiki page with film list
  def test_scrap_actor_with_film_list
    actor_name = 'Johnny Depp'
    url_actor = 'https://en.wikipedia.org/wiki/Johnny_Depp'
    urls = @s.scrap(url_actor)
    actor = @s.get_actor_by_name(actor_name)
    @s.print_all
    assert_not_nil(actor)
    assert_equal(actor_name, actor.name)
    films_in = actor.get_films_in
    # assert_equal(45, films_in.length)
  end


end