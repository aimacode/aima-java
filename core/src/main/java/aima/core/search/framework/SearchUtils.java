package aima.core.search.framework;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Subham Mishra
 * @author Ravi Mohan
 * 
 */
public class SearchUtils<A> {

	public static <A> List<A> actionsFromNodes(List<Node<A>> nodeList) {
		List<A> actions = new ArrayList<A>();
		if (nodeList.size() == 1) {
			// I'm at the root node, this indicates I started at the
			// Goal node, therefore just return a NoOp
			actions.add(null); //no operation
		} else {
			// ignore the root node this has no action
			// hence index starts from 1 not zero
			for (int i = 1; i < nodeList.size(); i++) {
				Node<A> node = nodeList.get(i);
				actions.add(node.getAction());
			}
		}
		return actions;
	}

	@SuppressWarnings("unchecked")
	public static <A> boolean isGoalState(Problem<A> p, Node<A> n) {
		boolean isGoal = false;
		GoalTest gt = p.getGoalTest();
		if (gt.isGoalState(n.getState())) {
			if (gt instanceof SolutionChecker) {
				isGoal = ((SolutionChecker<A>) gt).isAcceptableSolution(
						SearchUtils.actionsFromNodes(n.getPathFromRoot()),
						n.getState());
			} else {
				isGoal = true;
			}
		}
		return isGoal;
	}
}
