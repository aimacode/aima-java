package aima.core.search.framework;

import aima.core.search.framework.problem.Problem;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Interface for all AIMA3e search algorithms which store at least a part of the
 * exploration history as search tree and return a list of actions leading from
 * the initial state to a goal state. This search framework expects all search
 * algorithms to provide some metrics and to actually explore the search space
 * by expanding nodes.
 *
 * @param <S> The type used to represent states
 * @param <A> The type of the actions to be used to navigate through the state space
 *
 * @author Ruediger Lunde
 */
public interface SearchForActions<S, A> {
	/**
	 * Returns a list of actions leading to a goal state if a goal was found,
	 * otherwise empty. Note that the list can be empty which means that the
	 * initial state is a goal state.
	 * 
	 * @param p
	 *            the search problem
	 * 
	 * @return a (possibly empty) list of actions or empty
	 */
	Optional<List<A>> findActions(Problem<S, A> p);

	/**
	 * Returns all the metrics of the search.
	 */
	Metrics getMetrics();

	/**
	 * Adds a listener to the list of node listeners. It is informed whenever a
	 * node is expanded during search.
	 */
	void addNodeListener(Consumer<Node<S, A>> listener);

	/**
	 * Removes a listener from the list of node listeners.
	 */
	boolean removeNodeListener(Consumer<Node<S, A>> listener);
}
