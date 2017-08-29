package aima.extra.probability.bayes;

import aima.extra.probability.proposition.Proposition;

/**
 * General interface to be implemented by Bayesian Inference algorithms.
 * 
 * @author Ciaran O'Reilly
 * @author Nagaraj Poti
 */
public interface BayesInference {
	
    /**
     * @param X
     *            the query variables.
     * @param observedEvidence
     *            observed values for variables E.
     * @param bn
     *            a Bayes net with variables {X} &cup; E &cup; Y // Y = hidden
     *            variables //
     *            
     * @return a distribution over the query variables.
     */
    CategoricalDistribution ask(Proposition X, Proposition observedEvidence, BayesianNetwork bn);
}
