package aima.core.search.framework;

import java.util.ArrayList;
import java.util.List;

import aima.core.agent.Action;
import aima.core.agent.impl.NoOpAction;

/**
 * @author Ravi Mohan
 * 
 */
public class SearchUtils {

	public static List<Action> actionsFromNodes(List<Node> nodeList) {
		List<Action> actions = new ArrayList<Action>();
		if (nodeList.size() == 1) {
			// I'm at the root node, this indicates I started at the
			// Goal node, therefore just return a NoOp
			actions.add(NoOpAction.NO_OP);
		} else {
			// ignore the root node this has no action
			// hence index starts from 1 not zero
			for (int i = 1; i < nodeList.size(); i++) {
				Node node = nodeList.get(i);
				actions.add(node.getAction());
			}
		}
		return actions;
	}

	public static boolean isGoalState(Problem p, Node n) {
		boolean isGoal = false;
		GoalTest gt = p.getGoalTest();
		if (gt.isGoalState(n.getState())) {
			if (gt instanceof SolutionChecker) {
				isGoal = ((SolutionChecker) gt).isAcceptableSolution(
						SearchUtils.actionsFromNodes(n.getPathFromRoot()),
						n.getState());
			} else {
				isGoal = true;
			}
		}
		return isGoal;
	}
}