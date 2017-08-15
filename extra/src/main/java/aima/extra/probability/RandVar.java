package aima.extra.probability;

import aima.extra.probability.constructs.ProbabilityUtilities;
import aima.extra.probability.domain.Domain;
import aima.extra.probability.domain.FiniteDomain;

/**
 * A finite, discrete random variable.
 * 
 * @author Ciaran O'Reilly
 * @author Nagaraj Poti
 */
public class RandVar implements RandomVariable {

	/**
	 * Name associated with the random variable.
	 */
	private String name;

	/**
	 * FiniteDomain associated with a finite, discrete random variable.
	 */
	private FiniteDomain domain;

	/**
	 * Constructor.
	 * 
	 * @param name
	 *            of the random variable.
	 * @param domain
	 *            of FiniteDomain type.
	 */
	public RandVar(String name, Domain domain) {
		ProbabilityUtilities.checkValidRandomVariableName(name);
		if (null == domain) {
			throw new IllegalArgumentException("Domain of RandVar must be specified.");
		} else if (!(domain instanceof FiniteDomain)) {
			throw new IllegalArgumentException("Domain must be of type FiniteDomain.");
		}
		this.name = name;
		this.domain = (FiniteDomain) domain;
	}

	// START-RandomVariable

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public FiniteDomain getDomain() {
		return this.domain;
	}

	// END-RandomVariable

	/**
	 * Two RandVar objects are equal if they have the same name and same domain.
	 */
	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		} else if (!(o instanceof RandVar)) {
			return false;
		}
		RandVar other = (RandVar) o;
		boolean result = this.name.equals(other.name) && this.domain.equals(other.domain);
		return result;
	}

	/**
	 * Two RandVar objects have the same hashcode if they have the same names.
	 */
	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public String toString() {
		return this.name;
	}
}
