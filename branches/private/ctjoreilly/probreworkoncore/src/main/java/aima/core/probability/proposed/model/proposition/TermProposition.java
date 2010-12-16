package aima.core.probability.proposed.model.proposition;

import aima.core.probability.proposed.model.RandomVariable;

public abstract class TermProposition extends Proposition {

	private RandomVariable variable = null;
	
	public TermProposition(RandomVariable var) {
		if (null == var) {
			throw new IllegalArgumentException(
					"The Random Variable for the Term must be specified.");
		}
		this.variable = var;
	}
	
	public RandomVariable getRandomVariable() {
		return variable;
	}
}
