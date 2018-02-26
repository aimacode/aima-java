package aima.core.Probability.Proposition;

import aima.core.Probability.RandomVariable;

public abstract class AbstractTermProposition extends AbstractProposition
        implements TermProposition {
    private RandomVariable termVariable;

    public AbstractTermProposition(RandomVariable var) {
        if (var == null) {
            throw new IllegalArgumentException(
                    "The Random Variable for the Term must be specified.");
        }
        this.termVariable = var;
        addScope(this.termVariable);
    }

    public RandomVariable getTermVariable() {
        return termVariable;
    }
}
