import java.util.List;

/**
 * @author Jordan Turley
 * @author Will Edwards
 */
public class Solver {
	public static void main(String[] args) {
		Board board = new Board();
		board.setInitialState(44);
		System.out.println(board);
		board.switchSide();
		System.out.println(board);

		long beforeTime = System.currentTimeMillis();

		List<Action> actions = board.solve();

		long afterTime = System.currentTimeMillis();

		double diff = (double) (afterTime - beforeTime) / 1000.0;

		System.out.println("Solving took: " + diff + " seconds");
		System.out.println();
		System.out.println("Solution:");

		for (Action a : actions) {
			System.out.println(a);
			System.out.println();
		}
	}
}
