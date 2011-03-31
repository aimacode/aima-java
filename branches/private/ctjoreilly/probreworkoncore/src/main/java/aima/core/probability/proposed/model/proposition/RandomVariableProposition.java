package aima.core.probability.proposed.model.proposition;

import java.util.Map;

import aima.core.probability.proposed.model.RandomVariable;

public class RandomVariableProposition extends AbstractTermProposition {

	public RandomVariableProposition(RandomVariable var) {
		super(var);
		addUnboundScope(var);
	}

	@Override
	public boolean holds(Map<RandomVariable, Object> possibleWorld) {
		return possibleWorld.containsKey(getTermVariable());
	}

	@Override
	public String toString() {
		return getTermVariable().getName();
	}
}
