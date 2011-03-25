package aima.core.probability.proposed.model;

import java.util.Set;

import aima.core.probability.proposed.model.proposition.Proposition;

// TODO - This is a working, to be removed.
// TODO - On AIMA (on 13.2.2)
// TODO - P(doubles) = 1/4. How to assert this, pg 485?
//		  P(doubles | Dice = 5) - supported as well?
// TODO - Doubles proposition in common probability model
//		  test is incorrectly defined - i.e. both pairs need to 
//		  be the same not, just that their total is even.
// TODO	- product rule defined (pg. 486) in java doc?

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 484.
 * 
 * A fully specified probability model associates a numerical probability
 * P(&omega;) with each possible world. The set of all possible worlds is called
 * the sample space &Omega;.
 * 
 * @author Ciaran O'Reilly
 */
public interface ProbabilityModel {
	/**
	 * The default threshold for rounding errors. Example, to test if
	 * probabilities sum to 1:
	 * 
	 * Math.abs(1 - probabilitySum) <
	 * ProbabilityModel.DEFAULT_ROUNDING_THRESHOLD;
	 */
	final double DEFAULT_ROUNDING_THRESHOLD = 1e-8;

	/**
	 * 
	 * @return true, if 0 <= P(&omega;) <= 1 for every &omega; and &sum;<sub>&omega; &isin;
	 *         &Omega;</sub> P(&omega;) = 1 (Equation 13.1 pg. 484 AIMA3e), false
	 *         otherwise.
	 */
	boolean isValid();

	/**
	 * For any proposition &phi;, P(&phi;) = &sum;<sub>&omega; &isin; &phi;</sub> P(&omega;). Refer
	 * to equation 13.2 page 485 of AIMA3e. Probabilities such as P(Total = 11)
	 * and P(doubles) are called unconditional or prior probabilities (and
	 * sometimes just "priors" for short); they refer to degrees of belief in
	 * propositions in the absence of any other information.
	 * 
	 * @param phi
	 *            the proposition for which a probability value is to be
	 *            returned.
	 * @return the probability of the proposition &phi;.
	 */
	double prior(Proposition phi);

	/**
	 * Unlike unconditional or prior probabilities, most of the time we have
	 * some information, usually called evidence, that has already been
	 * revealed. This is the conditional or posterior probability (or just
	 * "posterior" for short). Mathematically speaking, conditional
	 * probabilities are defined in terms of unconditional probabilities as
	 * follows: for any propositions a and b, we have
	 * 
	 * P(a | b) = P(a AND b)/P(b)
	 * 
	 * which holds whenever P(b) > 0. Refer to equation 13.3 page 485 of AIMA3e.
	 * This can be rewritten in a different form called the product rule:
	 * 
	 * P(a AND b) = P(a | b)P(b)
	 * 
	 * and also as:
	 * 
	 * P(a AND b) = P(b | a)P(a)
	 * 
	 * whereby, equating the two right-hand sides and dividing by P(a) gives
	 * you Bayes' rule:
	 * 
	 * P(b | a) = P(a | b)P(b)/P(a)
	 * 
	 * @param phi
	 *            the proposition for which a probability value is to be
	 *            returned.
	 * @param evidence
	 *            information we already have.
	 * @return the probability of the proposition &phi; given evidence.
	 */
	double posterior(Proposition phi, Proposition... evidence);

	/**
	 * @return a consistent ordered Set (e.g. LinkedHashSet) of the random
	 *         variables describing the atomic variable/value pairs this
	 *         probability model can take on. Refer to pg. 486 AIMA3e.
	 */
	Set<RandomVariable> getRepresentation();
}