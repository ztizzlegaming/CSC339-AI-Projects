package com.jordanturley;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import com.jordanturley.astar.Action;
import com.jordanturley.astar.Node;
import com.jordanturley.astar.NodeCost;

/**
 * The <code>Cube</code> class represents a 2D Rubiks cube. Rotating any side rotates it counter-clockwise.
 * Calling solve() returns a List of Actions, which can be printed/used in some way
 * to see the way to solve the cube.
 * 
 * I just hard coded the rotations. I'm sure there is probably some algorithm I could use, but
 * I didn't really want to think about it, and because it is only a 2x2, it isn't too bad. If it
 * was a 3x3 cube, it would be a lot more tedious to do.
 * 
 * @author Jordan Turley
 */
public class Cube {
	public static final int SIZE = 2;
	public static final int NUM_EACH_int = SIZE * SIZE;

	private int[][] front;
	private int[][] back;
	private int[][] left;
	private int[][] right;
	private int[][] bottom;
	private int[][] top;

	/**
	 * Constructs a new <code>Cube</code> object, from the given int arrays.
	 * @param front The front side of the cube
	 * @param back The back side of the cube
	 * @param left The left side of the cube
	 * @param right The right side of the cube
	 * @param bottom The bottom side of the cube
	 * @param top The top side of the cube
	 */
	public Cube(int[][] front, int[][] back, int[][] left, int[][] right, int[][] bottom, int[][] top) {
		//First, make sure there are four of each color
		Map<String, Integer> numColorsMap = new HashMap<String, Integer>();
		Collection<String> ints = Solver.STRING_TO_COLOR_MAP.keySet();
		for (String color : ints) {
			numColorsMap.put(color, 0);
		}

		//Go through and count the colors on each side
		for (int y = 0; y < SIZE; y++) {
			for (int x = 0; x < SIZE; x++) {
				countColor(x, y, front, numColorsMap);
				countColor(x, y, back, numColorsMap);
				countColor(x, y, left, numColorsMap);
				countColor(x, y, right, numColorsMap);
				countColor(x, y, bottom, numColorsMap);
				countColor(x, y, top, numColorsMap);
			}
		}

		//Make sure each color is equal to the number that it should be
		Collection<Integer> nums = numColorsMap.values();
		for (int num : nums) {
			if (num != NUM_EACH_int) {
				throw new IllegalArgumentException("There were not " + NUM_EACH_int + " of each color. Please re-enter the cube");
			}
		}

		this.front = front;
		this.back = back;
		this.left = left;
		this.right = right;
		this.bottom = bottom;
		this.top = top;
	}
	
	/**
	 * Constructs a new already solved Cube object
	 */
	public Cube() {
		front = new int[][] {
			new int[] {Solver.GREEN, Solver.GREEN},
			new int[] {Solver.GREEN, Solver.GREEN}};
		back = new int[][] {
			new int[] {Solver.BLUE, Solver.BLUE},
			new int[] {Solver.BLUE, Solver.BLUE}};
		left = new int[][] {
			new int[] {Solver.YELLOW, Solver.YELLOW},
			new int[] {Solver.YELLOW, Solver.YELLOW}};
		right = new int[][] {
			new int[] {Solver.BLACK, Solver.BLACK},
			new int[] {Solver.BLACK, Solver.BLACK}};
		bottom = new int[][] {
			new int[] {Solver.RED, Solver.RED},
			new int[] {Solver.RED, Solver.RED}};
		top = new int[][] {
			new int[] {Solver.PURPLE, Solver.PURPLE},
			new int[] {Solver.PURPLE, Solver.PURPLE}};
	}
	
	/**
	 * Constructs a new Cube object, making a copy from the given object
	 * @param cube The Cube object to make the copy from
	 */
	public Cube(Cube cube) {
		front = new int[SIZE][SIZE];
		back = new int[SIZE][SIZE];
		left = new int[SIZE][SIZE];
		right = new int[SIZE][SIZE];
		bottom = new int[SIZE][SIZE];
		top = new int[SIZE][SIZE];
		
		for (int y = 0; y < SIZE; y++) {
			for (int x = 0; x < SIZE; x++) {
				front[y][x] = cube.front[y][x];
				back[y][x] = cube.back[y][x];
				left[y][x] = cube.left[y][x];
				right[y][x] = cube.right[y][x];
				bottom[y][x] = cube.bottom[y][x];
				top[y][x] = cube.top[y][x];
			}
		}
	}

	/**
	 * Counts a color at an (x, y) location in a color array to the num colors map
	 * @param x The X location to count
	 * @param y The Y location to count
	 * @param arr The 2D array to count the color from
	 * @param numColorsMap The map of strings (colors) and integers (counts) to count the color to
	 */
	private void countColor(int x, int y, int[][] arr, Map<String, Integer> numColorsMap) {
		int color = arr[y][x];
		String intStr = Solver.COLOR_TO_STRING_MAP.get(color);
		int curNum = numColorsMap.remove(intStr);
		curNum++;
		numColorsMap.put(intStr, curNum);
	}

	/**
	 * Turns the front side of the cube clockwise
	 */
	public void turnFront() {
		front = rotateArr(front);

		//Top goes to the right, right goes to bottom, bottom goes to left, left goes to top
		int right1Temp = top[1][0];
		int right2Temp = top[1][1];
		top[1][0] = left[0][1];
		top[1][1] = left[1][1];
		left[0][1] = bottom[0][0];
		left[1][1] = bottom[0][1];
		bottom[0][0] = right[0][0];
		bottom[0][1] = right[1][0];
		right[0][0] = right1Temp;
		right[1][0] = right2Temp;
	}
	
	/**
	 * Turns the right side of the cube clockwise
	 */
	public void turnRight() {
		right = rotateArr(right);

		int front1Temp = bottom[0][1];
		int front2Temp = bottom[1][1];
		bottom[0][1] = back[0][1];
		bottom[1][1] = back[1][1];
		back[0][1] = top[0][1];
		back[1][1] = top[1][1];
		top[0][1] = front[0][1];
		top[1][1] = front[1][1];
		front[0][1] = front1Temp;
		front[1][1] = front2Temp;
	}
	
	/**
	 * Turns the top side of the cube clockwise
	 */
	public void turnTop() {
		top = rotateArr(top);

		int front1Temp = right[0][0];
		int front2Temp = right[0][1];
		right[0][0] = back[1][1];
		right[0][1] = back[1][0];
		back[1][1] = left[0][0];
		back[1][0] = left[0][1];
		left[0][0] = front[0][0];
		left[0][1] = front[0][1];
		front[0][0] = front1Temp;
		front[0][1] = front2Temp;
	}
	
	/**
	 * Turns the front side of the cube counter-clockwise
	 */
	public void turnFrontInverse() {
		front = rotateArr(front);
		front = rotateArr(front);
		front = rotateArr(front);
		
		int left1Temp = top[1][0];
		int left2Temp = top[1][1];
		top[1][0] = right[0][0];
		top[1][1] = right[1][0];
		right[0][0] = bottom[0][0];
		right[1][0] = bottom[0][1];
		bottom[0][0] = left[0][1];
		bottom[0][1] = left[1][1];
		left[0][1] = left1Temp;
		left[1][1] = left2Temp;
	}
	
	/**
	 * Turns the right side of the cube counter-clockwise
	 */
	public void turnRightInverse() {
		right = rotateArr(right);
		right = rotateArr(right);
		right = rotateArr(right);
		
		int back1Temp = bottom[0][1];
		int back2Temp = bottom[1][1];
		bottom[0][1] = front[0][1];
		bottom[1][1] = front[1][1];
		front[0][1] = top[0][1];
		front[1][1] = top[1][1];
		top[0][1] = back[0][1];
		top[1][1] = back[1][1];
		back[0][1] = back1Temp;
		back[1][1] = back2Temp;
	}
	
	/**
	 * Turns the top side of the cube counter-clockwise
	 */
	public void turnTopInverse() {
		top = rotateArr(top);
		top = rotateArr(top);
		top = rotateArr(top);
		
		int back1Temp = right[0][0];
		int back2Temp = right[0][1];
		right[0][0] = front[0][0];
		right[0][1] = front[0][1];
		front[0][0] = left[0][0];
		front[0][1] = left[0][1];
		left[0][0] = back[1][1];
		left[0][1] = back[1][0];
		back[1][1] = back1Temp;
		back[1][0] = back2Temp;
	}
	
	/**
	 * Turns the back side of the cube clockwise
	 */
	public void turnBack() {
		back = rotateArr(back);
		
		int right1Temp = bottom[1][1];
		int right2Temp = bottom[1][0];
		bottom[1][1] = left[1][0];
		bottom[1][0] = left[0][0];
		left[1][0] = top[0][0];
		left[0][0] = top[0][1];
		top[0][0] = right[0][1];
		top[0][1] = right[1][1];
		right[0][1] = right1Temp;
		right[1][1] = right2Temp;
	}
	
	/**
	 * Turns the left side of the cube clockwise
	 */
	public void turnLeft() {
		left = rotateArr(left);

		int front1Temp = top[0][0];
		int front2Temp = top[1][0];
		top[0][0] = back[0][0];
		top[1][0] = back[1][0];
		back[0][0] = bottom[0][0];
		back[1][0] = bottom[1][0];
		bottom[0][0] = front[0][0];
		bottom[1][0] = front[1][0];
		front[0][0] = front1Temp;
		front[1][0] = front2Temp;
	}

	/**
	 * Turns the bottom side of the cube clockwise
	 */
	public void turnBottom() {
		bottom = rotateArr(bottom);

		int front1Temp = left[1][0];
		int front2Temp = left[1][1];
		left[1][0] = back[0][1];
		left[1][1] = back[0][0];
		back[0][1] = right[1][0];
		back[0][0] = right[1][1];
		right[1][0] = front[1][0];
		right[1][1] = front[1][1];
		front[1][0] = front1Temp;
		front[1][1] = front2Temp;
	}
	
	/**
	 * Turns the back side of the cube counter-clockwise
	 */
	public void turnBackInverse() {
		back = rotateArr(back);
		back = rotateArr(back);
		back = rotateArr(back);
		
		int left1Temp = bottom[1][1];
		int left2Temp = bottom[1][0];
		bottom[1][1] = right[0][1];
		bottom[1][0] = right[1][1];
		right[0][1] = top[0][0];
		right[1][1] = top[0][1];
		top[0][0] = left[1][0];
		top[0][1] = left[0][0];
		left[1][0] = left1Temp;
		left[0][0] = left2Temp;
	}
	
	/**
	 * Turns the left side of the cube counter-clockwise
	 */
	public void turnLeftInverse() {
		left = rotateArr(left);
		left = rotateArr(left);
		left = rotateArr(left);
		
		int back1Temp = top[0][0];
		int back2Temp = top[1][0];
		top[0][0] = front[0][0];
		top[1][0] = front[1][0];
		front[0][0] = bottom[0][0];
		front[1][0] = bottom[1][0];
		bottom[0][0] = back[0][0];
		bottom[1][0] = back[1][0];
		back[0][0] = back1Temp;
		back[1][0] = back2Temp;
	}

	/**
	 * Turns the bottom side of the cube counter-clockwise
	 */
	public void turnBottomInverse() {
		bottom = rotateArr(bottom);
		bottom = rotateArr(bottom);
		bottom = rotateArr(bottom);
		
		int back1Temp = left[1][0];
		int back2Temp = left[1][1];
		left[1][0] = front[1][0];
		left[1][1] = front[1][1];
		front[1][0] = right[1][0];
		front[1][1] = right[1][1];
		right[1][0] = back[0][1];
		right[1][1] = back[0][0];
		back[0][1] = back1Temp;
		back[0][0] = back2Temp;
	}

	/**
	 * Solves the Rubik's cube. Finds the correct order of turns to get the cube to the solved state
	 * @return A List of Actions that shows how the cube is solved
	 */
	public List<Action> solve() {
		PriorityQueue<NodeCost> q = new PriorityQueue<NodeCost>();
		
		//Creates the start node from this state
		Node startNode = new Node(this, null, null, 0.0);
		NodeCost startNodeCost = new NodeCost(startNode, 0.0);
		q.add(startNodeCost);

		List<Cube> exploredSet = new ArrayList<Cube>();
		
		while (!q.isEmpty()) {
			NodeCost nc = q.remove();

			Node currentNode = nc.getNode();
			exploredSet.add(currentNode.getState());

			if (currentNode.getState().isSolved()) { //If the cube is solved, return the list of actions
				List<Action> pathReverse = new ArrayList<Action>();
				Node node = currentNode;
				while (node != null && node.getAction() != null) {
					pathReverse.add(node.getAction());
					node = node.getParent();
				}
				
				List<Action> path = new ArrayList<Action>();
				
				//The actions are reversed, so reverse them
				for (int i1 = pathReverse.size() - 1; i1 >= 0; i1--) {
					path.add(pathReverse.get(i1));
				}
				
				return path;
			} else {
				List<NodeCost> children = getChildrenForNode(currentNode);
				for (NodeCost child : children) {
					Cube childState = child.getNode().getState();
					if (!exploredSet.contains(childState)) {
						q.add(child);
					}
				}
			}
		}

		//No solution, they entered the cube such that it can't be solved?
		return null;
	}

	/**
	 * Gets the children nodes for a given node. Each child will be turning the cube
	 * one way from the current state
	 * @param currentNode
	 * @return
	 */
	private static List<NodeCost> getChildrenForNode(Node currentNode) {
		List<NodeCost> list = new ArrayList<NodeCost>();
		
		String[] actions = {
				"front",
				"front inverse",
				"right",
				"right inverse",
				"top",
				"top inverse",
				"left",
				"left inverse",
				"bottom",
				"bottom inverse",
				"back",
				"back inverse"};
		
		for (String action : actions) {
			list.add(generateChild(currentNode, action));
		}

		return list;
	}
	
	private static NodeCost generateChild(Node currentNode, String actionStr) {
		Cube cube = new Cube(currentNode.getState());
		
		switch (actionStr) {
		case "front":
			cube.turnFront();
			break;
		case "front inverse":
			cube.turnFrontInverse();
			break;
		case "right inverse":
			cube.turnRightInverse();
			break;
		case "right":
			cube.turnRight();
			break;
		case "top inverse":
			cube.turnTopInverse();
			break;
		case "top":
			cube.turnTop();
			break;
		case "left":
			cube.turnLeft();
			break;
		case "left inverse":
			cube.turnLeftInverse();
			break;
		case "bottom":
			cube.turnBottom();
			break;
		case "bottom inverse":
			cube.turnBottomInverse();
			break;
		case "back":
			cube.turnBack();
			break;
		case "back inverse":
			cube.turnBackInverse();
			break;
		default:
			throw new IllegalArgumentException("Unknown action");
		}
		
		Action action = new Action(actionStr);
		
		double pathCost = currentNode.getPathCost() + 1.0;
		
		Node topTurnNode = new Node(cube, currentNode, action, pathCost);
		
		double functionCost = pathCost + cube.heuristic();
		
		NodeCost nc = new NodeCost(topTurnNode, functionCost);
		
		return nc;
	}

	/**
	 * Measures the number of different ints on each side. Solved cube heuristic value = 0,
	 * so it gets put at the front of the queue
	 * @return
	 */
	public double heuristic() {
		double num = 0;
		num += heuristicForSide(front);
		num += heuristicForSide(back);
		num += heuristicForSide(left);
		num += heuristicForSide(right);
		num += heuristicForSide(bottom);
		num += heuristicForSide(top);
		return num;
	}

	/**
	 * Calculates the heuristic value for a given side
	 * @param side The 2D array for that side
	 * @return The heuristic value
	 */
	private double heuristicForSide(int[][] side) {
		List<Integer> differentints = new ArrayList<Integer>();
		for (int y = 0; y < SIZE; y++) {
			for (int x = 0; x < SIZE; x++) {
				int color = side[y][x];
				if (!differentints.contains(color)) {
					differentints.add(color);
				}
			}
		}
		
		return differentints.size() - 1;
	}

	/**
	 * Checks if the cube is solved, by making sure the colors are the same on all sides
	 * @return True if the cube is solved
	 */
	public boolean isSolved() {
		int frontColor = front[0][0];
		int backColor = back[0][0];
		int leftColor = left[0][0];
		int rightColor = right[0][0];
		int bottomColor = bottom[0][0];
		int topColor = top[0][0];

		for (int y = 0; y < SIZE; y++) {
			for (int x = 0; x < SIZE; x++) {
				int curFrontColor = front[y][x];
				int curBackColor = back[y][x];
				int curLeftColor = left[y][x];
				int curRightColor = right[y][x];
				int curBottomColor = bottom[y][x];
				int curTopColor = top[y][x];

				//If any of the colors are different on a side, it's not solved
				if (frontColor != curFrontColor ||
						backColor != curBackColor ||
						leftColor != curLeftColor ||
						rightColor != curRightColor ||
						bottomColor != curBottomColor ||
						topColor != curTopColor) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Rotates a 2D int array clockwise
	 * @param arr The array to rotate
	 * @return The rotated array
	 */
	private static int[][] rotateArr(int[][] arr) {
		int[][] rotatedArr = new int[SIZE][SIZE];
		for (int y = 0; y < SIZE; y++) {
			for (int x = 0; x < SIZE; x++) {
				rotatedArr[y][x] = arr[SIZE - 1 - x][y];
			}
		}
		return rotatedArr;
	}

	@Override
	public String toString() {
		String str = "Front:\n" + getSideString(front);
		str += "\nBack\n" + getSideString(back);
		str += "\nLeft:\n" + getSideString(left);
		str += "\nRight:\n" + getSideString(right);
		str += "\nBottom:\n" + getSideString(bottom);
		str += "\nTop:\n" + getSideString(top);
		return str;
	}

	private String getSideString(int[][] side) {
		String str = "";
		for (int[] row : side) {
			for (int color: row) {
				str += Solver.COLOR_TO_STRING_MAP.get(color) + " ";
			}
			str += "\n";
		}
		return str;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Cube) {
			Cube c = (Cube) o;
			for (int y = 0; y < SIZE; y++) {
				for (int x = 0; x < SIZE; x++) {
					if (front[y][x] != c.front[y][x]
							|| back[y][x] != c.back[y][x]
							|| left[y][x] != c.left[y][x]
							|| right[y][x] != c.right[y][x]
							|| bottom[y][x] != c.bottom[y][x]
							|| top[y][x] != c.top[y][x]) {
						return false;
					}
				}
			}
			return true;
		}
		return false;
	}
}
