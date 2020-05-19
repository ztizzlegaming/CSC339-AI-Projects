import java.util.List;

/**
 * The <code>Probability</code> class stores a List of NodeValue objects, to represent what has to be true
 * or false for this to be the probability, and an int for the probability itself. For example, for the
 * first Alarm probability, there would be a NodeValue that says Burglary is true and Earthquake is true
 * in the list, and probability would be equal to 97.
 * 
 * @author Jordan Turley
 */
public class Probability {
	private List<NodeValue> values;
	private int probability;
	
	public Probability(List<NodeValue> values, int probability) {
		this.values = values;
		this.probability = probability;
	}
	
	public List<NodeValue> getValues() {
		return values;
	}
	
	public int getProbability() {
		return probability;
	}
}
