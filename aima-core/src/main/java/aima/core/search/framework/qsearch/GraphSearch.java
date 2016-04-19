package aima.core.search.framework.qsearch;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import aima.core.agent.Action;
import aima.core.search.framework.Node;
import aima.core.search.framework.Problem;
import aima.core.util.datastructure.Queue;

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
 * See superclass {@link QueueSearch} for the implementation of the main
 * algorithm.
 * 
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 */
public class GraphSearch extends QueueSearch {

	private Set<Object> explored = new HashSet<Object>();
	// public static List<Node> expandedNodes;

	/**
	 * Clears the set of explored states and calls the search implementation of
	 * <code>QueSearch</code>
	 */
	@Override
	public List<Action> search(Problem problem, Queue<Node> frontier) {
		// initialize the explored set to be empty
		explored.clear();
		// expandedNodes = new ArrayList<Node>();
		return super.search(problem, frontier);
	}

	/**
	 * Inserts the node at the tail of the frontier if the corresponding state
	 * was not yet explored.
	 */
	@Override
	protected void insertIntoFrontier(Node node) {
		if (!explored.contains(node.getState())) {
			frontier.insert(node);
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
	protected Node popNodeFromFrontier() {
		Node result = frontier.pop();
		// add the node to the explored set
		explored.add(result.getState());
		updateMetrics(frontier.size());
		return result;
	}

	/**
	 * Pops nodes of already explored states from the top end of the frontier
	 * and checks whether there are still some nodes left.
	 */
	@Override
	protected boolean isFrontierEmpty() {
		while (!frontier.isEmpty() && explored.contains(frontier.peek().getState()))
			frontier.pop();
		updateMetrics(frontier.size());
		return frontier.isEmpty();
	}
}