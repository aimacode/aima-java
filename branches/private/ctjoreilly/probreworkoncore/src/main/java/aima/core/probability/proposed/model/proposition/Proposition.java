package aima.core.probability.proposed.model.proposition;

import java.util.Map;
import java.util.Set;

import aima.core.probability.proposed.model.RandomVariable;

public interface Proposition {

	/**
	 * 
	 * @return the Set of RandomVariables in the World (sample space) that this
	 *         Proposition is applicable to.
	 */
	Set<RandomVariable> getScope();
	
	/**
	 * 
	 * @param possibleWorld
	 *            A possible world is defined to be an assignment of values to
	 *            all of the random variables under consideration.
	 * @return
	 */
	boolean matches(Map<RandomVariable, Object> possibleWorld);
}
