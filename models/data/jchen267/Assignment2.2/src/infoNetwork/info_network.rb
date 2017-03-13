require_relative '../graph/edge.rb'
require_relative '../graph/node.rb'
require_relative '../graph/graph.rb'
require_relative '../scrapper/actor.rb'
require_relative '../scrapper/film.rb'
require 'pp'


class InfoNetwork
  attr_accessor :graph

  # @!method initialize the network
  def initialize
    @graph = Graph.new
  end

  # @!method build the network from json
  # @param [String] filename of given json
  def build_network_from_json(filename='../resource/data.json')
    data = JSON.parse(File.read(filename))

    # construct actor nodes
    actor_data = data[0]
    puts('adding actors: ', actor_data.length)
    actor_data.each do |d|
      d1 = d[1]
      actor = Actor.new(d1['name'], d1['age'], d1['total_gross'].to_i)
      d1['movies'].each do |m|
        actor.add_film(m)
      end
      @graph.add_node(Node.new(actor))
    end

    # construct film nodes
    film_data = data[1]
    # puts('adding films: ', film_data.length)
    film_data.each do |d|
      d1 = d[1]
      if (d1['box_office']<100)
        box_office = d1['box_office']==0 ? rand(1000..10000) : d1['box_office']*100
      else
        box_office = d1['box_office']
      end
      film = Film.new(d1['name'], d1['year'].to_i, d1['wiki_page'], box_office)
      # process its cast
      d1['actors'].each do |a|
        film.add_actor(a)
        if @graph.find_node(a).nil?
          @graph.add_node(Node.new(Actor.new(a)))
        end
      end
      film_n = @graph.add_node(Node.new(film))
    end
    complete_edges_for_graph()
    # @graph.print
  end

  # @!method complete edge constructions for the graph
  def complete_edges_for_graph
    @graph.nodes.each do |n|
      f = n.content
      if f.instance_of? Film
        age_sum = 0
        f.actor_to_grossing.each do |actor_name, grossing|
          actor_n = @graph.find_node(actor_name)
          age_sum += actor_n.nil? ? 0 : actor_n.content.age
        end

        # link edges
        # puts('box', f.box_office.to_s, ' age sum: ', age_sum.to_s)
        f.actor_to_grossing.each do |a, g|
          if ((actor_n = @graph.find_node(a)).nil? == false)
            a = actor_n.content
            # pp f.box_office
            weight = max(12, f.box_office / age_sum * a.age)
            @graph.add_edge(actor_n, n, weight)
          end
        end

      end
    end
  end


  # @!method store graph info to json file
  # @param [String] filename to store as
  def store_to_json(filename)
    obj = {}
    obj['nodes'] = []
    @graph.nodes.each do |n|
      group_num = (n.content.instance_of? Film) ? 1 : 0
      val = (n.content.instance_of? Film) ? n.content.box_office : n.content.age
      obj['nodes'] << {'name': n.id, 'group': group_num, 'val': val}
    end

    obj['links'] = []
    @graph.edges.each do |e|
      obj['links'] << {'source': @graph.find_node_idx(e.node1.id),
                       'target': @graph.find_node_idx(e.node2.id),
                       'value': e.weight}
    end
    # store to disk
    json = JSON.dump(obj)
    # puts JSON.pretty_generate(json)
    File.open(filename, 'w') {
        |file| file.write(json)
    }
  end

  # @!method perform analysis on data
  def analysis
    actor_to_costars = find_hub_actors(5)
    find_richest_age_group(5)
    analyze_degrees_of_bacon(actor_to_costars)

  end

  # @!method Answer question
  #  4. Your client wants to know if the "Six Degrees of Kevin Bacon" is really true.
  #   Write code to test this for your own data set.
  #   Specifically, you should run your code for each actor pair in your dataset
  #   and be able to say what the average degree of separation is,
  #   what the maximum degree is and which actor pairs are the farthest apart.
  # @param[Hash] actor_to_costars hash from actor to costars
  def analyze_degrees_of_bacon(actor_to_costars)
    #   for each actor, calculate degree with each of other actors
    actor_to_idx = Hash.new
    actor_to_costars.keys.each_with_index { |actor, idx| actor_to_idx[actor] = idx }
    # create 2D array of distances
    actor_dists = Array.new(actor_to_idx.length) { Array.new(actor_to_idx.length) }
    actor_to_costars.each_with_index do |(actor_self, costars), idx_self|
      update_distance_in_bfs(actor_self, actor_to_idx, actor_to_costars, actor_dists)
    end
    # print answers tpo question
    puts('Discard inf distance')
    ct = 0
    degree_sum = 0
    max_degree = 0
    max_pairs = []
    actor_dists.each_with_index do |actor_dists_row, i|
      actor_dists_row.each_with_index do |degree, j|
        if degree.nil?
          next
        end
        ct += 1
        degree_sum += degree
        # update max pair
        if (degree > max_degree)
          max_degree = degree
          max_pairs = [[i, j]]
        elsif (degree = max_degree)
          max_pairs << [i, j]
        end
      end
    end
    puts('Average degree between actors: ' + (degree_sum/ct).to_s)
    puts('Farthest degree: ' + max_degree.to_s + ', ' + (max_pairs.length/2).to_s + ' such pairs.')
    # max_pairs.each do |pair|
    #   puts(actor_to_idx.keys[pair[0]] + ' and ' + actor_to_idx.keys[pair[1]])
    # end
  end

  # @!method traverse from the starting actor,
  #            update its distance to other actors
  # @param[Actor] actor_self to start with
  # @param[Hash] actor_to_idx hash from actor to idx
  # @param[Hash] actor_to_costars hash from actor to costars
  # @param[Array] actor_dists 2d array of distances
  def update_distance_in_bfs(actor_self, actor_to_idx, actor_to_costars, actor_dists)
    queue = []
    queue << actor_self
    id_self = actor_to_idx[actor_self]
    actor_dists[id_self][id_self] = 0

    while queue.any?
      actor_curr = queue.shift
      id_curr = actor_to_idx[actor_curr]
      actor_to_costars[actor_curr].each do |costar|
        id_costar = actor_to_idx[costar]
        # if unvisited
        if actor_dists[id_self][id_costar].nil?
          actor_dists[id_self][id_costar] = actor_dists[id_self][id_curr] + 1
          queue << costar
        end
      end
    end

  end

  # @!method Answer question 1. Who are the "hub" actors in your dataset?
  # @param [Fixnum] k is the number of top hub actors to find
  def find_hub_actors(k)
    # construct hash table from actor to co-stars
    actor_to_costars = Hash.new
    @graph.nodes.each do |n|
      if (n.content.instance_of? Actor)
        next
      end

      f = n.content
      f.actor_to_grossing.each do |a_self, g|
        f.actor_to_grossing.each do |a, g|
          if a_self == a
            next
          end
          if actor_to_costars.has_key?(a_self) == false
            actor_to_costars[a_self] = Set.new
          end
          actor_to_costars[a_self].add(a)
        end
      end
    end
    #  find top k popular hubs
    actor_to_costars_arr = actor_to_costars.sort_by { |actor, co_stars| -co_stars.length }
    for i in 0..k-1
      puts('Popular Hub #' + i.to_s + ' ' + actor_to_costars_arr[i][0] +
               ' ; number connections: ' + actor_to_costars_arr[i][1].length.to_s)
    end
    return actor_to_costars
  end

  # Answer question 2. Is there an age group that generates the most amount of money?
  # @param [Fixnum] interval_len is the interval desired to use to divide groups into
  def find_richest_age_group(interval_len)
    # construct array of avg grossing grouped by age
    number_groups = (120.0/interval_len).ceil
    total_grossing_by_age_group = Array.new(number_groups, 0)
    population_by_age_group = Array.new(number_groups, 0)
    @graph.nodes.each do |n|
      if (n.content.instance_of? Film)
        next
      end

      a = n.content
      group_idx = a.age/interval_len
      total_grossing_by_age_group[group_idx] += a.total_gross
      ++population_by_age_group[group_idx]
    end
    avg_grossing_by_age_group = total_grossing_by_age_group.zip(population_by_age_group).map { |total, ct| total/max(1, ct) }
    pp(avg_grossing_by_age_group)
    group_idx = avg_grossing_by_age_group.each_with_index.max[1]
    puts('Age group that generates the most amount of money ' +
             ((group_idx-1)*interval_len + 1).to_s + '~' + ((group_idx)*interval_len).to_s)
  end

end

# @!method find max between two params
def max (a, b)
  a>b ? a : b
end

# entry point
if __FILE__ == $0
  net = InfoNetwork.new
  filename = '../resource/data.json'
  net.build_network_from_json(filename)
  net.store_to_json('C:\Users\jiayu\PycharmProjects\untitled1\network\data.json')

  net.analysis()
end