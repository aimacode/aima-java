package aima.extra.probability.constructs;

import java.util.List;
import java.util.regex.Pattern;
import aima.extra.probability.RandVar;
import aima.extra.probability.RandomVariable;
import aima.extra.probability.domain.FiniteDomain;

/**
 * Collection of utility functions for the probability sections.
 * 
 * @author Nagaraj Poti
 */
public class ProbabilityUtilities {

	private static final Pattern LEGAL_RAND_VAR_NAME_PATTERN = Pattern.compile("[A-Za-z0-9-_]+");
	private static final Pattern LEGAL_LEADING_CHAR_RAND_VAR_NAME_PATTERN = Pattern.compile("^[A-Z].*");

	/**
	 * Check if name provided is valid for use as the name of a RandomVariable.
	 * 
	 * @param name
	 *            proposed for the RandomVariable.
	 * 
	 * @throws IllegalArgumentException
	 *             if not a valid RandomVariable name.
	 */
	public static void checkValidRandomVariableName(String name) throws IllegalArgumentException {
		if (!LEGAL_RAND_VAR_NAME_PATTERN.matcher(name).matches()) {
			throw new IllegalArgumentException(
					"Name of RandomVariable must be specified and contain no leading, trailing or embedded spaces or special characters.");
		}
		if (!LEGAL_LEADING_CHAR_RAND_VAR_NAME_PATTERN.matcher(name).matches()) {
			throw new IllegalArgumentException("Name must start with a leading upper case letter.");
		}
	}

	/**
	 * Calculated the expected size of a probability table for the provided
	 * random variables.
	 * 
	 * @param vars
	 *            null, 0 or more random variables that are to be used to
	 *            construct a CategoricalDistribution.
	 * 
	 * @return the size that the probability table will need to be in order to
	 *         represent the specified random variables.
	 */
	public static int expectedSizeofProbabilityTable(List<RandomVariable> vars) {
		Long expectedSize = 1L;
		if (null != vars) {
			boolean isValid = ProbabilityUtilities.checkIfFiniteRandomVariables(vars);
			if (!isValid) {
				throw new IllegalArgumentException(
						"All variables must be finite random variables. Cannot have an infinite sized domain for a variable in this calculation.");
			}
			vars.stream().map(var -> Long.valueOf(((FiniteDomain) (((RandomVariable) var).getDomain())).size()))
					.reduce(expectedSize, Math::multiplyExact);
			if (expectedSize > Integer.MAX_VALUE) {
				throw new IllegalArgumentException("Cannot exceed limit of number of values representable in memory.");
			}
		}
		return expectedSize.intValue();
	}

	/**
	 * Check if all random variables have finite domains or not.
	 * 
	 * @param vars
	 *            is a list of random variables that are to be checked for
	 *            finiteness.
	 * 
	 * @return true if all random variables have finite domains, false
	 *         otherwise.
	 */
	public static boolean checkIfFiniteRandomVariables(List<?> vars) {
		boolean result = true;
		if (null != vars) {
			result = vars.stream().allMatch(RandVar.class::isInstance);
		}
		return result;
	}
}
