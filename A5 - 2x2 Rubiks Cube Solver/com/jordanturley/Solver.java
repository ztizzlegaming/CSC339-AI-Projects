package com.jordanturley;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.jordanturley.astar.Action;

/**
 * The <code>Solver</code> class lets the user input the colors on each side, and attempts to solve
 * the cube.
 * 
 * @author Jordan Turley
 */
public class Solver {
	public static final int RED = 0;
	public static final String RED_STRING = "red";

	public static final int GREEN = 1;
	public static final String GREEN_STRING = "green";

	public static final int BLUE = 2;
	public static final String BLUE_STRING = "blue";

	public static final int YELLOW = 3;
	public static final String YELLOW_STRING = "yellow";

	public static final int BLACK = 4;
	public static final String BLACK_STRING = "black";

	public static final int PURPLE = 5;
	public static final String PURPLE_STRING = "purple";

	public static final Map<String, Integer> STRING_TO_COLOR_MAP = new HashMap<String, Integer>();
	static {
		STRING_TO_COLOR_MAP.put(RED_STRING, RED);
		STRING_TO_COLOR_MAP.put(GREEN_STRING, GREEN);
		STRING_TO_COLOR_MAP.put(BLUE_STRING, BLUE);
		STRING_TO_COLOR_MAP.put(YELLOW_STRING, YELLOW);
		STRING_TO_COLOR_MAP.put(BLACK_STRING, BLACK);
		STRING_TO_COLOR_MAP.put(PURPLE_STRING, PURPLE);
	}

	//Java doesn't have a reversable hashmap (look up key by value) so I just made another
	public static final Map<Integer, String> COLOR_TO_STRING_MAP = new HashMap<Integer, String>();
	static {
		COLOR_TO_STRING_MAP.put(RED, RED_STRING);
		COLOR_TO_STRING_MAP.put(GREEN, GREEN_STRING);
		COLOR_TO_STRING_MAP.put(BLUE, BLUE_STRING);
		COLOR_TO_STRING_MAP.put(YELLOW, YELLOW_STRING);
		COLOR_TO_STRING_MAP.put(BLACK, BLACK_STRING);
		COLOR_TO_STRING_MAP.put(PURPLE, PURPLE_STRING);
	}

	private static final Scanner INPUT = new Scanner(System.in);

	public static final String[] SIDES = {"front", "back", "left", "right", "bottom", "top"};

	public static void main(String[] args) {
		System.out.println("How to use:");
		System.out.println("First, pick a side to be the front. This will determine the front, back, left... sides");
		System.out.println("Then, enter the colors. Seperate columns by a space, and rows by 'enter'");
		System.out.println("To enter a side, turn it to that side from the front.");
		System.out.println("So, for example, to enter the bottom, with the front facing you, rotate the cube up, so the front is facing up and the bottom is facing you.");

		int[][][] sides = new int[SIDES.length][][];

		Cube cube = null;

		//Let the user enter each side, and the side is stored in sides
		boolean cubeSuccess = false;
		while (!cubeSuccess) {
			try {
				for (int i1 = 0; i1 < SIDES.length; i1++) {
					boolean successfulSideEntry = false;
					while (!successfulSideEntry) {
						try {
							System.out.println(SIDES[i1] + ":");
							int[][] sideArr = getColorArrForInput(INPUT.nextLine(), INPUT.nextLine());
							successfulSideEntry = true;
							sides[i1] = sideArr;
						} catch (IllegalArgumentException e) {
							System.out.println(e.getMessage() + " Re-enter " + SIDES[i1] + ":");
						}
					}
				}

				cube = new Cube(sides[0], sides[1], sides[2], sides[3], sides[4], sides[5]);
				cubeSuccess = true;
			} catch (IllegalArgumentException e) {
				System.out.println(e.getMessage());
			}
		}

		//Solve it and print out the actions
		List<Action> actions = cube.solve();
		System.out.println("Solution:");
		for (Action a : actions) {
			System.out.println(a);
		}
	}

	/**
	 * Parses the user's input into a 2D array representing a side
	 * @param firstRow The first row of their input
	 * @param secondRow The second row of their input
	 * @return A 2D array representing the side
	 * @throws IllegalArgumentException If something is wrong with the user input
	 */
	private static int[][] getColorArrForInput(String firstRow, String secondRow) throws IllegalArgumentException { 
		int[][] arr = new int[Cube.SIZE][Cube.SIZE];

		String[] firstRowArr = firstRow.split(" ");
		if (firstRowArr.length != Cube.SIZE) {
			throw new IllegalArgumentException("Incorrect number of colors entered.");
		}

		Integer c = STRING_TO_COLOR_MAP.get(firstRowArr[0]);
		if (c == null) {
			throw new IllegalArgumentException("One of the colors you entered was not recognized.");
		}
		arr[0][0] = c;

		c = STRING_TO_COLOR_MAP.get(firstRowArr[1]);
		if (c == null) {
			throw new IllegalArgumentException("One of the colors you entered was not recognized.");
		}
		arr[0][1] = c;

		String[] secondRowArr = secondRow.split(" ");

		if (secondRowArr.length != Cube.SIZE) {
			throw new IllegalArgumentException("Incorrect number of colors entered.");
		}

		c = STRING_TO_COLOR_MAP.get(secondRowArr[0]);
		if (c == null) {
			throw new IllegalArgumentException("One of the colors you entered was not recognized.");
		}
		arr[1][0] = c;

		c = STRING_TO_COLOR_MAP.get(secondRowArr[1]);
		if (c == null) {
			throw new IllegalArgumentException("One of the colors you entered was not recognized.");
		}
		arr[1][1] = c;

		return arr;
	}
}
