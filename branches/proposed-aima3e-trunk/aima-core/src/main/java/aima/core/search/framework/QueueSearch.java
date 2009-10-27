package aima.core.search.framework;

import java.util.ArrayList;
import java.util.List;

import aima.core.agent.Action;
import aima.core.util.datastructure.Queue;

/**
 * @author Ravi Mohan
 * 
 */
public abstract class QueueSearch extends NodeExpander {
	private static String QUEUE_SIZE = "queueSize";

	private static String MAX_QUEUE_SIZE = "maxQueueSize";

	private static String PATH_COST = "pathCost";

	public List<Action> search(Problem problem, Queue<Node> frontier) {
		clearInstrumentation();
		frontier.insert(new Node(problem.getInitialState()));
		setQueueSize(frontier.size());
		while (!(frontier.isEmpty())) {
			Node node = frontier.pop();
			setQueueSize(frontier.size());
			if (problem.isGoalState(node.getState())) {
				setPathCost(node.getPathCost());
				return SearchUtils.actionsFromNodes(node.getPathFromRoot());
			}
			addExpandedNodesToFringe(frontier, node, problem);
			setQueueSize(frontier.size());
		}
		return new ArrayList<Action>();// Empty List indicates Failure
	}

	@Override
	public void clearInstrumentation() {
		super.clearInstrumentation();
		metrics.set(QUEUE_SIZE, 0);
		metrics.set(MAX_QUEUE_SIZE, 0);
		metrics.set(PATH_COST, 0);
	}

	public int getQueueSize() {
		return metrics.getInt("queueSize");
	}

	public void setQueueSize(int queueSize) {

		metrics.set(QUEUE_SIZE, queueSize);
		int maxQSize = metrics.getInt(MAX_QUEUE_SIZE);
		if (queueSize > maxQSize) {
			metrics.set(MAX_QUEUE_SIZE, queueSize);
		}
	}

	public int getMaxQueueSize() {
		return metrics.getInt(MAX_QUEUE_SIZE);
	}

	public double getPathCost() {
		return metrics.getDouble(PATH_COST);
	}

	public void setPathCost(Double pathCost) {
		metrics.set(PATH_COST, pathCost);
	}

	public abstract void addExpandedNodesToFringe(Queue<Node> frontier, Node node,
			Problem p);
}