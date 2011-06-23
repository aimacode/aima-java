package aima.core.probability.proposition;

import java.util.Map;
import java.util.Set;

import aima.core.probability.RandomVariable;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 486.<br>
 * <br>
 * Propositions describing sets of possible worlds are written in a notation
 * that combines elements of propositional logic and constraint satisfaction
 * notation. In the terminology of Section 2.4.7, it is a factored
 * representation, in which a possible world is represented by a set of
 * variable/value pairs.<br>
 * <br>
 * A possible world is defined to be an assignment of values to all of the
 * random variables under consideration.
 * 
 * @author Ciaran O'Reilly
 */
public interface Proposition {
	/**
	 * 
	 * @return the Set of RandomVariables in the World (sample space) that this
	 *         Proposition is applicable to.
	 */
	Set<RandomVariable> getScope();

	/**
	 * 
	 * @return the Set of RandomVariables from this propositions scope that are
	 *         not constrained to any particular set of values (e.g. bound =
	 *         P(Total = 11), while unbound = P(Total)). If a variable is
	 *         unbound it implies the distributions associated with the variable
	 *         is being sought.
	 */
	Set<RandomVariable> getUnboundScope();

	/**
	 * Determine whether or not the proposition holds in a particular possible
	 * world.
	 * 
	 * @param possibleWorld
	 *            A possible world is defined to be an assignment of values to
	 *            all of the random variables under consideration.
	 * @return true if the proposition holds in the given possible world, false
	 *         otherwise.
	 */
	boolean holds(Map<RandomVariable, Object> possibleWorld);
}