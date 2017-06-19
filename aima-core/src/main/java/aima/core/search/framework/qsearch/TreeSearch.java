package aima.core.search.framework.qsearch;

import java.util.Queue;

import aima.core.search.framework.Node;
import aima.core.search.framework.NodeExpander;
import aima.core.search.framework.problem.Problem;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure 3.7, page 77.
 * <br>
 * 
 * <pre>
 * function TREE-SEARCH(problem) returns a solution, or failure
 *   initialize the frontier using the initial state of the problem
 *   loop do
 *     if the frontier is empty then return failure
 *     choose a leaf node and remove it from the frontier
 *     if the node contains a goal state then return the corresponding solution
 *     expand the chosen node, adding the resulting nodes to the frontier
 * </pre>
 * 
 * Figure 3.7 An informal description of the general tree-search algorithm.
 * 
 * <br>
 * This implementation is based on the template method
 * {@link #findNode(Problem, Queue)} from superclass {@link QueueSearch} and
 * provides implementations for the needed primitive operations.
 *
 * @param <S> The type used to represent states
 * @param <A> The type of the actions to be used to navigate through the state space
 *
 * @author Ruediger Lunde
 * @author Ravi Mohan
 */
public class TreeSearch<S, A> extends QueueSearch<S, A> {

	public TreeSearch() {
		this(new NodeExpander<>());
	}

	public TreeSearch(NodeExpander<S, A> nodeExpander) {
		super(nodeExpander);
	}
	
	/**
	 * Inserts the node at the tail of the frontier.
	 */
	@Override
	protected void addToFrontier(Node<S, A> node) {
		frontier.add(node);
		updateMetrics(frontier.size());
	}

	/**
	 * Removes and returns the node at the head of the frontier.
	 * 
	 * @return the node at the head of the frontier.
	 */
	@Override
	protected Node<S, A> removeFromFrontier() {
		Node<S, A> result = frontier.remove();
		updateMetrics(frontier.size());
		return result;
	}

	/**
	 * Checks whether the frontier contains not yet expanded nodes.
	 */
	@Override
	protected boolean isFrontierEmpty() {
		return frontier.isEmpty();
	}
}