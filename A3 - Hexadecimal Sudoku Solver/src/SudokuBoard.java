import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * The <code>SudokuBoard</code> class is used to represent a hexadecimal sudoku problem, and to
 * reduce the problem to SAT.
 * 
 * @author Jordan Turley
 */
public class SudokuBoard {
	public static final int SQUARE_SIZE = 4;
	public static final int SIZE = SQUARE_SIZE * SQUARE_SIZE;
	public static final int NUM_BOOLEANS = SIZE * SIZE * SIZE;
	
	public static final int BASE_NUM_CLAUSES = 123904;
	
	private Integer[][] board;
	
	private int numAlreadySet;
	
	private BufferedWriter writer;
	
	/**
	 * Creates a new SudokuBoard object
	 * @param filename The filename to read the given starting numbers
	 * @param output The file to output to for the SAT reduction
	 * @throws IOException If an error occurs when reading in the file or creating the BufferedWriter
	 * for the SAT reduction
	 */
	public SudokuBoard(String filename, String output) throws IOException {
		board = new Integer[SIZE][SIZE];
		
		numAlreadySet = 0;
		
		//Read in the starting numbers
		Scanner scanner = new Scanner(new File(filename));
		for (int i1 = 0; i1 < SIZE; i1++) {
			String line = scanner.nextLine();
			for (int i2 = 0; i2 < line.length(); i2++) {
				String s = line.substring(i2, i2 + 1);
				if (!s.equals("_")) {
					numAlreadySet++;
					board[i1][i2] = Integer.parseInt(s, 16);
				}
			}
		}
		
		File file = new File(output);
		if (!file.exists()) {
            file.createNewFile();
        }
		FileWriter fw = new FileWriter(file.getAbsoluteFile());
        writer = new BufferedWriter(fw);
		
		scanner.close();
	}
	
	/**
	 * Reduces this Sudoku problem to SAT, and writes it to the output file given when creating the
	 * SudokuBoard.
	 * @throws IOException If there is some problem when writing to the file
	 */
	public void satReduce() throws IOException {
		//Calculate total clauses from the known number of base clauses and the number set
		//in the problem.
		int totalClauses = BASE_NUM_CLAUSES + numAlreadySet;
        writer.write("p cnf " + NUM_BOOLEANS + " " + totalClauses + "\n");
        
        //Start by going through the board and checking which
        //ones are already set, and add them as unit clauses
        for (int y = 0; y < SIZE; y++) {
        	for (int x = 0; x < SIZE; x++) {
        		if (board[y][x] != null) {
        			String c = board[y][x] + x * SIZE + y * SIZE * SIZE + 1 + "";
        			addClause(c);
        		}
        	}
        }
        
        //Go through all the individual cells and make sure only one variable can be in each cell
        for (int y = 0; y < SIZE; y++) {
        	for (int x = 0; x < SIZE; x++) {
        		//Calculate the starting index from the x and y
        		int start = x * SIZE + y * SIZE * SIZE + 1;
        		
        		//Build the base clause, that there has to be at least one number in each cell
        		String s = "";
        		List<Integer> nums = new ArrayList<Integer>();
        		
        		for (int i1 = 0; i1 < SIZE; i1++) {
        			int temp = start + i1;
        			s += temp + " ";
        			nums.add(temp);
        		}
        		
        		addClause(s);
        		
        		//Then, go through 
        		for (int fnum : nums) {
            		for (int snum : nums) {
            			if (snum > fnum) {
            				addClause("-" + fnum + " -" + snum);
            			}
            		}
            	}
        	}
        }
        
        //Make sure there is only one of each number in each row
        for (int row = 0; row < SIZE; row++) {
        	for (int num = 0; num < SIZE; num++) {
        		int start = row * SIZE * SIZE + num + 1; //Start is the starting idx of that row for that number
        		
            	String s = "";
            	
            	List<Integer> nums = new ArrayList<Integer>();
            	
            	for (int i1 = 0; i1 < SIZE; i1++) {
            		int x = start + i1 * SIZE;
            		s += x + " ";
            		nums.add(x);
            	}
            	
            	addClause(s);
            	
            	for (int fnum : nums) {
            		for (int snum : nums) {
            			if (snum > fnum) {
            				addClause("-" + fnum + " -" + snum);
            			}
            		}
            	}
        	}
        }
        
        //Make sure there is only one of each number in each column
        for (int col = 0; col < SIZE; col++) {
        	for (int num = 0; num < SIZE; num++) {
        		int start = col * SIZE + num + 1;
        		
        		String s = "";
        		
        		List<Integer> nums = new ArrayList<Integer>();
        		
        		for (int i1 = 0; i1 < SIZE; i1++) {
        			int x = start + i1 * SIZE * SIZE;
        			s += x + " ";
        			nums.add(x);
        		}
        		
        		addClause(s);
        		
        		for (int fnum : nums) {
            		for (int snum : nums) {
            			if (snum > fnum) {
            				addClause("-" + fnum + " -" + snum);
            			}
            		}
            	}
        	}
        }
        
        //Make sure there is only one of each number in the square
        for (int yBox = 0; yBox < SIZE / 4; yBox++) {
        	for (int xBox = 0; xBox < SIZE / 4; xBox++) {
        		for (int num = 0; num < SIZE; num++) {
        			int start = (xBox * SIZE * (SIZE / 4)) + (yBox * SIZE * SIZE * (SIZE / 4)) + num + 1;
            		
            		String s = "";
            		
            		List<Integer> nums = new ArrayList<Integer>();
            		
            		for (int y = 0; y < SIZE / 4; y++) {
            			for (int x = 0; x < SIZE / 4; x++) {
            				int temp = start + (x * SIZE + y * SIZE * SIZE) ;
            				s += temp + " ";
            				nums.add(temp);
            			}
            		}
            		
            		addClause(s);
            		
            		for (int fnum : nums) {
                		for (int snum : nums) {
                			if (snum > fnum) {
                				addClause("-" + fnum + " -" + snum);
                			}
                		}
                	}
        		}
        	}
        }
	}
	
	/**
	 * Writes a clause to the BufferedWriter. A "0" is added at the end of the line, so it
	 * should not be included in the given clause
	 * @param clause The clause to write
	 */
	private void addClause(String clause) {
		clause = clause.trim();
		try {
			writer.write(clause + " 0\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Closes the BufferedWriter that is used to output the problem to a cnf file
	 * @throws IOException If closing the writer throws this exception
	 */
	public void close() throws IOException {
		writer.close();
	}
	
	@Override
	public String toString() {
		String s = "";
		
		for (int y = 0; y < SIZE; y++) {
			for (int x = 0; x < SIZE; x++) {
				if (board[y][x] == null) {
					System.out.print("_");
				} else {
					String hex = Integer.toHexString(board[y][x]);
					System.out.print(hex);
				}
				if (x % 4 == 3) {
					System.out.print(" ");
				}
			}
			if (y != SIZE - 1) {
				System.out.println();
			}
			if (y % 4 == 3) {
				System.out.println();
			}
		}
		
		return s;
	}
}
