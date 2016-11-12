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
 * {@link QueueSearch#findNode(Problem, Queue)} of the superclass and
 * provides implementations for the needed primitive operations. It is the most
 * efficient variant of graph search for breadth first search. But don't expect
 * shortest paths in combination with priority queue frontiers.
 * 
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 */
public class GraphSearchBFS extends QueueSearch {

	private Set<Object> explored = new HashSet<Object>();
	private Set<Object> frontierStates = new HashSet<Object>();

	public GraphSearchBFS() {
		this(new NodeExpander());
	}

	public GraphSearchBFS(NodeExpander nodeExpander) {
		super(nodeExpander);
	}
	
	
	/**
	 * Clears the set of explored states and calls the search implementation of
	 * <code>QueSearch</code>
	 */
	@Override
	public Node findNode(Problem problem, Queue<Node> frontier) {
		// Initialize the explored set to be empty
		explored.clear();
		frontierStates.clear();
		return super.findNode(problem, frontier);
	}

	/**
	 * Inserts the node at the tail of the frontier if the corresponding state
	 * is not already a frontier state and was not yet explored.
	 */
	@Override
	protected void addToFrontier(Node node) {
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
	protected Node removeFromFrontier() {
		Node result = frontier.remove();
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
