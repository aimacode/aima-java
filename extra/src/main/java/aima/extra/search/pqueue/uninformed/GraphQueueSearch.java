package aima.extra.search.pqueue.uninformed;

import java.util.List;
import java.util.Queue;
import java.util.Set;

import aima.core.search.api.Node;
import aima.core.search.api.Problem;
import aima.extra.search.pqueue.AbstractQueueSearchForActions;
import aima.extra.search.pqueue.QueueSearchForActions;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): Figure ??, page ??.
 * <br>
 * <br>
 *
 * <pre>
 * function GRAPH-SEARCH(problem) returns a solution, or failure
 *   initialize the frontier using the initial state of problem
 *   initialize the explored set to be empty
 *   loop do
 *     if the frontier is empty then return failure
 *     choose a leaf node and remove it from the frontier
 *     if the node contains a goal state then return the corresponding solution
 *     add the node to the explored set
 *     expand the chosen node, adding the resulting nodes to the frontier
 *       only if not in the frontier or explored set
 * </pre>
 *
 * Figure ?? An informal description of the general graph-search algorithm.
 *
 *
 * @author Ciaran O'Reilly
 */
public class GraphQueueSearch<A, S> extends AbstractQueueSearchForActions<A, S>
		implements QueueSearchForActions.DoesStateContainmentCheckingOnFrontier {
	// function GRAPH-SEARCH(problem) returns a solution, or failure
	@Override
	public List<A> apply(Problem<A, S> problem) {
		// initialize the frontier using the initial state of problem
		Queue<Node<A, S>> frontier = newFrontier(problem.initialState());
		// initialize the explored set to be empty
		Set<S> explored = newExploredSet();
		// loop do
		while (loopDo()) {
			// if the frontier is empty then return failure
			if (frontier.isEmpty()) {
				return failure();
			}
			// choose a leaf node and remove it from the frontier
			Node<A, S> node = frontier.remove();
			// if the node contains a goal state then return the corresponding
			// solution
			if (isGoalState(node, problem)) {
				return solution(node);
			}
			// add the node to the explored set
			explored.add(node.state());
			// expand the chosen node, adding the resulting nodes to the
			// frontier
			for (A action : problem.actions(node.state())) {
				Node<A, S> child = newChildNode(problem, node, action);
				// only if not in the frontier or explored set
				if (!(containsState(frontier, child.state()) || explored.contains(child.state()))) {
					frontier.add(child);
				}
			}
		}
		return failure();
	}
}
