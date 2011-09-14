package aima.core.probability.temporal.generic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aima.core.probability.CategoricalDistribution;
import aima.core.probability.FiniteProbabilityModel;
import aima.core.probability.RandomVariable;
import aima.core.probability.proposition.AssignmentProposition;
import aima.core.probability.proposition.Proposition;
import aima.core.probability.temporal.ForwardBackwardInference;
import aima.core.probability.util.ProbUtil;
import aima.core.probability.util.ProbabilityTable;
import aima.core.probability.util.RandVar;

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
 * <b>Note:</b> An implementation of the FORWARD-BACKWARD algorithm using the
 * general purpose probability APIs, i.e. the underlying model implementation is
 * abstracted away.
 * 
 * @author Ciaran O'Reilly
 */
public class ForwardBackward implements ForwardBackwardInference {

	private FiniteProbabilityModel transitionModel = null;
	private Map<RandomVariable, RandomVariable> tToTm1StateVarMap = new HashMap<RandomVariable, RandomVariable>();
	private FiniteProbabilityModel sensorModel = null;

	/**
	 * Instantiate an instance of the Forward Backward algorithm.
	 * 
	 * @param transitionModel
	 *            the transition model.
	 * @param tToTm1StateVarMap
	 *            a map from the X<sub>t<sub> random variables in the transition
	 *            model the to X<sub>t-1</sub> random variables.
	 * @param sensorModel
	 *            the sensor model.
	 */
	public ForwardBackward(FiniteProbabilityModel transitionModel,
			Map<RandomVariable, RandomVariable> tToTm1StateVarMap,
			FiniteProbabilityModel sensorModel) {
		this.transitionModel = transitionModel;
		this.tToTm1StateVarMap.putAll(tToTm1StateVarMap);
		this.sensorModel = sensorModel;
	}

	//
	// START-ForwardBackwardInference

	// function FORWARD-BACKWARD(ev, prior) returns a vector of probability
	// distributions
	@Override
	public List<CategoricalDistribution> forwardBackward(
			List<List<AssignmentProposition>> ev, CategoricalDistribution prior) {
		// local variables: fv, a vector of forward messages for steps 0,...,t
		List<CategoricalDistribution> fv = new ArrayList<CategoricalDistribution>(
				ev.size() + 1);
		// b, a representation of the backward message, initially all 1s
		CategoricalDistribution b = initBackwardMessage();
		// sv, a vector of smoothed estimates for steps 1,...,t
		List<CategoricalDistribution> sv = new ArrayList<CategoricalDistribution>(
				ev.size());

		// fv[0] <- prior
		fv.add(prior);
		// for i = 1 to t do
		for (int i = 0; i < ev.size(); i++) {
			// fv[i] <- FORWARD(fv[i-1], ev[i])
			fv.add(forward(fv.get(i), ev.get(i)));
		}
		// for i = t downto 1 do
		for (int i = ev.size() - 1; i >= 0; i--) {
			// sv[i] <- NORMALIZE(fv[i] * b)
			sv.add(0, fv.get(i + 1).multiplyBy(b).normalize());
			// b <- BACKWARD(b, ev[i])
			b = backward(b, ev.get(i));
		}

		// return sv
		return sv;
	}

	@Override
	public CategoricalDistribution forward(CategoricalDistribution f1_t,
			List<AssignmentProposition> e_tp1) {
		final CategoricalDistribution s1 = new ProbabilityTable(f1_t.getFor());
		// Set up required working variables
		Proposition[] props = new Proposition[s1.getFor().size()];
		int i = 0;
		for (RandomVariable rv : s1.getFor()) {
			props[i] = new RandVar(rv.getName(), rv.getDomain());
			i++;
		}
		final Proposition Xtp1 = ProbUtil.constructConjunction(props);
		final AssignmentProposition[] xt = new AssignmentProposition[tToTm1StateVarMap
				.size()];
		final Map<RandomVariable, AssignmentProposition> xtVarAssignMap = new HashMap<RandomVariable, AssignmentProposition>();
		i = 0;
		for (RandomVariable rv : tToTm1StateVarMap.keySet()) {
			xt[i] = new AssignmentProposition(tToTm1StateVarMap.get(rv),
					"<Dummy Value>");
			xtVarAssignMap.put(rv, xt[i]);
			i++;
		}

		// Step 1: Calculate the 1 time step prediction
		// &sum;<sub>x<sub>t</sub></sub>
		CategoricalDistribution.Iterator if1_t = new CategoricalDistribution.Iterator() {
			public void iterate(Map<RandomVariable, Object> possibleWorld,
					double probability) {
				// <b>P</b>(X<sub>t+1</sub> | x<sub>t</sub>)*
				// P(x<sub>t</sub> | e<sub>1:t</sub>)
				for (Map.Entry<RandomVariable, Object> av : possibleWorld
						.entrySet()) {
					xtVarAssignMap.get(av.getKey()).setValue(av.getValue());
				}
				int i = 0;
				for (double tp : transitionModel
						.posteriorDistribution(Xtp1, xt).getValues()) {
					s1.setValue(i, s1.getValues()[i] + (tp * probability));
					i++;
				}
			}
		};
		f1_t.iterateOver(if1_t);

		// Step 2: multiply by the probability of the evidence
		// and normalize
		// <b>P</b>(e<sub>t+1</sub> | X<sub>t+1</sub>)
		CategoricalDistribution s2 = sensorModel.posteriorDistribution(ProbUtil
				.constructConjunction(e_tp1.toArray(new Proposition[e_tp1
						.size()])), Xtp1);

		return s2.multiplyBy(s1).normalize();
	}

	@Override
	public CategoricalDistribution backward(CategoricalDistribution b_kp2t,
			List<AssignmentProposition> e_kp1) {
		final CategoricalDistribution b_kp1t = new ProbabilityTable(
				b_kp2t.getFor());
		// Set up required working variables
		Proposition[] props = new Proposition[b_kp1t.getFor().size()];
		int i = 0;
		for (RandomVariable rv : b_kp1t.getFor()) {
			RandomVariable prv = tToTm1StateVarMap.get(rv);
			props[i] = new RandVar(prv.getName(), prv.getDomain());
			i++;
		}
		final Proposition Xk = ProbUtil.constructConjunction(props);
		final AssignmentProposition[] ax_kp1 = new AssignmentProposition[tToTm1StateVarMap
				.size()];
		final Map<RandomVariable, AssignmentProposition> x_kp1VarAssignMap = new HashMap<RandomVariable, AssignmentProposition>();
		i = 0;
		for (RandomVariable rv : b_kp1t.getFor()) {
			ax_kp1[i] = new AssignmentProposition(rv, "<Dummy Value>");
			x_kp1VarAssignMap.put(rv, ax_kp1[i]);
			i++;
		}
		final Proposition x_kp1 = ProbUtil.constructConjunction(ax_kp1);
		props = new Proposition[e_kp1.size()];
		final Proposition pe_kp1 = ProbUtil.constructConjunction(e_kp1
				.toArray(props));

		// &sum;<sub>x<sub>k+1</sub></sub>
		CategoricalDistribution.Iterator ib_kp2t = new CategoricalDistribution.Iterator() {
			public void iterate(Map<RandomVariable, Object> possibleWorld,
					double probability) {
				// Assign current values for x<sub>k+1</sub>
				for (Map.Entry<RandomVariable, Object> av : possibleWorld
						.entrySet()) {
					x_kp1VarAssignMap.get(av.getKey()).setValue(av.getValue());
				}

				// P(e<sub>k+1</sub> | x<sub>k+1</sub>)
				// P(e<sub>k+2:t</sub> | x<sub>k+1</sub>)
				double p = sensorModel.posterior(pe_kp1, x_kp1) * probability;

				// <b>P</b>(x<sub>k+1</sub> | X<sub>k</sub>)
				int i = 0;
				for (double tp : transitionModel.posteriorDistribution(x_kp1,
						Xk).getValues()) {
					b_kp1t.setValue(i, b_kp1t.getValues()[i] + (tp * p));
					i++;
				}
			}
		};
		b_kp2t.iterateOver(ib_kp2t);

		return b_kp1t;
	}

	// END-ForwardBackwardInference
	//

	//
	// PRIVATE METHODS
	//
	private CategoricalDistribution initBackwardMessage() {
		ProbabilityTable b = new ProbabilityTable(tToTm1StateVarMap.keySet());

		for (int i = 0; i < b.size(); i++) {
			b.setValue(i, 1.0);
		}

		return b;
	}
}
