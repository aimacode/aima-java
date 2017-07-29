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
public class FiniteBooleanDomain extends AbstractFiniteDomain {

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

	// Private Constructor

	/**
	 * Constructor for the singleton instance.
	 */
	private FiniteBooleanDomain() {
		indexPossibleValues(possibleValues);
	}

	// Public methods

	// START-Domain
	
	@Override
	public int size() {
		return 2;
	}

	@Override
	public boolean isOrdered() {
		return false;
	}

	// END-Domain
	
	// START-FiniteDomain
	
	@Override
	public Set<Boolean> getPossibleValues() {
		return possibleValues;
	}
	
	// END-FiniteDomain
	
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
}
