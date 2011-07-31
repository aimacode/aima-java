package aima.core.probability.proposition;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import aima.core.probability.RandomVariable;

public abstract class AbstractProposition implements Proposition {

	private LinkedHashSet<RandomVariable> scope = new LinkedHashSet<RandomVariable>();
	private LinkedHashSet<RandomVariable> unboundScope = new LinkedHashSet<RandomVariable>();

	public AbstractProposition() {

	}

	//
	// START-Proposition
	public Set<RandomVariable> getScope() {
		return scope;
	}

	public Set<RandomVariable> getUnboundScope() {
		return unboundScope;
	}

	public abstract boolean holds(Map<RandomVariable, Object> possibleWorld);

	// END-Proposition
	//

	//
	// Protected Methods
	//
	protected void addScope(RandomVariable var) {
		scope.add(var);
	}

	protected void addScope(Collection<RandomVariable> vars) {
		scope.addAll(vars);
	}

	protected void addUnboundScope(RandomVariable var) {
		unboundScope.add(var);
	}

	protected void addUnboundScope(Collection<RandomVariable> vars) {
		unboundScope.addAll(vars);
	}
}
