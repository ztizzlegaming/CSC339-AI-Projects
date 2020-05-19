import java.util.ArrayList;
import java.util.List;

/**
 * The <code>Clause</code> class is used to represent a single clause in a SAT sentence.
 * It converts a String, like "-1 2 3 0" to a List of SymbolValue objects
 * 
 * @author Jordan Turley
 */
public class Clause {
	private List<SymbolValue> literals;
	
	public Clause(String clauseStr) {
		if (clauseStr.length() == 0) { //Make sure the clause String isn't empty
			throw new IllegalArgumentException("The Clause String cannot be empty.");
		}
		
		literals = new ArrayList<SymbolValue>();
		
		//Go through each part of the string. Each literal should be separated by a space
		String[] vars = clauseStr.split(" ");
		for (String var : vars) {
			boolean bool = true;
			String symbol = "";
			if (var.charAt(0) == '-') { //If there is a '-', the value is false
				bool = false;
				symbol = var.substring(1);
			} else {
				symbol = var;
			}
			
			//If you find a '0', you know it is the end of the line, break out
			if (symbol.equals("0")) {
				break;
			}
			SymbolValue node = new SymbolValue(symbol, bool);
			literals.add(node);
		}
	}
	
	/**
	 * @return The number of literals in this clause
	 */
	public int size() {
		return literals.size();
	}
	
	/**
	 * @return The List of literals that make up this clause
	 */
	public List<SymbolValue> getLiterals() {
		return literals;
	}
	
	/**
	 * Checks if a given SymbolValue is in this clause
	 * @param sv The SymbolValue to check for
	 * @return True or false if the SymbolValue is in the clause
	 */
	public boolean contains(SymbolValue sv) {
		return literals.contains(sv);
	}
	
	@Override
	public String toString() {
		String str = "";
		for (SymbolValue node : literals) {
			str += node + " ";
		}
		return str;
	}
}
