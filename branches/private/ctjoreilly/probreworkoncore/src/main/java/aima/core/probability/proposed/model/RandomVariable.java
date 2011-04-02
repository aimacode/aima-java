package aima.core.probability.proposed.model;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import aima.core.probability.proposed.model.domain.Domain;
import aima.core.probability.proposed.model.proposition.TermProposition;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 486.
 * 
 * Variables in probability theory are called random variables and their names
 * begin with an uppercase letter. Every random variable has a domain - the set
 * of possible values it can take on.
 * 
 * @author Ciaran O'Reilly
 */
public class RandomVariable implements TermProposition {

	private String name = null;
	private Domain domain = null;
	private Set<RandomVariable> scope = new HashSet<RandomVariable>();

	public RandomVariable(String name, Domain domain) {
		checkValidRandomVariableName(name);
		if (null == domain) {
			throw new IllegalArgumentException(
					"Domain of RandomVariable must be specified.");
		}

		this.name = name;
		this.domain = domain;
		this.scope.add(this);
	}

	/**
	 * Check if name provided is valid for use as the name of a RandomVariable.
	 * 
	 * @param name
	 *            proposed for the RandomVariable.
	 * @throws IllegalArgumentException
	 *             if not a valid RandomVariable name.
	 */
	public static void checkValidRandomVariableName(String name)
			throws IllegalArgumentException {
		if (null == name || name.trim().length() == 0
				|| name.trim().length() != name.length() 
				|| name.contains(" ")) {
			throw new IllegalArgumentException(
					"Name of RandomVariable must be specified and contain no leading, trailing or embedded spaces.");
		}
		if (name.substring(0, 1).toLowerCase().equals(name.substring(0, 1))) {
			throw new IllegalArgumentException(
					"Name must start with a leading upper case letter.");
		}
	}

	/**
	 * 
	 * @return the name used to uniquely identify this variable.
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @return the Set of possible values the Random Variable can take on.
	 */
	public Domain getDomain() {
		return domain;
	}
	
	// Note: Implements this interface so its easy to use RandomVariables
	// in conjunction with propositions about them in the API.
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

		return this.name.equals(other.name);
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
