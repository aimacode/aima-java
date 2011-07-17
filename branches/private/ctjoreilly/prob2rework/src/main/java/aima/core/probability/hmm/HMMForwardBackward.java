package aima.core.probability.hmm;

import java.util.ArrayList;
import java.util.List;

import aima.core.probability.CategoricalDistribution;
import aima.core.probability.proposition.AssignmentProposition;
import aima.core.probability.temporal.ForwardBackwardInference;
import aima.core.util.math.Matrix;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 576.<br>
 * <br>
 * 
 * <pre>
 * function FORWARD-BACKWARD(ev, prior) returns a vector of probability distributions
 *   inputs: ev, a vector of evidence values for steps 1,...,t
 *           prior, the prior distribution on the initial state, <b>P</b>(X<sub>0</sub>)
 *   local variables: fv, a vector of forward messages for steps 0,...,t
 *                    b, a representation of the backward message, initially all 1s
 *                    sv, a vector of smoothed estimates for steps 1,...,t
 *                    
 *   fv[0] <- prior
 *   for i = 1 to t do
 *       fv[i] <- FORWARD(fv[i-1], ev[i])
 *   for i = t downto 1 do
 *       sv[i] <- NORMALIZE(fv[i] * b)
 *       b <- BACKWARD(b, ev[i])
 *   return sv
 * </pre>
 * 
 * Figure 15.4 The forward-backward algorithm for smoothing: computing posterior
 * probabilities of a sequence of states given a sequence of observations. The
 * FORWARD and BACKWARD operators are defined by Equations (15.5) and (15.9),
 * respectively.<br>
 * <br>
 * <b>Note:</b> An implementation of the FORWARD-BACKWARD algorithm using a
 * Hidden Markov Model as the underlying model implementation.
 * 
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 */
public class HMMForwardBackward implements ForwardBackwardInference {

	protected HiddenMarkovModel hmm = null;

	public HMMForwardBackward(HiddenMarkovModel hmm) {
		this.hmm = hmm;
	}

	//
	// START-ForwardBackwardInference
	@Override
	public List<CategoricalDistribution> forwardBackward(
			List<List<AssignmentProposition>> ev, CategoricalDistribution prior) {
		// local variables: fv, a vector of forward messages for steps 0,...,t
		List<Matrix> fv = new ArrayList<Matrix>(ev.size() + 1);
		// b, a representation of the backward message, initially all 1s
		Matrix b = hmm.createUnitMessage();
		// sv, a vector of smoothed estimates for steps 1,...,t
		List<Matrix> sv = new ArrayList<Matrix>(ev.size());

		// fv[0] <- prior
		fv.add(hmm.convert(prior));
		// for i = 1 to t do
		for (int i = 0; i < ev.size(); i++) {
			// fv[i] <- FORWARD(fv[i-1], ev[i])
			fv.add(forward(fv.get(i), hmm.getEvidence(ev.get(i))));
		}
		// for i = t downto 1 do
		for (int i = ev.size() - 1; i >= 0; i--) {
			// sv[i] <- NORMALIZE(fv[i] * b)
			sv.add(0, hmm.normalize(fv.get(i + 1).arrayTimes(b)));
			// b <- BACKWARD(b, ev[i])
			b = backward(b, hmm.getEvidence(ev.get(i)));
		}

		// return sv
		return hmm.convert(sv);
	}

	@Override
	public CategoricalDistribution forward(CategoricalDistribution f1_t,
			List<AssignmentProposition> e_tp1) {
		return hmm.convert(forward(hmm.convert(f1_t), hmm.getEvidence(e_tp1)));
	}

	@Override
	public CategoricalDistribution backward(CategoricalDistribution b_kp2t,
			List<AssignmentProposition> e_kp1) {
		return hmm
				.convert(backward(hmm.convert(b_kp2t), hmm.getEvidence(e_kp1)));
	}

	// END-ForwardBackwardInference
	//

	public Matrix forward(Matrix f1_t, Matrix e_tp1) {
		return hmm.normalize(e_tp1.times(hmm.getTransitionModel().transpose()
				.times(f1_t)));
	}

	public Matrix backward(Matrix b_kp2t, Matrix e_kp1) {
		return hmm.getTransitionModel().times(e_kp1).times(b_kp2t);
	}
}
