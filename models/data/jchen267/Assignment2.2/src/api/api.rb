require_relative '../graph/edge.rb'
require_relative '../graph/node.rb'
require_relative '../graph/graph.rb'
require_relative '../scrapper/actor.rb'
require_relative '../scrapper/film.rb'
require_relative '../infoNetwork/info_network.rb'
require 'sinatra'
require 'pp'
require 'uri'

# perform filtering based on given actor info
# @param condition is the sub-rule
# @param result is the Hash to store actor_name -> actor_instance mapping
# @param graph is the Graph instance holding all data
def subfilter_actor(condition, result, graph)

  #remove the logical operator at the end
  cond_copy = String.new(condition)
  if cond_copy[-1] == '|'
    cond_copy.chop!
  elsif cond_copy[-1] == '&'
    cond_copy.chop!
  end

  #parse the rule
  split = cond_copy.split('=')
  tag = split[0]
  value = split[1].delete('"')

  result.clear
  graph.nodes.each do |n|
    if n.content.instance_of? Actor
      if tag == 'name'
        if n.content.name.include? value
          result[n.content.name] = n.content
        end
      end
      if tag == 'age'
        if n.content.age == value.to_i
          result[n.content.name] = n.content
        end
      end
    end
  end

end

# perform filtering based on given film info
# @param condition is the sub-rule
# @param result is the Hash to store film_name -> film_instance mapping
# @param graph is the Graph instance holding all data
def subfilter_film(condition, result, graph)

  #remove the logical operator at the end
  cond_copy = String.new(condition)
  if cond_copy[-1] == '|'
    cond_copy.chop!
  elsif cond_copy[-1] == '&'
    cond_copy.chop!
  end

  #parse the rule
  split = cond_copy.split('=')
  tag = split[0]
  value = split[1].delete('"')

  result.clear
  graph.nodes.each do |n|
    if n.content.instance_of? Film
      if tag == 'name'
        if n.content.name.include? value
          result[n.content.name] = n.content
        end
      end
      if tag == 'year'
        if n.content.year == value.to_i
          result[n.content.name] = n.content
        end
      end
    end
  end

end

# perform filtering based on rules
# @param condition is the sub-rule
# @param actor_not_film indicates whether is about actor
# @param graph is the Graph instance holding all data
def filter(condition, actor_not_film, graph)
  #break filtering condition into sub-rules
  sub_conditions = []
  condition.each_line('|') { |line|
    line.each_line('&') { |rule|
      sub_conditions << rule
    }
  }

  #items store accumulated result
  items = Hash.new
  #items_propose stores result for one sub-rule
  items_propose = Hash.new

  #filter for the first sub-rule
  if actor_not_film
    subfilter_actor(sub_conditions[0], items, graph)
  else
    subfilter_film(sub_conditions[0], items, graph)
  end


  #filter for all other sub-rules
  (1..sub_conditions.length-1).each { |i|
    if actor_not_film
      subfilter_actor(sub_conditions[i], items_propose, graph)
    else
      subfilter_film(sub_conditions[i], items_propose, graph)
    end
    #logical merge or join
    if sub_conditions[i-1][-1] == '|'
      #store all keys
      items_propose.each do |name, item|
        items[name] = item
      end
    elsif sub_conditions[i-1][-1] == '&'
      #only store shared keys
      tmp = Hash.new
      items.each do |name, item|
        if items_propose.has_key?(name)
          tmp[name] = item
        end
      end
      items = tmp
    end
  }
  return items
end

# find actor among all nodes of the graph
# @param item_name is the name of actor to find
# @param graph is the Graph instance holding all data
def find_actor(item_name, graph)
  item_name.gsub!('_', ' ')

  graph.nodes.each do |n|
    if n.content.instance_of? Actor
      if n.content.name == item_name
        return n.content
      end
    end
  end
end

# find film among all nodes of the graph
# @param item_name is the name of film to find
# @param graph is the Graph instance holding all data
def find_film(item_name, graph)
  item_name.gsub!('_', ' ')

  pp item_name
  graph.nodes.each do |n|
    if n.content.instance_of? Film
      if n.content.name == item_name
        return n.content
      end
    end
  end
  return nil
end

# entry point
if __FILE__ == $0
  net = InfoNetwork.new
  filename = '../resource/data.json'
  net.build_network_from_json(filename)

  set :port, 2345
  set :environment, :production

  # == GET requests
  # GET request with Actor related filtering
  get '/actors' do
    condition = URI.unescape(request.query_string)
    actors = filter(condition, true, net.graph)
    return_message = actors
    return return_message.to_json
  end

  get '/movies' do
    condition = URI.unescape(request.query_string)
    films = filter(condition, false, net.graph)
    return_message = films
    return return_message.to_json
  end

  get '/actors/*' do
    actor_name = params['splat'][0]
    actor = find_actor(actor_name, net.graph)
    return_message = actor
    return return_message.to_json
  end

  get '/movies/*' do
    film_name = params['splat'][0]
    film = find_film(film_name, net.graph)
    return_message = [film]
    return return_message.to_json
  end

  # == PUT requests
  put '/actors/:name' do
    name = params['name'].gsub('_', ' ')

    #remove the '' at the begin/end of the input string
    input = String.new(request.body.read).gsub(/\s|'/, '')
    data = JSON.parse(input)

    actor = nil
    net.graph.nodes.each do |n|
      if n.content.instance_of? Actor
        if n.content.name == name
          if data.has_key? 'name'
            n.content.name = data['name']
          end
          if data.has_key? 'age'
            n.content.age = data['age']
          end
          if data.has_key? 'total_gross'
            n.content.total_gross = data['total_gross']
          end
          actor = n.content
        end
      end
    end

    return_message = [actor]
    return return_message.to_json
  end

  put '/movies/:name' do
    name = params['name'].gsub('_', ' ')
    #remove the '' at the begin/end of the input string
    input = String.new(request.body.read).gsub(/\s|'/, '')
    data = JSON.parse(input)

    film = nil
    net.graph.nodes.each do |n|
      if n.content.instance_of? Film
        if n.content.name == name
          if data.has_key? 'name'
            n.content.name = data['name']
          end
          if data.has_key? 'year'
            n.content.year = data['year']
          end
          if data.has_key? 'box_office'
            n.content.box_office = data['box_office']
          end
          film = n.content
        end
      end
    end

    return_message = [film]
    return return_message.to_json
  end

  # == POST requests
  post '/actors' do
    pp '----------------------'
    #remove the '' at the begin/end of the input string
    input = String.new(request.body.read)
    pp input
    #input = input.gsub(/\s|'/, '')
    input = input.tr("'", "")
    pp input
    data = JSON.parse(input)
    pp data

    actor = Actor.new(data['name'])
    if data.has_key? 'age'
      actor.age = data['age']
    end
    if data.has_key? 'total_gross'
      actor.total_gross = data['total_gross']
    end

    net.graph.add_node(Node.new(actor))
    return_message = [net.graph.find_node(data['name']).content]
    pp return_message.to_json
    return return_message.to_json
  end

  #POST requests
  post '/movies' do
    pp '----------------------'
    #remove the '' at the begin/end of the input string
    input = String.new(request.body.read)
    pp input
    #input = input.gsub(/\s|'/, '')
    input = input.tr("'", "")
    pp input
    data = JSON.parse(input)
    pp data

    film = Film.new(data['name'])
    if data.has_key? 'year'
      film.year = data['year']
    end
    if data.has_key? 'box_office'
      film.box_office = data['box_office']
    end

    net.graph.add_node(Node.new(film))
    return_message = [net.graph.find_node(data['name']).content]
    pp return_message.to_json
    return return_message.to_json
  end

  # == DELETE requests
  delete '/actors/:name' do
    pp '----------------------'
    pp '----------------------'
    name = params['name'].gsub('_', ' ')
    net.graph.remove_node(name)
    # try to find it again
    if net.graph.find_node(name).nil?
      return 'Safely deleted'
    else
      return 'Still remain to delete'
    end
  end

  # == DELETE requests
  delete '/movies/:name' do
    pp '----------------------'
    name = params['name'].gsub('_', ' ')
    net.graph.remove_node(name)
    # try to find it again
    if net.graph.find_node(name).nil?
      return 'Safely deleted'
    else
      return 'Still remain to delete'
    end
  end


end