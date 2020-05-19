
/**
 * The <code>BoardCell</code> class represents one cell on the board for Back 2 Back.
 * It stores if the front or back is covered, and the color covering it.
 * 
 * @author Jordan Turley
 * @author Will Edwards
 */
public class BoardCell implements Cloneable {
	private boolean front;
	private char frontColor;
	
	private boolean back;
	private char backColor;
	
	public BoardCell() {
		setFront(false);
		setFrontColor(' ');
		
		setBack(false);
		setBackColor(' ');
	}

	public boolean getFront() {
		return front;
	}

	public void setFront(boolean front) {
		this.front = front;
	}

	public boolean getBack() {
		return back;
	}

	public void setBack(boolean back) {
		this.back = back;
	}
	
	public char getFrontColor() {
		return frontColor;
	}

	public void setFrontColor(char frontColor) {
		this.frontColor = frontColor;
	}

	public char getBackColor() {
		return backColor;
	}

	public void setBackColor(char backColor) {
		this.backColor = backColor;
	}

	@Override
	public String toString() {
		return "Front: " + front + ", back: " + back;
	}
	
	@Override
	public Object clone() {
		Object o = null;
		try {
			o = super.clone();
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return o;
	}
}
