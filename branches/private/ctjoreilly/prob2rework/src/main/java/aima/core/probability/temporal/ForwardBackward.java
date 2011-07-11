package aima.core.probability.temporal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aima.core.probability.CategoricalDistribution;
import aima.core.probability.FiniteProbabilityModel;
import aima.core.probability.RandomVariable;
import aima.core.probability.proposition.Proposition;
import aima.core.probability.proposition.AssignmentProposition;
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
 * 
 * @author Ciaran O'Reilly
 */
public class ForwardBackward {

	private FiniteProbabilityModel transitionModel = null;
	private Map<RandomVariable, RandomVariable> tToTm1StateVarMap = new HashMap<RandomVariable, RandomVariable>();
	private FiniteProbabilityModel sensorModel = null;

	public ForwardBackward(FiniteProbabilityModel transitionModel,
			Map<RandomVariable, RandomVariable> tToTm1StateVarMap,
			FiniteProbabilityModel sensorModel) {
		this.transitionModel = transitionModel;
		this.tToTm1StateVarMap.putAll(tToTm1StateVarMap);
		this.sensorModel = sensorModel;
	}

	// function FORWARD-BACKWARD(ev, prior) returns a vector of probability
	// distributions
	/**
	 * The forward-backward algorithm for smoothing: computing posterior
	 * probabilities of a sequence of states given a sequence of observations.
	 * 
	 * @param ev
	 *            a vector of evidence values for steps 1,...,t
	 * @param prior
	 *            the prior distribution on the initial state,
	 *            <b>P</b>(X<sub>0</sub>)
	 * @return a vector of smoothed estimates for steps 1,...,t
	 */
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

	/**
	 * The FORWARD operator is defined by Equation (15.5).<br>
	 * 
	 * <pre>
	 * <b>P</b>(X<sub>t+1</sub> | e<sub>1:t+1</sub>) 
	 * = &alpha;<b>P</b>(e<sub>t+1</sub> | X<sub>t+1</sub>)&sum;<sub>x<sub>t</sub></sub><b>P</b>(X<sub>t+1</sub> | x<sub>t</sub>, e<sub>1:t</sub>)P(x<sub>t</sub> | e<sub>1:t</sub>)
	 * = &alpha;<b>P</b>(e<sub>t+1</sub> | X<sub>t+1</sub>)&sum;<sub>x<sub>t</sub></sub><b>P</b>(X<sub>t+1</sub> | x<sub>t</sub>)P(x<sub>t</sub> | e<sub>1:t</sub>) (Markov Assumption)
	 * </pre>
	 * 
	 * @param f1_t
	 *            f<sub>1:t</sub>
	 * @param e_tp1
	 *            e<sub>t+1</sub>
	 * @return f<sub>1:t+1</sub>
	 */
	public CategoricalDistribution forward(CategoricalDistribution f1_t,
			List<AssignmentProposition> e_tp1) {
		final ProbabilityTable s1 = new ProbabilityTable(f1_t.getFor());
		// Set up required working variables
		Proposition[] props = new Proposition[s1.getFor().size()];
		int i = 0;
		for (RandomVariable rv : s1.getFor()) {
			props[i] = new RandVar(rv.getName(), rv.getDomain());
			i++;
		}
		final Proposition Xtp1 = ProbUtil.constructConjunction(props);

		// Step 1: Calculate the 1 time step prediction
		// &sum;<sub>x<sub>t</sub></sub>
		ProbabilityTable.Iterator if1_t = new ProbabilityTable.Iterator() {
			public void iterate(Map<RandomVariable, Object> possibleWorld,
					double probability) {
				// <b>P</b>(X<sub>t+1</sub> | x<sub>t</sub>)*
				// P(x<sub>t</sub> | e<sub>1:t</sub>)
				AssignmentProposition[] xt = new AssignmentProposition[possibleWorld
						.size()];
				int i = 0;
				for (RandomVariable rv : possibleWorld.keySet()) {
					xt[i] = new AssignmentProposition(
							tToTm1StateVarMap.get(rv), possibleWorld.get(rv));
					i++;
				}
				i = 0;
				for (double tp : transitionModel
						.posteriorDistribution(Xtp1, xt).getValues()) {
					s1.setValue(i, s1.getValues()[i] + (tp * probability));
					i++;
				}
			}

			public Object getPostIterateValue() {
				return null; // N/A
			}
		};
		((ProbabilityTable) f1_t).iterateOverTable(if1_t);

		// Step 2: multiply by the probability of the evidence
		// and normalize
		// <b>P</b>(e<sub>t+1</sub> | X<sub>t+1</sub>)
		props = new Proposition[e_tp1.size()];
		props = e_tp1.toArray(props);
		CategoricalDistribution s2 = sensorModel.posteriorDistribution(
				ProbUtil.constructConjunction(props), Xtp1);

		return s2.multiplyBy(s1).normalize();
	}

	/**
	 * The BACKWARD operator is defined by Equation (15.9).<br>
	 * 
	 * <pre>
	 * <b>P</b>(e<sub>k+1:t</sub> | X<sub>k</sub>) 
	 * = &sum;<sub>x<sub>k+1</sub></sub><b>P</b>(e<sub>k+1:t</sub> | X<sub>k</sub>, x<sub>k+1</sub>)<b>P</b>(x<sub>k+1</sub> | X<sub>k</sub>) (conditioning on X<sub>k+1</sub>)
	 * = &sum;<sub>x<sub>k+1</sub></sub><b>P</b>(e<sub>k+1:t</sub> | x<sub>k+1</sub>)<b>P</b>(x<sub>k+1</sub> | X<sub>k</sub>) (by conditional independence)
	 * = &sum;<sub>x<sub>k+1</sub></sub><b>P</b>(e<sub>k+1</sub>, e<sub>k+2:t</sub> | x<sub>k+1</sub>)<b>P</b>(x<sub>k+1</sub> | X<sub>k</sub>)
	 * = &sum;<sub>x<sub>k+1</sub></sub><b>P</b>(e<sub>k+1</sub> | x<sub>k+1</sub>)<b>P</b>(e<sub>k+2:t</sub> | x<sub>k+1</sub>)<b>P</b>(x<sub>k+1</sub> | X<sub>k</sub>)
	 * </pre>
	 * 
	 * @param current
	 * @param evidence
	 * @return
	 */
	public CategoricalDistribution backward(CategoricalDistribution current,
			List<AssignmentProposition> evidence) {
		CategoricalDistribution b = null;
		// TODO
		return b;
	}

	//
	// PRIVATE METHODS
	//
	private CategoricalDistribution initBackwardMessage() {
		ProbabilityTable b = new ProbabilityTable(
				transitionModel.getRepresentation());

		for (int i = 0; i < b.size(); i++) {
			b.setValue(0, 1.0);
		}

		return b;
	}
}
