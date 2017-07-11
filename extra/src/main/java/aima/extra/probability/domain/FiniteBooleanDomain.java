package aima.extra.probability.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Singleton implementation of boolean domain whose values are {true, false}.
 * 
 * @author Ciaran O'Reilly
 * @author Nagaraj Poti
 */
public class FiniteBooleanDomain extends AbstractFiniteDomain<Boolean> {

	// PRIVATE

	/**
	 * Single instance of FiniteBooleanDomain.
	 */
	private static FiniteBooleanDomain onlyInstance = null;

	/**
	 * Set of possible values for boolean domain consists of {true, false}.
	 * Consistent order is ensured.
	 */
	private static final Set<Boolean> possibleValues = Collections
			.unmodifiableSet(new LinkedHashSet<Boolean>(Arrays.asList(Boolean.TRUE, Boolean.FALSE)));

	// Private Constructor (singleton)
	private FiniteBooleanDomain() {
		indexPossibleValues(possibleValues);
	}

	// PUBLIC

	/**
	 * Instantiate onlyInstance for the first time only.
	 * 
	 * @return onlyInstance
	 */
	public static FiniteBooleanDomain getFiniteBooleanDomain() {
		if (null == onlyInstance) {
			synchronized (FiniteBooleanDomain.class) {
				if (null == onlyInstance) {
					onlyInstance = new FiniteBooleanDomain();
				}
			}
		}
		return onlyInstance;
	}

	@Override
	public int size() {
		return 2;
	}

	@Override
	public boolean isOrdered() {
		return false;
	}

	@Override
	public Set<Boolean> getPossibleValues() {
		return Collections.unmodifiableSet(possibleValues);
	}
}
