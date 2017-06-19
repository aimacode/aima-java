package aima.core.search.framework.problem;

import aima.core.search.framework.Node;

import java.util.List;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 147.<br>
 * <br>
 * An online search problem must be solved by an agent executing actions, rather
 * than by pure computation. We assume a deterministic and fully observable
 * environment (Chapter 17 relaxes these assumptions), but we stipulate that the
 * agent knows only the following: <br>
 * <ul>
 * <li>ACTIONS(s), which returns a list of actions allowed in state s;</li>
 * <li>The step-cost function c(s, a, s') - note that this cannot be used until
 * the agent knows that s' is the outcome; and</li>
 * <li>GOAL-TEST(s).</li>
 * </ul>
 *
 * @param <S> The type used to represent states
 * @param <A> The type of the actions to be used to navigate through the state space
 *
 * @author Ruediger Lunde
 */
public interface OnlineSearchProblem<S, A> {

	/**
	 * Returns the initial state of the agent.
	 */
	S getInitialState();

	/**
	 * Returns the description of the possible actions available to the agent.
	 */
	List<A> getActions(S state);

	/**
	 * Determines whether a given state is a goal state.
	 */
	boolean testGoal(S state);

	/**
	 * Returns the <b>step cost</b> of taking action <code>action</code> in state <code>state</code> to reach state
	 * <code>stateDelta</code> denoted by c(s, a, s').
	 */
	double getStepCosts(S state, A action, S stateDelta);
}