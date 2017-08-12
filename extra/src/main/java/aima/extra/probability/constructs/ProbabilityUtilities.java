package aima.extra.probability.constructs;

import java.util.List;
import java.util.regex.Pattern;
import aima.extra.probability.RandVar;
import aima.extra.probability.RandomVariable;

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
	 * @return true if the name to be assigned to a RandomVariable is valid.
	 * 
	 * @throws IllegalArgumentException
	 *             if not a valid RandomVariable name.
	 */
	public static boolean checkValidRandomVariableName(String name) {
		if (null == name) {
			throw new IllegalArgumentException("Name of the RandomVariable cannot be null.");
		} else if (!LEGAL_RAND_VAR_NAME_PATTERN.matcher(name).matches()) {
			throw new IllegalArgumentException(
					"Name of RandomVariable must be specified and contain no leading, trailing or embedded spaces or special characters.");
		} else if (!LEGAL_LEADING_CHAR_RAND_VAR_NAME_PATTERN.matcher(name).matches()) {
			throw new IllegalArgumentException("Name must start with a leading upper case letter.");
		}
		return true;
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
			expectedSize = vars.stream().map(var -> Long.valueOf(var.getDomain().size())).reduce(expectedSize,
					Math::multiplyExact);
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
	public static boolean checkIfFiniteRandomVariables(List<RandomVariable> vars) {
		boolean result = true;
		if (null != vars) {
			result = vars.stream().allMatch(RandVar.class::isInstance);
		}
		return result;
	}

	/**
	 * Check if all the random variables in the list are unique or not (i.e the
	 * random variables must have different names).
	 * 
	 * @param vars
	 *            is the list of random variables to be checked.
	 * 
	 * @return true if all are unique, false otherwise.
	 */
	public static boolean checkIfRandomVariablesAreUnique(List<RandomVariable> vars) {
		boolean result = true;
		if (null != vars) {
			long distinctCount = vars.stream().map(var -> var.getName()).distinct().count();
			if (distinctCount != vars.size()) {
				result = false;
			}
		}
		return result;
	}
}
