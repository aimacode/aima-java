package aima.search.framework;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Ravi Mohan
 * 
 */

/**
 * Artificial Intelligence A Modern Approach (2nd Edition): Figure 3.19, page
 * 83. <code>
 * function GRAPH-SEARCH(problem, fringe) returns a solution, or failure
 * 
 *   closed <- an empty set
 *   fringe <- INSERT(MAKE-NODE(INITIAL-STATE[problem]), fringe)
 *   loop do
 *     if EMPTY?(fringe) then return failure
 *     node <- REMOVE-FIRST(fringe)
 *     if (GOAL-TEST[problem](STATE[node]) then return SOLUTION(node)
 *     if STATE[node] is not in closed then
 *       add STATE[node] to closed
 *       fringe <- INSERT-ALL(EXPAND(node, problem), fringe)
 * </code>
 * Figure 3.19 The general graph-search algorithm, The set closed can be
 * implemented with a hash table to allow efficient checking for repeated
 * states. This algorithm assumes that the first path to a state s is the
 * cheapest (see text).
 */

public class GraphSearch extends QueueSearch {

	Set<Object> closed = new HashSet<Object>();

	// Need to override search() method so that I can re-initialize
	// the closed list should multiple calls to search be made.
	@Override
	public List<String> search(Problem problem, NodeStore fringe) {
		closed.clear();
		return super.search(problem, fringe);
	}

	@Override
	public void addExpandedNodesToFringe(NodeStore fringe, Node node,
			Problem problem) {

		// if STATE[node] is not in closed then
		if (!(alreadySeen(node))) {
			// add STATE[node] to closed
			closed.add(node.getState());
			// fringe <- INSERT-ALL(EXPAND(node, problem), fringe)
			fringe.add(expandNode(node, problem));

		}
	}

	private boolean alreadySeen(Node node) {
		return closed.contains(node.getState());
	}
}