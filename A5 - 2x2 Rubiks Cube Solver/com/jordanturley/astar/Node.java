package com.jordanturley.astar;

import com.jordanturley.Cube;

/**
 * The <code>Node</code> class stores a state of the cube, the parent node, the action that got the cube
 * to this state, and the path cost
 * 
 * @author Jordan Turley
 */
public class Node implements Comparable<Node> {
	private Cube state;
	private Node parent;
	private Action action;
	private double pathCost;

	public Node(Cube state, Node parent, Action action, double pathCost) {
		this.state = state;
		this.parent = parent;
		this.action = action;
		this.pathCost = pathCost;
	}

	public Cube getState() {
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

	@Override
	public boolean equals(Object o) {
		if (o instanceof Node) {
			Node n = (Node) o;

			Cube nState = n.getState();
			Node nParent = n.getParent();
			Action nAction = n.getAction();

			if (nState != null) {
				if (!nState.equals(state)) {
					return false;
				}
			} else if (state != null) {
				return false;
			}

			if (nParent != null) {
				if (!nParent.equals(parent)) {
					return false;
				}
			} else if (parent != null){
				return false;
			}

			if (nAction != null) {
				if (!nAction.equals(action)) {
					return false;
				}
			} else if (action != null) {
				return false;
			}

			return n.getPathCost() == pathCost;
		}
		return false;
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
