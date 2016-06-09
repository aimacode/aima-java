package aima.core.search.basic.queue;

import java.util.List;
import java.util.Queue;

import aima.core.search.api.Node;
import aima.core.search.api.Problem;

/**
 * <pre>
 * function TREE-PRIORITY-SEARCH(problem) returns a solution, or failure
 *   node &lt;- a node with STATE = problem.INITIAL-STATE
 *   frontier &lt;- a priority queue, with node as the only element
 *   loop do
 *      if EMPTY?(frontier) then return failure
 *      node &lt;- POP(frontier) // chooses the highest priority node in frontier
 *      if problem.GOAL-TEST(node.STATE) then return SOLUTION(node)
 *      for each action in problem.ACTIONS(node.STATE) do
 *          child &lt;- CHILD-NODE(problem, node, action)
 *          if child.STATE is not in frontier then
 *             frontier &lt;- INSERT(child, frontier)
 *          else if child.STATE is in frontier with lower priority then
 *             replace that frontier node with child
 * </pre>
 *
 * The algorithm is identical to the general tree search algorithm in Figure ??,
 * except for the use of a priority queue and the addition of an extra check in
 * case a higher priority to a frontier state is discovered. The data structure
 * for frontier needs to support efficient membership testing, so it should
 * combine the capabilities of a priority queue and a hash table.
 *
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 */
public class TreePriorityQueueSearch<A, S> extends AbstractQueueSearchForActions<A, S> {
	// function TREE-PRIORITY-SEARCH(problem) returns a solution, or failure
	@Override
	public List<A> apply(Problem<A, S> problem) {
		// node <- a node with STATE = problem.INITIAL-STATE
		Node<A, S> node = getNodeFactory().newRootNode(problem.initialState());
		// frontier <- a priority queue, with node as the only element
		Queue<Node<A, S>> frontier = newFrontier(node);
		// loop do
		while (getSearchController().isExecuting()) {
			// if EMPTY?(frontier) then return failure
			if (frontier.isEmpty()) { return getSearchController().failure(); }
			// node <- POP(frontier) // chooses the highest priority node in frontier
			node = frontier.remove();
			// if problem.GOAL-TEST(node.STATE) then return SOLUTION(node)
			if (getSearchController().isGoalState(node, problem)) { return getSearchController().solution(node);}
			// for each action in problem.ACTIONS(node.STATE) do
			for (A action : problem.actions(node.state())) {
				// child <- CHILD-NODE(problem, node, action)
				Node<A, S> child = getNodeFactory().newChildNode(problem, node, action);
				// if child.STATE is not in frontier then
				boolean childStateInFrontier = containsState(frontier, child.state());
				if (!childStateInFrontier) {
					// frontier <- INSERT(child, frontier)
					frontier.add(child);
				} // else if child.STATE is in frontier with lower priority then
				else if (childStateInFrontier &&
						// NOTE: by Java's PriorityQueue convention, nodes that compare
						// lower have a higher priority.
						frontier.removeIf(n -> child.state().equals(n.state())
								&& getNodeFactory().compare(child, n) < 0)) {
					// replace that frontier node with child
					frontier.add(child);
				}
			}
		}
		return getSearchController().failure();
	}
}