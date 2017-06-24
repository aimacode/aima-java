package aima.extra.probability.domain;

import java.util.Arrays;
import java.util.List;

/**
 * Factory class for instantiating various FiniteDomain implementations.
 * 
 * @author Nagaraj Poti
 */
public class FiniteDomainFactory {

	/**
	 * Create and return a FiniteArbitraryTokenDomain instance.
	 * 
	 * @param pValues
	 *            is an ordered list of arbitrary tokens.
	 * 
	 * @return FiniteArbitraryTokenDomain instance.
	 */
	public FiniteArbitraryTokenDomain createOrderedArbitraryTokenDomain(Object... pValues) {
		return new FiniteArbitraryTokenDomain(true, Arrays.asList(pValues));
	}

	/**
	 * Create and return a FiniteArbitraryTokenDomain instance.
	 * 
	 * @param pValues
	 *            is an unordered list of arbitrary tokens.
	 * 
	 * @return FiniteArbitraryTokenDomain instance.
	 */
	public FiniteArbitraryTokenDomain createUnorderedArbitraryTokenDomain(Object... pValues) {
		return new FiniteArbitraryTokenDomain(Arrays.asList(pValues));
	}

	/**
	 * Return the singleton FiniteBooleanDomain instance.
	 * 
	 * @return singleton FiniteBooleanDomain instance.
	 */
	public FiniteBooleanDomain createBooleanDomain() {
		return FiniteBooleanDomain.getFiniteBooleanDomain();
	}

	/**
	 * Create and return a FiniteComparableDomain instance.
	 * 
	 * @param pValues
	 *            is a parameterised list of domain values.
	 * 
	 * @return FiniteComparableDomain instance.
	 */
	public <T extends Comparable<T>> FiniteComparableDomain<T> createComparableDomain(List<T> pValues) {
		return new FiniteComparableDomain<T>(pValues);
	}
}
