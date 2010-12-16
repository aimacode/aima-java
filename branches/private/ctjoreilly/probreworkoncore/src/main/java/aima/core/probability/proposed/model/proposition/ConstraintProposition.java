package aima.core.probability.proposed.model.proposition;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import aima.core.probability.proposed.model.RandomVariable;
import aima.core.probability.proposed.model.domain.BooleanDomain;

public abstract class ConstraintProposition extends TermProposition {

	private LinkedHashSet<RandomVariable> scope = new LinkedHashSet<RandomVariable>();
	
	public ConstraintProposition(String name) {
		// Constraint Domain is always boolean, for whether it holds or not.
		super(new RandomVariable(name, new BooleanDomain()));
	}

	/**
	 * 
	 * @return the Set of RandomVariables in the World (sample space) that this
	 *         Proposition is applicable to.
	 */
	public Set<RandomVariable> getScope() {
		return Collections.unmodifiableSet(scope);
	}
	
	//
	// Protected Methods
	//
	protected void addScope(RandomVariable var) {
		scope.add(var);
	}
}