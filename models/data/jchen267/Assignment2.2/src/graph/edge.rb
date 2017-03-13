class Edge
  attr_accessor :node1, :node2, :weight

  # @!method initialize instance
  def initialize(node1, node2, weight)
    @node1, @node2, @weight = node1, node2, weight
  end

  # @!method override comparing method
  # @param [Edge] edge to compare with
  def <=>(other)
    self.weight <=> other.weight
  end

  # @!method Convert edge to string representation
  def to_s
    "#{node1.to_s} <---> #{node2.to_s} with weight #{weight}"
  end
end