package aima.core.search.framework;

import java.util.ArrayList;
import java.util.List;

import aima.core.agent.Action;

/**
 * @author Ravi Mohan
 * 
 */
public class NodeExpander {
	protected Metrics metrics;

	protected static String NODES_EXPANDED = "nodesExpanded";

	public NodeExpander() {
		metrics = new Metrics();
	}

	public void clearInstrumentation() {
		metrics.set(NODES_EXPANDED, 0);
	}

	public List<Node> expandNode(Node node, Problem problem) {
		List<Node> childNodes = new ArrayList<Node>();

		ActionsFunction actionsFunction = problem.getActionsFunction();
		ResultFunction resultFunction = problem.getResultFunction();
		StepCostFunction stepCostFunction = problem.getStepCostFunction();

		for (Action action : actionsFunction.actions(node.getState())) {
			Object successorState = resultFunction.result(node.getState(),
					action);

			Node childNode = new Node(node, action, successorState);
			double stepCost = stepCostFunction.cost(node.getState(), action,
					successorState);
			childNode.setStepCost(stepCost);
			childNode.addToPathCost(stepCost);
			childNodes.add(childNode);
		}
		metrics.set(NODES_EXPANDED, metrics.getInt(NODES_EXPANDED) + 1);

		return childNodes;
	}

	public int getNodesExpanded() {
		return metrics.getInt(NODES_EXPANDED);
	}

	public void setNodesExpanded(int nodesExpanded) {
		metrics.set(NODES_EXPANDED, nodesExpanded);
	}

	public Object getSearchMetric(String name) {
		return metrics.get(name);
	}

	public Metrics getMetrics() {
		return metrics;
	}
}