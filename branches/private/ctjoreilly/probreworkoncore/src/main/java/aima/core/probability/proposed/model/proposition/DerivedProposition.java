package aima.core.probability.proposed.model.proposition;

import aima.core.probability.proposed.model.RandomVariable;
import aima.core.probability.proposed.model.domain.BooleanDomain;

public abstract class DerivedProposition extends AbstractTermProposition {
	
	public DerivedProposition(String name) {
		// Derived Domain is always boolean, for whether it holds or not.
		super(new RandomVariable(name, new BooleanDomain()));
	}
}