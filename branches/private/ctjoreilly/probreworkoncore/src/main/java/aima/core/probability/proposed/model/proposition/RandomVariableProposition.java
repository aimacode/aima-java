package aima.core.probability.proposed.model.proposition;

import java.util.Map;

import aima.core.probability.proposed.model.RandomVariable;

public class RandomVariableProposition extends TermProposition {

	public RandomVariableProposition(RandomVariable var) {
		super(var);
	}


	@Override
	public boolean matches(Map<RandomVariable, Object> possibleWorld) {
		return possibleWorld.containsKey(getRandomVariable());
	}

	@Override
	public String toString() {
		return getRandomVariable().getName();
	}
}
