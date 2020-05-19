
/**
 * The <code>SymbolValue</code> class stores a symbol as a String and a value for that symbol
 * 
 * @author Jordan Turley
 */
public class SymbolValue implements Cloneable {
	private String symbol;
	private boolean value;
	
	public SymbolValue(String symbol, boolean value) {
		this.symbol = symbol;
		this.value = value;
	}

	public String getSymbol() {
		return symbol;
	}

	public boolean getValue() {
		return value;
	}
	
	public void flipValue() {
		value = !value;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof SymbolValue) {
			SymbolValue n = (SymbolValue) o;
			return value == n.getValue() && symbol.equals(n.getSymbol());
		} else {
			return false;
		}
	}
	
	@Override
	public String toString() {
		return (value ? "" : "-") + symbol;
	}
	
	@Override
	public Object clone() {
		Object o = null;
		try {
			o = super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return o;
	}
}
