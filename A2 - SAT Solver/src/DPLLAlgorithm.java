import java.util.ArrayList;
import java.util.List;

/**
 * The <code>DPLLAlgorithm</code> class implements the DPLL algorithm for solving SAT problems.
 * 
 * @author Jordan Turley
 */
public class DPLLAlgorithm implements SATStrategy{
	@Override
	public Model solve(List<Clause> clauses, List<String> symbols) throws UnsatisfiableException {
		Model model = solveRecursive(clauses, symbols, new Model()); 
		if (model == null) {
			throw new UnsatisfiableException("UNSATISFIABLE");
		}
		return model;
	}

	private Model solveRecursive(List<Clause> clauses, List<String> symbols, Model model) {
		//Make a deep copy of the symbols object so if it is changed in this iteration of recursion,
		//it is not changed in any other iterations
		List<String> symbols_ = new ArrayList<String>();
		for (String s : symbols) {
			symbols_.add(s);
		}
		symbols = symbols_;

		//Check if the solution satisfies all the clauses
		if (SAT.checkClausesTrueWRTModel(clauses, model)) {
			if (symbols.size() > 0) { //If there are any unset symbols, just set them to true
				for (String symbol : symbols) {
					model.add(new SymbolValue(symbol, true));
				}
			}
			
			return model;
		}

		//Check if any of the clauses are false
		if (SAT.checkClausesFalseWRTModel(clauses, model)) {
			return null;
		}

		//Check for a pure symbol
		SymbolValue pureSymbol = findPureSymbol(clauses, symbols, model);
		if (pureSymbol != null) {
			//If there is a pure symbol, remove it from symbols and add it with it's value to the model
			String symbol = pureSymbol.getSymbol();
			symbols.remove(symbol);
			model.add(pureSymbol);
			return solveRecursive(clauses, symbols, model);
		}

		//Check for any unit clauses
		SymbolValue unitClause = findUnitClause(clauses, model);
		if (unitClause != null) {
			//If there is a unit clause, remove it from symbols and add it with it's value to the model
			String symbol = unitClause.getSymbol();
			symbols.remove(symbol);
			model.add(unitClause);
			return solveRecursive(clauses, symbols, model);
		}

		//Get the first symbol left in symbols and remove it from symbols
		String firstSymbol = symbols.get(0);
		symbols.remove(firstSymbol);

		//Create the left and right children of the tree, with the removed symbol true or false
		SymbolValue firstNodeTrue = new SymbolValue(firstSymbol, true);
		SymbolValue firstNodeFalse = new SymbolValue(firstSymbol, false);

		//Create copys of the model and add the symbol with true and false to each, respectively
		Model modelWithTrue = (Model) model.clone();
		modelWithTrue.add(firstNodeTrue);

		Model modelWithFalse = (Model) model.clone();
		modelWithFalse.add(firstNodeFalse);

		//Get the full model for the symbol when it is true
		Model modelWithTrueResult = solveRecursive(clauses, symbols, modelWithTrue);
		if (modelWithTrueResult != null) { //If it is not null, return it
			return modelWithTrueResult;
		} else {
			//Get the full model for the symbol when it is false
			Model modelWithFalseResult = solveRecursive(clauses, symbols, modelWithFalse);
			if (modelWithFalseResult != null) { //If it is not null, return it
				return modelWithFalseResult;
			} else {
				//There is no solution, return null. An exception will be thrown
				return null;
			}
		}
	}
	
	/**
	 * Finds a pure symbol, i.e. a variable that is the same value (true or false) in all clauses
	 * @param clauses The clauses of this SAT problem
	 * @param symbols The symbols of this SAT problem
	 * @param model The model of this SAT problem
	 * @return A pure symbol if one is found, or null if no pure symbol can be found
	 */
	private SymbolValue findPureSymbol(List<Clause> clauses, List<String> symbols, Model model) {
		List<SymbolValue> pureSymbols = new ArrayList<SymbolValue>();
		List<String> nonPureSymbols = new ArrayList<String>();
		
		//Go through every clause
		for (Clause clause : clauses) {
			List<SymbolValue> literals = clause.getLiterals(); //Get the literals for the clause
			for (SymbolValue node : literals) { //Go through every symbol in the literals
				String symbol = node.getSymbol();
				if (!symbols.contains(symbol)) { //If this symbol isn't in symbols, go on
					continue;
				}
				
				//Check if this is a non-pure symbol
				SymbolValue sameSymbolNodeInList = symbolValueListContainsSymbol(pureSymbols, node);
				if (sameSymbolNodeInList != null) {
					if (!sameSymbolNodeInList.equals(node)) {
						pureSymbols.remove(sameSymbolNodeInList);
						nonPureSymbols.add(node.getSymbol());
					}
				}
				
				//Check if the symbol is already pure or nonpure
				if (nonPureSymbols.contains(node.getSymbol()) || pureSymbols.contains(node)) {
					continue;
				}
				
				pureSymbols.add(node);
			}
		}
		
		//If there is at least one pure symbol, return it
		if (pureSymbols.size() > 0) {
			return pureSymbols.get(0);
		}
		//else return null
		return null;
	}
	
	/**
	 * Checks if a List of SymbolValues contains a SymbolValue with the same Symbol
	 * @param list The List to check
	 * @param sv The SymbolValue to look for
	 * @return The SymbolValue if it is found, or null
	 */
	private SymbolValue symbolValueListContainsSymbol(List<SymbolValue> list, SymbolValue sv) {
		for (SymbolValue n : list) {
			if (n.getSymbol().equals(sv.getSymbol())) {
				return n;
			}
		}
		return null;
	}
	
	/**
	 * Finds a unit clause, i.e. a clause that is only 1 literal, that is not already in the Model.
	 * @param clauses The clauses to find a unit clause in
	 * @param model The model to check
	 * @return The SymbolValue of the unit clause, or null if none are found
	 */
	private SymbolValue findUnitClause(List<Clause> clauses, Model model) {
		for (Clause clause : clauses) {
			if (clause.size() == 1) {
				//If a clause of size 1 is found, make sure it isn't already in the model
				SymbolValue n = clause.getLiterals().get(0);
				if (!model.contains(n)) {
					return n;
				}
			}
		}
		return null;
	}
}
