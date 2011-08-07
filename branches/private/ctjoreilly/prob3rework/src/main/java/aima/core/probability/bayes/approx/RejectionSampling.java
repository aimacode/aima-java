package aima.core.probability.bayes.approx;

import java.util.Map;

import aima.core.probability.CategoricalDistribution;
import aima.core.probability.RandomVariable;
import aima.core.probability.bayes.BayesianNetwork;
import aima.core.probability.proposition.AssignmentProposition;
import aima.core.probability.util.ProbUtil;
import aima.core.probability.util.ProbabilityTable;

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
 * evidence in a Bayesian Network.<br>
 * <br>
 * <b>Note:</b> The implementation has been extended to handle queries with
 * multiple variables. <br>
 * 
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 */
public class RejectionSampling implements BayesSampleInference {

	private PriorSample ps = null;

	public RejectionSampling() {
		this(new PriorSample());
	}

	public RejectionSampling(PriorSample ps) {
		this.ps = ps;
	}

	// function REJECTION-SAMPLING(X, e, bn, N) returns an estimate of
	// <b>P</b>(X|e)
	/**
	 * The REJECTION-SAMPLING algorithm in Figure 14.14. For answering queries
	 * given evidence in a Bayesian Network.
	 * 
	 * @param X
	 *            the query variables
	 * @param e
	 *            observed values for variables E
	 * @param bn
	 *            a Bayesian network
	 * @param Nsamples
	 *            the total number of samples to be generated
	 * @return an estimate of <b>P</b>(X|e)
	 */
	public CategoricalDistribution rejectionSampling(RandomVariable[] X,
			AssignmentProposition[] e, BayesianNetwork bn, int Nsamples) {
		// local variables: <b>N</b>, a vector of counts for each value of X,
		// initially zero
		double[] N = new double[ProbUtil
				.expectedSizeOfCategoricalDistribution(X)];

		// for j = 1 to N do
		for (int j = 0; j < Nsamples; j++) {
			// <b>x</b> <- PRIOR-SAMPLE(bn)
			Map<RandomVariable, Object> x = ps.priorSample(bn);
			// if <b>x</b> is consistent with e then
			if (isConsistent(x, e)) {
				// <b>N</b>[x] <- <b>N</b>[x] + 1
				// where x is the value of X in <b>x</b>
				N[ProbUtil.indexOf(X, x)] += 1.0;
			}
		}
		// return NORMALIZE(<b>N</b>)
		return new ProbabilityTable(N, X).normalize();
	}

	//
	// START-BayesSampleInference
	@Override
	public CategoricalDistribution ask(final RandomVariable[] X,
			final AssignmentProposition[] observedEvidence,
			final BayesianNetwork bn, int N) {
		return rejectionSampling(X, observedEvidence, bn, N);
	}

	// END-BayesSampleInference
	//

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
}
