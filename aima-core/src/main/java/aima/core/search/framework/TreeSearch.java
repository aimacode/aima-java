package aima.core.search.framework;

import java.util.*;

import aima.core.agent.Action;
import aima.core.util.CancelableThread;


/**
 * @author Ravi Mohan
 * @author Sadhana Srinivasan
 */
public class TreeSearch extends QueueSearch {
	public static final String METRIC_TREE_SIZE = "treeSize";

	public static final String METRIC_MAX_TREE_SIZE = "maxTreeSize";

	public static final String METRIC_PATH_COST = "pathCost";

	//
	//
	private Queue<Node> frontier = null;
	private boolean checkGoalBeforeAddingToFrontier = false;

	public boolean isFailure(List<Action> result) {
		return 0 == result.size();
	}

	/**
	 * Returns a list of actions to the goal if the goal was found, a list
	 * containing a single NoOp Action if already at the goal, or an empty list
	 * if the goal could not be found.
	 *
	 * @param problem
	 *            the search problem
	 * @param frontier
	 *            the collection of nodes that are waiting to be expanded
	 *
	 * @return a list of actions to the goal if the goal was found, a list
	 *         containing a single NoOp Action if already at the goal, or an
	 *         empty list if the goal could not be found.
	 */
	public List<Action> search(Problem problem, Queue<Node> frontier) {
		this.frontier = frontier;

		clearInstrumentation();
		// initialize the frontier using the initial state of the problem
		Node root = new Node(problem.getInitialState());
		if (isCheckGoalBeforeAddingToFrontier()) {
			if (SearchUtils.isGoalState(problem, root)) {
				return SearchUtils.actionsFromNodes(root.getPathFromRoot());
			}
		}
		frontier.add(root);
		//".add" adds the root to the frontier
		setTreeSize(frontier.size());
		while (!(frontier.isEmpty()) && !CancelableThread.currIsCanceled()) {
			// choose a leaf node and remove it from the frontier
			Node nodeToExpand = popNodeFromFrontier();
			setTreeSize(frontier.size());
			// Only need to check the nodeToExpand if have not already
			// checked before adding to the frontier
			if (!isCheckGoalBeforeAddingToFrontier()) {
				// if the node contains a goal state then return the
				// corresponding solution
				if (SearchUtils.isGoalState(problem, nodeToExpand)) {
					setPathCost(nodeToExpand.getPathCost());
					return SearchUtils.actionsFromNodes(nodeToExpand
							.getPathFromRoot());
				}
			}
			// expand the chosen node, adding the resulting nodes to the
			// frontier
			for (Node fn : getResultingNodesToAddToFrontier(nodeToExpand,
					problem)) {
				if (isCheckGoalBeforeAddingToFrontier()) {
					if (SearchUtils.isGoalState(problem, fn)) {
						setPathCost(fn.getPathCost());
						return SearchUtils.actionsFromNodes(fn
								.getPathFromRoot());
					}
				}
				frontier.add(fn);
				//".add" gets the resulting nodes and adds the nodes to the frontier
			}
			setTreeSize(frontier.size());
		}
		// if the frontier is empty then return failure
		return failure();
	}

	public boolean isCheckGoalBeforeAddingToFrontier() {
		return checkGoalBeforeAddingToFrontier;
	}

	public void setCheckGoalBeforeAddingToFrontier(
			boolean checkGoalBeforeAddingToFrontier) {
		this.checkGoalBeforeAddingToFrontier = checkGoalBeforeAddingToFrontier;
	}

	/**
	 * Removes and returns the node at the head of the frontier.
	 *
	 * @return the node at the head of the frontier.
	 */
	public Node popNodeFromFrontier() {
		return frontier.remove();
		//".remove" returns the topmost node from frontier, similar to pop
	}

	public boolean removeNodeFromFrontier(Node toRemove) {
		return frontier.remove(toRemove);
		//".remove" removes the specified node here.
	}

	public List<Node> getResultingNodesToAddToFrontier(Node nodeToExpand,
			Problem problem) {
		// expand the chosen node, adding the resulting nodes to the frontier
		return expandNode(nodeToExpand, problem);

	}


	@Override
	public void clearInstrumentation() {
		super.clearInstrumentation();
		metrics.set(METRIC_TREE_SIZE, 0);
		metrics.set(METRIC_MAX_TREE_SIZE, 0);
		metrics.set(METRIC_PATH_COST, 0);
	}

	public int getQueueSize() {
		return metrics.getInt("treeSize");
	}

	public void setTreeSize(int treeSize) {

		metrics.set(METRIC_TREE_SIZE, treeSize);
		int maxQSize = metrics.getInt(METRIC_MAX_TREE_SIZE);
		if (treeSize > maxQSize) {
			metrics.set(METRIC_MAX_TREE_SIZE, treeSize);
		}
	}

	public int getMaxTreeSize() {
		return metrics.getInt(METRIC_MAX_TREE_SIZE);
	}

	public double getPathCost() {
		return metrics.getDouble(METRIC_PATH_COST);
	}

	public void setPathCost(Double pathCost) {
		metrics.set(METRIC_PATH_COST, pathCost);
	}

	//
	// PRIVATE METHODS
	//
	private List<Action> failure() {
		return Collections.emptyList();
	}
}
