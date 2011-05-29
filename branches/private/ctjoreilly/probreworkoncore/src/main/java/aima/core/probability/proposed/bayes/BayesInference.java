package aima.core.probability.proposed.bayes;

import aima.core.probability.proposed.Distribution;
import aima.core.probability.proposed.RandomVariable;
import aima.core.probability.proposed.proposition.AssignmentProposition;

/**
 * General interface to be implemented by Bayesian Inference algorithms.
 * 
 * @author Ciaran O'Reilly
 */
public interface BayesInference {
	/**
	 * @param X
	 *            the query variables.
	 * @param observedEvidence
	 *            observed values for variables E.
	 * @param bn
	 *            a Bayes net with variables {X} &cup; E &cup; Y /* Y = hidden
	 *            variables
	 * @return a distribution over the query variables.
	 */
	Distribution ask(final RandomVariable[] X,
			final AssignmentProposition[] observedEvidence,
			final BayesianNetwork bn);
}
