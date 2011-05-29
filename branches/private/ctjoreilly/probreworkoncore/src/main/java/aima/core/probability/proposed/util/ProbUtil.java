package aima.core.probability.proposed.util;

import aima.core.probability.proposed.proposition.ConjunctiveProposition;
import aima.core.probability.proposed.proposition.Proposition;

public class ProbUtil {

	/**
	 * Check if name provided is valid for use as the name of a RandomVariable.
	 * 
	 * @param name
	 *            proposed for the RandomVariable.
	 * @throws IllegalArgumentException
	 *             if not a valid RandomVariable name.
	 */
	public static void checkValidRandomVariableName(String name)
			throws IllegalArgumentException {
		if (null == name || name.trim().length() == 0
				|| name.trim().length() != name.length() 
				|| name.contains(" ")) {
			throw new IllegalArgumentException(
					"Name of RandomVariable must be specified and contain no leading, trailing or embedded spaces.");
		}
		if (name.substring(0, 1).toLowerCase().equals(name.substring(0, 1))) {
			throw new IllegalArgumentException(
					"Name must start with a leading upper case letter.");
		}
	}
	
	/**
	 * Convenience method for ensure a conjunction of probabilistic
	 * propositions.
	 * 
	 * @param props
	 *            propositions to be combined into a ConjunctiveProposition if
	 *            necessary.
	 * @return a ConjunctivePropositions if more than 1 proposition in 'props',
	 *         otherwise props[0].
	 */
	public static Proposition constructConjunction(Proposition[] props) {
		return constructConjunction(props, 0);
	}

	//
	// PRIVATE METHODS
	//

	private static Proposition constructConjunction(Proposition[] props, int idx) {
		if ((idx + 1) == props.length) {
			return props[idx];
		}

		return new ConjunctiveProposition(props[idx], constructConjunction(
				props, idx + 1));
	}
}
