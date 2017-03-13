require 'set'
require 'pp'

class Film
  attr_accessor :name, :year, :actor_to_grossing, :wiki, :box_office

  # Initialize an instance
  # @param name [String] @name of the actor to set
  # @param year [Fixnum] age of the actor to set
  def initialize (name='unnamed', year=-1, wiki='', box_office=0)
    @name, @year, @wiki, @box_office = name, year, wiki, box_office
    @actor_to_grossing = Hash.new
  end

  def add_actor (actorName, grossing=0)
    @actor_to_grossing[actorName] = grossing
  end

  def get_cast_members
    @actor_to_grossing.each do |actor_name, grossing|
      puts('Cast member: ' + actor_name + ', grossing: ' + grossing.to_s)
    end
    return @actor_to_grossing
  end

  # Print the content of the instance
  def print
    puts('Film [' + @name + '] in year ' + @year.to_s + ' box office: ' + @box_office.to_s + "\n")
    get_cast_members
  end

  # Print the content of the instance
  def to_s
    #ret = 'Film ' + @name + "\n"
    ret = 'Film [' + @name + '] in year ' + @year.to_s + ' box office: ' + @box_office.to_s + "\n"
    # pp(actorToGrossing)
    return ret
  end


end