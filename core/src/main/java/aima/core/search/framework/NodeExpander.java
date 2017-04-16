package aima.core.search.framework;

import java.util.ArrayList;
import java.util.List;

import aima.core.search.framework.ResultFunction;

/**
 * @author Subham Mishra
 * @author Ravi Mohan
 * @author Mike Stampone
 */
public class NodeExpander<A> {
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
	public List<Node<A>> expandNode(Node<A> node, Problem<A> problem) {
		List<Node<A>> childNodes = new ArrayList<Node<A>>();

		ActionsFunction<A> actionsFunction = problem.getActionsFunction();
		ResultFunction<A> resultFunction = problem.getResultFunction();
		StepCostFunction<A> stepCostFunction = problem.getStepCostFunction();

		for (A action : actionsFunction.actions(node.getState())) {
			Object successorState = resultFunction.result(node.getState(),
					action);

			double stepCost = stepCostFunction.c(node.getState(), action,
					successorState);
			childNodes.add(new Node<A>(successorState, node, action, stepCost));
		}
		metrics.set(METRIC_NODES_EXPANDED,
				metrics.getInt(METRIC_NODES_EXPANDED) + 1);

		return childNodes;
	}
}
