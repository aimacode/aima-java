package aima.core.search.framework.problem;

import aima.core.search.framework.Node;

import java.util.List;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 66.<br>
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
 * This implementation provides an additional solution test. It can be used to
 * compute more than one solution or to formulate acceptance criteria for the
 * sequence of actions.
 *
 * @param <S> The type used to represent states
 * @param <A> The type of the actions to be used to navigate through the state space
 *
 * @author Ruediger Lunde
 * @author Mike Stampone
 */
public interface Problem<S, A> extends OnlineSearchProblem<S, A> {

    /**
     * Returns the initial state of the agent.
     */
    S getInitialState();

    /**
     * Returns the description of the possible actions available to the agent.
     */
    List<A> getActions(S state);

    /**
     * Returns the description of what each action does.
     */
    S getResult(S state, A action);

    /**
     * Determines whether a given state is a goal state.
     */
    boolean testGoal(S state);

    /**
     * Returns the <b>step cost</b> of taking action <code>action</code> in state <code>state</code> to reach state
     * <code>stateDelta</code> denoted by c(s, a, s').
     */
    double getStepCosts(S state, A action, S stateDelta);

    /**
     * Tests whether a node represents an acceptable solution. The default implementation
     * delegates the check to the goal test. Other implementations could make use of the additional
     * information given by the node (e.g. the sequence of actions leading to the node). A
     * solution tester implementation could for example always return false and internally collect
     * the paths of all nodes whose state passes the goal test. Search implementations should always
     * access the goal test via this method to support solution acceptance testing.
     */
    default boolean testSolution(Node<S, A> node) {
        return testGoal(node.getState());
    }
}
