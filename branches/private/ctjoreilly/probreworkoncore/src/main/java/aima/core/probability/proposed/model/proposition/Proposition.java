package aima.core.probability.proposed.model.proposition;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

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

	private LinkedHashSet<RandomVariable> scope = new LinkedHashSet<RandomVariable>();

	/**
	 * 
	 * @return the Set of RandomVariables in the World (sample space) that this
	 *         Proposition is applicable to.
	 */
	public Set<RandomVariable> getScope() {
		return Collections.unmodifiableSet(scope);
	}

	/**
	 * 
	 * @param possibleWorld
	 *            A possible world is defined to be an assignment of values to
	 *            all of the random variables under consideration.
	 * @return
	 */
	abstract boolean matches(Map<RandomVariable, Object> possibleWorld);

	//
	// Protected Methods
	//
	protected void addScope(RandomVariable var) {
		scope.add(var);
	}
}