package aima.core.probability.proposed.bayes.approx;

import aima.core.probability.proposed.CategoricalDistribution;
import aima.core.probability.proposed.RandomVariable;
import aima.core.probability.proposed.bayes.BayesianNetwork;
import aima.core.probability.proposed.proposition.AssignmentProposition;

/**
 * General interface to be implemented by approximate Bayesian Inference
 * algorithms.
 * 
 * @author Ciaran O'Reilly
 */
public interface BayesSampleInference {
	/**
	 * @param X
	 *            the query variables.
	 * @param observedEvidence
	 *            observed values for variables E.
	 * @param bn
	 *            a Bayes net with variables {X} &cup; E &cup; Y /* Y = hidden
	 *            variables
	 * @param N
	 *            the total number of samples to be generated
	 * @return an estimate of <b>P</b>(X|e).
	 */
	CategoricalDistribution ask(final RandomVariable[] X,
			final AssignmentProposition[] observedEvidence,
			final BayesianNetwork bn, int N);
}
