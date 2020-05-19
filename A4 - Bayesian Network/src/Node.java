import java.util.ArrayList;
import java.util.List;

/**
 * The <code>Node</code> class stores the name of a node (usually one character, but doesn't have to be),
 * the names of the nodes it is dependent on, and each probability for the event (the CPT).
 * For dependent on, I mean, for example, the Alarm node would be dependent on Burglary and Earthquake 
 * It also stores the children of the node
 * 
 * @author Jordan Turley
 */
public class Node {
	private String name;
	private List<Probability> probabilitiesCPT;
	private List<String> conditions;
	
	private List<Node> children;
	
	/**
	 * Constructs a new Node object
	 * @param name The name of the node
	 * @param probabilitiesCPT The probability CPT of this node
	 * @param conditions The other nodes that this node is dependent on
	 */
	public Node(String name, List<Probability> probabilitiesCPT, List<String> conditions) {
		this.name = name;
		this.probabilitiesCPT = probabilitiesCPT;
		this.conditions = conditions;
		children = new ArrayList<Node>();
	}
	
	/**
	 * Gets the probability of this node being true or false, for a list of given values this node
	 * is dependent on. For example, if you have a node Z that is dependent on X and Y, you could
	 * pass in  that X is true and Y is false, and that you want the probability that Z is true, and it
	 * will give it to you
	 * @param depdendencies The values of the nodes that this node is dependent on
	 * @param trueFalse If you want the probability that this node is true or false
	 * @return The probability (an int greater than zero, less than 100)
	 */
	public int getProbabilityForDependencies(List<NodeValue> depdendencies, boolean trueFalse) {
		int probability = 0;
		
		//Go through each probability and find the one that matches the given values
		for (Probability p : probabilitiesCPT) {
			List<NodeValue> probabilityValues = p.getValues();
			if (probabilityValues.equals(depdendencies)) {
				probability = p.getProbability();
			}
		}
		
		//If they want the probability that it's false, do 1 - probability to get the complement
		if (!trueFalse) {
			probability = 100 - probability;
		}
		
		return probability;
	}
	
	/**
	 * Adds a child to this node
	 * @param child The child node to add
	 */
	public void addChild(Node child) {
		children.add(child);
	}
	
	/**
	 * @return The list of Node objects that are children of this node
	 */
	public List<Node> getChildren() {
		return children;
	}
	
	/**
	 * @return The list of node names that this Node is dependent on
	 */
	public List<String> getConditions() {
		return conditions;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
