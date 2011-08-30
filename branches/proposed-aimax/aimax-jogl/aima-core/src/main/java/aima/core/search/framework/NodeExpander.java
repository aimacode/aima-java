package aima.core.search.framework;

import java.util.ArrayList;
import java.util.List;

import aima.core.agent.Action;

/**
 * @author Ravi Mohan
 * @author Mike Stampone
 */
public class NodeExpander {
	public static final String METRIC_NODES_EXPANDED = "nodesExpanded";

	protected Metrics metrics;

	public NodeExpander() {
		metrics = new Metrics();
	}

	/**
	 * Sets the nodes expanded metric to zero.
	 */
	public void clearInstrumentation() {
		metrics.set(METRIC_NODES_EXPANDED, 0);
	}

	/**
	 * Returns the number of nodes expanded so far.
	 * 
	 * @return the number of nodes expanded so far.
	 */
	public int getNodesExpanded() {
		return metrics.getInt(METRIC_NODES_EXPANDED);
	}

	/**
	 * Returns all the metrics of the node expander.
	 * 
	 * @return all the metrics of the node expander.
	 */
	public Metrics getMetrics() {
		return metrics;
	}

	/**
	 * Returns the children obtained from expanding the specified node in the
	 * specified problem.
	 * 
	 * @param node
	 *            the node to expand
	 * @param problem
	 *            the problem the specified node is within.
	 * 
	 * @return the children obtained from expanding the specified node in the
	 *         specified problem.
	 */
	public List<Node> expandNode(Node node, Problem problem) {
		List<Node> childNodes = new ArrayList<Node>();

		ActionsFunction actionsFunction = problem.getActionsFunction();
		ResultFunction resultFunction = problem.getResultFunction();
		StepCostFunction stepCostFunction = problem.getStepCostFunction();

		for (Action action : actionsFunction.actions(node.getState())) {
			Object successorState = resultFunction.result(node.getState(),
					action);

			double stepCost = stepCostFunction.c(node.getState(), action,
					successorState);
			childNodes.add(new Node(successorState, node, action, stepCost));
		}
		metrics.set(METRIC_NODES_EXPANDED,
				metrics.getInt(METRIC_NODES_EXPANDED) + 1);

		return childNodes;
	}
}