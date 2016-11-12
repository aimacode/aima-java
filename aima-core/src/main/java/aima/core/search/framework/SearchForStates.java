package aima.core.search.framework;

import aima.core.search.framework.problem.Problem;

/**
 * Interface for all AIMA3e search algorithms which forget the exploration history and
 * return just a single state which is hopefully a goal state. This search framework expects
 * all search algorithms to provide some metrics and to actually explore the search space
 * by expanding nodes.
 * 
 * @author Ruediger Lunde
 *
 */
public interface SearchForStates {
	/**
	 * Returns a state which is might be but not necessary is a goal state of
	 * the problem.
	 * 
	 * @param p
	 *            the search problem
	 * 
	 * @return a state.
	 */
	Object findState(Problem p);
	
	/**
	 * Returns all the metrics of the search.
	 */
	Metrics getMetrics();
	
	/**
	 * Returns the node expander used by the search. Useful for progress tracing.
	 */
	NodeExpander getNodeExpander();
}
