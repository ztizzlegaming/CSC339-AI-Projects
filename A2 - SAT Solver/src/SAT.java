import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * The <code>SAT</code> class is used for solving boolean satisfiability problems. It reads in
 * a DIMACS file, and you can call solve() to solve it.
 * 
 * @author Jordan Turley
 */
public class SAT {
	private int numBooleans;
	private int numClauses;
	
	private List<Clause> clauses;
	private List<String> symbols;
	
	private SATStrategy strategy;
	
	/**
	 * Creates a new SAT object, from a given filename, using the algorithm given by the strategy
	 * @param filename The filename of the DIMACS file to read
	 * @param strategy The algorithm to use
	 * @throws FileNotFoundException If the DIMACS file could not be found
	 */
	public SAT(String filename, SATStrategy strategy) throws FileNotFoundException {
		this.strategy = strategy;
		
		clauses = new ArrayList<Clause>();
		symbols = new ArrayList<String>();
		
		Scanner s = new Scanner(new File(filename));
		while (s.hasNextLine()) {
			String line = s.nextLine().trim();
			char first = line.charAt(0);
			if (first == 'c') {
				continue;
			} else if (first == 'p') {
				String[] parts = line.split(" ");
				if (parts.length != 4) {
					throw new IllegalStateException("One of the lines of the DIMACS file was not formed right.");
				}
				
				String numBooleansStr = parts[2];
				numBooleans = Integer.parseInt(numBooleansStr); 
				
				String numClausesStr = parts[3];
				numClauses = Integer.parseInt(numClausesStr);
			} else {
				Clause clause = new Clause(line);
				clauses.add(clause);
			}
		}
		
		s.close();
		
		for (int i1 = 1; i1 <= numBooleans; i1++) {
			String str = String.valueOf(i1);
			symbols.add(str);
		}
	}
	
	/**
	 * Tries to solve the SAT problem using the algorithm given
	 * @return The Model of the solved SAT problem
	 * @throws UnsatisfiableException If the problem was unsatisfiable
	 */
	public Model solve() throws UnsatisfiableException {
		return strategy.solve(clauses, symbols);
	}
	
	/**
	 * Checks if all of the clauses are satisfied by the given model
	 * @param clauses The clauses of the problem
	 * @param model The model of the problem
	 * @return True or false if all clauses are true or not
	 */
	public static boolean checkClausesTrueWRTModel(List<Clause> clauses, Model model) {
		for (Clause clause : clauses) { //Go through each clause and get the List of literals
			List<SymbolValue> literals = clause.getLiterals();
			
			//Assume the clause is unsatisfied until we find a variable in the model that satisfies it
			boolean clauseSuccess = false; 
			
			for (SymbolValue node : literals) {
				String symbol = node.getSymbol();
				SymbolValue nodeFromModel = getVarFromModel(symbol, model);
				//Check if this symbol from the model satisfies the clause
				if (nodeFromModel != null) {
					if (nodeFromModel.equals(node)) {
						clauseSuccess = true;
						break;
					}
				}
			}
			
			//If we find one clause that is not satisfied, return false
			if (!clauseSuccess) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Checks if there is at least one clause that is not satisfied. Basically a reverse of
	 * the above method
	 * @param clauses The clauses of the problem
	 * @param model The Model of the problem
	 * @return True or false if there is at least one unsatisfied clause
	 */
	public static boolean checkClausesFalseWRTModel(List<Clause> clauses, Model model) {
		for (Clause clause : clauses) {
			List<SymbolValue> literals = clause.getLiterals();
			
			//Assume the clause fails until we find one variable in the model that satisfies it
			boolean clauseFailure = true;
			
			for (SymbolValue node : literals) {
				String symbol = node.getSymbol();
				SymbolValue nodeFromModel = getVarFromModel(symbol, model);
				if ((nodeFromModel != null && nodeFromModel.equals(node)) || nodeFromModel == null) {
					clauseFailure = false;
					break;
				}
			}
			
			if (clauseFailure) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Gets a SymbolVariable from the Model for a given symbol
	 * @param symbol The String symbol
	 * @param model The Model to get the SymbolValue from
	 * @return The SymbolValue, or null if one could not be found
	 */
	public static SymbolValue getVarFromModel(String symbol, Model model) {
		for (SymbolValue node : model) {
			if (node.getSymbol().equals(symbol)) {
				return node;
			}
		}
		return null;
	}
	
	@Override
	public String toString() {
		String str = "Booleans: " + numBooleans + ", Clauses: " + numClauses + "\n";
		for (Clause clause : clauses) {
			str += clause + "\n";
		}
		return str;
	}
	
	/* public static void main(String[] args) {
		List<Clause> clauses = new ArrayList<Clause>();
		clauses.add(new Clause("1 -2"));
		//clauses.add(new Clause("-1 -3"));
		//clauses.add(new Clause("2 -4"));
		
		List<String> symbols = new ArrayList<String>();
		symbols.add("4");
		
		Model model = new Model();
		model.add(new Node("1", false));
		model.add(new Node("2", false));
		//model.add(new Node("3", false));
		//model.add(new Node("4", false));
		
		//System.out.println(findPureSymbol(clauses, symbols, model));
		
		//System.out.println(checkClausesTrueWRTModel(clauses, model));
		
		System.out.println(checkClausesFalseWRTModel(clauses, model));
	} */
}
