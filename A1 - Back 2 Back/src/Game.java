import java.util.Scanner;

/**
 * The <code>Game</code> class is used for a user to play the Back 2 Back game.
 * It gets input from the user, such as to show the board, or place a piece on the board,
 * and executes that action.
 * 
 * @author Jordan Turley
 */
public class Game {
	public static void main(String[] args) {
		Board board = new Board();
		
		Scanner scanner = new Scanner(System.in);
		
		printInstructions();
		
		String input = "";
		do {
			input = scanner.nextLine();
			if (input.length() == 0) {
				continue;
			}
			String[] inputParts = input.split(" ");
			String firstInput = inputParts[0];
			
			if (firstInput.equals("board")) {
				System.out.println(board);
			} else if (firstInput.equals("pieces")) {
				board.printPieces();
			} else if (firstInput.equals("piece")) {
				if (inputParts.length != 2) {
					errMsg();
					continue;
				}
				String pieceNumStr = inputParts[1];
				if (!pieceNumStr.matches("^-?\\d+$")) {
					errMsg("The piece number was not recognized.");
					continue;
				}
				int pieceNum = Integer.parseInt(pieceNumStr) - 1;
				if (pieceNum < 0 || pieceNum >= Piece.ALL_PIECES.length) {
					errMsg("The piece number was not recognized.");
					continue;
				}
				System.out.println(Piece.ALL_PIECES[pieceNum]);
			} else if (firstInput.equals("flipboard")) {
				board.switchSide();
				System.out.println("Board flipped:");
				System.out.println(board);
			} else if (firstInput.equals("clear")) {
				board.clearBoard();
				System.out.println("Board cleared");
			} else if (firstInput.equals("place")) {
				if (inputParts.length != 3) {
					errMsg();
					continue;
				}
				String pieceNumStr = inputParts[1];
				if (!pieceNumStr.matches("^-?\\d+$")) {
					errMsg("The piece number was not recognized.");
					continue;
				}
				int pieceNum = Integer.parseInt(pieceNumStr) - 1;
				if (pieceNum < 0 || pieceNum >= Piece.ALL_PIECES.length) {
					errMsg("The piece number was not valid.");
					continue;
				}
				Piece piece = Piece.ALL_PIECES[pieceNum];
				
				String xyLoc = inputParts[2];
				String[] xy = xyLoc.split(",");
				if (xy.length != 2) {
					errMsg("The x-y location was not recognized.");
					continue;
				}
				String xStr = xy[0];
				String yStr = xy[1];
				if (!xStr.matches("^-?\\d+$") || !yStr.matches("^-?\\d+$")) {
					errMsg("The x-y location was not recognized.");
					continue;
				}
				int x = Integer.parseInt(xStr) - 1;
				int y = Integer.parseInt(yStr) - 1;
				try {
					board.addPiece(piece, x, y);
					System.out.println("Piece placed.");
				} catch (IllegalArgumentException e) {
					errMsg(e.getMessage());
				}
			} else if (firstInput.equals("colorkeys")) {
				Piece.printColorKeys();
			} else if (firstInput.equals("rotate")) {
				if (inputParts.length != 2) {
					errMsg();
					continue;
				}
				String pieceNumStr = inputParts[1];
				if (!pieceNumStr.matches("^-?\\d+$")) {
					errMsg("The piece number was not recognized.");
					continue;
				}
				int pieceNum = Integer.parseInt(pieceNumStr) - 1;
				if (pieceNum < 0 || pieceNum >= Piece.ALL_PIECES.length) {
					errMsg("The piece number was not recognized.");
					continue;
				}
				try {
					Piece.ALL_PIECES[pieceNum].rotate();
					System.out.println("Piece " + pieceNumStr + " rotated.");
				} catch (IllegalStateException e) {
					errMsg(e.getMessage());
				}
			} else if (!firstInput.equals("exit")) {
				errMsg();
			}
		} while (!input.equals("exit"));
		
		scanner.close();
	}
	
	private static void printInstructions() {
		System.out.println("How to use:");
		System.out.println("'board' to print the board");
		System.out.println("'pieces' to print available pieces");
		System.out.println("'piece [piece number]' to print that specific piece");
		System.out.println("'flipboard' to turn the board around");
		System.out.println("'clear' to clear board");
		System.out.println("'place [piece number] [x loc],[y loc]' to place piece");
		System.out.println("(when placing a piece, the top-left will be placed at the x-y loc)");
		System.out.println("(the top left of the board is (1,1), one to the right is (2, 1)...)");
		System.out.println("'colorkeys' to print out the piece color-key and it's actual color");
		System.out.println("'rotate [piece number]' to rotate that piece 90 degrees to the right");
		System.out.println("'exit' to exit program");
		System.out.println();
	}

	private static void errMsg() {
		errMsg("Your command was not recognized.");
	}
	private static void errMsg(String msg) {
		System.err.println("Error: " + msg + " Try again");
	}
}
