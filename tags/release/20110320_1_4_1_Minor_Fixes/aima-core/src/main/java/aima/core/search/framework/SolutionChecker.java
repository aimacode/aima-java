package aima.core.search.framework;

import java.util.List;

import aima.core.agent.Action;

/**
 * A specialization of the GoalTest interface so that it is possible to check
 * the solution once a Goal has been identified to determine if it is
 * acceptable. This allows you to continue searching for alternative solutions
 * without having to restart the search.
 * 
 * However, care needs to be taken when doing this as it does not always make
 * sense to continue with a search once an initial goal is found, for example if
 * using a heuristic targeted at a single goal.
 * 
 * @author Ciaran O'Reilly
 */
public interface SolutionChecker extends GoalTest {
	/**
	 * This method is only called if GoalTest.isGoalState() returns true.
	 * 
	 * @param actions
	 *            the list of actions to get to the goal state.
	 * 
	 * @param goal
	 *            the goal the list of actions will reach.
	 * 
	 * @return true if the solution is acceptable, false otherwise, which
	 *         indicates the search should be continued.
	 */
	boolean isAcceptableSolution(List<Action> actions, Object goal);
}