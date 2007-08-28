package aima.search.informed;

import java.util.List;

import aima.search.framework.Node;
import aima.search.framework.NodeExpander;
import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchUtils;

/**
 * @author Ravi Mohan
 * 
 */

public class HillClimbingSearch extends NodeExpander implements Search {

	public HillClimbingSearch() {
	}

	public List search(Problem p) throws Exception {
		clearInstrumentation();
		Node current = new Node(p.getInitialState());
		Node neighbor = null;
		while (true) {
			List children = expandNode(current, p);
			neighbor = getHighestValuedNodeFrom(children, p);

			if ((neighbor == null)
					|| (getValue(p, neighbor) <= getValue(p, current))) {
				return SearchUtils.actionsFromNodes(current.getPathFromRoot());
			}
			current = neighbor;
		}

	}

	private Node getHighestValuedNodeFrom(List children, Problem p) {
		int highestValue = Integer.MIN_VALUE;
		Node nodeWithHighestValue = null;
		for (int i = 0; i < children.size(); i++) {
			Node child = (Node) children.get(i);
			int value = getValue(p, child);
			if (value > highestValue) {
				highestValue = value;
				nodeWithHighestValue = child;
			}
		}
		return nodeWithHighestValue;
	}

	private int getValue(Problem p, Node n) {
		return -1 * getHeuristic(p, n); // assumption greater heuristic value =>
		// HIGHER on hill; 0 == goal state;
	}

	private int getHeuristic(Problem p, Node aNode) {
		return p.getHeuristicFunction().getHeuristicValue(aNode.getState());
	}

}