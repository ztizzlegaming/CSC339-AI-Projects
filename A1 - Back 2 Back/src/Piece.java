import java.util.HashMap;

/**
 * The <code>Piece</code> class represents a game piece with some pegs 1 deep (only on one side)
 * and some pegs 2 deep (goes through to the other side).
 * 
 * @author Jordan Turley
 * @author Will Edwards
 */
public class Piece implements Cloneable {
	public static final int NUM_PIECES = 11;
	public static final Piece[] ALL_PIECES = new Piece[NUM_PIECES];
	public static final HashMap<Character, String> COLOR_DICTIONARY = new HashMap<Character, String>();
	static {
		initPieces();
	}
	
	private int[][] layout;
	private char color;
	private boolean placed;
	
	/**
	 * Creates a new Piece object with a layout.
	 * @param layout A 2D array of integers representing the layout of the pegs of this piece
	 * @param color A character to represent the color of this piece, to tell it from other pieces
	 * @throws IllegalArgumentException If there is no peg 2 deep
	 */
	private Piece(int[][] layout, char color) throws IllegalArgumentException {
		boolean hasPeg2Deep = false;
		for (int y = 0; y < layout.length; y++) {
			for (int x = 0; x < layout[0].length; x++) {
				if (layout[y][x] == 2) {
					hasPeg2Deep = true;
				}
			}
		}
		if (!hasPeg2Deep) {
			throw new IllegalArgumentException("There was no peg 2 deep");
		}
		
		this.layout = layout;
		this.color = color;
		placed = false;
	}
	
	/**
	 * Rotates the game piece 90 degrees to the right
	 * @throws IllegalStateException If the piece has already been placed on the board
	 */
	public void rotate() throws IllegalStateException {
		if (placed) {
			throw new IllegalStateException("This piece cannot be rotated, it is already placed on the board.");
		}
		
		int origHeight = layout.length;
		int origWidth = layout[0].length;
		
		int rotatedArrHeight = origWidth;
		int rotatedArrWidth = origHeight;
		
		//{{1,  2,  3,  4},
		// {5,  6,  7,  8},
		// {9, 10, 11, 12}}
		
		//{{ 9, 5, 1},
		// {10, 6, 2},
		// {11, 7, 3},
		// {12, 8, 4}}
		
		int[][] rotatedArr = new int[rotatedArrHeight][rotatedArrWidth];
		for (int y = 0; y < rotatedArrHeight; y++) {
			for (int x = 0; x < rotatedArrWidth; x++) {
				rotatedArr[y][x] = layout[origHeight - 1 - x][y];
			}
		}
		layout = rotatedArr;
	}
	
	public boolean isPlaced() {
		return placed;
	}
	
	/**
	 * Sets the piece as placed on the board
	 */
	public void place() {
		placed = true;
	}
	
	/**
	 * Sets this piece as not placed on the board. Used when clearing the board
	 */
	public void unplace() {
		placed = false;
	}
	
	/**
	 * @return The 2D array of ints representing the layout of the piece
	 */
	public int[][] getLayout() {
		return layout;
	}
	
	public char getColor() {
		return color;
	}
	
	/**
	 * Calculates the cost of placing this piece, by the number of positions filled on the board
	 * @return The cost of placing this piece
	 */
	public double getCost() {
		double count = 0;
		for (int y = 0; y < layout.length; y++) {
			for (int x = 0; x < layout[0].length; x++) {
				count += layout[y][x];
			}
		}
		return count;
	}
	
	@Override
	public Object clone() {
		Object o = null;
		try {
			o = super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return o;
	}
	
	/* @Override
	public boolean equals(Object o) {
		if (o instanceof Piece) {
			Piece p = (Piece) o;
			
		} else {
			return false;
		}
	} */
	
	/**
	 * @return A String representation of the piece, with the color and layout
	 */
	@Override
	public String toString() {
		String colorStr = COLOR_DICTIONARY.get(color);
		String str = "Color: " + colorStr + "\n";
		for (int y = 0; y < layout.length; y++) {
			for (int x = 0; x < layout[0].length; x++) {
				str += layout[y][x] + " ";
			}
			str += "\n";
		}
		return str;
	}
	
	/**
	 * Sets up the pieces array and char-string color dictionary<br>
	 * Color codes:<br>
	 * P: Pink<br>
	 * B: Dark Blue<br>
	 * G: Light Green<br>
	 * R: Red<br>
	 * Y: Yellow<br>
	 * O: Orange<br>
	 * U: Purple<br>
	 * E: Dark Green<br>
	 * T: Teal<br>
	 * L: Blue
	 */
	private static void initPieces() {
		int[][] layout1 =
			{
				{0, 1},
				{2, 2}
			};
		Piece piece1 = new Piece(layout1, 'P');
		ALL_PIECES[0] = piece1;
		COLOR_DICTIONARY.put('P', "Pink");
		
		int[][] layout2 =
			{
				{0, 1},
				{2, 2}
			};
		Piece piece2 = new Piece(layout2, 'P');
		ALL_PIECES[1] = piece2;
		
		int[][] layout3 = 
			{
				{1, 0},
				{2, 2}
			};
		Piece piece3 = new Piece(layout3, 'B');
		ALL_PIECES[2] = piece3;
		COLOR_DICTIONARY.put('B', "Dark Blue");
		
		int[][] layout4 =
			{
				{0, 0, 2},
				{2, 1, 1}
			};
		Piece piece4 = new Piece(layout4, 'G');
		ALL_PIECES[3] = piece4;
		COLOR_DICTIONARY.put('G', "Light Green");
		
		int[][] layout5 =
			{
				{1, 2, 2}
			};
		Piece piece5 = new Piece(layout5, 'R');
		ALL_PIECES[4] = piece5;
		COLOR_DICTIONARY.put('R', "Red");
		
		int[][] layout6 =
			{
				{0, 1, 0},
				{1, 2, 2}
			};
		Piece piece6 = new Piece(layout6, 'Y');
		ALL_PIECES[5] = piece6;
		COLOR_DICTIONARY.put('Y', "Yellow");
		
		int[][] layout7 =
			{
				{0, 0, 1},
				{2, 1, 2}
			};
		Piece piece7 = new Piece(layout7, 'O');
		ALL_PIECES[6] = piece7;
		COLOR_DICTIONARY.put('O', "Orange");
		
		int[][] layout8 =
			{
				{0, 1, 1},
				{2, 2, 0}
			};
		Piece piece8 = new Piece(layout8, 'U');
		ALL_PIECES[7] = piece8;
		COLOR_DICTIONARY.put('U', "Purple");
		
		int[][] layout9 =
			{
				{0, 2},
				{2, 1}
			};
		Piece piece9 = new Piece(layout9, 'E');
		ALL_PIECES[8] = piece9;
		COLOR_DICTIONARY.put('E', "Dark Green");
		
		int[][] layout10 =
			{
				{2, 1, 2}
			};
		Piece piece10 = new Piece(layout10, 'T');
		ALL_PIECES[9] = piece10;
		COLOR_DICTIONARY.put('T', "Teal");
		
		int[][] layout11 =
			{
				{1, 0, 0},
				{2, 1, 2}
			};
		Piece piece11 = new Piece(layout11, 'L');
		ALL_PIECES[10] = piece11;
		COLOR_DICTIONARY.put('L', "Light Blue");
	}
	
	/**
	 * Prints out all of the color char codes and the actual String for that color
	 */
	public static void printColorKeys() {
		for (Character key : COLOR_DICTIONARY.keySet()) {
			String color = COLOR_DICTIONARY.get(key);
			System.out.println(key + ": " + color);
		}
	}
}
