package aima.core.probability.mdp.impl;

import java.util.HashMap;
import java.util.Map;

import aima.core.agent.Action;
import aima.core.probability.mdp.MarkovDecisionProcess;
import aima.core.probability.mdp.PolicyEvaluation;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 657.<br>
 * <br>
 * For small state spaces, policy evaluation using exact solution methods is
 * often the most efficient approach. For large state spaces, O(n<sup>3</sup>)
 * time might be prohibitive. Fortunately, it is not necessary to do exact
 * policy evaluation. Instead, we can perform some number of simplified value
 * iteration steps (simplified because the policy is fixed) to give a reasonably
 * good approximation of utilities. The simplified Bellman update for this
 * process is:<br>
 * <br>
 * 
 * <pre>
 * U<sub>i+1</sub>(s) <- R(s) + &gamma;&Sigma;<sub>s'</sub>P(s'|s,&pi;<sub>i</sub>(s))U<sub>i</sub>(s')
 * </pre>
 * 
 * and this is repeated k times to produce the next utility estimate. The
 * resulting algorithm is called <b>modified policy iteration</b>. It is often
 * much more efficient than standard policy iteration or value iteration.
 * 
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
public class ModifiedPolicyEvaluation<S, A extends Action> implements PolicyEvaluation<S, A> {
	// # iterations to use to produce the next utility estimate
	private int k;
	// discount &gamma; to be used.
	private double gamma;

	/**
	 * Constructor.
	 * 
	 * @param k
	 *            number iterations to use to produce the next utility estimate
	 * @param gamma
	 *            discount &gamma; to be used
	 */
	public ModifiedPolicyEvaluation(int k, double gamma) {
		if (gamma > 1.0 || gamma <= 0.0) {
			throw new IllegalArgumentException("Gamma must be > 0 and <= 1.0");
		}
		this.k = k;
		this.gamma = gamma;
	}
	
	//
	// START-PolicyEvaluation
	@Override
	public Map<S, Double> evaluate(Map<S, A> pi_i, Map<S, Double> U,
			MarkovDecisionProcess<S, A> mdp) {
		Map<S, Double> U_i = new HashMap<S, Double>(U);
		Map<S, Double> U_ip1 = new HashMap<S, Double>(U);
		// repeat k times to produce the next utility estimate
		for (int i = 0; i < k; i++) {
			// U<sub>i+1</sub>(s) <- R(s) +
			// &gamma;&Sigma;<sub>s'</sub>P(s'|s,&pi;<sub>i</sub>(s))U<sub>i</sub>(s')
			for (S s : U.keySet()) {
				A ap_i = pi_i.get(s);
				double aSum = 0;
				// Handle terminal states (i.e. no actions)
				if (null != ap_i) {
					for (S sDelta : U.keySet()) {
						aSum += mdp.transitionProbability(sDelta, s, ap_i)
								* U_i.get(sDelta);
					}
				}
				U_ip1.put(s, mdp.reward(s) + gamma * aSum);
			}

			U_i.putAll(U_ip1);
		}
		return U_ip1;
	}
	
	// END-PolicyEvaluation
	//
}
