package aima.core.probability.hmm.exact;

import java.util.ArrayList;
import java.util.List;

import aima.core.probability.CategoricalDistribution;
import aima.core.probability.hmm.HiddenMarkovModel;
import aima.core.probability.proposition.AssignmentProposition;
import aima.core.util.math.Matrix;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 579.<br>
 * <br>
 * 
 * Smoothing for any time slice <em>k</em> requires the simultaneous presence of
 * both the forward and backward messages, <b>f</b><sub>1:k</sub> and
 * <b>b</b><sub>k+1:t</sub>, according to Equation (15.8). The forward-backward
 * algorithm achieves this by storing the <b>f</b>s computed on the forward pass
 * so that they are available during the backward pass. Another way to achieve
 * this is with a single pass that propagates both <b>f</b> and <b>b</b> in the
 * same direction. For example, the "forward" message <b>f</b> can be propagated
 * backward if we manipulate Equation (15.12) to work in the other direction:<br>
 * 
 * <pre>
 * <b>f</b><sub>1:t</sub> = &alpha;<sup>'</sup>(<b>T</b><sup>T</sup>)<sup>-1</sup><b>O</b><sup>-1</sup><sub>t+1</sub><b>f</b><sub>1:t+1</sub>
 * </pre>
 * 
 * The modified smoothing algorithm works by first running the standard forward
 * pass to compute <b>f</b><sub>t:t</sub> (forgetting all intermediate results)
 * and then running the backward pass for both <b>b</b> and <b>f</b> together,
 * using them to compute the smoothed estimate at each step. Since only one copy
 * of each message is needed, the storage requirements are constant (i.e.
 * independent of t, the length of the sequence). There are two significant
 * restrictions on the algorithm: it requires that the transition matrix be
 * invertible and that the sensor model have no zeroes - that is, that every
 * observation be possible in every state.
 * 
 * @author Ciaran O'Reilly
 */
public class HMMForwardBackwardConstantSpace extends HMMForwardBackward {

	public HMMForwardBackwardConstantSpace(HiddenMarkovModel hmm) {
		super(hmm);
	}

	//
	// START-ForwardBackwardInference
	@Override
	public List<CategoricalDistribution> forwardBackward(
			List<List<AssignmentProposition>> ev, CategoricalDistribution prior) {
		// local variables: f, the forward message <- prior
		Matrix f = hmm.convert(prior);
		// b, a representation of the backward message, initially all 1s
		Matrix b = hmm.createUnitMessage();
		// sv, a vector of smoothed estimates for steps 1,...,t
		List<Matrix> sv = new ArrayList<Matrix>(ev.size());

		// for i = 1 to t do
		for (int i = 0; i < ev.size(); i++) {
			// fv[i] <- FORWARD(fv[i-1], ev[i])
			f = forward(f, hmm.getEvidence(ev.get(i)));
		}
		// for i = t downto 1 do
		for (int i = ev.size() - 1; i >= 0; i--) {
			// sv[i] <- NORMALIZE(fv[i] * b)
			sv.add(0, hmm.normalize(f.arrayTimes(b)));
			Matrix e = hmm.getEvidence(ev.get(i));
			// b <- BACKWARD(b, ev[i])
			b = backward(b, e);
			// f1:t <-
			// NORMALIZE((T<sup>T<sup>)<sup>-1</sup>O<sup>-1</sup><sub>t+1</sub>f<sub>1:t+1</sub>)
			f = forwardRecover(e, f);
		}

		// return sv
		return hmm.convert(sv);
	}

	// END-ForwardBackwardInference
	//

	/**
	 * Calculate:
	 * 
	 * <pre>
	 * <b>f</b><sub>1:t</sub> = &alpha;<sup>'</sup>(<b>T</b><sup>T</sup>)<sup>-1</sup><b>O</b><sup>-1</sup><sub>t+1</sub><b>f</b><sub>1:t+1</sub>
	 * </pre>
	 * 
	 * @param O_tp1
	 *            <b>O</b><sub>t+1</sub>
	 * @param f1_tp1
	 *            <b>f</b><sub>1:t+1</sub>
	 * @return <b>f</b><sub>1:t</sub>
	 */
	public Matrix forwardRecover(Matrix O_tp1, Matrix f1_tp1) {
		return hmm.normalize(hmm.getTransitionModel().transpose().inverse()
				.times(O_tp1.inverse()).times(f1_tp1));
	}
}
