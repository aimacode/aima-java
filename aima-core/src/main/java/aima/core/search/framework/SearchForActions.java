package aima.core.search.framework;

import java.util.List;

import aima.core.agent.Action;
import aima.core.search.framework.problem.Problem;

/**
 * Interface for all search algorithms which store at least a part of the
 * exploration history as search tree and return a list of actions which lead
 * from the initial state to a goal state.
 * 
 * @author Ruediger Lunde
 *
 */
public interface SearchForActions extends SearchInfrastructure {
	/**
	 * Returns a list of actions to the goal if the goal was found, a list
	 * containing a single NoOp Action if already at the goal, or an empty list
	 * if the goal could not be found.
	 * 
	 * @param p
	 *            the search problem
	 * 
	 * @return a list of actions to the goal if the goal was found, a list
	 *         containing a single NoOp Action if already at the goal, or an
	 *         empty list if the goal could not be found.
	 */
	List<Action> search(Problem p);
}
