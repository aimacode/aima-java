package aima.core.probability.mdp.next.search;

import java.util.Map;

import aima.core.probability.mdp.next.MarkovDecisionProcess;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 647.<br>
 * <br>
 * 
 * <pre>
 * function VALUE-ITERATION(mdp, &epsilon;) returns a utility function
 *   inputs: mdp, an MDP with states S, actions A(s), transition model P(s' | s, a),
 *             rewards R(s), discount &gamma;
 *           &epsilon; the maximum error allowed in the utility of any state
 *   local variables: U, U', vectors of utilities for states in S, initially zero
 *                    &delta; the maximum change in the utility of any state in an iteration
 *                    
 *   repeat
 *       U <- U'; &delta; <- 0
 *       for each state s in S do
 *           U'[s] <- R(s) + &gamma;  max<sub>a &isin; A(s)</sub> &Sigma;<sub>s'</sub>P(s' | s, a) U[s']
 *           if |U'[s] - U[s]| > &delta; then &delta; <- |U'[s] - U[s]|
 *   until &delta; < &epsilon;(1 - &gamma;)/&gamma;
 *   return U
 * </pre>
 * 
 * Figure 17.4 The value iteration algorithm for calculating utilities of
 * states. The termination condition is from Equation (17.8):<br>
 * 
 * <pre>
 * if ||U<sub>i+1</sub> - U<sub>i</sub>|| < &epsilon;(1 - &gamma;)/&gamma; then ||U<sub>i+1</sub> - U|| < &epsilon;
 * </pre>
 * 
 * @param <S>
 *            the state type.
 * @param <T>
 *            the action type.
 * 
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 * 
 */
public class ValueIteration<S, A> {
	private double gamma = 0;

	/**
	 * Constructor.
	 * 
	 * @param gamma
	 *            discount &gamma; to be used.
	 */
	public ValueIteration(double gamma) {
		this.gamma = gamma;
	}

	/**
	 * The value iteration algorithm for calculating the utility of states.
	 * 
	 * @param mdp
	 *            an MDP with states S, actions A(s), <br>
	 *            transition model P(s' | s, a), rewards R(s)
	 * @param epsilon
	 *            the maximum error allowed in the utility of any state
	 * @return a vector of utilities for states in S
	 */
	public Map<S, Double> valueIteration(MarkovDecisionProcess<S, A> mdp,
			double epsilon) {

		return null; // TODO
	}

}
