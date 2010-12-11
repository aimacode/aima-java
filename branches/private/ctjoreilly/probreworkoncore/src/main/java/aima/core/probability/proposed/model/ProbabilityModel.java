package aima.core.probability.proposed.model;

import java.util.Set;

import aima.core.probability.proposed.model.proposition.RandomVariableProposition;
import aima.core.probability.proposed.model.proposition.ValueProposition;
import aima.core.probability.proposed.model.proposition.Proposition;

public interface ProbabilityModel {
	/**
	 * 
	 * @return true, if 0 <= P(&omega) <= 1 for every &omega and SUM(&omega E
	 *         &Omega, P(&omega)) = 1 (Equation 13.1 pg. 484 AIMA3e), false
	 *         otherwise.
	 */
	boolean isValid();
	
	/**
	 * @return a consistent ordered Set (e.g. LinkedHashSet) of the random
	 *         variables describing the atomic variable/value pairs this
	 *         probability model can take on.
	 */
	Set<RandomVariable> getRepresentation();

	/**
	 * For any proposition &phi, P(&phi) = SUM(&omega E &phi, P(&omega)). Refer
	 * to equation 13.2 page 485 of AIMA3e.
	 * 
	 * @param phi
	 *            the proposition for which a probability value is to be
	 *            returned.
	 * @return the probability of the proposition &phi.
	 */
	// TODO-document based on pg. 485
	double prior(ValueProposition phi);
	
	// TODO-document based on pg. 485 - (13.3), +product rule.
	double posterior(ValueProposition phi, ValueProposition... evidence);
	
	// TODO-document based on pg. 486
	ProbabilityDistribution prior(RandomVariableProposition var);
	
	// TODO-document based on pg. 487
	ProbabilityDistribution posterior(RandomVariableProposition phi, RandomVariableProposition... evidence);
	
	// TODO-document based on pg. 487
	JointProbabilityDistribution jointProbabilityDistribution(Proposition... propositions);
}