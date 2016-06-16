package aima.core.logic.basic.propositional.inference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import aima.core.logic.basic.propositional.kb.data.Clause;
import aima.core.logic.basic.propositional.kb.data.Model;
import aima.core.logic.basic.propositional.parsing.ast.PropositionSymbol;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): page ???.<br>
 * <br>
 * 
 * <pre>
 * <code>
 * function WALKSAT(clauses, p, max_flips) returns a satisfying model or failure
 *   inputs: clauses, a set of clauses in propositional logic
 *           p, the probability of choosing to do a "random walk" move, typically around 0.5
 *           max_flips, number of flips allowed before giving up
 *           
 *   model <- a random assignment of true/false to the symbols in clauses
 *   for i = 1 to max_flips do
 *       if model satisfies clauses then return model
 *       clause <- a randomly selected clause from clauses that is false in model
 *       with probability p flip the value in model of a randomly selected symbol from clause
 *       else flip whichever symbol in clause maximizes the number of satisfied clauses
 *   return failure
 * </code>
 * </pre>
 * 
 * Figure ?.?? The WALKSAT algorithm for checking satisfiability by randomly
 * flipping the values of variables. Many versions of the algorithm exist.
 * 
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 * @author Mike Stampone
 */
public class WalkSAT {

	/**
	 * WALKSAT(clauses, p, max_flips)<br>
	 * 
	 * @param clauses
	 *            a set of clauses in propositional logic
	 * @param p
	 *            the probability of choosing to do a "random walk" move,
	 *            typically around 0.5
	 * @param maxFlips
	 *            number of flips allowed before giving up. Note: a value < 0 is
	 *            interpreted as infinity.
	 * 
	 * @return a satisfying model or failure (null).
	 */
	public Model walkSAT(Set<Clause> clauses, double p, int maxFlips) {
		assertLegalProbability(p);
		
		// model <- a random assignment of true/false to the symbols in clauses
		Model model = randomAssignmentToSymbolsInClauses(clauses);
		// for i = 1 to max_flips do (Note: maxFlips < 0 means infinity)
		for (int i = 0; i < maxFlips || maxFlips < 0; i++) {
			// if model satisfies clauses then return model
			if (model.satisfies(clauses)) {
				return model;
			}

			// clause <- a randomly selected clause from clauses that is false
			// in model
			Clause clause = randomlySelectFalseClause(clauses, model);

			// with probability p flip the value in model of a randomly selected
			// symbol from clause
			if (random.nextDouble() < p) {
				model = model.flip(randomlySelectSymbolFromClause(clause));
			} else {
				// else flip whichever symbol in clause maximizes the number of
				// satisfied clauses
				model = flipSymbolInClauseMaximizesNumberSatisfiedClauses(
						clause, clauses, model);
			}
		}

		// return failure
		return null;
	}

	//
	// SUPPORTING CODE
	//
	private Random random = new Random();

	/**
	 * Default Constructor.
	 */
	public WalkSAT() {
	}

	/**
	 * Constructor.
	 * 
	 * @param random
	 *            the random generator to be used by the algorithm.
	 */
	public WalkSAT(Random random) {
		this.random = random;
	}

	//
	// PROTECTED
	//
	protected void assertLegalProbability(double p) {
		if (p < 0 || p > 1) {
			throw new IllegalArgumentException("p is not a legal propbability value [0-1]: "+p);
		}
	}
	
	protected Model randomAssignmentToSymbolsInClauses(Set<Clause> clauses) {
		// Collect the symbols in clauses
		Set<PropositionSymbol> symbols = new LinkedHashSet<PropositionSymbol>();
		for (Clause c : clauses) {
			symbols.addAll(c.getSymbols());
		}

		// Make initial set of assignments
		Map<PropositionSymbol, Boolean> values = new HashMap<PropositionSymbol, Boolean>();
		for (PropositionSymbol symbol : symbols) {
			// a random assignment of true/false to the symbols in clauses
			values.put(symbol, random.nextBoolean());
		}

		Model result = new Model(values);

		return result;
	}

	protected Clause randomlySelectFalseClause(Set<Clause> clauses, Model model) {
		// Collect the clauses that are false in the model
		List<Clause> falseClauses = new ArrayList<Clause>();
		for (Clause c : clauses) {
			if (Boolean.FALSE.equals(model.determineValue(c))) {
				falseClauses.add(c);
			}
		}

		// a randomly selected clause from clauses that is false
		Clause result = falseClauses.get(random.nextInt(falseClauses.size()));
		return result;
	}

	protected PropositionSymbol randomlySelectSymbolFromClause(Clause clause) {
		// all the symbols in clause
		Set<PropositionSymbol> symbols = clause.getSymbols();

		// a randomly selected symbol from clause
		PropositionSymbol result = (new ArrayList<PropositionSymbol>(symbols))
				.get(random.nextInt(symbols.size()));
		return result;
	}

	protected Model flipSymbolInClauseMaximizesNumberSatisfiedClauses(
			Clause clause, Set<Clause> clauses, Model model) {
		Model result = model;

		// all the symbols in clause
		Set<PropositionSymbol> symbols = clause.getSymbols();
		int maxClausesSatisfied = -1;
		for (PropositionSymbol symbol : symbols) {
			Model flippedModel = result.flip(symbol);
			int numberClausesSatisfied = 0;
			for (Clause c : clauses) {
				if (Boolean.TRUE.equals(flippedModel.determineValue(c))) {
					numberClausesSatisfied++;
				}
			}
			// test if this symbol flip is the new maximum
			if (numberClausesSatisfied > maxClausesSatisfied) {
				result              = flippedModel;
				maxClausesSatisfied = numberClausesSatisfied;
				if (numberClausesSatisfied == clauses.size()) {
					// i.e. satisfies all clauses
					break; // this is our goal.
				}
			}
		}

		return result;
	}
}
