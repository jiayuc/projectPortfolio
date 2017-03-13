require 'set'
require 'pp'

class Actor
  attr_accessor :name, :age, :filmToGrossing, :total_gross

  # Initialize an instance
  # @param name [String] @name of the actor to set
  # @param age [Fixnum] age of the actor to set
  def initialize (name='unnamed', age=12, total_gross=0)
    @name, @age, @total_gross = name, age, total_gross
    @filmToGrossing = Hash.new
  end

  def add_film (filmname, grossing=0)
    @filmToGrossing[filmname] = grossing
  end

  def get_films_in
    @filmToGrossing.each do |film_name, grossing|
      puts('Film in: ' + film_name + ', grossing: ' + grossing.to_s)
    end
    return @filmToGrossing
  end

  # Print the content of the instance
  def print
    puts('Actor ' + @name + ': age of ' + @age.to_s + "\n")
    get_films_in
  end

  # Print the content of the instance
  def to_s
    # TODO return_message.to_json uses to_s
    'Actor ' + @name + ': age of ' + @age.to_s + ': total_gross of ' + @age.to_s + "\n"
  end


end