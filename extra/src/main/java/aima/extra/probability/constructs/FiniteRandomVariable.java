package aima.extra.probability.constructs;

import aima.extra.probability.domain.Domain;
import aima.extra.probability.domain.FiniteDomain;

/**
 * A finite, discrete random variable.
 * 
 * @author Ciaran O'Reilly
 * @author Nagaraj Poti
 */
public class FiniteRandomVariable implements RandomVariable {

	/**
	 * Name associated with the random variable.
	 */
	private String name;

	/**
	 * FiniteDomain associated with a finite, discrete random variable.
	 */
	private FiniteDomain<?> domain;

	/**
	 * Constructor.
	 * 
	 * @param name
	 *            of the random variable.
	 * 
	 * @param domain
	 *            of FiniteDomain type.
	 */
	public FiniteRandomVariable(String name, Domain domain) {
		ProbabilityUtilities.checkValidRandomVariableName(name);
		if (null == domain) {
			throw new IllegalArgumentException("Domain of FiniteRandomVariable must be specified.");
		} else if (!(domain instanceof FiniteDomain<?>)) {
			throw new IllegalArgumentException("Domain must be of type FiniteDomain.");
		}
		this.name = name;
		this.domain = (FiniteDomain<?>) domain;
	}

	// START-RandomVariable

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public FiniteDomain<?> getDomain() {
		return this.domain;
	}

	// END-RandomVariable

	/**
	 * Two random variables are equal if they have the same name and same
	 * domain.
	 */
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof FiniteRandomVariable)) {
			return false;
		}
		FiniteRandomVariable other = (FiniteRandomVariable) o;
		if (this.name.equals(other.name) && this.domain.equals(other.domain)) {
			return true;
		}
		return false;
	}
}
