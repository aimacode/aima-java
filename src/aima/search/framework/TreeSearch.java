package aima.search.framework;

public class TreeSearch extends QueueSearch {

	protected void addExpandedNodesToFringe(NodeStore fringe, Node node,
			Problem problem) {
		fringe.add(expandNode(node, problem));
	}

}