package aima.extra.search.pqueue.uninformed;

import java.util.List;
import java.util.Queue;
import java.util.Set;

import aima.core.search.api.Node;
import aima.core.search.api.Problem;
import aima.extra.search.pqueue.AbstractPriorityQueueSearchForActions;

/**
 * <pre>
 * function GRAPH-RL-PRIORITY-QUEUE-SEARCH(problem) returns a solution, or failure
 *   node &larr; a node with STATE = problem.INITIAL-STATE
 *   frontier &larr; a priority queue, with node as the only element
 *   explored &larr; an empty set
 *   loop do
 *      repeat
 *          if EMPTY?(frontier) then return failure
 *          node &larr; POP(frontier) // chooses the highest priority node in frontier
 *      until node.STATE not in explored // ensures not already visited by higher priority node
 *      
 *      if problem.GOAL-TEST(node.STATE) then return SOLUTION(node)
 *      add node.STATE to explored
 *      for each action in problem.ACTIONS(node.STATE) do
 *          child &larr; CHILD-NODE(problem, node, action)
 *          if child.STATE is not in explored then
 *             frontier &larr; INSERT(child, frontier)
 * </pre>
 *
 * This is a version of the priority queue based graph search developed by
 * Ruediger Lunde on the AIMA3e branch. The advantage of this over the one
 * described in AIMA3e, via the UNIFORM-COST-SEARCH pseudocode, is that it is no
 * longer necessary to support the containment and replacement functionality on
 * the frontier (which can be expensive and tricky to implement). Instead,
 * duplicate states are dropped when popped off the frontier if they exist in
 * the explored set. Due to being a priority queue the highest priority node for
 * a state will be processed first. The main disadvantage is that the frontier
 * will be larger during processing in the case where you encounter duplicate
 * states before you explore the highest priority node related to it.
 *
 * @author Ruediger Lunde
 * @author Ciaran O'Reilly
 */
public class GraphRLPriorityQueueSearch<A, S> extends AbstractPriorityQueueSearchForActions<A, S> {
	// function GRAPH-RL-PRIORITY-QUEUE-SEARCH(problem) returns a solution, or
	// failure
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
			do {
				// if EMPTY?(frontier) then return failure
				if (frontier.isEmpty()) {
					return failure();
				}
				// node <- POP(frontier) /* chooses the highest priority node in
				// frontier */
				node = frontier.remove();
			}
			// until node.STATE not in explored /* ensures not already visited
			// by higher priority node */
			while (explored.contains(node.state()));

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
				// if child.STATE is not in explored then
				if (!explored.contains(child.state())) {
					// frontier <- INSERT(child, frontier)
					frontier.add(child);
				}

			}
		}
		return failure();
	}
}
