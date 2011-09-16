package aima.core.probability.bayes.approx;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import aima.core.probability.CategoricalDistribution;
import aima.core.probability.RandomVariable;
import aima.core.probability.bayes.BayesianNetwork;
import aima.core.probability.proposition.AssignmentProposition;
import aima.core.probability.util.ProbUtil;
import aima.core.probability.util.ProbabilityTable;
import aima.core.util.JavaRandomizer;
import aima.core.util.Randomizer;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 537.<br>
 * <br>
 * 
 * <pre>
 * function GIBBS-ASK(X, e, bn, N) returns an estimate of <b>P</b>(X|e)
 *   local variables: <b>N</b>, a vector of counts for each value of X, initially zero
 *                    Z, the nonevidence variables in bn
 *                    <b>x</b>, the current state of the network, initially copied from e
 *                    
 *   initialize <b>x</b> with random values for the variables in Z
 *   for j = 1 to N do
 *       for each Z<sub>i</sub> in Z do
 *           set the value of Z<sub>i</sub> in <b>x</b> by sampling from <b>P</b>(Z<sub>i</sub>|mb(Z<sub>i</sub>))
 *           <b>N</b>[x] <- <b>N</b>[x] + 1 where x is the value of X in <b>x</b>
 *   return NORMALIZE(<b>N</b>)
 * </pre>
 * 
 * Figure 14.16 The Gibbs sampling algorithm for approximate inference in
 * Bayesian networks; this version cycles through the variables, but choosing
 * variables at random also works.<br>
 * <br>
 * <b>Note:</b> The implementation has been extended to handle queries with
 * multiple variables. <br>
 * 
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 */
public class GibbsAsk implements BayesSampleInference {
	private Randomizer randomizer = null;

	public GibbsAsk() {
		this(new JavaRandomizer(new Random()));
	}

	public GibbsAsk(Randomizer r) {
		this.randomizer = r;
	}

	// function GIBBS-ASK(X, e, bn, N) returns an estimate of <b>P</b>(X|e)
	/**
	 * The GIBBS-ASK algorithm in Figure 14.16. For answering queries given
	 * evidence in a Bayesian Network.
	 * 
	 * @param X
	 *            the query variables
	 * @param e
	 *            observed values for variables E
	 * @param bn
	 *            a Bayesian network specifying joint distribution
	 *            <b>P</b>(X<sub>1</sub>,...,X<sub>n</sub>)
	 * @param Nsamples
	 *            the total number of samples to be generated
	 * @return an estimate of <b>P</b>(X|e)
	 */
	public CategoricalDistribution gibbsAsk(RandomVariable[] X,
			AssignmentProposition[] e, BayesianNetwork bn, int Nsamples) {
		// local variables: <b>N</b>, a vector of counts for each value of X,
		// initially zero
		double[] N = new double[ProbUtil
				.expectedSizeOfCategoricalDistribution(X)];
		// Z, the nonevidence variables in bn
		Set<RandomVariable> Z = new LinkedHashSet<RandomVariable>(
				bn.getVariablesInTopologicalOrder());
		for (AssignmentProposition ap : e) {
			Z.remove(ap.getTermVariable());
		}
		// <b>x</b>, the current state of the network, initially copied from e
		Map<RandomVariable, Object> x = new LinkedHashMap<RandomVariable, Object>();
		for (AssignmentProposition ap : e) {
			x.put(ap.getTermVariable(), ap.getValue());
		}

		// initialize <b>x</b> with random values for the variables in Z
		for (RandomVariable Zi : Z) {
			x.put(Zi, ProbUtil.randomSample(bn.getNode(Zi), x, randomizer));
		}

		// for j = 1 to N do
		for (int j = 0; j < Nsamples; j++) {
			// for each Z<sub>i</sub> in Z do
			for (RandomVariable Zi : Z) {
				// set the value of Z<sub>i</sub> in <b>x</b> by sampling from
				// <b>P</b>(Z<sub>i</sub>|mb(Z<sub>i</sub>))
				x.put(Zi,
						ProbUtil.mbRandomSample(bn.getNode(Zi), x, randomizer));
			}
			// Note: moving this outside the previous for loop,
			// as described in fig 14.6, as will only work
			// correctly in the case of a single query variable X.
			// However, when multiple query variables, rare events
			// will get weighted incorrectly if done above. In case
			// of single variable this does not happen as each possible
			// value gets * |Z| above, ending up with the same ratios
			// when normalized (i.e. its still more efficient to place
			// outside the loop).
			//
			// <b>N</b>[x] <- <b>N</b>[x] + 1
			// where x is the value of X in <b>x</b>
			N[ProbUtil.indexOf(X, x)] += 1.0;
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
		return gibbsAsk(X, observedEvidence, bn, N);
	}

	// END-BayesSampleInference
	//
}
