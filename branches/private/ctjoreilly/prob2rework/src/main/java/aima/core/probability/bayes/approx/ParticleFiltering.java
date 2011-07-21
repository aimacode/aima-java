package aima.core.probability.bayes.approx;

import java.util.ArrayList;
import java.util.List;

import aima.core.probability.bayes.DynamicBayesianNetwork;
import aima.core.probability.proposition.AssignmentProposition;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 598.<br>
 * <br>
 * 
 * <pre>
 * function PARTICLE-FILTERING(<b>e</b>, N, dbn) returns a set of samples for the next time step
 *   inputs: <b>e</b>, the new incoming evidence
 *           N, the number of samples to be maintained
 *           dbn, a DBN with prior <b>P</b>(<b>X</b><sub>0</sub>), transition model <b>P</b>(<b>X</b><sub>1</sub> | <b>X</b><sub>0</sub>), sensor model <b>P</b>(<b>E</b><sub>1</sub> | <b>X</b><sub>1</sub>)
 *   persistent: S, a vector of samples of size N, initially generated from <b>P</b>(<b>X</b><sub>0</sub>)
 *   local variables: W, a vector of weights of size N
 *   
 *   for i = 1 to N do
 *       S[i] <- sample from <b>P</b>(<b>X</b><sub>1</sub> | <b>X</b><sub>0</sub> = S[i]) /* step 1 
 *       W[i] <- <b>P</b>(<b>e</b> | <b>X</b><sub>1</sub> = S[i]) /* step 2
 *   S <- WEIGHTED-SAMPLE-WITH-REPLACEMENT(N, S, W) /* step 3
 *   return S
 * </pre>
 * 
 * Figure 15.17 The particle filtering algorithm implemented as a recursive
 * update operation with state (the set of samples). Each of the sampling
 * operations involves sampling the relevant slice variables in topological
 * order, much as in PRIOR-SAMPLE. The WEIGHTED-SAMPLE-WITH-REPLACEMENT
 * operation can be implemented to run in O(N) expected time. The step numbers
 * refer to the description in the text.
 * 
 * <ol>
 * <li>Each sample is propagated forward by sampling the next state value
 * <b>x</b><sub>t+1</sub> given the current value <b>x</b><sub>t</sub> for the
 * sample, based on the transition model <b>P</b>(<b>X</b><sub>t+1</sub> |
 * <b>x</b><sub>t</sub>).</li>
 * <li>Each sample is weighted by the likelihood if assigns to the new evidence,
 * P(<b>e</b><sub>t+1</sub> | <b>x</b><sub>t+1</sub>).</li>
 * <li>The population is resampled to generate a new population of N samples.
 * Each new sample is selected from the current population; the probability that
 * a particular sample is selected is proportional to its weight. The new
 * samples are unweighted.</li>
 * </ol>
 * 
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 * 
 */
public class ParticleFiltering {

	private int N = 0;
	private DynamicBayesianNetwork dbn = null;
	private List<List<AssignmentProposition>> S = new ArrayList<List<AssignmentProposition>>();

	/**
	 * 
	 * @param N
	 *            the number of samples to be maintained
	 * @param dbn
	 *            a DBN with prior <b>P</b>(<b>X</b><sub>0</sub>), transition
	 *            model <b>P</b>(<b>X</b><sub>1</sub> | <b>X</b><sub>0</sub>),
	 *            sensor model <b>P</b>(<b>E</b><sub>1</sub> |
	 *            <b>X</b><sub>1</sub>)
	 */
	public ParticleFiltering(int N, DynamicBayesianNetwork dbn) {
		reset(N, dbn);
	}

	/**
	 * 
	 * @param e
	 *            <b>e</b>, the new incoming evidence
	 * @return a set of samples for the next time step
	 */
	public List<List<AssignmentProposition>> particleFiltering(
			List<AssignmentProposition> e) {
		// local variables: W, a vector of weights of size N
		double[] W = new double[N];
		   
		// for i = 1 to N do
		for (int i = 0; i < N; i++) {
			// S[i] <- sample from <b>P</b>(<b>X</b><sub>1</sub> | <b>X</b><sub>0</sub> = S[i]) /* step 1 
			
			// W[i] <- <b>P</b>(<b>e</b> | <b>X</b><sub>1</sub> = S[i]) /* step 2
			
		}
		// S <- WEIGHTED-SAMPLE-WITH-REPLACEMENT(N, S, W) /* step 3
		
		// return S
		return S;
	}

	public void reset(int N, DynamicBayesianNetwork dbn) {
		this.N = N;
		this.dbn = dbn;
		// persistent: S, a vector of samples of size N, initially generated
		// from <b>P</b>(<b>X</b><sub>0</sub>)
		S.clear();
		// TODO
	}

}
