package aima.core.probability.proposed;

import java.util.Set;

import aima.core.probability.proposed.proposition.Proposition;

// TODO - This is a working set, to be removed.
// TODO - On AIMA (on 14.5)
// 
// TODO - pg. 515 P(MaryCalls | JohnCalls, Alarm, Earthquake, Burglary) 
//        = P(MaryCalls | Alarm), will require a sub distribution mechanism
//        to be able to express with API.
// TODO - P(doubles) = 1/4. How to assert this, pg 485?
// TODO - pg 486, how to more easily represent 'Total' (domain=2-12 for 2 dices)
//        and then use its assignment, e.g. Total=11.
// TODO - pg 492, doc for marginalization and conditioning.
// TODO - pg 493, (13.9)
// TODO - pg 499, (add distribution tests for Wumpus World example - just Bayesian, not full).
//
// TODO - should ConditionalProbabilityDistribution extend ProbabilityDistribution?
//        (currently don't think so as it is the specification of a collection of conditioned distributions).
//        Note: If do extend just @Override and keep specialized documentation in interface.
// TODO - Consider adding an iterate capability for CategoricalDistributions
//        to ProbUtil, will avoid needing to cast to a ProbabilityTable
//        in the FiniteBayesModel implementation.
//
// TODO - Integrate feedback from Rodrigo :-
// It might be a good idea to make a BayesianNetwork a type of distribution, 
// since that is what is specifies. When you answer a query on several random 
// variables, which must return a distribution on those variables, sometimes it 
// makes sense to return a Bayesian network on them, which gives a compact 
// representation of the distribution. It is also the natural representation 
// you obtain after, say, doing variable elimination on a larger Bayesian network. 
// It would make the step Distribution product = pointwiseProduct(factors) 
// in Elimination-Ask unnecessary; you would simply return those factors as a BN.
// 
// You eliminate nodes in topological order, but I think a far more efficient heuristic 
// is eliminating whichever node creates the factor with the least variables, keeping 
// the connectivity low and therefore things more efficient.
// TODO - also consider removal of leaf nodes as described on pg. 528.
// TODO - clustering described on pg. 529.
//
/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 484.<br>
 * <br>
 * A fully specified probability model associates a numerical probability
 * P(&omega;) with each possible world. The set of all possible worlds is called
 * the sample space &Omega;.
 * 
 * @author Ciaran O'Reilly
 */
public interface ProbabilityModel {
	/**
	 * The default threshold for rounding errors. Example, to test if
	 * probabilities sum to 1:<br>
	 * <br>
	 * Math.abs(1 - probabilitySum) <
	 * ProbabilityModel.DEFAULT_ROUNDING_THRESHOLD;
	 */
	final double DEFAULT_ROUNDING_THRESHOLD = 1e-8;

	/**
	 * 
	 * @return true, if 0 <= P(&omega;) <= 1 for every &omega; and
	 *         &sum;<sub>&omega; &isin; &Omega;</sub> P(&omega;) = 1 (Equation
	 *         13.1 pg. 484 AIMA3e), false otherwise.
	 */
	boolean isValid();

	/**
	 * For any proposition &phi;, P(&phi;) = &sum;<sub>&omega; &isin;
	 * &phi;</sub> P(&omega;). Refer to equation 13.2 page 485 of AIMA3e.
	 * Probabilities such as P(Total = 11) and P(doubles) are called
	 * unconditional or prior probabilities (and sometimes just "priors" for
	 * short); they refer to degrees of belief in propositions in the absence of
	 * any other information.
	 * 
	 * @param phi
	 *            the propositional terms for which a probability value is to be
	 *            returned.
	 * @return the probability of the proposition &phi;.
	 */
	double prior(Proposition... phi);

	/**
	 * Unlike unconditional or prior probabilities, most of the time we have
	 * some information, usually called evidence, that has already been
	 * revealed. This is the conditional or posterior probability (or just
	 * "posterior" for short). Mathematically speaking, conditional
	 * probabilities are defined in terms of unconditional probabilities as
	 * follows, for any propositions a and b, we have:<br>
	 * <br>
	 * P(a | b) = P(a AND b)/P(b)<br>
	 * <br>
	 * which holds whenever P(b) > 0. Refer to equation 13.3 page 485 of AIMA3e.
	 * This can be rewritten in a different form called the <b>product rule</b>: <br>
	 * <br>
	 * P(a AND b) = P(a | b)P(b)<br>
	 * <br>
	 * and also as:<br>
	 * <br>
	 * P(a AND b) = P(b | a)P(a)<br>
	 * <br>
	 * whereby, equating the two right-hand sides and dividing by P(a) gives you
	 * Bayes' rule:<br>
	 * <br>
	 * P(b | a) = (P(a | b)P(b))/P(a) - i.e. (likelihood * prior)/evidence
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