package aima.core.search.framework;

import java.util.ArrayList;
import java.util.List;

import aima.core.agent.Action;
import aima.core.search.framework.problem.ActionsFunction;
import aima.core.search.framework.problem.Problem;
import aima.core.search.framework.problem.ResultFunction;
import aima.core.search.framework.problem.StepCostFunction;

/**
 * Instances of this class are responsible for node creation and expansion. They
 * compute path costs, support progress tracing, and count the number of
 * {@link #expand(Node, Problem)} calls.
 * 
 * @author Ruediger Lunde
 *
 */
public class NodeExpander {

	///////////////////////////////////////////////////////////////////////
	// expanding nodes

	/**
	 * Factory method, which creates a root node for the specified state.
	 */
	public Node createRootNode(Object state) {
		return new Node(state);
	}

	/**
	 * Computes the path cost for getting from the root node state via the
	 * parent node state to the specified state, creates a new node for the
	 * specified state, adds it as child of the provided parent, and returns it.
	 */
	public Node createNode(Object state, Node parent, Action action, double stepCost) {
		return new Node(state, parent, action, parent.getPathCost() + stepCost);
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
	public List<Node> expand(Node node, Problem problem) {
		List<Node> successors = new ArrayList<Node>();

		ActionsFunction actionsFunction = problem.getActionsFunction();
		ResultFunction resultFunction = problem.getResultFunction();
		StepCostFunction stepCostFunction = problem.getStepCostFunction();

		for (Action action : actionsFunction.actions(node.getState())) {
			Object successorState = resultFunction.result(node.getState(), action);

			double stepCost = stepCostFunction.c(node.getState(), action, successorState);
			successors.add(createNode(successorState, node, action, stepCost));
		}
		for (NodeListener listener : nodeListeners)
			listener.onNodeExpanded(node);
		counter++;
		return successors;
	}

	///////////////////////////////////////////////////////////////////////
	// progress tracing and statistical data

	/** Interface for progress Tracers */
	public static interface NodeListener {
		void onNodeExpanded(Node node);
	}

	/**
	 * All node listeners added to this list get informed whenever a node is
	 * expanded.
	 */
	private List<NodeListener> nodeListeners = new ArrayList<NodeListener>();

	/** Counts the number of {@link #expand(Node, Problem)} calls. */
	private int counter;

	/**
	 * Adds a listener to the list of node listeners. It is informed whenever a
	 * node is expanded during search.
	 */
	public void addNodeListener(NodeListener listener) {
		nodeListeners.add(listener);
	}

	/**
	 * Resets the counter for {@link #expand(Node, Problem)} calls.
	 */
	public void resetCounter() {
		counter = 0;
	}

	/**
	 * Returns the number of {@link #expand(Node, Problem)} calls since the last
	 * counter reset.
	 */
	public int getNumOfExpandCalls() {
		return counter;
	}
}
