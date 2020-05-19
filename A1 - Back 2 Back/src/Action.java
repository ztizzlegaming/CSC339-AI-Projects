
/**
 * The <code>Action</code> class stores the action of a piece being placed on the board.
 * The Piece, x-y location, and the side of the board is stored.
 * 
 * @author Jordan Turley
 * @author Will Edwards
 */
public class Action {
	private Piece piece;
	private int x;
	private int y;
	private boolean sideFront;
	
	public Action(Piece piece, int x, int y, boolean sideFront) {
		this.piece = piece;
		this.x = x;
		this.y = y;
		this.sideFront = sideFront;
	}
	
	@Override
	public String toString() {
		String side = sideFront ? "front" : "back";
		return "Place " + piece + "at (" + (x + 1) + ", " + (y + 1) + ") on the " + side;
	}
}
