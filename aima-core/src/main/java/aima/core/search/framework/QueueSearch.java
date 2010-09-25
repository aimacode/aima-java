package aima.core.search.framework;

import java.util.Collections;
import java.util.List;

import aima.core.agent.Action;
import aima.core.util.CancelableThread;
import aima.core.util.datastructure.Queue;

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public abstract class QueueSearch extends NodeExpander {
	public static final String METRIC_QUEUE_SIZE = "queueSize";

	public static final String METRIC_MAX_QUEUE_SIZE = "maxQueueSize";

	public static final String METRIC_PATH_COST = "pathCost";

	//
	//
	private Queue<Node> frontier = null;
	private boolean checkGoalBeforeAddingToFrontier = false;

	public boolean isFailure(List<Action> result) {
		return 0 == result.size();
	}

	/**
	 * 
	 * @param problem
	 * @param frontier
	 * @return if goal found, the list of actions to the Goal. If already at the
	 *         goal you will receive a List with a single NoOp Action in it. If
	 *         fail to find the Goal, an empty list will be returned to indicate
	 *         that the search failed.
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
		frontier.insert(root);
		setQueueSize(frontier.size());
		while (!(frontier.isEmpty()) && !CancelableThread.currIsCanceled()) {
			// choose a leaf node and remove it from the frontier
			Node nodeToExpand = popNodeFromFrontier();
			setQueueSize(frontier.size());
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
				frontier.insert(fn);
			}
			setQueueSize(frontier.size());
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

	public Node popNodeFromFrontier() {
		return frontier.pop();
	}

	public boolean removeNodeFromFrontier(Node toRemove) {
		return frontier.remove(toRemove);
	}

	public abstract List<Node> getResultingNodesToAddToFrontier(
			Node nodeToExpand, Problem p);

	@Override
	public void clearInstrumentation() {
		super.clearInstrumentation();
		metrics.set(METRIC_QUEUE_SIZE, 0);
		metrics.set(METRIC_MAX_QUEUE_SIZE, 0);
		metrics.set(METRIC_PATH_COST, 0);
	}

	public int getQueueSize() {
		return metrics.getInt("queueSize");
	}

	public void setQueueSize(int queueSize) {

		metrics.set(METRIC_QUEUE_SIZE, queueSize);
		int maxQSize = metrics.getInt(METRIC_MAX_QUEUE_SIZE);
		if (queueSize > maxQSize) {
			metrics.set(METRIC_MAX_QUEUE_SIZE, queueSize);
		}
	}

	public int getMaxQueueSize() {
		return metrics.getInt(METRIC_MAX_QUEUE_SIZE);
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