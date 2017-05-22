package aima.core.search.framework;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import aima.core.agent.Action;
import aima.core.agent.impl.NoOpAction;
import aima.core.search.framework.problem.GoalTest;
import aima.core.search.framework.problem.Problem;

/**
 * Provides several useful static methods for implementing search.
 * 
 * @author Ravi Mohan
 * @author Ruediger Lunde
 * 
 */
public class SearchUtils {
	/**
	 * Returns the list of actions corresponding to the complete path to the
	 * given node or NoOp if path length is one.
	 */
	public static List<Action> getSequenceOfActions(Node node) {
		List<Node> nodes = node.getPathFromRoot();
		List<Action> actions = new ArrayList<>();
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
	
	/** Checks whether a list of actions is empty. */
	public static boolean isFailure(List<Action> actions) {
		return actions.isEmpty();
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

	/**
	 * A specialization of the <code>GoalTest<code> interface so that it is possible
	 * to check the solution once a goal has been identified to determine if it is
	 * acceptable. This allows you to continue searching for alternative solutions
	 * without having to restart the search.<br>
	 * <br>
	 * However, care needs to be taken when doing this as it does not always make
	 * sense to continue with a search once an initial goal is found, for example if
	 * using a heuristic targeted at a single goal.
	 *
	 * @author Ciaran O'Reilly
	 */
	public interface SolutionChecker extends GoalTest {
		/**
		 * This method is only called if GoalTest.isGoalState() returns true.
		 *
		 * @param actions
		 *            the list of actions to get to the goal state.
		 *
		 * @param goal
		 *            the goal the list of actions will reach.
		 *
		 * @return true if the solution is acceptable, false otherwise, which
		 *         indicates the search should be continued.
		 */
		boolean isAcceptableSolution(List<Action> actions, Object goal);
	}
}