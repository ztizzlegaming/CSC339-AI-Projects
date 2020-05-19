package com.jordanturley.astar;

/**
 * The <code>NodeCost</code> class stores a node and the function cost
 * 
 * @author Jordan Turley
 */
public class NodeCost implements Comparable<NodeCost>{
	private Node node;
	private double cost;
	
	public NodeCost(Node node, double cost) {
		this.node = node;
		this.cost = cost;
	}
	
	public Node getNode() {
		return node;
	}
	
	public double getCost() {
		return cost;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof NodeCost) {
			NodeCost nc = (NodeCost) o;
			return nc.getNode().equals(node) && nc.getCost() == cost;
		}
		return false;
	}
	
	@Override
	public int compareTo(NodeCost nc) {
		if (cost < nc.getCost()) {
			return -1;
		} else if (cost == nc.getCost()) {
			return 0;
		} else {
			return 1;
		}
	}
}
