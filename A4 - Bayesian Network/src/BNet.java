import java.util.ArrayList;
import java.util.List;

/**
 * The <code>BNet</code> class gets input from the user for what probability they want to
 * calculate, then, using rejection sampling with 1 million trials, calculates the probability
 * 
 * You use it something like this: java BNet Bt Af given Mf
 * 
 * @author Jordan Turley
 */
public class BNet {
	/**
	 * The number of times to run rejection sampling.
	 */
	public static final int TRIALS = 1000000;
	
	/**
	 * topNodes stores all of the top level Nodes that are not dependent on any other nodes. This is
	 * how you start traversing the graph.
	 */
	private static List<Node> topNodes = new ArrayList<Node>();

	private static List<NodeValue> query = new ArrayList<NodeValue>();
	private static List<NodeValue> evidence = new ArrayList<NodeValue>();

	public static void main(String[] args) {
		setupBNet();
		readFromCommandLineArgs(args);
		
		double probability = rejectionSample();
		System.out.println(probability);
	}

	/**
	 * Parses the user's input from the command line arguments, putting the query variables
	 * into the query List, and evidence variables into the evidence List
	 * @param args The command line arguments from main
	 */
	private static void readFromCommandLineArgs(String[] args) {
		boolean queryVars = true;

		for (String str : args) {
			if (str.equals("given")) {
				//If they write given, change from adding them to the query vars to evidence
				queryVars = false;
				continue;
			}
			
			//If you want to use node names more than one character, you can. It takes the whole string up
			//to the last character as the node name, then the last character as a t or f.
			String nodeName = str.substring(0, str.length() - 1);
			char valueChar = str.charAt(str.length() - 1);
			boolean value = false;
			if (valueChar == 't') {
				value = true;
			}
			NodeValue nv = new NodeValue(nodeName, value);

			//Add it to the correct list
			if (queryVars) {
				query.add(nv);
			} else {
				evidence.add(nv);
			}
		}
	}

	/**
	 * Sets up the actual Bayesian network, with burglary, earthquake, alarm, John calls, and Mary calls.
	 * The probabilities are represented as an integer greater than 0 and less than 100. This code can
	 * be kind of confusing, but it's basically repeating the same thing for each node, it just depends
	 * on if the node depends on other nodes or not.
	 */
	private static void setupBNet() {
		//Burglary
		List<NodeValue> burglaryDependencies = new ArrayList<NodeValue>(); //Empty list, because doesn't depend on anything
		int burglaryProbabilityNum = 2;
		Probability burglaryProbability = new Probability(burglaryDependencies, burglaryProbabilityNum);

		List<Probability> burglaryProbabilities = new ArrayList<Probability>();
		burglaryProbabilities.add(burglaryProbability); //Burglary only has one probability: if it's true
		Node burglary = new Node("B", burglaryProbabilities, new ArrayList<String>());
		topNodes.add(burglary);

		//Earthquake
		List<NodeValue> earthquakeDependencies = new ArrayList<NodeValue>(); //Same thing, no dependencies
		int earthquakeProbabilityNum = 3;
		Probability earthquakeProbability = new Probability(earthquakeDependencies, earthquakeProbabilityNum);

		List<Probability> earthquakeProbabilities = new ArrayList<Probability>();
		earthquakeProbabilities.add(earthquakeProbability);
		Node earthquake = new Node("E", earthquakeProbabilities, new ArrayList<String>());
		topNodes.add(earthquake);

		//Alarm
		//Alarm depdends on B and E, so you have to make 4 probability objects for each combination of T/F
		//for them.
		List<NodeValue> alarmDependencies1 = new ArrayList<NodeValue>();
		alarmDependencies1.add(new NodeValue("B", true));
		alarmDependencies1.add(new NodeValue("E", true));
		int alarmProbability1Num = 97;
		Probability alarmProbability1 = new Probability(alarmDependencies1, alarmProbability1Num);

		List<NodeValue> alarmDependencies2 = new ArrayList<NodeValue>();
		alarmDependencies2.add(new NodeValue("B", true));
		alarmDependencies2.add(new NodeValue("E", false));
		int alarmProbability2Num = 92;
		Probability alarmProbability2 = new Probability(alarmDependencies2, alarmProbability2Num);

		List<NodeValue> alarmDependencies3 = new ArrayList<NodeValue>();
		alarmDependencies3.add(new NodeValue("B", false));
		alarmDependencies3.add(new NodeValue("E", true));
		int alarmProbability3Num = 36;
		Probability alarmProbability3 = new Probability(alarmDependencies3, alarmProbability3Num);

		List<NodeValue> alarmDependencies4 = new ArrayList<NodeValue>();
		alarmDependencies4.add(new NodeValue("B", false));
		alarmDependencies4.add(new NodeValue("E", false));
		int alarmProbability4Num = 3;
		Probability alarmProbability4 = new Probability(alarmDependencies4, alarmProbability4Num);

		List<Probability> alarmProbabilities = new ArrayList<Probability>();
		alarmProbabilities.add(alarmProbability1);
		alarmProbabilities.add(alarmProbability2);
		alarmProbabilities.add(alarmProbability3);
		alarmProbabilities.add(alarmProbability4);
		
		List<String> alarmConditions = new ArrayList<String>();
		alarmConditions.add("B");
		alarmConditions.add("E");
		Node alarm = new Node("A", alarmProbabilities, alarmConditions);

		burglary.addChild(alarm);
		
		earthquake.addChild(alarm);

		//John calls
		//John calls only depends on if the alarm is true or false, so it's a bit shorter
		List<NodeValue> johnDependencies1 = new ArrayList<NodeValue>();
		johnDependencies1.add(new NodeValue("A", true));
		int johnProbability1Num = 85;
		Probability johnProbability1 = new Probability(johnDependencies1, johnProbability1Num);

		List<NodeValue> johnDependencies2 = new ArrayList<NodeValue>();
		johnDependencies2.add(new NodeValue("A", false));
		int johnProbability2Num = 7;
		Probability johnProbability2 = new Probability(johnDependencies2, johnProbability2Num);

		List<Probability> johnProbabilities = new ArrayList<Probability>();
		johnProbabilities.add(johnProbability1);
		johnProbabilities.add(johnProbability2);
		
		List<String> johnConditions = new ArrayList<String>();
		johnConditions.add("A");
		Node johnCalls = new Node("J", johnProbabilities, johnConditions);

		alarm.addChild(johnCalls);

		//Mary calls
		//Same for mary calls, only depends on the Alarm
		List<NodeValue> maryDependencies1 = new ArrayList<NodeValue>();
		maryDependencies1.add(new NodeValue("A", true));
		int maryProbability1Num = 69;
		Probability maryProbability1 = new Probability(maryDependencies1, maryProbability1Num);

		List<NodeValue> maryDependencies2 = new ArrayList<NodeValue>();
		maryDependencies2.add(new NodeValue("A", false));
		int maryProbability2Num = 2;
		Probability maryProbability2 = new Probability(maryDependencies2, maryProbability2Num);

		List<Probability> maryProbabilities = new ArrayList<Probability>();
		maryProbabilities.add(maryProbability1);
		maryProbabilities.add(maryProbability2);
		
		List<String> maryConditions = new ArrayList<String>();
		maryConditions.add("A");
		
		Node maryCalls = new Node("M", maryProbabilities, maryConditions);

		alarm.addChild(maryCalls);
	}
	
	/**
	 * Generates a prior sample (a random state of this world) of this bayesian network. Goes through
	 * all of the top nodes with no parents, generates values for them, then goes through all the children
	 * and generates values for them, depending on what values were already generated for the parents.
	 * @return The List of NodeValue objects that represents this random state of the world.
	 */
	private static List<NodeValue> priorSample() {
		List<NodeValue> nodeDependencies = new ArrayList<NodeValue>();
		List<Node> exploredNodes = new ArrayList<Node>();
		for (Node topNode : topNodes) {
			NodeValue nv = genNodeValueForNode(topNode, nodeDependencies);
			nodeDependencies.add(nv);
		}
		
		for (Node topNode : topNodes) {
			List<List<Node>> childrensChildren = new ArrayList<List<Node>>();
			childrensChildren.add(topNode.getChildren());
			
			while (!childrensChildren.isEmpty()) {
				List<Node> children = childrensChildren.get(0);
				
				for (Node child : children) {
					if (!exploredNodes.contains(child)) {
						exploredNodes.add(child);
						NodeValue nvForChild = genNodeValueForNode(child, nodeDependencies);
						nodeDependencies.add(nvForChild);
						childrensChildren.add(child.getChildren());
					}
				}
				
				childrensChildren.remove(children);
			}
		}
		
		return nodeDependencies;
	}
	
	/**
	 * Randomly generates a NodeValue for a given node, based on the probability of the node,
	 * also based on all of the previous conditions that have already
	 * been generated. For a top level node with no dependencies, previous conditions won't matter.
	 * But for something like Alarm, it does matter, because it needs to know if Burglary and Alarm are
	 * true or false.
	 * @param node The Node to generate the 
	 * @param previousConditions
	 * @return
	 */
	private static NodeValue genNodeValueForNode(Node node, List<NodeValue> previousConditions) {
		List<NodeValue> nodeDependencies = new ArrayList<NodeValue>();
		List<String> nodesDependentOn = node.getConditions();
		//Find the NodeValues in previousConditions that this Node is dependent on
		for (String nodeName : nodesDependentOn) {
			NodeValue nodeValue = null;
			for (NodeValue nv : previousConditions) {
				if (nv.getName().equals(nodeName)) {
					nodeValue = nv;
					break;
				}
			}
			nodeDependencies.add(nodeValue);
		}
		
		//Get the probability, randomly generate true or false based on this, and generate the NodeValue
		int probability = node.getProbabilityForDependencies(nodeDependencies, true);
		boolean value = probability > (Math.random() * 100);
		NodeValue nv = new NodeValue(node.getName(), value);
		return nv;
	}
	
	/**
	 * Runs rejection sampling on the query and evidence the user gave. I wrote this straight from the
	 * pseudo code given to us, it seems very straight forward.
	 * @return The probability, between 0 and 1, of the event given by the user
	 */
	private static double rejectionSample() {
		double accepted = 0;
		double matches = 0;
		
		for (int i1 = 0; i1 < TRIALS; i1++) {
			//Generate prior sample
			List<NodeValue> priorSample = priorSample();
			
			//If the prior sample is consistent with the evidence
			if (priorSample.containsAll(evidence)) {
				accepted++;
				
				//If the prior sample is consistent with the query
				if (priorSample.containsAll(query)) {
					matches++;
				}
			}
		}
		
		return matches / accepted;
	}
	
	/**
	 * Utility method for printing out all the contents of a List
	 * @param list The List to print out the contents of
	 */
	public static void printList(List list) {
		for (Object o : list) {
			System.out.println(o);
		}
	}
}
