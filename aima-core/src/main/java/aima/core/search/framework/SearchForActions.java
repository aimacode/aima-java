package aima.core.search.framework;

import java.util.List;

import aima.core.agent.Action;
import aima.core.search.framework.problem.Problem;

/**
 * Interface for all AIMA3e search algorithms which store at least a part of the
 * exploration history as search tree and return a list of actions leading from
 * the initial state to a goal state. This search framework expects all search
 * algorithms to provide some metrics and to actually explore the search space
 * by expanding nodes.
 * 
 * @author Ruediger Lunde
 *
 */
public interface SearchForActions {
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
	List<Action> findActions(Problem p);

	/**
	 * Returns all the metrics of the search.
	 */
	Metrics getMetrics();

	/**
	 * Returns the node expander used by the search. Useful for progress
	 * tracing.
	 */
	NodeExpander getNodeExpander();
}
