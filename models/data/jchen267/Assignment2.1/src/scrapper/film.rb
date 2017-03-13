require 'set'
require 'pp'

class Film
  attr_accessor :name, :year, :actorToGrossing

  # Initialize an instance
  # @param name [String] @name of the actor to set
  # @param year [Fixnum] age of the actor to set
  def initialize (name='unnamed', year=-1)
    @name, @year = name, year
    @actorToGrossing = Hash.new
  end

  def add_actor (actorName, grossing=0)
    @actorToGrossing[actorName] = grossing
  end

  def get_cast_members
    @actorToGrossing.each do |actor_name, grossing|
      puts('Cast member: ' + actor_name + ', grossing: ' + grossing.to_s)
    end
    return @actorToGrossing
  end

  # Print the content of the instance
  def print
    puts('Film [' + @name + '] in year ' + @year.to_s + "\n")
    get_cast_members
  end

  # Print the content of the instance
  def to_s
    ret = 'Film ' + @name + "\n"
    pp(actorToGrossing)
    return ret
  end


end