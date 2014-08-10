package aima.core.probability.proposition;

import aima.core.probability.RandomVariable;

/**
 * A proposition on a single variable term.
 * 
 * Note: The scope may be greater than a single variable as the term may be a
 * derived variable (e.g. Total=Dice1+Dice2).
 * 
 * @author Ciaran O'Reilly
 */
public interface TermProposition extends Proposition {
	/**
	 * 
	 * @return The Term's Variable.
	 */
	RandomVariable getTermVariable();
}
