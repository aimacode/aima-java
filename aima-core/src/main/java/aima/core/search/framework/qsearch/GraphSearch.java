package aima.core.search.framework.qsearch;

import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

import aima.core.search.framework.Node;
import aima.core.search.framework.NodeExpander;
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
 * {@link QueueSearch#findNode(Problem, Queue)} of the superclass and provides
 * implementations for the needed primitive operations. In contrast to the code
 * above, here, nodes resulting from node expansion are added to the frontier
 * even if nodes for the same states already exist there. This makes it possible
 * to use the implementation also in combination with priority queue frontiers.
 * This implementation avoids linear costs for frontier node removal (compared
 * to {@link GraphSearchReducedFrontier}) and gets by without node comparator
 * knowledge.
 * 
 * @author Ruediger Lunde
 */
public class GraphSearch extends QueueSearch {

	private Set<Object> explored = new HashSet<Object>();

	public GraphSearch() {
		this(new NodeExpander());
	}

	public GraphSearch(NodeExpander nodeExpander) {
		super(nodeExpander);
	}

	/**
	 * Clears the set of explored states and calls the search implementation of
	 * {@link QueueSearch}.
	 */
	@Override
	public Node findNode(Problem problem, Queue<Node> frontier) {
		// initialize the explored set to be empty
		explored.clear();
		return super.findNode(problem, frontier);
	}

	/**
	 * Inserts the node at the tail of the frontier if the corresponding state
	 * was not yet explored.
	 */
	@Override
	protected void addToFrontier(Node node) {
		if (!explored.contains(node.getState())) {
			frontier.add(node);
			updateMetrics(frontier.size());
		}
	}

	/**
	 * Removes the node at the head of the frontier, adds the corresponding
	 * state to the explored set, and returns the node. Leading nodes of already
	 * explored states are dropped. So the resulting node state will always be
	 * unexplored yet.
	 * 
	 * @return the node at the head of the frontier.
	 */
	@Override
	protected Node removeFromFrontier() {
		cleanUpFrontier(); // not really necessary because isFrontierEmpty
							// should be called before...
		Node result = frontier.remove();
		explored.add(result.getState());
		updateMetrics(frontier.size());
		return result;
	}

	/**
	 * Pops nodes of already explored states from the head of the frontier
	 * and checks whether there are still some nodes left.
	 */
	@Override
	protected boolean isFrontierEmpty() {
		cleanUpFrontier();
		updateMetrics(frontier.size());
		return frontier.isEmpty();
	}

	/**
	 * Helper method which removes nodes of already explored states from the head
	 * of the frontier.
	 */
	private void cleanUpFrontier() {
		while (!frontier.isEmpty() && explored.contains(frontier.element().getState()))
			frontier.remove();
	}
}