package aima.core.probability.proposed.model.proposition;

import java.util.Map;

import aima.core.probability.proposed.model.RandomVariable;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 486.
 * 
 * Propositions describing sets of possible worlds are written in a notation
 * that combines elements of propositional logic and constraint satisfaction
 * notation. In the terminology of Section 2.4.7, it is a factored
 * representation, in which a possible world is represented by a set of
 * variable/value pairs.
 * 
 * @author oreilly
 */
public abstract class Proposition {

	/**
	 * 
	 * @param possibleWorld
	 *            A possible world is defined to be an assignment of values to
	 *            all of the random variables under consideration.
	 * @return true if the proposition holds in the given possible world, false otherwise.
	 */
	public abstract boolean matches(Map<RandomVariable, Object> possibleWorld);
}