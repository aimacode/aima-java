package aima.core.probability.mdp.search;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import aima.core.agent.Action;
import aima.core.probability.mdp.MarkovDecisionProcess;
import aima.core.probability.mdp.Policy;
import aima.core.probability.mdp.PolicyEvaluation;
import aima.core.probability.mdp.impl.LookupPolicy;
import aima.core.util.Util;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 657.<br>
 * <br>
 * 
 * <pre>
 * function POLICY-ITERATION(mdp) returns a policy
 *   inputs: mdp, an MDP with states S, actions A(s), transition model P(s' | s, a)
 *   local variables: U, a vector of utilities for states in S, initially zero
 *                    &pi;, a policy vector indexed by state, initially random
 *                    
 *   repeat
 *      U <- POLICY-EVALUATION(&pi;, U, mdp)
 *      unchanged? <- true
 *      for each state s in S do
 *          if max<sub>a &isin; A(s)</sub> &Sigma;<sub>s'</sub>P(s'|s,a)U[s'] > &Sigma;<sub>s'</sub>P(s'|s,&pi;[s])U[s'] then do
 *             &pi;[s] <- argmax<sub>a &isin; A(s)</sub> &Sigma;<sub>s'</sub>P(s'|s,a)U[s']
 *             unchanged? <- false
 *   until unchanged?
 *   return &pi;
 * </pre>
 * 
 * Figure 17.7 The policy iteration algorithm for calculating an optimal policy.
 * 
 * @param <S>
 *            the state type.
 * @param <A>
 *            the action type.
 * 
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 * 
 */
public class PolicyIteration<S, A extends Action> {

	private PolicyEvaluation<S, A> policyEvaluation = null;

	/**
	 * Constructor.
	 * 
	 * @param policyEvaluation
	 *            the policy evaluation function to use.
	 */
	public PolicyIteration(PolicyEvaluation<S, A> policyEvaluation) {
		this.policyEvaluation = policyEvaluation;
	}

	// function POLICY-ITERATION(mdp) returns a policy
	/**
	 * The policy iteration algorithm for calculating an optimal policy.
	 * 
	 * @param mdp
	 *            an MDP with states S, actions A(s), transition model P(s'|s,a)
	 * @return an optimal policy
	 */
	public Policy<S, A> policyIteration(MarkovDecisionProcess<S, A> mdp) {
		// local variables: U, a vector of utilities for states in S, initially
		// zero
		Map<S, Double> U = Util.create(mdp.states(), new Double(0));
		// &pi;, a policy vector indexed by state, initially random
		Map<S, A> pi = initialPolicyVector(mdp);
		boolean unchanged;
		// repeat
		do {
			// U <- POLICY-EVALUATION(&pi;, U, mdp)
			U = policyEvaluation.evaluate(pi, U, mdp);
			// unchanged? <- true
			unchanged = true;
			// for each state s in S do
			for (S s : mdp.states()) {
				// calculate:
				// max<sub>a &isin; A(s)</sub>
				// &Sigma;<sub>s'</sub>P(s'|s,a)U[s']
				double aMax = Double.NEGATIVE_INFINITY, piVal = 0;
				A aArgmax = pi.get(s);
				for (A a : mdp.actions(s)) {
					double aSum = 0;
					for (S sDelta : mdp.states()) {
						aSum += mdp.transitionProbability(sDelta, s, a)
								* U.get(sDelta);
					}
					if (aSum > aMax) {
						aMax = aSum;
						aArgmax = a;
					}
					// track:
					// &Sigma;<sub>s'</sub>P(s'|s,&pi;[s])U[s']
					if (a.equals(pi.get(s))) {
						piVal = aSum;
					}
				}
				// if max<sub>a &isin; A(s)</sub>
				// &Sigma;<sub>s'</sub>P(s'|s,a)U[s']
				// > &Sigma;<sub>s'</sub>P(s'|s,&pi;[s])U[s'] then do
				if (aMax > piVal) {
					// &pi;[s] <- argmax<sub>a &isin;A(s)</sub>
					// &Sigma;<sub>s'</sub>P(s'|s,a)U[s']
					pi.put(s, aArgmax);
					// unchanged? <- false
					unchanged = false;
				}
			}
			// until unchanged?
		} while (!unchanged);

		// return &pi;
		return new LookupPolicy<S, A>(pi);
	}

	/**
	 * Create a policy vector indexed by state, initially random.
	 * 
	 * @param mdp
	 *            an MDP with states S, actions A(s), transition model P(s'|s,a)
	 * @return a policy vector indexed by state, initially random.
	 */
	public static <S, A extends Action> Map<S, A> initialPolicyVector(
			MarkovDecisionProcess<S, A> mdp) {
		Map<S, A> pi = new LinkedHashMap<S, A>();
		List<A> actions = new ArrayList<A>();
		for (S s : mdp.states()) {
			actions.clear();
			actions.addAll(mdp.actions(s));
			// Handle terminal states (i.e. no actions).
			if (actions.size() > 0) {
				pi.put(s, Util.selectRandomlyFromList(actions));
			}
		}
		return pi;
	}
}
