import java.util.List;

/**
 * The <code>WalkSATAlgorithm</code> class implements the WalkSAT algorithm for solving SAT problems.
 * 
 * @author Jordan Turley
 */
public class WalkSATAlgorithm implements SATStrategy {
	private double probability;
	private double maxSteps;
	
	/**
	 * Creates new WalkSATAlgorithm object with default values of 0.5 probability and 1000000 max steps
	 */
	public WalkSATAlgorithm() {
		this(0.5, 10000000);
	}
	
	/**
	 * Creates new WalkSatAlgorithm object with a given probability and maxSteps
	 * @param probability The probability to randomly pick a symbol in a false clause and flip it,
	 * or find the literal that maximizes the number of correct clauses and flip it
	 * @param maxSteps The maximum number of steps to go through before failing
	 */
	public WalkSATAlgorithm(double probability, int maxSteps) {
		this.probability = probability;
		this.maxSteps = maxSteps;
	}
	
	@Override
	public Model solve(List<Clause> clauses, List<String> symbols) throws UnsatisfiableException {
		Model model = new Model();
		for (String symbol : symbols) {
			boolean value = Math.random() > 0.5;
			SymbolValue symVal = new SymbolValue(symbol, value);
			model.add(symVal);
		}
		
		model = walkSAT(clauses, model);
		
		if (model == null) {
			throw new UnsatisfiableException("UNSATISFIABLE");
		}
		
		return model;
	}
	
	/**
	 * Finds Model to satisfy clauses, or if executes for more than maxFlips, returns failure
	 * @param clauses Clauses to satisfy
	 * @param probability The probability to randomly pick a symbol in a false clause and flip it,
	 * or find the literal that maximizes the number of correct clauses and flip it 
	 * @param maxFlips The maximum number of iterations to go through before failing
	 * @param model The Model
	 * @return A correct Model that satisfies all clauses, or null if no model is found
	 */
	private Model walkSAT(List<Clause> clauses, Model model) {
		for (int i1 = 0; i1 < maxSteps; i1++) {
			if (SAT.checkClausesTrueWRTModel(clauses, model)) { //Check if the model satisfies the clauses
				return model;
			}
			
			//Find one clause that is not satisfied
			Clause falseClause = findFalseClause(clauses, model);
			List<SymbolValue> syms = falseClause.getLiterals();
			
			if (Math.random() > probability) { //Flip value of one random variable in clause
				int idx = (int) (Math.random() * syms.size());
				SymbolValue sv = (SymbolValue) syms.get(idx).clone();
				sv.flipValue();
				SymbolValue svInModel = model.getSymbolValue(sv);
				svInModel.flipValue();
			} else { //Find which variable to flip depending on which one would maximize satisfied clauses
				//There's lots of cloning here so that when you flip a variable, it does not modify the clause
				SymbolValue best = (SymbolValue) syms.get(0).clone();
				
				//There is also some flipping to be able to check if the flipped version matches in
				//other unsatisfied clauses
				//So for example, the false clause is 1 -2 3
				//This means in the model, it is -1 2 -3
				//So, you are trying to find the ones where 1, -2, or 3 would satisfy it
				//Whichever one of those satisfies the most clauses, flip it in the model
				//So let's say it is found that 1 is in the most clauses, now the model is 1 2 -3
				
				int bestCount = 0;
				
				for (SymbolValue sv : syms) { //Go through each variable in the false clause
					sv = (SymbolValue) sv.clone();
					
					int curCount = 0;
					
					//Go through each clause in the problem and see if this variable
					//would satisfy it
					for (Clause clause : clauses) {
						if (clause.contains(sv)) {
							curCount++;
						}
					}
					
					//If this is better than the best one, set it as the best one
					if (curCount > bestCount) {
						bestCount = curCount;
						best = sv;
					}
				}
				best.flipValue();
				
				//Get this SymbolValue from the model and flip it, in the model
				SymbolValue svFromModel = model.getSymbolValue(best);
				svFromModel.flipValue();
			}
			//return null;
		}
		
		//Return null (fail) when it has gone through maxSteps
		return null;
	}
	
	/**
	 * Finds a Clause that is false with respect to the model
	 * @param clauses The clauses to look through
	 * @param model The model
	 * @return A Clause that is found to be false
	 */
	private Clause findFalseClause(List<Clause> clauses, Model model) {
		for (Clause clause : clauses) { //Go through each clause and get the List of literals
			List<SymbolValue> literals = clause.getLiterals();
			
			//Basically, I assume the clause does fail until one of the variables in the model
			//matches one in the clause, when I know that it is true and does not fail 
			boolean clauselFailure = true;
			
			for (SymbolValue sv : literals) { //Go through each SymbolValue in the clause
				//Find the SymbolValue in the model with the same symbol, and see if they match
				String symbol = sv.getSymbol();
				SymbolValue nodeFromModel = SAT.getVarFromModel(symbol, model);
				if (nodeFromModel.equals(sv)) {
					//Don't have to check if not null, because all variables are already in the model
					clauselFailure = false;
					break;
				}
			}
			if (clauselFailure) { //If the clause does fail, return it
				return clause;
			}
		}
		
		//Should never return null, this would mean all clauses are true, which
		//is already checked in walkSAT method at first of loop
		return null;
	}
	
	/**
	 * Prints out all the objects in a List. I just used this for debugging sometimes
	 * @param list The List to print
	 */
	private void printList(List list) {
		for (Object o : list) {
			System.out.println(o);
		}
	}
}
