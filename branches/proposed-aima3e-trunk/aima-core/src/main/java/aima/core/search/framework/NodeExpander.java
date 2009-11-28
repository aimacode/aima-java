package aima.core.search.framework;

import java.util.ArrayList;
import java.util.List;

import aima.core.agent.Action;

/**
 * @author Ravi Mohan
 * 
 */
public class NodeExpander {
	public static final String METRIC_NODES_EXPANDED = "nodesExpanded";

	protected Metrics metrics;

	public NodeExpander() {
		metrics = new Metrics();
	}

	public void clearInstrumentation() {
		metrics.set(METRIC_NODES_EXPANDED, 0);
	}

	public int getNodesExpanded() {
		return metrics.getInt(METRIC_NODES_EXPANDED);
	}

	public Metrics getMetrics() {
		return metrics;
	}

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
		metrics.set(METRIC_NODES_EXPANDED, metrics
				.getInt(METRIC_NODES_EXPANDED) + 1);

		return childNodes;
	}
}