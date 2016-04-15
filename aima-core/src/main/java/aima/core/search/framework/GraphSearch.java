package aima.core.search.framework;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import aima.core.agent.Action;
import aima.core.util.datastructure.Queue;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure 3.7, page 77.
 * <br>
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
 * 
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 */
public class GraphSearch extends QueueSearch {

	private Set<Object> explored = new HashSet<Object>();
	private List<Node> addToFrontier = new ArrayList<Node>();
	//public static List<Node> expandedNodes;

	/**
	 * Clears the set of explored states and calls the search implementation of
	 * <code>QueSearch</code>
	 */
	@Override
	public List<Action> search(Problem problem, Queue<Node> frontier) {
		// initialize the explored set to be empty
		explored.clear();
		//expandedNodes = new ArrayList<Node>();
		return super.search(problem, frontier);
	}

	/**
	 * Pops nodes of already explored states from the top end of the frontier
	 * and checks whether there are still some nodes left.
	 */
	@Override
	protected boolean isFrontierEmpty() {
		while (!frontier.isEmpty() && explored.contains(frontier.peek().getState()))
			frontier.pop();
		return frontier.isEmpty();
	}

	/**
	 * Expands the node and returns only nodes of those states which have not been explored yet.
	 */
	@Override
	public List<Node> getResultingNodesToAddToFrontier(Node nodeToExpand, Problem problem) {

		addToFrontier.clear();
		// add the node to the explored set
		explored.add(nodeToExpand.getState());
		// expand the chosen node, adding the resulting nodes to the frontier
		//expandedNodes.add(nodeToExpand);
		for (Node cfn : expandNode(nodeToExpand, problem)) {
			if (!explored.contains(cfn.getState())) {
				// child.STATE is not in frontier and not yet explored
				addToFrontier.add(cfn);
			}
		}

		return addToFrontier;
	}
}