package aima.core.Probability.Proposition;

import aima.core.Probability.RandomVariable;

/**
 * A proposition on a single variable term.
 *
 * Note: The scope may be greater than a single variable as the term may be a
 * derived variable (e.g. Total=Dice1+Dice2).
 *
 * @author Ciaran O'Reilly
 */

@FunctionalInterface
public interface TermProposition {
    /**
     *
     * @return The Term's Variable.
     */
    RandomVariable getTermVariable();
}
