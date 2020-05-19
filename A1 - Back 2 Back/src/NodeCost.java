
/**
 * The <code>NodeCost</code> class stores a Node and a cost. It is used in the Priority Queue
 * when solving the Board using A*
 * 
 * @author Jordan Turley
 * @author Will Edwards
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
