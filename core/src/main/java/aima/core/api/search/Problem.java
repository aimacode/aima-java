package aima.core.api.search;

import java.util.Set;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): page ??.<br>
 * <br>
 * A problem can be defined formally by five components: <br>
 * <ul>
 * <li>The <b>initial state</b> that the agent starts in.</li>
 * <li>A description of the possible <b>actions</b> available to the agent.
 * Given a particular state s, ACTIONS(s) returns the set of actions that can be
 * executed in s.</li>
 * <li>A description of what each action does; the formal name for this is the
 * <b>transition model, specified by a function RESULT(s, a) that returns the
 * state that results from doing action a in state s.</b></li>
 * <li>The <b>goal test</b>, which determines whether a given state is a goal
 * state.</li>
 * <li>A <b>path cost</b> function that assigns a numeric cost to each path. The
 * problem-solving agent chooses a cost function that reflects its own
 * performance measure. The <b>step cost</b> of taking action a in state s to
 * reach state s' is denoted by c(s,a,s')</li>
 * </ul>
 *
 * @param <A> the type of the actions that can be performed.
 * @param <S> the type of the initial state that the agent starts in.
 * @author Ciaran O'Reilly
 */
public interface Problem<A, S> {
    /**
     * @return the initial state that the agent starts in.
     */
    S initialState();

    /**
     * A description of the possible actions available to the agent.
     *
     * @param s a particular state s
     * @return the set of actions that can be executed in s
     */
    Set<A> actions(S s);

    /**
     * Represents the Transition Model.
     *
     * @param s
     *        a state
     * @param a
     *        an action performed in state s
     * @return the state that results from doing action a in state s.
     */
    S result(S s, A a);

    /**
     * The goal test, which determines if a given state is a goal state.
     *
     * @param state
     * a state to be tested.
     * @return true if the given state is a goal state, false otherwise.
     */
    boolean isGoalState(S state);

    /**
     * Calculates the step-cost between two states. Used to assign a
     * numeric cost to each path.
     *
     * @param s
     *        the starting state.
     * @param a
     *        the action performed in state s.
     * @param sPrime
     *        the resulting state from performing action a.
     * @return the step cost of taking action a in state s to reach state s'.
     */
    double stepCost(S s, A a, S sPrime);
}
