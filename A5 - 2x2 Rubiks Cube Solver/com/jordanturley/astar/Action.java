package com.jordanturley.astar;

/**
 * The <code>Action</code> class stores a side of the cube to be turned
 * 
 * @author Jordan Turley
 */
public class Action {
	private String side;
	
	public Action(String side) {
		this.side = side;
	}
	
	public String getSide() {
		return side;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Action) {
			Action a = (Action) o;
			return a.getSide().equals(side);
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "Turn: " + side;
	}
}
