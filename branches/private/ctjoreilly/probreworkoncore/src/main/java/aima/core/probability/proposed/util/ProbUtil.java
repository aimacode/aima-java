package aima.core.probability.proposed.util;

import aima.core.probability.proposed.RandomVariable;
import aima.core.probability.proposed.domain.FiniteDomain;
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
				|| name.trim().length() != name.length() || name.contains(" ")) {
			throw new IllegalArgumentException(
					"Name of RandomVariable must be specified and contain no leading, trailing or embedded spaces.");
		}
		if (name.substring(0, 1).toLowerCase().equals(name.substring(0, 1))) {
			throw new IllegalArgumentException(
					"Name must start with a leading upper case letter.");
		}
	}

	/**
	 * Calculated the expected size of a ProbabilityTable for the provided
	 * random variables.
	 * 
	 * @param vars
	 *            null, 0 or more random variables that are to be used to
	 *            construct a CategoricalDistribution.
	 * @return the size (i.e. getValues().length) that the
	 *         CategoricalDistribution will need to be in order to represent the
	 *         specified random variables.
	 * 
	 * @see CategoricalDistribution#getValues()
	 */
	public static int expectedSizeOfProbabilityTable(RandomVariable... vars) {
		// initially 1, as this will represent constant assignments
		// e.g. Dice1 = 1.
		int expectedSizeOfDistribution = 1;
		if (null != vars) {
			for (RandomVariable rv : vars) {
				// Create ordered domains for each variable
				if (!(rv.getDomain() instanceof FiniteDomain)) {
					throw new IllegalArgumentException(
							"Cannot have an infinite domain for a variable in this calculation:"
									+ rv);
				}
				FiniteDomain d = (FiniteDomain) rv.getDomain();
				expectedSizeOfDistribution *= d.size();
			}
		}

		return expectedSizeOfDistribution;
	}

	/**
	 * Calculated the expected size of a CategoricalDistribution for the
	 * provided random variables.
	 * 
	 * @param vars
	 *            null, 0 or more random variables that are to be used to
	 *            construct a CategoricalDistribution.
	 * @return the size (i.e. getValues().length) that the
	 *         CategoricalDistribution will need to be in order to represent the
	 *         specified random variables.
	 * 
	 * @see CategoricalDistribution#getValues()
	 */
	public static int expectedSizeOfCategoricalDistribution(
			RandomVariable... vars) {
		// Equivalent calculation
		return expectedSizeOfProbabilityTable(vars);
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
