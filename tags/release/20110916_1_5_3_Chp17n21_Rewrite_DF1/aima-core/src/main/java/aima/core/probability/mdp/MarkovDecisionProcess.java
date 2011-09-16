package aima.core.probability.mdp;

import java.util.Set;

import aima.core.agent.Action;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 647.<br>
 * <br>
 * 
 * A sequential decision problem for a fully observable, stochastic environment
 * with a Markovian transition model and additive rewards is called a <b>Markov
 * decision process</b>, or <b>MDP</b>, and consists of a set of states (with an
 * initial state s<sub>0</sub>; a set ACTIONS(s) of actions in each state; a
 * transition model P(s' | s, a); and a reward function R(s).<br>
 * <br>
 * <b>Note:</b> Some definitions of MDPs allow the reward to depend on the
 * action and outcome too, so the reward function is R(s, a, s'). This
 * simplifies the description of some environments but does not change the
 * problem in any fundamental way.
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
public interface MarkovDecisionProcess<S, A extends Action> {

	/**
	 * Get the set of states associated with the Markov decision process.
	 * 
	 * @return the set of states associated with the Markov decision process.
	 */
	Set<S> states();

	/**
	 * Get the initial state s<sub>0</sub> for this instance of a Markov
	 * decision process.
	 * 
	 * @return the initial state s<sub>0</sub>.
	 */
	S getInitialState();

	/**
	 * Get the set of actions for state s.
	 * 
	 * @param s
	 *            the state.
	 * @return the set of actions for state s.
	 */
	Set<A> actions(S s);

	/**
	 * Return the probability of going from state s using action a to s' based
	 * on the underlying transition model P(s' | s, a).
	 * 
	 * @param sDelta
	 *            the state s' being transitioned to.
	 * @param s
	 *            the state s being transitions from.
	 * @param a
	 *            the action used to move from state s to s'.
	 * @return the probability of going from state s using action a to s'.
	 */
	double transitionProbability(S sDelta, S s, A a);

	/**
	 * Get the reward associated with being in state s.
	 * 
	 * @param s
	 *            the state whose award is sought.
	 * @return the reward associated with being in state s.
	 */
	double reward(S s);
}
