import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * This is just how I collected data for my experiment for DPLL. It's not really important,
 * I basically just ran through each of the test files and got the time it took to solve it
 * 
 * @author Jordan Turley
 */
public class DPLLExperiment {
	public static void main(String[] args) {
		String str = "";
		for (int numBools = 20; numBools <= 40; numBools += 20) {
			for (int i1 = 1; i1 <= 8; i1++) {
				for (int solvableUnsolvable = 0; solvableUnsolvable < 2; solvableUnsolvable++) {
					long beforeTime = System.currentTimeMillis();
					try {
						String su = "";
						if (solvableUnsolvable == 0) {
							su = "s";
						} else {
							su = "u";
						}
						str = "tests" + File.separator + "f00" + numBools + "-0" + i1 + "-" + su + ".cnf";
						
						SAT sat = new SAT(str, SATStrategy.DEFAULT_DPLL_ALGORITHM);
						
						List<SymbolValue> solution = sat.solve();
						
						System.out.println(str);
						System.out.println("Solution:");
						for (SymbolValue node : solution) {
							System.out.print(node + " ");
						}
						System.out.println();
					} catch (UnsatisfiableException e) {
						System.out.println(str);
						System.out.println(e.getMessage());
					} catch (FileNotFoundException e) {
						e.printStackTrace();
					}
					long afterTime = System.currentTimeMillis();
					long diff = afterTime - beforeTime;
					double diffSeconds = (double) diff / 1000;
					System.out.println("Time: " + diffSeconds + " seconds\n-----------------------");
				}
			}
		}
	}
}
