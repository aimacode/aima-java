package aima.core.search.framework;

import aima.core.search.framework.problem.Problem;

import java.util.function.Consumer;

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
	 * Adds a listener to the list of node listeners. It is informed whenever a
	 * node is expanded during search.
	 */
	void addNodeListener(Consumer<Node> listener);

	/**
	 * Removes a listener from the list of node listeners.
	 */
	boolean removeNodeListener(Consumer<Node> listener);
}
