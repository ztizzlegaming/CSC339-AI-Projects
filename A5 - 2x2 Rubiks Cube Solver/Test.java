import java.util.List;

import com.jordanturley.Cube;
import com.jordanturley.astar.Action;

/**
 * Ignore this, I was just using it for testing
 * 
 * @author Jordan Turley
 */
public class Test {
	public static void main(String[] args) {
		Cube cube = new Cube();
		
		cube.turnRight();
		cube.turnTop();
		cube.turnRightInverse();
		cube.turnTopInverse();
		cube.turnFront();
		cube.turnLeftInverse();
		//cube.turnRightInverse();
		//cube.turnBack();
		
		List<Action> actions = cube.solve();
		System.out.println("Solution:");
		for (Action a : actions) {
			System.out.println(a);
		}
	}
}
