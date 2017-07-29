package aima.extra.probability.proposition;

import aima.extra.probability.RandomVariable;

/**
 * TermProposition class represents an assignment proposition on a single random
 * variable. The random variable may be assigned with a specific value (bound)
 * or null to represent all the possible values of the random variable
 * (unbound).
 * 
 * @author Ciaran O'Reilly
 * @author Nagaraj Poti
 */
public class TermProposition implements Proposition {

	private RandomVariable termVariable;

	/**
	 * Value associated with termVariable. termVariable can be set to a specific
	 * value (e.g. bound = P(Total = 11)). If value is set to null (by default),
	 * termVariable is not constrained to any particular set of values (e.g.
	 * unbound = P(Total)). If a variable is unbound it implies the
	 * distributions associated with the variable is being sought.
	 */
	private Object value = null;

	// Public constructors

	public TermProposition(RandomVariable var) {
		if (null == var) {
			throw new IllegalArgumentException("The random variable for the term must be specified");
		}
		this.termVariable = var;
	}

	public TermProposition(RandomVariable var, Object value) {
		if (null == var) {
			throw new IllegalArgumentException("The random variable for the term must be specified");
		}
		this.termVariable = var;
		this.value = value;
	}

	// START-Proposition

	@Override
	public void setPropositionVariable(RandomVariable var) {
		if (null == var) {
			throw new IllegalArgumentException("The random variable for the term must be specified.");
		}
		this.termVariable = var;
	}

	@Override
	public RandomVariable getPropositionVariable() {
		return this.termVariable;
	}

	@Override
	public void setPropositionValue(Object value) {
		this.value = value;
	}

	@Override
	public Object getPropositionValue() {
		return this.value;
	}

	// END-Proposition
}
