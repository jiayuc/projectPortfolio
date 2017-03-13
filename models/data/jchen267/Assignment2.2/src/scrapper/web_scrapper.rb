require 'logger'
require 'open-uri'
require 'Nokogiri'
require 'set'
require_relative 'Film'
require_relative 'Actor'
require_relative '../graph/edge.rb'
require_relative '../graph/node.rb'
require_relative '../graph/graph.rb'

ABS_PATH_PREFIX = 'C:/Users/jiayu/RubymineProjects/untitled/src/scrapper'
LOG_FILENAME = ABS_PATH_PREFIX + '/log/logfile.log'
NO_URL_CODE = -2
# urls
BASE_WIKI_URL = 'https://en.wikipedia.org'
START_URL_A = 'https://en.wikipedia.org/wiki/Morgan_Freeman'
START_URL_F = 'https://en.wikipedia.org/wiki/Demolition_(2015_film)'
TABLE_URL_I = 'https://en.wikipedia.org/wiki/Keanu_Reeves'

class WebScrapper
  attr_accessor :logger, :ff, :actors, :films, :graph

  # @!method initialize instance
  def initialize
    File.open(LOG_FILENAME, 'w')
    @logger = Logger.new(LOG_FILENAME)
    @logger.level = Logger::DEBUG
    @f = File.open(ABS_PATH_PREFIX + '/log/tmp.html', 'w')
    @ff = File.open(ABS_PATH_PREFIX + '/log/ret.html', 'w')
    @actors = Set.new
    @films = Set.new
    @graph = Graph.new
    # config ssl to solve sslErr on windows
    ENV['SSL_CERT_FILE'] = ABS_PATH_PREFIX + '/config/cacert.pem'
  end

  # @!method Scrap the website with given url
  # @param [String] url to start with
  def scrap(url = TABLE_URL_I)
    # Get html for reading
    doc = get_doc(url)
    # Parse differently according to the title
    title = doc.at_css('.firstHeading').inner_text.strip
    @logger.info('== Handling ' + title)
    if title.include?('film')
      puts('film')
      urls = parse_film_url(url, doc, title)
    else
      puts('actor')
      urls = parse_actor(url, doc, title)
    end

    return urls
  end

  # @!method Get document from the website with given url
  # @param [String] url to target
  def get_doc(url)
    @logger.info('Scrapping: ' + url + '...')
    begin
      html = open(url)
    rescue URI::InvalidURIError
      @logger.FATAL('Scrapper: InvalidURIError: ' + url)
      return
    end
    return Nokogiri::HTML(html)
  end

  # @!method Parse wiki page of an actor
  # @param [String] url to target
  # @param [Document] document of the target html
  # @param [String] name of the actor
  def parse_actor(url, doc, name)
    @logger.info('Actor proceeded: ' + name)
    # Find actor age
    bio_section = doc.css('.infobox.biography.vcard')
    if bio_section.empty?
      @logger.warn('In parse_actor: bio section not found.')
    else
      bio_section = bio_section.css('.noprint.ForceAgeToShow').inner_text.delete('()')
      age = bio_section.split(' ')[-1]
      @f.write(age)
    end
    # Record this actor if age info found
    if age.nil? == false
      actor = Actor.new(name, age)
      @actors.add(actor)
      @graph.add_node(Node.new(actor))
      # Find more urls to scrap
      urls = process_film_table(url)
      add_films_in(actor, urls)
    end

    return urls
  end

  # @!method Record films that this actor has been in
  # @param [Actor] actor we're interested in
  # @param [Array] urls of candidate film websites
  def add_films_in(actor, urls)
    @logger.info('Update films in for actor ' + actor.name)
    urls.each { |url|
      doc = get_doc(url)
      title = doc.at_css('.firstHeading').inner_text.strip
      if (title.include?('film)'))
        film = parse_film_url(url, doc, title, basic_parse=true)
        if (film.nil? == false)
          actor.add_film(title)
        end
      end
    }
  end

  # @!method Parse particular film url
  # @param [String] url of the website
  # @param [Array] urls of candidate film websites
  def parse_film_url(url, doc, name, basic_parse=false)
    @logger.info('Film processing: ' + name)
    urls = Array.new
    # Process doc to reduce noise
    processDocForFilm(doc)
    list_items = doc.css('ul li')
    # get basic film info without recursively research the cast members
    film = get_film_info(doc, name)
    if (film.nil? || basic_parse)
      return film
    end

    # Find cast members
    list_items.each { |item|
      if (item.inner_text.include?(' as '))
        a_elem = item.css('a')
        if (a_elem.nil?)
          @logger.warn('Cast member @content not found in tag a, improve accuracy.')
        else
          # handle case when cast member has no url
          if (a_elem.length==0)
            actor_name = item.inner_text.split(' as ')[0]
            film.add_actor(actor_name, NO_URL_CODE)
            @actors.add(Actor.new(actor_name, NO_URL_CODE))
            # handle case when cast member has url
          else
            actor_name = a_elem.attribute('title')
            @ff.print(actor_name)
            film.add_actor(actor_name.to_s)
            urls.push(BASE_WIKI_URL + a_elem.attribute('href'))
          end

        end
      end
    }
    # puts(urls)
    @films.add(film)
    @graph.add_node(Node.new(film))
    return urls
  end

  def get_film_info(doc, name)
    intro = doc.css('p')[0].text.split(' is a ')[1]
    candidate_year = intro.strip.split(' ')[0]
    if (is_valid_year(candidate_year))
      film = Film.new(name, candidate_year.to_i)
      @logger.info('Valid film: ' + name + 'in year ' + film.year.to_s)
      return film
    else
      @logger.warn('Invalid film: ' + name + ', perform manual check if this is correct')
      return nil
    end
  end

  # @!method Determine whether given string represents a valid year
  def is_valid_year(string)
    return /\A\d+\z/.match(string)
  end

  # @!method Eliminate irrelevant sections
  def processDocForFilm(doc)
    doc.search('.infobox.vevent').remove
    doc.search('.reflist').remove
    doc.search('#mw-panel').remove
  end


  # @!method try to find Film Table section
  # @return [Array] lists of url on success, null if not found
  def process_film_table(url)
    #   see if table is available
    urls = Array.new
    table_html = open(url)
    table_doc = Nokogiri::HTML(table_html)
    if table_doc.css('#Filmography').size != 0
      @logger.info('Succeed to locate film table from url: ' + url)
      tables = table_doc.css('table').css('.wikitable.sortable')
      # traverse trough urls from film table
      if tables.empty? == false
        rows = tables[0].css('tr')
        # iterate through rows to extract correct urls
        rows[1..-2].each do |row|
          hrefs = row.css('td a').map { |a|
            a['href'] if a['href'].match('/wiki/')
          }.compact.uniq

          hrefs.each do |href|
            urls.insert(-1, BASE_WIKI_URL + href)
            # @f.write( BASE_WIKI_URL + href + "/n")
          end
        end
      else
        @logger.warn('Film exists but no table? : ' + url)
      end

    else
      @logger.info('Failed to locate film table from url: ' + url)
      puts("no table")
    end

    return urls
  end

  # @!method Determine whether scrapping should stop
  # @return [Boolean] Indicate whether scrapped enough results
  def scrapped_enough
    return @actors.size > 1 && @films.size > 2
  end

  # @!method Get film by name
  # @param [String] name of the film to search for
  # @return [Film] film found
  def get_film_by_name(film_name)
    node = @graph.find_node(film_name)
    film = nil
    if (node.nil?)
      puts('No film of such name')
    else
      film = node.content
    end
    return film
  end

  # @!method Get actor by name
  # @param [String] name of the actor to search for
  # @return [Actor] actor found
  def get_actor_by_name (actor_name)
    node = @graph.find_node(actor_name)
    actor = nil
    if (node.nil?)
      puts('No film of such name')
    else
      actor = node.content
    end

    return actor
  end

  # @!method Print all actors and films
  def print_all
    puts('Scrapper: print_all----------------')
    puts('Actors of size ' + @actors.size.to_s + '; Films of size ' + @films.size.to_s)
    @actors.each do |a|
      # s.ff.write('Actor: ' + a + "/n")
      a.print
    end

    @films.each do |f|
      # s.ff.write('Film: ' + f + "/n")
      f.print
    end
  end
end


# entry point
if __FILE__ == $0
  s = WebScrapper.new()
  urls = s.scrap()

  # Continue scrapping until enough
  if s.scrapped_enough() == false
    urls.each do |u|
      s.scrap(u)
    end
  end
  s.print_all
  s.graph.print
end