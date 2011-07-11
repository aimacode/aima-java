package aima.core.probability.util;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import aima.core.probability.RandomVariable;
import aima.core.probability.domain.Domain;
import aima.core.probability.proposition.TermProposition;

/**
 * Default implementation of the RandomVariable interface.
 * 
 * Note: Also implements the TermProposition interface so its easy to use
 * RandomVariables in conjunction with propositions about them in the
 * Probability Model APIs.
 * 
 * @author Ciaran O'Reilly
 */
public class RandVar implements RandomVariable, TermProposition {
	private String name = null;
	private Domain domain = null;
	private Set<RandomVariable> scope = new HashSet<RandomVariable>();

	public RandVar(String name, Domain domain) {
		ProbUtil.checkValidRandomVariableName(name);
		if (null == domain) {
			throw new IllegalArgumentException(
					"Domain of RandomVariable must be specified.");
		}

		this.name = name;
		this.domain = domain;
		this.scope.add(this);
	}

	//
	// START-RandomVariable
	@Override
	public String getName() {
		return name;
	}

	@Override
	public Domain getDomain() {
		return domain;
	}

	// END-RandomVariable
	//

	//
	// START-TermProposition
	@Override
	public RandomVariable getTermVariable() {
		return this;
	}

	@Override
	public Set<RandomVariable> getScope() {
		return scope;
	}

	@Override
	public Set<RandomVariable> getUnboundScope() {
		return scope;
	}

	@Override
	public boolean holds(Map<RandomVariable, Object> possibleWorld) {
		return possibleWorld.containsKey(getTermVariable());
	}

	// END-TermProposition
	//

	@Override
	public boolean equals(Object o) {

		if (this == o) {
			return true;
		}
		if (!(o instanceof RandomVariable)) {
			return false;
		}

		// The name (not the name:domain combination) uniquely identifies a
		// Random Variable
		RandomVariable other = (RandomVariable) o;

		return this.name.equals(other.getName());
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public String toString() {
		return getName();
	}
}
