import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

/**
 * The <code>Board</code> class represents the actual game board in Back 2 Back that you put Pieces on.
 * 
 * @author Jordan Turley
 * @author Will Edwards
 */
public class Board implements Cloneable {
	public static final int WIDTH = 6;
	public static final int HEIGHT = 5;
	
	private boolean sideFront; //True if front side, false if back side
	
	private BoardCell[][] boardArr;
	
	private Piece[] pieces;
	
	/**
	 * Creates a new empty Board object, of size WIDTH and HEIGHT.
	 */
	public Board() {
		boardArr = new BoardCell[HEIGHT][WIDTH];
		for (int y = 0; y < HEIGHT; y++) {
			for (int x = 0; x < WIDTH; x++) {
				boardArr[y][x] = new BoardCell();
			}
		}
		sideFront = true;
		
		pieces = Piece.ALL_PIECES.clone();
	}
	
	/**
	 * Horizontally flips the board, as if the user was turning the game around to see the other side.
	 */
	public void switchSide() {
		sideFront = !sideFront;
		
		//horizontally flip boardArr array
		BoardCell[][] newBoardArr = new BoardCell[HEIGHT][WIDTH];
		for (int y = 0; y < HEIGHT; y++) {
			for (int x = 0; x < WIDTH; x++) {
				BoardCell cell = boardArr[y][x];
				newBoardArr[y][WIDTH - 1 - x] = cell;
			}
		}
		boardArr = newBoardArr;
	}
	
	/**
	 * Adds a game piece to the board, at the x-y location
	 * @param piece The piece to add to the board
	 * @param xLoc The x location to add the piece
	 * @param yLoc The y location to add the piece
	 * @throws IllegalArgumentException When there is an error placing the piece,
	 * such as the piece hanging off the side or illegally overlapping another piece.
	 * Details for the reason this was thrown can be found in the exception's message.
	 */
	public void addPiece(Piece piece, int xLoc, int yLoc) throws IllegalArgumentException {
		//Make sure the x-y location is on the board
		if (xLoc < 0 || yLoc < 0 || xLoc >= WIDTH || yLoc >= HEIGHT) {
			throw new IllegalArgumentException("The x-y loc was out of bounds of the board.");
		}
		
		if (piece.isPlaced()) {
			throw new IllegalArgumentException("That piece has already been placed.");
		}
		
		//Make sure the piece does not hang off the side of the board
		int[][] pieceLayout = piece.getLayout();
		int pieceHeight = pieceLayout.length;
		int pieceWidth = pieceLayout[0].length;
		if (xLoc + pieceWidth > WIDTH || yLoc + pieceHeight > HEIGHT) {
			throw new IllegalArgumentException("The piece cannot hang off the side of the board.");
		}
		
		//Check if it can be placed there
		for (int y = 0; y < pieceHeight; y++) {
			for (int x = 0; x < pieceWidth; x++) {
				int pieceCell = pieceLayout[y][x];
				BoardCell boardCell = boardArr[yLoc + y][xLoc + x];
				if (!checkBoardCell(pieceCell, boardCell)) {
					throw new IllegalArgumentException("The piece cannot overlap with another piece on the same side.");
				}
			}
		}
		
		//Then actually place it there
		for (int y = 0; y < pieceHeight; y++) {
			for (int x = 0; x < pieceWidth; x++) {
				int pieceCell = pieceLayout[y][x];
				BoardCell boardCell = boardArr[yLoc + y][xLoc + x];
				setBoardCell(pieceCell, piece.getColor(), boardCell);
			}
		}
		
		//Mark the piece as being placed so it can't be used again
		piece.place();
		
	}
	
	/**
	 * Checks if one cell of a piece can be placed on a cell of the board
	 * @param pieceCell 0, 1, or 2 for the depth of the cell of the piece
	 * @param boardCell The board cell you are trying to place the piece on
	 * @return True or false if it can or cannot be placed there.
	 */
	private boolean checkBoardCell(int pieceCell, BoardCell boardCell) {
		if (pieceCell == 2) {
			if (boardCell.getFront() || boardCell.getBack()) {
				return false;
			}
		} else if (pieceCell == 1) {
			if (sideFront) {
				if (boardCell.getFront()) {
					return false;
				}
			} else {
				if (boardCell.getBack()) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Sets a piece cell at a board cell, once it has already been checked if it can be placed there.
	 * @param pieceCell 0, 1, or 2 for the depth of the cell of the piece
	 * @param pieceColor A char representing the color of the piece
	 * @param boardCell The board cell you are trying to place the piece on
	 */
	private void setBoardCell(int pieceCell, char pieceColor, BoardCell boardCell){
		if (pieceCell == 2) {
			boardCell.setFront(true);
			boardCell.setFrontColor(pieceColor);
			boardCell.setBack(true);
			boardCell.setBackColor(pieceColor);
		} else if (pieceCell == 1) {
			if (sideFront) {
				boardCell.setFront(true);
				boardCell.setFrontColor(pieceColor);
			} else {
				boardCell.setBack(true);
				boardCell.setBackColor(pieceColor);
			}
		}
	}
	
	/**
	 * Clears the board, sets all pieces to unplaced, and sets the side to the front of the board
	 */
	public void clearBoard() {
		for (int y = 0; y < HEIGHT; y++) {
			for (int x = 0; x < WIDTH; x++) {
				BoardCell cell = boardArr[y][x];
				cell.setFront(false);
				cell.setBack(false);
			}
		}
		for (Piece piece : pieces) {
			piece.unplace();
		}
		sideFront = true;
	}
	
	/**
	 * Sets the initial state of the board, before it is solved 
	 * @param state The initial state number from the game booklet. Must be between 1 and 60
	 */
	public void setInitialState(int state) {
		clearBoard();
		switch (state) {
		//k = 9
		case 1:
			//Pink
			pieces[0].rotate();
			pieces[0].rotate();
			addPiece(pieces[0], 0, 0);
			
			//Purple
			pieces[7].rotate();
			pieces[7].rotate();
			addPiece(pieces[7], 3, 0);
			
			//Light Green
			pieces[3].rotate();
			pieces[3].rotate();
			pieces[3].rotate();
			addPiece(pieces[3], 1, 2);
			
			//Orange
			addPiece(pieces[6], 3, 3);
			
			switchSide();
			
			//Dark Blue
			pieces[2].rotate();
			addPiece(pieces[2], 0, 1);
			
			//Yellow
			pieces[5].rotate();
			pieces[5].rotate();
			pieces[5].rotate();
			addPiece(pieces[5], 0, 2);
			
			//Teal
			pieces[9].rotate();
			addPiece(pieces[9], 2, 0);
			
			//Red
			pieces[4].rotate();
			pieces[4].rotate();
			pieces[4].rotate();
			addPiece(pieces[4], 3, 0);
			
			//Dark Green
			pieces[8].rotate();
			pieces[8].rotate();
			pieces[8].rotate();
			addPiece(pieces[8], 4, 1);
			
			break;
			
		//k = 8
		case 2:
			//Light Green
			addPiece(pieces[3], 0, 0);
			
			//Red
			pieces[4].rotate();
			pieces[4].rotate();
			addPiece(pieces[4], 0, 2);
			
			//Pink
			pieces[0].rotate();
			addPiece(pieces[0], 0, 3);
			
			//Orange
			addPiece(pieces[6], 2, 2);
			
			//Teal
			pieces[9].rotate();
			addPiece(pieces[9], 5, 0);
			
			//Dark Blue
			pieces[2].rotate();
			pieces[2].rotate();
			pieces[2].rotate();
			addPiece(pieces[2], 4, 3);
			
			switchSide();
			
			//Purple
			pieces[7].rotate();
			addPiece(pieces[7], 2, 0);
			
			//Light Blue
			pieces[10].rotate();
			pieces[10].rotate();
			pieces[10].rotate();
			addPiece(pieces[10], 1, 2);
			
			break;
		
		//k = 9
		case 3:
			//Orange
			pieces[6].rotate();
			pieces[6].rotate();
			pieces[6].rotate();
			addPiece(pieces[6], 0, 0);
			
			//Dark Green
			pieces[8].rotate();
			addPiece(pieces[8], 2, 0);
			
			//Dark Blue
			addPiece(pieces[2], 4, 0);
			
			//Pink
			pieces[0].rotate();
			pieces[0].rotate();
			addPiece(pieces[0], 2, 3);
			
			//Light Blue
			pieces[10].rotate();
			pieces[10].rotate();
			pieces[10].rotate();
			addPiece(pieces[10], 4, 2);
			
			switchSide();
			
			//Light Green
			addPiece(pieces[3], 2, 3);
			
			//Yellow
			pieces[5].rotate();
			pieces[5].rotate();
			pieces[5].rotate();
			addPiece(pieces[5], 0, 2);
			
			//Red
			pieces[4].rotate();
			addPiece(pieces[4], 5, 0);
			
			//Purple
			addPiece(pieces[7], 2, 1);
			
			break;
			
		//k = 9
		case 4:
			//Pink
			pieces[0].rotate();
			addPiece(pieces[0], 0, 0);
			
			//Dark Blue
			addPiece(pieces[2], 2, 0);
			
			//Pink
			pieces[1].rotate();
			addPiece(pieces[1], 4, 0);
			
			//Orange
			addPiece(pieces[6], 2, 3);
			
			//Red
			pieces[4].rotate();
			addPiece(pieces[4], 5, 2);
			
			switchSide();
			
			//Light Green
			pieces[3].rotate();
			addPiece(pieces[3], 0, 0);
			
			//Light Blue
			pieces[10].rotate();
			pieces[10].rotate();
			addPiece(pieces[10], 2, 0);
			
			//Teal
			pieces[9].rotate();
			addPiece(pieces[9], 5, 2);
			
			//Dark Green
			pieces[8].rotate();
			pieces[8].rotate();
			pieces[8].rotate();
			addPiece(pieces[8], 3, 3);
			
			break;
			
		//k = 8
		case 5:
			//Dark Green
			pieces[8].rotate();
			pieces[8].rotate();
			pieces[8].rotate();
			addPiece(pieces[8], 1, 1);
			
			//Teal
			pieces[9].rotate();
			addPiece(pieces[9], 3, 0);
			
			//Yellow
			pieces[5].rotate();
			addPiece(pieces[5], 4, 0);
			
			//Pink
			pieces[0].rotate();
			pieces[0].rotate();
			pieces[0].rotate();
			addPiece(pieces[0], 0, 3);
			
			switchSide();
			
			//Purple
			pieces[7].rotate();
			pieces[7].rotate();
			addPiece(pieces[7], 2, 0);
			
			//Light Green
			pieces[3].rotate();
			pieces[3].rotate();
			pieces[3].rotate();
			addPiece(pieces[3], 4, 2);
			
			//Pink
			addPiece(pieces[1], 0, 3);
			
			//Dark Blue
			pieces[2].rotate();
			pieces[2].rotate();
			pieces[2].rotate();
			addPiece(pieces[2], 2, 3);
			
			break;
			
		//k = 7
		case 6:
			//Light Blue
			pieces[10].rotate();
			addPiece(pieces[10], 3, 0);
			
			switchSide();
			
			//Pink
			pieces[0].rotate();
			pieces[0].rotate();
			pieces[0].rotate();
			addPiece(pieces[0], 4, 0);
			
			//Red
			pieces[4].rotate();
			addPiece(pieces[4], 5, 2);
			
			//Teal
			addPiece(pieces[9], 1, 1);
			
			//Dark Blue
			pieces[2].rotate();
			addPiece(pieces[2], 0, 0);
			
			//Purple
			pieces[7].rotate();
			pieces[7].rotate();
			pieces[7].rotate();
			addPiece(pieces[7], 0, 2);
			
			//Yellow
			pieces[5].rotate();
			pieces[5].rotate();
			pieces[5].rotate();
			addPiece(pieces[5], 2, 2);
			
			break;
			
		//k = 7
		case 7:
			//Dark Green
			pieces[8].rotate();
			pieces[8].rotate();
			addPiece(pieces[8], 1, 0);
			
			//Teal
			pieces[9].rotate();
			addPiece(pieces[9], 3, 0);
			
			//Pink
			pieces[0].rotate();
			pieces[0].rotate();
			pieces[0].rotate();
			addPiece(pieces[0], 4, 0);
			
			//Dark Blue
			pieces[2].rotate();
			pieces[2].rotate();
			pieces[2].rotate();
			addPiece(pieces[2], 2, 3);
			
			switchSide();
			
			//Light Green
			pieces[3].rotate();
			pieces[3].rotate();
			pieces[3].rotate();
			addPiece(pieces[3], 0, 2);
			
			//Red
			addPiece(pieces[4], 3, 2);
			
			//Yellow
			addPiece(pieces[5], 3, 3);
			
			break;
		
		//k = 6
		case 8:
			//Teal
			addPiece(pieces[9], 0, 4);
			
			//Purple
			pieces[7].rotate();
			pieces[7].rotate();
			addPiece(pieces[7], 1, 0);
			
			//Pink
			pieces[0].rotate();
			pieces[0].rotate();
			pieces[0].rotate();
			addPiece(pieces[0], 4, 1);
			
			switchSide();
			
			//Light Green
			addPiece(pieces[3], 2, 0);
			
			//Dark Green
			pieces[8].rotate();
			pieces[8].rotate();
			pieces[8].rotate();
			addPiece(pieces[8], 4, 2);
			
			//Yellow
			pieces[5].rotate();
			pieces[5].rotate();
			pieces[5].rotate();
			addPiece(pieces[5], 0, 2);
			
			break;
			
		//k = 5
		case 11:
			//Light Green
			addPiece(pieces[3], 2, 2);
			
			//Pink
			pieces[0].rotate();
			addPiece(pieces[0], 0, 3);
			
			//Teal
			addPiece(pieces[9], 3, 4);
			
			switchSide();
			
			//Dark Blue
			addPiece(pieces[2], 4, 1);
			
			//Light Blue
			addPiece(pieces[10], 0, 0);
			
			break;
			
		//k = 4
		case 25:
			//Light Green
			pieces[3].rotate();
			pieces[3].rotate();
			pieces[3].rotate();
			addPiece(pieces[3], 2, 1);
			
			//Dark Green
			pieces[8].rotate();
			addPiece(pieces[8], 1, 3);
			
			switchSide();
			
			//Light Blue
			pieces[10].rotate();
			addPiece(pieces[10], 4, 0);
			
			//Purple
			pieces[7].rotate();
			pieces[7].rotate();
			pieces[7].rotate();
			addPiece(pieces[7], 2, 1);
			
			break;
			
		//k = 4
		case 26:
			//Yellow
			pieces[5].rotate();
			addPiece(pieces[5], 0, 1);

			//Purple
			pieces[7].rotate();
			addPiece(pieces[7], 2, 0);
			
			switchSide();
			
			//Light Blue
			pieces[10].rotate();
			pieces[10].rotate();
			addPiece(pieces[10], 2, 3);
			
			//Red
			pieces[4].rotate();
			pieces[4].rotate();
			addPiece(pieces[4], 0, 2);
			
			break;
			
		//k = 4
		case 35:
			//Dark Blue
			addPiece(pieces[2], 4, 0);
			
			//Red
			addPiece(pieces[4], 2, 4);
			
			switchSide();
			
			//Yellow
			addPiece(pieces[5], 3, 3);
			
			//Teal
			pieces[9].rotate();
			addPiece(pieces[9], 0, 2);
			
			break;
			
		//k = 3
		case 44:
			//Light Green
			pieces[3].rotate();
			pieces[3].rotate();
			addPiece(pieces[3], 1, 0);
			
			//Dark Green
			pieces[8].rotate();
			addPiece(pieces[8], 3, 2);
			
			switchSide();
			
			//Pink
			pieces[0].rotate();
			pieces[0].rotate();
			pieces[0].rotate();
			addPiece(pieces[0], 4, 0);
			
			break;
			
		//k = 3
		case 45:
			//Red
			addPiece(pieces[4], 3, 2);
			
			switchSide();
			
			//Light Blue
			pieces[10].rotate();
			pieces[10].rotate();
			addPiece(pieces[10], 0, 1);
			
			//Yellow
			pieces[5].rotate();
			pieces[5].rotate();
			addPiece(pieces[5], 2, 0);
			
			break;
			
		//k = 3
		case 46:
			//Pink
			pieces[0].rotate();
			pieces[0].rotate();
			pieces[0].rotate();
			addPiece(pieces[0], 2, 0);
			
			//Red
			pieces[4].rotate();
			addPiece(pieces[4], 0, 2);
			
			switchSide();
			
			//Light Blue
			pieces[10].rotate();
			addPiece(pieces[10], 4, 2);
			
			break;
			
		//k = 3
		case 47:
			//Orange
			pieces[6].rotate();
			pieces[6].rotate();
			pieces[6].rotate();
			addPiece(pieces[6], 4, 0);
			
			//Green
			pieces[3].rotate();
			pieces[3].rotate();
			addPiece(pieces[3], 0, 3);
			
			switchSide();
			
			//Red
			pieces[4].rotate();
			pieces[4].rotate();
			pieces[4].rotate();
			addPiece(pieces[4], 5, 1);
			
			break;
			
		//k = 3
		case 48:
			//Pink
			addPiece(pieces[0], 3, 3);
			
			switchSide();
			
			//Dark Green
			addPiece(pieces[8], 3, 3);
			
			//Yellow
			pieces[5].rotate();
			addPiece(pieces[5], 0, 2);
			
			break;
			
		//k = 2
		case 49:
			//Teal
			pieces[9].rotate();
			addPiece(pieces[9], 1, 0);
			
			//Orange
			addPiece(pieces[6], 0, 3);
			
			break;
		
		//k = 2
		case 50:
			//Orange
			addPiece(pieces[6], 0, 0);
			
			//Pink
			pieces[0].rotate();
			pieces[0].rotate();
			addPiece(pieces[0], 1, 2);
			
			break;
		
		case 51:
			//Orange
			addPiece(pieces[6], 2, 2);
			
			switchSide();
			
			//Teal
			pieces[9].rotate();
			addPiece(pieces[9], 5, 0);
			
			break;
		
		case 52:
			//Orange
			pieces[6].rotate();
			pieces[6].rotate();
			pieces[6].rotate();
			addPiece(pieces[6], 0, 0);
			
			switchSide();
			
			//Teal
			pieces[9].rotate();
			addPiece(pieces[9], 2, 0);
			
			break;
		
		case 53:
			switchSide();
			
			//Purple
			pieces[7].rotate();
			pieces[7].rotate();
			addPiece(pieces[7], 2, 0);
			
			//Yellow
			addPiece(pieces[5], 1, 3);
			
			break;

		case 54:
			//Dark Blue
			pieces[2].rotate();
			pieces[2].rotate();
			addPiece(pieces[2], 0, 0);
			
			//Pink
			pieces[0].rotate();
			addPiece(pieces[0], 0, 2);
			
			break;

		case 55:
			switchSide();
			
			//Light Blue
			pieces[10].rotate();
			pieces[10].rotate();
			pieces[10].rotate();
			addPiece(pieces[10], 4, 0);
			
			//Dark Green
			pieces[8].rotate();
			pieces[8].rotate();
			addPiece(pieces[8], 3, 0);
			
			break;

		case 56:
			switchSide();
			
			//Pink
			pieces[0].rotate();
			pieces[0].rotate();
			pieces[0].rotate();
			addPiece(pieces[0], 3, 0);
			
			//Dark Blue
			pieces[2].rotate();
			pieces[2].rotate();
			addPiece(pieces[2], 0, 3);
			
			break;

		case 57:
			switchSide();
			
			//Light Green
			pieces[3].rotate();
			pieces[3].rotate();
			pieces[3].rotate();
			addPiece(pieces[3], 1, 0);
			
			break;

		case 58:
			switchSide();
			
			//Light Green
			pieces[3].rotate();
			pieces[3].rotate();
			pieces[3].rotate();
			addPiece(pieces[3], 3, 1);
			
			break;

		case 59:
			//Pink
			pieces[0].rotate();
			pieces[0].rotate();
			pieces[0].rotate();
			addPiece(pieces[0], 3, 1);
			
			break;

		case 60:
			switchSide();
			
			//Purple
			addPiece(pieces[7], 1, 2);
			
			break;
			
		default:
			throw new IllegalArgumentException("The initial state number was not recognized. Number must be between 1 and 60.");
		}
		
		if (!sideFront) {
			switchSide();
		}
	}
	
	public BoardCell[][] getBoardArr() {
		return boardArr;
	}
	
	public boolean getSide() {
		return sideFront;
	}
	
	/**
	 * Solves the board in its current state using the A* algorithm
	 * @return A List of Action objects to represent the moves to a solved board
	 */
	public List<Action> solve() {
		PriorityQueue<NodeCost> q = new PriorityQueue<NodeCost>();
		Node startNode = new Node(this, null, null, 0);
		NodeCost startNodeCost = new NodeCost(startNode, 0);
		q.add(startNodeCost);
		
		Board goalBoard = new Board();
		BoardCell[][] goalBoardArr = new BoardCell[HEIGHT][WIDTH];
		for (int y = 0; y < HEIGHT; y++) {
			for (int x = 0; x < WIDTH; x++) {
				BoardCell bc = new BoardCell();
				bc.setFront(true);
				bc.setBack(true);
				goalBoardArr[y][x] = bc;
			}
		}
		goalBoard.boardArr = goalBoardArr;
		
		while (!q.isEmpty()) {
			NodeCost nc = q.remove();
			Node currentNode = nc.getNode();
			if (currentNode.getState().equals(goalBoard)) {
				ArrayList<Action> path = new ArrayList<Action>();
				Node node = currentNode;
				while (node != null && node.getAction() != null) {
					path.add(node.getAction());
					node = node.getParent();
				}
				return path;
			} else {
				ArrayList<NodeCost> children = getChildren(currentNode);
				for (NodeCost child : children) {
					q.add(child);
				}
			}
		}
		
		return null;
	}
	
	private static ArrayList<NodeCost> getChildren(Node node) {
		ArrayList<NodeCost> list = new ArrayList<NodeCost>();
		
		int numPieces = node.getState().getAvailablePieces().size();
		
		for (int pieceNum = 0; pieceNum < numPieces; pieceNum++) {
			for (int boardSide = 0; boardSide < 2; boardSide++) {
				for (int y = 0; y < HEIGHT; y++) {
					for (int x = 0; x < WIDTH; x++) {
						for (int rotation = 0; rotation < 4; rotation++) {
							Board childState = (Board) node.getState().clone();
							if (boardSide == 1) {
								childState.switchSide();
							}
							
							ArrayList<Piece> pieces = childState.getAvailablePieces();
							Piece p = pieces.get(pieceNum);
							
							for (int i1 = 0; i1 < rotation; i1++) {
								p.rotate();
							}
							
							try {
								childState.addPiece(p, x, y);
								Action childAction = new Action(p, x, y, childState.getSide());
								
								double pathCost = node.getPathCost() + p.getCost();
								
								Node childNode = new Node(childState, node, childAction, pathCost);
								
								double functionCost = childNode.getPathCost() + childState.heuristic();
								NodeCost childNodeCost = new NodeCost(childNode, functionCost); 
								list.add(childNodeCost);
							} catch (IllegalArgumentException e) {
								//Tried to place piece on top of another, off the edge, etc...
								continue;
							}
						}
					}
				}
			}
		}
		
		return list;
	}
	
	public double heuristic() {
		double total = 0;
		for (int y = 0; y < HEIGHT; y++) {
			for (int x = 0; x < WIDTH; x++) {
				BoardCell bc = boardArr[y][x];
				if (!bc.getFront() && !bc.getBack()) {
					total += 2.0;
				} else if (!bc.getFront() || !bc.getBack()) {
					total += 1.5;
				}
			}
		}
		return total;
	}
	
	public ArrayList<Piece> getAvailablePieces() {
		ArrayList<Piece> pieces = new ArrayList<Piece>();
		for (Piece p : this.pieces) {
			if (!p.isPlaced()) {
				pieces.add(p);
			}
		}
		return pieces;
	}
	
	/**
	 * Prints out all of the pieces that have not been placed on the board yet
	 */
	public void printPieces() {
		ArrayList<Piece> pieces = getAvailablePieces();
		for (int i1 = 0; i1 < pieces.size(); i1++) {
			System.out.println("Piece " + (i1 + 1) + ":");
			System.out.println(pieces.get(i1));
		}
	}
	
	/**
	 * @return A string representation of the board for the user to see
	 */
	@Override
	public String toString() {
		String str = "Current side: ";
		if (sideFront) {
			str += "front";
		} else {
			str += "back";
		}
		str += "\n";
		for (int y = 0; y < HEIGHT; y++) {
			for (int x = 0; x < WIDTH; x++) {
				BoardCell cell = boardArr[y][x];
				if (sideFront) {
					if (cell.getFront()) {
						str += "|" + cell.getFrontColor();
					} else if (cell.getBack()) {
						str += "|" + Character.toLowerCase(cell.getBackColor());
					} else {
						str += "| ";
					}
				} else {
					if (cell.getBack()) {
						str += "|" + cell.getBackColor();
					} else if (cell.getFront()) {
						str += "|" + Character.toLowerCase(cell.getFrontColor());
					} else {
						str += "| ";
					}
				}
				if (x == WIDTH - 1) {
					str += "|";
				}
			}
			str += "\n";
		}
		return str;
	}
	
	@Override
	public Object clone() {
		Object o = null;
		try {
			o = super.clone();
			Board b = (Board) o;
			b.boardArr = boardArr.clone();
			for (int i1 = 0; i1 < boardArr.length; i1++) {
				b.boardArr[i1] = boardArr[i1].clone();
			}
			for (int y = 0; y < b.boardArr.length; y++) {
				for (int x = 0; x < b.boardArr[0].length; x++) {
					b.boardArr[y][x] = (BoardCell) boardArr[y][x].clone();
				}
			}
			b.pieces = pieces.clone();
			for (int i1 = 0; i1 < pieces.length; i1++) {
				b.pieces[i1] = (Piece) pieces[i1].clone();
			}
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return o;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Board) {
			Board b = (Board) o;
			BoardCell[][] otherBoardArr = b.getBoardArr();
			for (int y = 0; y < HEIGHT; y++) {
				for (int x = 0; x < WIDTH; x++) {
					BoardCell tBC = boardArr[y][x];
					BoardCell oBC = otherBoardArr[y][x];
					if (tBC.getFront() != oBC.getFront() || tBC.getBack() != oBC.getBack()) {
						return false;
					}
				}
			}
			return true;
		}
		return false;
	}
}
