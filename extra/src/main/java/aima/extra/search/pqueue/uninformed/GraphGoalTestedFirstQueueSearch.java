package aima.extra.search.pqueue.uninformed;

import java.util.List;
import java.util.Queue;
import java.util.Set;

import aima.core.search.api.Node;
import aima.core.search.api.Problem;
import aima.extra.search.pqueue.AbstractQueueSearchForActions;
import aima.extra.search.pqueue.QueueSearchForActions;

/**
 * <pre>
 * function GRAPH-GOAL-TESTED-FIRST-SEARCH(problem) returns a solution, or failure
 *   node &larr; a node with STATE = problem.INITIAL-STATE
 *   if problem.GOAL-TEST(node.STATE) then return SOLUTION(node)
 *   frontier &larr; a queue with node as the only element
 *   explored &larr; an empty set
 *   loop do
 *      if EMPTY?(frontier) then return failure
 *      node &larr; POP(frontier) // chooses the shallowest node in frontier
 *      add node.STATE to explored
 *      for each action in problem.ACTIONS(node.STATE) do
 *          child &larr; CHILD-NODE(problem, node, action)
 *          if child.STATE is not in explored or frontier then
 *              if problem.GOAL-TEST(child.STATE) then return SOLUTION(child)
 *              frontier &larr; INSERT(child, frontier)
 * </pre>
 *
 * An instance of the general graph-search algorithm (Figure ?.?) in which the
 * node is chosen for expansion by using a queue for the frontier. There is one
 * slight tweak on the general graph-search algorithm, which is that the goal
 * test is applied to each node when it is generated rather than when it is
 * selected for expansion.
 *
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 */
public class GraphGoalTestedFirstQueueSearch<A, S> extends AbstractQueueSearchForActions<A, S>
		implements QueueSearchForActions.DoesStateContainmentCheckingOnFrontier {
	// function GRAPH-GOAL-TESTED-FIRST-SEARCH(problem) returns a solution, or
	// failure
	@Override
	public List<A> apply(Problem<A, S> problem) {
		// node <- a node with STATE = problem.INITIAL-STATE
		Node<A, S> node = newRootNode(problem.initialState());
		// if problem.GOAL-TEST(node.STATE) then return SOLUTION(node)
		if (isGoalState(node, problem)) {
			return solution(node);
		}
		// frontier <- a queue with node as the only element
		Queue<Node<A, S>> frontier = newFrontier(node);
		// explored <- an empty set
		Set<S> explored = newExploredSet();
		// loop do
		while (loopDo()) {
			// if EMPTY?(frontier) then return failure
			if (frontier.isEmpty()) {
				return failure();
			}
			// node <- POP(frontier) // chooses the shallowest node in frontier
			node = frontier.remove();
			// add node.STATE to explored
			explored.add(node.state());
			// for each action in problem.ACTIONS(node.STATE) do
			for (A action : problem.actions(node.state())) {
				// child <- CHILD-NODE(problem, node, action)
				Node<A, S> child = newChildNode(problem, node, action);
				// if child.STATE is not in explored or frontier then
				if (!(explored.contains(child.state()) || containsState(frontier, child.state()))) {
					// if problem.GOAL-TEST(child.STATE) then return
					// SOLUTION(child)
					if (isGoalState(child, problem)) {
						return solution(child);
					}
					// frontier <- INSERT(child, frontier)
					frontier.add(child);
				}
			}
		}
		return failure();
	}
}
