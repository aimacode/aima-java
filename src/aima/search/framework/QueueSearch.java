/*
 * Created on Sep 8, 2004
 *
 */
package aima.search.framework;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ravi Mohan
 *  
 */
public abstract class QueueSearch extends NodeExpander {
	private static String QUEUE_SIZE = "queueSize";

	private static String MAX_QUEUE_SIZE = "maxQueueSize";

	public List<String> search(Problem problem, NodeStore fringe) {
		clearInstrumentation();
		fringe.add(new Node(problem.getInitialState()));
		setQueueSize(fringe.size());
		while (!(fringe.isEmpty())) {
			Node node = (Node) fringe.remove();
			setQueueSize(fringe.size());
			if (problem.isGoalState(node.getState())) {
				return SearchUtils.actionsFromNodes(node.getPathFromRoot());
			}
			addExpandedNodesToFringe(fringe, node, problem);
			setQueueSize(fringe.size());
		}
		return new ArrayList<String>();//Empty List indicates Failure
	}

	public void clearInstrumentation() {
		super.clearInstrumentation();
		metrics.set(QUEUE_SIZE, 0);
		metrics.set(MAX_QUEUE_SIZE, 0);
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
		return metrics.getInt("maxQueueSize");
	}

	protected abstract void addExpandedNodesToFringe(NodeStore fringe,
			Node node, Problem p);
}