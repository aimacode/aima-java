package aima.core.probability.hmm.exact;

import java.util.LinkedList;
import java.util.List;

import aima.core.probability.CategoricalDistribution;
import aima.core.probability.hmm.HiddenMarkovModel;
import aima.core.probability.proposition.AssignmentProposition;
import aima.core.util.math.Matrix;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 580.<br>
 * <br>
 * 
 * <pre>
 * function FIXED-LAG-SMOOTHING(e<sub>t</sub>, hmm, d) returns a distribution over <b>X</b><sub>t-d</sub>
 *   inputs: e<sub>t</sub>, the current evidence from time step t
 *           hmm, a hidden Markov model with S * S transition matrix <b>T</b>
 *           d, the length of the lag for smoothing
 *   persistent: t, the current time, initially 1
 *               <b>f</b>, the forward message <b>P</b>(X<sub>t</sub> | e<sub>1:t</sub>), initially hmm.PRIOR
 *               <b>B</b>, the d-step backward transformation matrix, initially the identity matrix
 *               e<sub>t-d:t</sub>, double-ended list of evidence from t-d to t, initially empty
 *   local variables: <b>O</b><sub>t-d</sub>, <b>O</b><sub>t</sub>, diagonal matrices containing the sensor model information
 *   
 *   add e<sub>t</sub> to the end of e<sub>t-d:t</sub>
 *   <b>O</b><sub>t</sub> <- diagonal matrix containing <b>P</b>(e<sub>t</sub> | X<sub>t</sub>)
 *   if t > d then
 *        <b>f</b> <- FORWARD(<b>f</b>, e<sub>t</sub>)
 *        remove e<sub>t-d-1</sub> from the beginning of e<sub>t-d:t</sub>
 *        <b>O</b><sub>t-d</sub> <- diagonal matrix containing <b>P</b>(e<sub>t-d</sub> | X<sub>t-d</sub>)
 *        <b>B</b> <- <b>O</b><sup>-1</sup><sub>t-d</sub><b>B</b><b>T</b><b>O</b><sub>t</sub>
 *   else <b>B</b> <- <b>BTO</b><sub>t</sub>
 *   t <- t + 1
 *   if t > d then return NORMALIZE(<b>f</b> * <b>B1</b>) else return null
 * </pre>
 * 
 * Figure 15.6 An algorithm for smoothing with a fixed time lag of d steps,
 * implemented as an online algorithm that outputs the new smoothed estimate
 * given the observation for a new time step. Notice that the final output
 * NORMALIZE(<b>f</b> * <b>B1</b>) is just &alpha;<b>f</b>*<b>b</b>, by Equation
 * (15.14).<br>
 * <br>
 * <b>Note:</b> There appears to be two minor defects in the algorithm outlined
 * in the book:<br>
 * <b>f</b> <- FORWARD(<b>f</b>, e<sub>t</sub>)<br>
 * should be:<br>
 * <b>f</b> <- FORWARD(<b>f</b>, e<sub>t-d</sub>)<br>
 * as we are returning a smoothed step for t-d and not the current time t. <br>
 * <br>
 * The update of:<br>
 * t <- t + 1<br>
 * should occur after the return value is calculated. Otherwise when t == d the
 * value returned is based on HMM.prior in the calculation as opposed to a
 * correctly calculated forward message. Comments welcome.
 * 
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 * 
 */
public class FixedLagSmoothing {

	// persistent:
	// t, the current time, initially 1
	private int t = 1;
	// <b>f</b>, the forward message <b>P</b>(X<sub>t</sub> | e<sub>1:t</sub>),
	// initially hmm.PRIOR
	private Matrix f = null;
	// <b>B</b>, the d-step backward transformation matrix, initially the
	// identity matrix
	private Matrix B = null;
	// e<sub>t-d:t</sub>, double-ended list of evidence from t-d to t, initially
	// empty
	private List<Matrix> e_tmd_to_t = new LinkedList<Matrix>();
	// a hidden Markov model with S * S transition matrix <b>T</b>
	private HiddenMarkovModel hmm = null;
	// d, the length of the lag for smoothing
	private int d = 1;
	//
	private Matrix unitMessage = null;

	/**
	 * Create a Fixed-Lag-Smoothing implementation, that sets up the required
	 * persistent values.
	 * 
	 * @param hmm
	 *            a hidden Markov model with S * S transition matrix <b>T</b>
	 * @param d
	 *            d, the length of the lag for smoothing
	 */
	public FixedLagSmoothing(HiddenMarkovModel hmm, int d) {
		this.hmm = hmm;
		this.d = d;
		initPersistent();
	}

	/**
	 * Algorithm for smoothing with a fixed time lag of d steps, implemented as
	 * an online algorithm that outputs the new smoothed estimate given the
	 * observation for a new time step.
	 * 
	 * @param et
	 *            the current evidence from time step t
	 * @return a distribution over <b>X</b><sub>t-d</sub>
	 */
	public CategoricalDistribution fixedLagSmoothing(
			List<AssignmentProposition> et) {
		// local variables: <b>O</b><sub>t-d</sub>, <b>O</b><sub>t</sub>,
		// diagonal matrices containing the sensor model information
		Matrix O_tmd, O_t;

		// add e<sub>t</sub> to the end of e<sub>t-d:t</sub>
		e_tmd_to_t.add(hmm.getEvidence(et));
		// <b>O</b><sub>t</sub> <- diagonal matrix containing
		// <b>P</b>(e<sub>t</sub> | X<sub>t</sub>)
		O_t = e_tmd_to_t.get(e_tmd_to_t.size() - 1);
		// if t > d then
		if (t > d) {
			// remove e<sub>t-d-1</sub> from the beginning of e<sub>t-d:t</sub>
			e_tmd_to_t.remove(0);
			// <b>O</b><sub>t-d</sub> <- diagonal matrix containing
			// <b>P</b>(e<sub>t-d</sub> | X<sub>t-d</sub>)
			O_tmd = e_tmd_to_t.get(0);
			// <b>f</b> <- FORWARD(<b>f</b>, e<sub>t-d</sub>)
			f = forward(f, O_tmd);
			// <b>B</b> <-
			// <b>O</b><sup>-1</sup><sub>t-d</sub><b>B</b><b>T</b><b>O</b><sub>t</sub>
			B = O_tmd.inverse().times(hmm.getTransitionModel().inverse())
					.times(B).times(hmm.getTransitionModel()).times(O_t);
		} else {
			// else <b>B</b> <- <b>BTO</b><sub>t</sub>
			B = B.times(hmm.getTransitionModel()).times(O_t);
		}

		// if t > d then return NORMALIZE(<b>f</b> * <b>B1</b>) else return null
		CategoricalDistribution rVal = null;
		if (t > d) {
			rVal = hmm
					.convert(hmm.normalize(f.arrayTimes(B.times(unitMessage))));
		}
		// t <- t + 1
		t = t + 1;
		return rVal;
	}

	/**
	 * The forward equation (15.5) in Matrix form becomes (15.12):<br>
	 * 
	 * <pre>
	 * <b>f</b><sub>1:t+1</sub> = &alpha;<b>O</b><sub>t+1</sub><b>T</b><sup>T</sup><b>f</b><sub>1:t</sub>
	 * </pre>
	 * 
	 * @param f1_t
	 *            <b>f</b><sub>1:t</sub>
	 * @param O_tp1
	 *            <b>O</b><sub>t+1</sub>
	 * @return <b>f</b><sub>1:t+1</sub>
	 */
	public Matrix forward(Matrix f1_t, Matrix O_tp1) {
		return hmm.normalize(O_tp1.times(hmm.getTransitionModel().transpose()
				.times(f1_t)));
	}

	//
	// PRIVATE METHODS
	//
	private void initPersistent() {
		// t, the current time, initially 1
		t = 1;
		// <b>f</b>, the forward message <b>P</b>(X<sub>t</sub> |
		// e<sub>1:t</sub>),
		// initially hmm.PRIOR
		f = hmm.getPrior();
		// <b>B</b>, the d-step backward transformation matrix, initially the
		// identity matrix
		B = Matrix.identity(f.getRowDimension(), f.getRowDimension());
		// e<sub>t-d:t</sub>, double-ended list of evidence from t-d to t,
		// initially
		// empty
		e_tmd_to_t.clear();
		unitMessage = hmm.createUnitMessage();
	}
}
