package aima.core.probability.proposed.model.proposition;

import java.util.Map;

import aima.core.probability.proposed.model.RandomVariable;

public class RandomVariableProposition extends TermProposition {
	private RandomVariable var = null;

	public RandomVariableProposition(RandomVariable var) {
		if (null == var) {
			throw new IllegalArgumentException(
					"Random Variable must be specified.");
		}

		addScope(var);
		this.var = var;
	}

	@Override
	public boolean matches(Map<RandomVariable, Object> possibleWorld) {
		return possibleWorld.containsKey(var);
	}

	@Override
	public String toString() {
		return var.getName();
	}
}
