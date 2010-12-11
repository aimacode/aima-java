package aima.core.probability.proposed.model.proposition;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import aima.core.probability.proposed.model.RandomVariable;

public abstract class AbstractProposition implements Proposition {

	private LinkedHashSet<RandomVariable> scope = new LinkedHashSet<RandomVariable>();
	
	//
	// END-Proposition
	public Set<RandomVariable> getScope() {
		return Collections.unmodifiableSet(scope);
	}
	
	// START-Proposition
	//
	
	//
	// Protected Methods
	//
	protected void addScope(RandomVariable var) {
		scope.add(var);
	}
} 
