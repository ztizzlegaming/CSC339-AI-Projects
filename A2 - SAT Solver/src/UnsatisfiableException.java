/**
 * The <code>UnsatisfiableException</code> exception is thrown when
 * a SAT problem is found to be unsatisfiable
 * 
 * @author Jordan Turley
 */
public class UnsatisfiableException extends Exception {
	private static final long serialVersionUID = 1L;

	public UnsatisfiableException() {
		super();
	}
	
	public UnsatisfiableException(String s) {
		super(s);
	}
}
