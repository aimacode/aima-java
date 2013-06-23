package aima.core.logic.propositional.inference;

import java.util.Set;

import aima.core.logic.propositional.Model;
import aima.core.logic.propositional.kb.data.Clause;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 263.<br>
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
 * Figure 7.18 The WALKSAT algorithm for checking satisfiability by randomly
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
	 *            number of flips allowed before giving up
	 * 
	 * @return a satisfying model or failure (null).
	 */
	public Model walkSAT(Set<Clause> clauses, double p, int maxFlips) {
		throw new UnsupportedOperationException("TODO");
	}
	
	//
	// SUPPORTING CODE
	//
}
