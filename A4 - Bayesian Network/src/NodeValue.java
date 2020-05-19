
/**
 * The <code>NodeValue</code> stores the name of a node and a true or false value. This is used to say,
 * for example, a robbery did occur, or an earthquake did not occur, so you can pass this to the
 * alarm node to get a certain probability. Nothing special happens in this class, it just stores the values
 * 
 * @author Jordan Turley
 */
public class NodeValue {
	private String name;
	private boolean value;
	
	public NodeValue(String name, boolean value) {
		this.name = name;
		this.value = value;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean getValue() {
		return value;
	}
	
	@Override
	public String toString() {
		return name + ": " + value;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof NodeValue) {
			NodeValue nv = (NodeValue) o;
			return nv.getValue() == value && nv.getName().equals(name);
		}
		return false;
	}
}
