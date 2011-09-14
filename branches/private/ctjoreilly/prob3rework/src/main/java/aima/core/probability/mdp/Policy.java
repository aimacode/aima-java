package aima.core.probability.mdp;

import aima.core.agent.Action;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 647.<br>
 * <br>
 * 
 * A solution to a Markov decision process is called a <b>policy</b>. It
 * specifies what the agent should do for any state that the agent might reach.
 * It is traditional to denote a policy by &pi;, and &pi;(s) is the action
 * recommended by the policy &pi; for state s. If the agent has a complete
 * policy, then no matter what the outcome of any action, the agent will always
 * know what to do next.
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
public interface Policy<S, A extends Action> {
	/**
	 * &pi;(s) is the action recommended by the policy &pi; for state s.
	 * 
	 * @param s
	 *            the state s
	 * @return the action recommended by the policy &pi; for state s.
	 */
	A action(S s);
}
