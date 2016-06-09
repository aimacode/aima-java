package aima.core.search.basic.queue;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import aima.core.search.api.Node;
import aima.core.search.api.Problem;

/**
 * <pre>
 * function TREE-GOAL-TESTED-FIRST-SEARCH(problem) returns a solution, or failure
 *   node &lt;- a node with STATE = problem.INITIAL-STATE
 *   if problem.GOAL-TEST(node.STATE) then return SOLUTION(node)
 *   frontier &lt;- a queue with node as the only element
 *   loop do
 *      if EMPTY?(frontier) then return failure
 *      node &lt;- POP(frontier) // chooses the shallowest node in frontier
 *      for each action in problem.ACTIONS(node.STATE) do
 *          child &lt;- CHILD-NODE(problem, node, action)
 *          if problem.GOAL-TEST(child.STATE) then return SOLUTION(child)
 *          frontier &lt;- INSERT(child, frontier)
 * </pre>
 *
 * An instance of the general tree-search algorithm (Figure ?.?) in which the
 * node is chosen for expansion by using a queue for the frontier. There is one
 * slight tweak on the general tree-search algorithm, which is that the goal
 * test is applied to each node when it is generated rather than when it is
 * selected for expansion.
 *
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 */
public class TreeGoalTestedFirstQueueSearch<A, S> extends AbstractQueueSearchForActions<A, S> {
	// function TREE-GOAL-TESTED-FIRST-SEARCH(problem) returns a solution, or failure
	@Override
	public List<A> apply(Problem<A, S> problem) {
		// node <- a node with STATE = problem.INITIAL-STATE
		Node<A, S> node = getNodeFactory().newRootNode(problem.initialState(), 0);
		// if problem.GOAL-TEST(node.STATE) then return SOLUTION(node)
		if (getSearchController().isGoalState(node, problem)) { return getSearchController().solution(node); }
		// frontier <- a queue with node as the only element
		Queue<Node<A, S>> frontier = newFrontier(node);
		// loop do
		while (getSearchController().isExecuting()) {
			// if EMPTY?(frontier) then return failure
			if (frontier.isEmpty()) { return getSearchController().failure(); }
			// node <- POP(frontier) // chooses the shallowest node in frontier
			node = frontier.remove();
			// for each action in problem.ACTIONS(node.STATE) do
			for (A action : problem.actions(node.state())) {
				// child <- CHILD-NODE(problem, node, action)
				Node<A, S> child = getNodeFactory().newChildNode(problem, node, action);
				// if problem.GOAL-TEST(child.STATE) then return SOLUTION(child)
				if (getSearchController().isGoalState(child, problem)) { return getSearchController().solution(child); }
				// frontier <- INSERT(child, frontier)
				frontier.add(child);
			}
		}
		return getSearchController().failure();
	}
	
	//
	// Supporting Code
	public TreeGoalTestedFirstQueueSearch() {
		// We default to a LinkedList instead of the parent classes default of a queue that supports
		// state tracking as it is not required in this instance.
		setFrontierSupplier(LinkedList::new);
	}
}
