package aima.extra.probability.constructs;

import java.util.regex.Pattern;
import aima.extra.probability.domain.FiniteDomain;

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
	 * Calculated the expected size of a ProbabilityTable for the provided
	 * random variables.
	 * 
	 * @param vars
	 *            null, 0 or more random variables that are to be used to
	 *            construct a CategoricalDistribution.
	 * 
	 * @return the size that the ProbabilityTable will need to be in order to
	 *         represent the specified random variables.
	 */
	public static <T extends RandomVariable> int expectedSizeofProbabilityTable(Iterable<T> vars) {
		Long expectedSize = 1L;
		if (null != vars) {
			for (RandomVariable rv : vars) {
				if (!(rv.getDomain() instanceof FiniteDomain)) {
					throw new IllegalArgumentException(
							"Cannot have an infinite sized domain for a variable in this calculation:" + rv);
				}
				FiniteDomain<?> rvDomain = (FiniteDomain<?>) rv.getDomain();
				Math.multiplyExact(expectedSize, rvDomain.size());
			}
		}
		if (expectedSize > Integer.MAX_VALUE) {
			throw new IllegalArgumentException("Cannot exceed limit of number of values representable in memory.");
		}
		return expectedSize.intValue();
	}

}
