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

	public List search(Problem p) throws Exception {
		clearInstrumentation();
		Node current = new Node(p.getInitialState());
		Node neighbor = null;
		while (true) {
			List children = expandNode(current, p);
			neighbor = getHighestValuedNodeFrom(children, p);

			if ((neighbor == null)
					|| (getValue(neighbor, p) <= getValue(current, p))) {
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
			int value = getValue(child, p);
			if (value > highestValue) {
				highestValue = value;
				nodeWithHighestValue = child;
			}
		}
		return nodeWithHighestValue;
	}

	private int getHeuristic(Node aNode, Problem p) {

		return p.getHeuristicFunction().getHeuristicValue(aNode.getState());
	}

	private int getValue(Node n, Problem p) {

		return -1 * getHeuristic(n, p); //assumption greater heuristic value =>
		// HIGHER on hill; 0 == goal state;
	}

}