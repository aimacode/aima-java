package aima.core.search.framework.qsearch;

import java.util.HashSet;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;

import aima.core.search.framework.Node;
import aima.core.search.framework.NodeFactory;
import aima.core.search.framework.problem.Problem;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure 3.7, page 77.
 * <br>
 * 
 * <pre>
 * function GRAPH-SEARCH(problem) returns a solution, or failure
 *   initialize the frontier using the initial state of problem
 *   initialize the explored set to be empty
 *   loop do
 *     if the frontier is empty then return failure
 *     choose a leaf node and remove it from the frontier
 *     if the node contains a goal state then return the corresponding solution
 *     add the node to the explored set
 *     expand the chosen node, adding the resulting nodes to the frontier
 *       only if not in the frontier or explored set
 * </pre>
 * 
 * Figure 3.7 An informal description of the general graph-search algorithm.
 * <br>
 * This implementation is based on the template method
 * {@link TreeSearch#findNode(Problem, Queue)} of the superclass and
 * provides implementations for the needed primitive operations. It is the most
 * efficient variant of graph search for breadth first. But don't expect
 * shortest paths in combination with priority queue frontiers.
 *
 * @param <S> The type used to represent states
 * @param <A> The type of the actions to be used to navigate through the state space
 *
 * @author Ruediger Lunde
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public class GraphSearchBFS<S, A> extends TreeSearch<S, A> {

	private Set<S> explored = new HashSet<>();
	private Set<S> frontierStates = new HashSet<>();

	public GraphSearchBFS() {
		this(new NodeFactory<>());
	}

	public GraphSearchBFS(NodeFactory<S, A> nodeFactory) {
		super(nodeFactory);
	}
	
	
	/**
	 * Clears the set of explored states and calls the search implementation of
	 * <code>QueSearch</code>
	 */
	@Override
	public Optional<Node<S, A>> findNode(Problem<S, A> problem, Queue<Node<S, A>> frontier) {
		// initialize the explored set to be empty
		explored.clear();
		frontierStates.clear();
		return super.findNode(problem, frontier);
	}

	/**
	 * Inserts the node at the tail of the frontier if the corresponding state
	 * is not already a frontier state and was not yet explored.
	 */
	@Override
	protected void addToFrontier(Node<S, A> node) {
		if (!explored.contains(node.getState()) && !frontierStates.contains(node.getState())) {
			frontier.add(node);
			frontierStates.add(node.getState());
			updateMetrics(frontier.size());
		}
	}

	/**
	 * Removes the node at the head of the frontier, adds the corresponding
	 * state to the explored set, and returns the node.
	 * 
	 * @return the node at the head of the frontier.
	 */
	@Override
	protected Node<S, A> removeFromFrontier() {
		Node<S, A> result = frontier.remove();
		explored.add(result.getState());
		frontierStates.remove(result.getState());
		updateMetrics(frontier.size());
		return result;
	}

	/**
	 * Checks whether there are still some nodes left.
	 */
	@Override
	protected boolean isFrontierEmpty() {
		return frontier.isEmpty();
	}
}
