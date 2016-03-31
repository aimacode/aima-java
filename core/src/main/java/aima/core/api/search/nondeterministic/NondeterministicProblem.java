package aima.core.api.search.nondeterministic;

import java.util.Set;

/**
 * Non-deterministic problems may have multiple results for a given state and
 * action; this class handles these results by mimicking Problem and replacing
 * ResultFunction (one result) with ResultsFunction (a set of results).
 * 
 * @author Anurag Rai
 */
public interface NondeterministicProblem<A, S> {

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
    
    
    Set<S> results(S s,A a);
    
    
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
