package aima.core.search.framework;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import aima.core.agent.Action;
import aima.core.util.datastructure.Queue;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure 3.7, page 77. <br>
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
 */
public class GraphSearch extends QueueSearch {

	private Set<Object> explored = new HashSet<Object>();
	private Map<Object, Node> frontierState = new HashMap<Object, Node>();
	private Comparator<Node> replaceFrontierNodeAtStateCostFunction = null;
	private List<Node> addToFrontier = new ArrayList<Node>();

	public Comparator<Node> getReplaceFrontierNodeAtStateCostFunction() {
		return replaceFrontierNodeAtStateCostFunction;
	}

	public void setReplaceFrontierNodeAtStateCostFunction(
			Comparator<Node> replaceFrontierNodeAtStateCostFunction) {
		this.replaceFrontierNodeAtStateCostFunction = replaceFrontierNodeAtStateCostFunction;
	}

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
	public Node popNodeFromFrontier() {
		Node toRemove = super.popNodeFromFrontier();
		frontierState.remove(toRemove.getState());
		return toRemove;
	}

	@Override
	public boolean removeNodeFromFrontier(Node toRemove) {
		boolean removed = super.removeNodeFromFrontier(toRemove);
		if (removed) {
			frontierState.remove(toRemove.getState());
		}
		return removed;
	}

	@Override
	public List<Node> getResultingNodesToAddToFrontier(Node nodeToExpand,
			Problem problem) {

		addToFrontier.clear();
		// add the node to the explored set
		explored.add(nodeToExpand.getState());
		// expand the chosen node, adding the resulting nodes to the frontier
		for (Node cfn : expandNode(nodeToExpand, problem)) {
			Node frontierNode = frontierState.get(cfn.getState());
			boolean yesAddToFrontier = false;
			// only if not in the frontier or explored set
			if (null == frontierNode && !explored.contains(cfn.getState())) {
				yesAddToFrontier = true;
			} else if (null != frontierNode
					&& null != replaceFrontierNodeAtStateCostFunction
					&& replaceFrontierNodeAtStateCostFunction.compare(cfn,
							frontierNode) < 0) {
				// child.STATE is in frontier with higher cost
				// replace that frontier node with child
				yesAddToFrontier = true;
				// Want to replace the current frontier node with the child
				// node therefore mark the child to be added and remove the
				// current fontierNode
				removeNodeFromFrontier(frontierNode);
				// Ensure removed from add to frontier as well
				// as 1 or more may reach the same state at the same time
				addToFrontier.remove(frontierNode);
			}

			if (yesAddToFrontier) {
				addToFrontier.add(cfn);
				frontierState.put(cfn.getState(), cfn);
			}
		}

		return addToFrontier;
	}
}