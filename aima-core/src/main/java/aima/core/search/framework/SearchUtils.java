package aima.core.search.framework;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import aima.core.agent.Action;
import aima.core.agent.impl.NoOpAction;

/**
 * Provides several useful static methods for implementing search.
 * 
 * @author Ravi Mohan
 * @author Ruediger Lunde
 * 
 */
public class SearchUtils {

	public static interface NodeListener {
		void onNodeExpanded(Node node);
	}

	/**
	 * All node listeners added to this list get informed whenever a node is
	 * expanded. This is for demonstration and debugging purposes only. Handle
	 * with care!
	 */
	public static List<NodeListener> nodeListeners = new ArrayList<NodeListener>();

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
	public static List<Node> expandNode(Node node, Problem problem) {
		List<Node> successors = new ArrayList<Node>();

		for (NodeListener listener : nodeListeners)
			listener.onNodeExpanded(node);

		ActionsFunction actionsFunction = problem.getActionsFunction();
		ResultFunction resultFunction = problem.getResultFunction();
		StepCostFunction stepCostFunction = problem.getStepCostFunction();

		for (Action action : actionsFunction.actions(node.getState())) {
			Object successorState = resultFunction.result(node.getState(), action);

			double stepCost = stepCostFunction.c(node.getState(), action, successorState);
			successors.add(new Node(successorState, node, action, stepCost));
		}
		return successors;
	}

	/**
	 * Returns the list of actions corresponding to the complete path to the
	 * given node or NoOp if path length is one.
	 */
	public static List<Action> getSequenceOfActions(Node node) {
		List<Node> nodes = node.getPathFromRoot();
		List<Action> actions = new ArrayList<Action>();
		if (nodes.size() == 1) {
			// I'm at the root node, this indicates I started at the
			// Goal node, therefore just return a NoOp
			actions.add(NoOpAction.NO_OP);
		} else {
			// ignore the root node this has no action
			// hence index starts from 1 not zero
			for (int i = 1; i < nodes.size(); i++)
				actions.add(nodes.get(i).getAction());
		}
		return actions;
	}

	/** Returns an empty action list. */
	public static List<Action> failure() {
		return Collections.emptyList();
	}

	/**
	 * Calls the goal test of the problem and - if the goal test is effectively
	 * a {@link SolutionChecker} - additionally checks, whether the solution is
	 * acceptable. Solution checkers can be used to analyze several or all
	 * solutions with only one search run.
	 */
	public static boolean isGoalState(Problem p, Node n) {
		boolean isGoal = false;
		GoalTest gt = p.getGoalTest();
		if (gt.isGoalState(n.getState())) {
			if (gt instanceof SolutionChecker) {
				isGoal = ((SolutionChecker) gt).isAcceptableSolution(getSequenceOfActions(n), n.getState());
			} else {
				isGoal = true;
			}
		}
		return isGoal;
	}
}