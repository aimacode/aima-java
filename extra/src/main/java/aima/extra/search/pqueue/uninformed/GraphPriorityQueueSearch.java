package aima.extra.search.pqueue.uninformed;

import java.util.List;
import java.util.Queue;
import java.util.Set;

import aima.core.search.api.Node;
import aima.core.search.api.Problem;
import aima.extra.search.pqueue.AbstractPriorityQueueSearchForActions;
import aima.extra.search.pqueue.QueueSearchForActions;

/**
 * <pre>
 * function GRAPH-PRIORITY-SEARCH(problem) returns a solution, or failure
 *   node &larr; a node with STATE = problem.INITIAL-STATE
 *   frontier &larr; a priority queue, with node as the only element
 *   explored &larr; an empty set
 *   loop do
 *      if EMPTY?(frontier) then return failure
 *      node &larr; POP(frontier) // chooses the highest priority node in frontier
 *      if problem.GOAL-TEST(node.STATE) then return SOLUTION(node)
 *      add node.STATE to explored
 *      for each action in problem.ACTIONS(node.STATE) do
 *          child &larr; CHILD-NODE(problem, node, action)
 *          if child.STATE is not in explored or frontier then
 *             frontier &larr; INSERT(child, frontier)
 *          else if child.STATE is in frontier with lower priority then
 *             replace that frontier node with child
 * </pre>
 *
 * The algorithm is identical to the general graph search algorithm in Figure
 * ??, except for the use of a priority queue and the addition of an extra check
 * in case a higher priority to a frontier state is discovered. The data
 * structure for frontier needs to support efficient membership testing, so it
 * should combine the capabilities of a priority queue and a hash table.
 *
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 */
public class GraphPriorityQueueSearch<A, S> extends AbstractPriorityQueueSearchForActions<A, S>
		implements QueueSearchForActions.DoesStateContainmentCheckingOnFrontier {
	// function GRAPH-PRIORITY-SEARCH((problem) returns a solution, or failure
	@Override
	public List<A> apply(Problem<A, S> problem) {
		// node <- a node with STATE = problem.INITIAL-STATE
		Node<A, S> node = newRootNode(problem.initialState());
		// frontier <- a priority queue, with node as the only element
		Queue<Node<A, S>> frontier = newFrontier(node);
		// explored <- an empty set
		Set<S> explored = newExploredSet();
		// loop do
		while (loopDo()) {
			// if EMPTY?(frontier) then return failure
			if (frontier.isEmpty()) {
				return failure();
			}
			// node <- POP(frontier) // chooses the highest priority node in
			// frontier
			node = frontier.remove();
			// if problem.GOAL-TEST(node.STATE) then return SOLUTION(node)
			if (isGoalState(node, problem)) {
				return solution(node);
			}
			// add node.STATE to explored
			explored.add(node.state());
			// for each action in problem.ACTIONS(node.STATE) do
			for (A action : problem.actions(node.state())) {
				// child <- CHILD-NODE(problem, node, action)
				Node<A, S> child = newChildNode(problem, node, action);
				// if child.STATE is not in explored or frontier then
				boolean childStateInFrontier = containsState(frontier, child.state());
				if (!(childStateInFrontier || explored.contains(child.state()))) {
					// frontier <- INSERT(child, frontier)
					frontier.add(child);
				} // else if child.STATE is in frontier with lower priority then
				else if (childStateInFrontier
						&& removedNodeFromFrontierWithSameStateAndLowerPriority(child, frontier)) {
					// replace that frontier node with child
					frontier.add(child);
				}
			}
		}
		return failure();
	}
}
