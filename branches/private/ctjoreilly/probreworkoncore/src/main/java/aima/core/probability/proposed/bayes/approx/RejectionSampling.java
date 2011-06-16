package aima.core.probability.proposed.bayes.approx;

import java.util.Map;

import aima.core.probability.proposed.CategoricalDistribution;
import aima.core.probability.proposed.RandomVariable;
import aima.core.probability.proposed.bayes.BayesianNetwork;
import aima.core.probability.proposed.domain.FiniteDomain;
import aima.core.probability.proposed.proposition.AssignmentProposition;
import aima.core.probability.proposed.util.ProbabilityTable;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 533.<br>
 * <br>
 * 
 * <pre>
 * function REJECTION-SAMPLING(X, e, bn, N) returns an estimate of <b>P</b>(X|e)
 *   inputs: X, the query variable
 *           e, observed values for variables E
 *           bn, a Bayesian network
 *           N, the total number of samples to be generated
 *   local variables: <b>N</b>, a vector of counts for each value of X, initially zero
 *   
 *   for j = 1 to N do
 *       <b>x</b> <- PRIOR-SAMPLE(bn)
 *       if <b>x</b> is consistent with e then
 *          <b>N</b>[x] <- <b>N</b>[x] + 1 where x is the value of X in <b>x</b>
 *   return NORMALIZE(<b>N</b>)
 * </pre>
 * 
 * Figure 14.14 The rejection-sampling algorithm for answering queries given
 * evidence in a Bayesian Network.
 * 
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 */
public class RejectionSampling {

	private PriorSample ps = null;

	public RejectionSampling() {
		this(new PriorSample());
	}

	public RejectionSampling(PriorSample ps) {
		this.ps = ps;
	}

	/**
	 * The REJECTION-SAMPLING algorithm in Figure 14.14. For answering queries
	 * given evidence in a Bayesian Network.
	 * 
	 * @param X
	 *            the query variable
	 * @param e
	 *            observed values for variables E
	 * @param bn
	 *            a Bayesian network
	 * @param Nsamples
	 *            the total number of samples to be generated
	 * @return an estimate of <b>P</b>(X|e)
	 */
	public CategoricalDistribution rejectionSampling(RandomVariable X,
			AssignmentProposition[] e, BayesianNetwork bn, int Nsamples) {
		// local variables: <b>N</b>, a vector of counts for each value of X,
		// initially zero
		double[] N = new double[X.getDomain().size()];

		// for j = 1 to N do
		for (int j = 0; j < Nsamples; j++) {
			// <b>x</b> <- PRIOR-SAMPLE(bn)
			Map<RandomVariable, Object> x = ps.priorSample(bn);
			// if <b>x</b> is consistent with e then
			if (isConsistent(x, e)) {
				// <b>N</b>[x] <- <b>N</b>[x] + 1
				// where x is the value of X in <b>x</b>
				N[indexOf(X, x)] += 1.0;
			}
		}
		// return NORMALIZE(<b>N</b>)
		return new ProbabilityTable(N, X).normalize();
	}

	//
	// PRIVATE METHODS
	//
	private boolean isConsistent(Map<RandomVariable, Object> x,
			AssignmentProposition[] e) {

		for (AssignmentProposition ap : e) {
			if (!ap.getValue().equals(x.get(ap.getTermVariable()))) {
				return false;
			}
		}
		return true;
	}

	private int indexOf(RandomVariable X, Map<RandomVariable, Object> x) {
		return ((FiniteDomain) X.getDomain()).getOffset(x.get(X));
	}
}
