package aima.core.search.framework;

import java.util.ArrayList;
import java.util.List;

import aima.core.agent.Action;

/**
 * @author Ravi Mohan
 * 
 */
public class SearchUtils {

	public static List<Action> actionsFromNodes(List<Node> nodeList) {
		List<Action> stateList = new ArrayList<Action>();
		for (int i = 1; i < nodeList.size(); i++) { // ignore root node this has
			// no action hence index starts from 1 not
			// zero
			Node node = nodeList.get(i);
			stateList.add(node.getAction());
		}
		return stateList;
	}
}