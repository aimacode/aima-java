package aima.core.search.framework;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import aima.core.agent.Action;
import aima.core.util.datastructure.Queue;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure 3.7, page ??. 
 * <code>
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
 * </code> 
 * Figure 3.7 An informal description of the general graph-search algorithm.
 */

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public class GraphSearch extends QueueSearch {

	private Set<Object> explored = new HashSet<Object>();
	private Set<Object> frontierState = new HashSet<Object>();
	private List<Node> addToFrontier = new ArrayList<Node>();
	
	// Need to override search() method so that I can re-initialize
	// the explored set should multiple calls to search be made.
	@Override
	public List<Action> search(Problem problem, Queue<Node> frontier) {
		// initialize the explored set to be empty
		explored.clear();
		frontierState.clear();
		return super.search(problem, frontier);
	}
	
	@Override
	public Node removeNodeFromFrontier(Queue<Node> frontier) {
		Node toRemove = super.removeNodeFromFrontier(frontier);
		frontierState.remove(toRemove.getState());
		return toRemove;
	}

	@Override
	public List<Node> getResultingNodesToAddToFrontier(
			Node nodeToExpand, Problem problem) {

		addToFrontier.clear();
		// add the node to the explored set
		explored.add(nodeToExpand.getState());		
		// expand the chosen node, adding the resulting nodes to the frontier
		for (Node cfn : expandNode(nodeToExpand, problem)) {
			// only if not in the frontier or explored set
			if (!frontierState.contains(cfn.getState()) && !explored.contains(cfn.getState())) {
				addToFrontier.add(cfn);
				frontierState.add(cfn.getState());
			}
		}
		
		return addToFrontier;
	}
}