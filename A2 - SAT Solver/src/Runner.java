import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * The <code>Runner</code> class is used to run the SAT Solver on a given .cnf file.
 * <br>
 * You choose the file to run by modifying the FILE variable, and choose the algorithm to use
 * by passing either of the default algorithm objects stored in SATStrategy, or creating your own 
 * if there is something you want to customize (you can't customize anything for DPLL, but for
 * WalkSAT, you can set the probability and maxSteps)
 * 
 * @author Jordan Turley
 */
public class Runner {
	public static final String PATH = "tests" + File.separator;
	public static final String FILE = /* "basictest.cnf"; */ "f0040-07-s.cnf";

	public static void main(String[] args) {
		long beforeTime = System.currentTimeMillis();
		try {
			SAT sat = new SAT(PATH + FILE, SATStrategy.DEFAULT_WALKSAT_ALGORITHM);
			
			List<SymbolValue> solution = sat.solve();
			
			System.out.println("Solution:");
			for (SymbolValue node : solution) {
				System.out.print(node + " ");
			}
			System.out.println();
		} catch (UnsatisfiableException e) {
			System.out.println(e.getMessage());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		long afterTime = System.currentTimeMillis();
		long diff = afterTime - beforeTime;
		double diffSeconds = (double) diff / 1000;
		System.out.println("Time: " + diffSeconds + " seconds");
	}

}
