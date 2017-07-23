package aima.extra.probability.proposition;

import aima.extra.probability.RandomVariable;

/**
 * Propositions describing sets of possible worlds are written in a notation
 * that combines elements of propositional logic and constraint satisfaction
 * notation. It is a factored representation, in which a possible world is
 * represented by a set of variable/value pairs.
 * 
 * @author Ciaran O'Reilly
 * @author Nagaraj Poti
 */
public interface Proposition {
	
	/**
	 * Set the random variable associated with the proposition.
	 * 
	 * @param var
	 */
	void setPropositionVariable(RandomVariable var);
	
	/**
	 * Get the random variable associated with the proposition.
	 * 
	 * @return random variable
	 */
	RandomVariable getPropositionVariable();
	
	/**
	 * Set the value associated with the random variable.
	 * 
	 * @param value
	 */
	void setPropositionValue(Object value);
	
	/**
	 * Get the value associated with the random variable.
	 * 
	 * @return value
	 */
	Object getPropositionValue();
}
