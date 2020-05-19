import java.util.ArrayList;

/**
 * The <code>Model</code> class is an ArrayList of SymbolValues, but allows you to clone it,
 * and get a matching SymbolValue from it
 * 
 * @author Jordan Turley
 */
public class Model extends ArrayList<SymbolValue> implements Cloneable {
	private static final long serialVersionUID = 1L;

	public Model() {
		super();
	}
	
	/**
	 * Gets the SymbolValue (x) from this list where x.equals(sv)
	 * @param sv The SymbolValue to look for
	 * @return The same SymbolValue but pointing to this list, or null if it couldn't be found
	 */
	public SymbolValue getSymbolValue(SymbolValue sv) {
		for (SymbolValue symbolValue : this) {
			if (symbolValue.equals(sv)) {
				return symbolValue;
			}
		}
		return null;
	}
	
	@Override
	public Object clone() {
		Object o = super.clone();
		Model m = (Model) o;
		//Go through each item in this list and clone it, to get a deep copy
		for (int i1 = 0; i1 < size(); i1++) {
			SymbolValue orig = get(i1);
			SymbolValue copy = (SymbolValue) orig.clone();
			m.set(i1, copy);
		}
		return o;
	}
	
	@Override
	public String toString() {
		String str = "";
		for (SymbolValue node : this) {
			str += node + " ";
		}
		return str;
	}
}
