import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import org.sat4j.minisat.SolverFactory;
import org.sat4j.reader.DimacsReader;
import org.sat4j.reader.Reader;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;

/**
 * The <code>Runner</code> class tries to solve all of the 10 Hex Sudoku problems in SudokuPuzzles, and checks
 * if the solution is unique.
 * 
 * @author Jordan Turley
 */
public class Runner {
	public static final int NUM_PROBLEMS = 10;
	
	public static final String OUTPUT_FOLDER = "SudokuCNFFiles" + File.separator;
	
	public static void main(String[] args) {
		try {
			for (int i1 = 1; i1 <= NUM_PROBLEMS; i1++) {
				System.out.println("Problem " + i1);
				System.out.println();
				
				//Get filenames and create SudokuBoard object
				String filename = "SudokuPuzzles" + File.separator + "prob_" + i1 + ".inp";
				String output = i1 + "_sat.cnf";
				SudokuBoard board = new SudokuBoard(filename, OUTPUT_FOLDER + output);
				System.out.println(board);
				
				//Reduce board to sat and write it to the output cnf file
				board.satReduce();
				board.close();
				
				//Send the cnf file to the sat solver
				int[] model = getModelForSAT(OUTPUT_FOLDER + output);
				
				if (model == null) {
					//This should never happen unless the board has no solutions
					System.out.println("No solution!");
				} else {
					//Print out the board
					System.out.println("Solution");
					printBoard(model);
					
					//Go through the solution and flip each variable, to try to get a different solution
					String oppositeAllVarsClause = "";
					for (int modelIdx = 0; modelIdx < model.length; modelIdx++) {
						model[modelIdx] = model[modelIdx] * -1;
						oppositeAllVarsClause += model[modelIdx] + " ";
					}
					oppositeAllVarsClause.trim();
					oppositeAllVarsClause += "0";
					
					//Write the new clause to a new file
					BufferedReader br = new BufferedReader(new FileReader(OUTPUT_FOLDER + output));
					BufferedWriter bw = new BufferedWriter(new FileWriter(OUTPUT_FOLDER + "diff_" + output));
					
					//This goes in to the current file and gets the number of clauses, so it can be incremented
					//by one, when you add the new clause
					String firstLine = br.readLine();
					String[] firstLineParts = firstLine.split(" ");
					int numClauses = Integer.parseInt(firstLineParts[3]);
					numClauses++;
					String newFirstLine = firstLineParts[0] + " " + firstLineParts[1] + " " + firstLineParts[2] + " " + numClauses;
					
					//Write all of the old clauses and the new clause to the new file
					bw.write(newFirstLine + "\n");
					String curLine = br.readLine();
					while (curLine != null) {
						bw.write(curLine + "\n");
						curLine = br.readLine();
					}
					bw.write(oppositeAllVarsClause);
					
					br.close();
					bw.close();
					
					//Try to solve the new file
					int[] differentModel = getModelForSAT(OUTPUT_FOLDER + "diff_" + output);
					if (differentModel == null) {
						//If it is unsatisfiable, the solution is unique
						System.out.println("Unique solution!");
					} else {
						//If there's another solution to the board, show it
						System.out.println("Another solution:");
						printBoard(differentModel);
					}
					
					//Print out a divider between each problem
					if (i1 != NUM_PROBLEMS) {
						System.out.println();
						System.out.println("---------------------------------------");
						System.out.println();
					}
				}
			}
		} catch (Exception e) { //Ideally, no exceptions are thrown unless something completely goes wrong
			e.printStackTrace();
		}
	}
	
	/**
	 * Prints out the sudoku board from the given SAT model
	 * @param model The SAT model to print the board from
	 */
	private static void printBoard(int[] model) {
		for (int y = 0; y < SudokuBoard.SIZE; y++) {
			for (int x = 0; x < SudokuBoard.SIZE; x++) {
				for (int withinCell = 0; withinCell < SudokuBoard.SIZE; withinCell++) {
					int idx = withinCell + x * SudokuBoard.SIZE + y * SudokuBoard.SIZE * SudokuBoard.SIZE;
					int cur = model[idx];
					if (cur > 0) {
						String hex = Integer.toHexString(--cur % 16);
						System.out.print(hex);
						break;
					}
				}
				if (x % 4 == 3) {
					System.out.print(" ");
				}
			}
			System.out.println();
			if (y % 4 == 3) {
				System.out.println();
			}
		}
	}
	
	/**
	 * 
	 * @param cnfFilename The file to read in the SAT problem from
	 * @return The satisfying model for the given SAT problem in the file, or null if no solution is found
	 * @throws Exception If something goes wrong reading in the file or with SAT4J
	 */
	private static int[] getModelForSAT(String cnfFilename) throws Exception {
		ISolver solver = SolverFactory.newDefault();
		solver.setTimeout(60);
		Reader reader = new DimacsReader(solver);
		IProblem problem = reader.parseInstance(cnfFilename);
		if (problem.isSatisfiable()) {
			int[] model = problem.model();
			return model;
		} else {
			return null;
		}
	}
}
