package aima.search.framework;

import java.util.HashSet;
import java.util.Set;

public class GraphSearch extends QueueSearch {

	Set<Object> closed = new HashSet<Object>();




	protected void addExpandedNodesToFringe(NodeStore fringe, Node node,
			Problem problem) {

		if (!(alreadySeen(node))) {
			closed.add(node.getState());
			fringe.add(expandNode(node,problem));

		}
	}

	private boolean alreadySeen(Node node) {

		return closed.contains(node.getState());
	}




}