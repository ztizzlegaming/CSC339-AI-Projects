import java.util.List;

/**
 * The <code>SATStrategy</code> class lets you implement a SAT solving algorithm, and choose
 * which algorithm you use at runtime. This is using the Strategy design pattern.
 * 
 * @author Jordan Turley
 */
public interface SATStrategy {
	public static final DPLLAlgorithm DEFAULT_DPLL_ALGORITHM = new DPLLAlgorithm();
	public static final WalkSATAlgorithm DEFAULT_WALKSAT_ALGORITHM = new WalkSATAlgorithm();
	
	/**
	 * Solves a SAT problem from the given clauses and symbols 
	 * @param clauses The clauses of the SAT problem
	 * @param symbols The symbols of the SAT problem
	 * @return The Model that satisfies all clauses in the problem
	 * @throws UnsatisfiableException If the problem is unsatisfiable
	 */
	public Model solve(List<Clause> clauses, List<String> symbols) throws UnsatisfiableException;
}
