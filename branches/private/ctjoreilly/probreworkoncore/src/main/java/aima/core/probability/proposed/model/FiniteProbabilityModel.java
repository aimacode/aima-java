package aima.core.probability.proposed.model;

import aima.core.probability.proposed.model.proposition.TermProposition;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 484.
 * 
 * A probability model on a discrete, countable set of worlds. The proper
 * treatment of the continuous case brings in certain complications that are
 * less relevant for most purposes in AI.
 * 
 * @author oreilly
 */
public interface FiniteProbabilityModel extends ProbabilityModel {

	/**
	 * 
	 * @param phi
	 *            the propositional term of interest.
	 * @return all the possible values of the propositional term &phi. This is a
	 *         Vector of numbers, where we assume a predefined ordering of the
	 *         domain of the relevant random variables.
	 */
	Distribution priorDistribution(TermProposition phi);

	/**
	 * Get a conditional distribution. Example:
	 * 
	 * P(X | Y) gives the values of P(X = x1 | Y = y1) for each possible i, j
	 * pair.
	 * 
	 * @param phi
	 *            the propositional term for which a probability distribution is
	 *            to be returned.
	 * @param evidence
	 *            information we already have.
	 * @return the conditional distribution for P(&phi | evidence).
	 */
	Distribution posteriorDistribution(TermProposition phi,
			TermProposition... evidence);

	/**
	 * Get a distribution on multiple variables. Example:
	 * 
	 * P(X, Y) gives the values of P(X = x1 | Y = y1)P(Y = y1) for each possible
	 * i, j pair.
	 * 
	 * @param propositions
	 *            the propositional terms for which a joint probability
	 *            distribution is to be returned.
	 * @return the joint distribution for P(X, Y, ...).
	 */
	Distribution jointProbabilityDistribution(TermProposition... propositions);
}
