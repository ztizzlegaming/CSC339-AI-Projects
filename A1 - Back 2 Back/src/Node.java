
/**
 * The <code>Node</code> class is used to store the current state of the Board,
 * the parent Node, the Action that got us here, and the path cost to this node.
 * 
 * @author Jordan Turley
 * @author Will Edwards
 */
public class Node implements Comparable<Node> {
	private Board state;
	private Node parent;
	private Action action;
	private double pathCost;
	
	public Node(Board state, Node parent, Action action, double pathCost) {
		this.state = state;
		this.parent = parent;
		this.action = action;
		this.pathCost = pathCost;
	}
	
	public Board getState() {
		return state;
	}
	
	public Node getParent() {
		return parent;
	}
	
	public Action getAction() {
		return action;
	}
	
	public double getPathCost() {
		return pathCost;
	}

	public int compareTo(Node node) {
		if (pathCost < node.getPathCost()) {
			return -1;
		} else if (pathCost == node.getPathCost()) {
			return 0;
		} else {
			return 1;
		}
	}
}
