package aima.core.logic.propositional.inference;

import aima.core.logic.propositional.Model;

/**
 * @author Ravi Mohan
 * @author Mike Stampone
 */
public class WalkSAT {

	/**
	 * Returns a satisfying model or failure (null).
	 * 
	 * @param logicalSentence
	 *            a set of clauses in propositional logic
	 * @param numberOfFlips
	 *            number of flips allowed before giving up
	 * @param probabilityOfRandomWalk
	 *            the probability of choosing to do a "random walk" move,
	 *            typically around 0.5
	 * 
	 * @return a satisfying model or failure (null).
	 */
	public Model findModelFor(String logicalSentence, int numberOfFlips,
			double probabilityOfRandomWalk) {
		throw new UnsupportedOperationException("TODO");
	}
}
