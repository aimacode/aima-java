package aima.core.probability.mdp;

import java.util.Map;

import aima.core.agent.Action;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 656.<br>
 * <br>
 * Given a policy &pi;<sub>i</sub>, calculate
 * U<sub>i</sub>=U<sup>&pi;<sub>i</sub></sup>, the utility of each state if
 * &pi;<sub>i</sub> were to be executed.
 * 
 * @param <S>
 *            the state type.
 * @param <A>
 *            the action type.
 * 
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 */
public interface PolicyEvaluation<S, A extends Action> {
	/**
	 * <b>Policy evaluation:</b> given a policy &pi;<sub>i</sub>, calculate
	 * U<sub>i</sub>=U<sup>&pi;<sub>i</sub></sup>, the utility of each state if
	 * &pi;<sub>i</sub> were to be executed.
	 * 
	 * @param pi_i
	 *            a policy vector indexed by state
	 * @param U
	 *            a vector of utilities for states in S
	 * @param mdp
	 *            an MDP with states S, actions A(s), transition model P(s'|s,a)
	 * @return U<sub>i</sub>=U<sup>&pi;<sub>i</sub></sup>, the utility of each
	 *         state if &pi;<sub>i</sub> were to be executed.
	 */
	Map<S, Double> evaluate(Map<S, A> pi_i, Map<S, Double> U,
			MarkovDecisionProcess<S, A> mdp);
}
