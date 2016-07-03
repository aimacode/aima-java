package aima.extra.search.pqueue.uninformed;

import java.util.List;
import java.util.Queue;

import aima.core.search.api.Node;
import aima.core.search.api.Problem;
import aima.extra.search.pqueue.AbstractQueueSearchForActions;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): Figure ??, page ??.
 * <br>
 * <br>
 *
 * <pre>
 * function TREE-SEARCH(problem) returns a solution, or failure
 *   initialize the frontier using the initial state of the problem
 *   loop do
 *     if the frontier is empty then return failure
 *     choose a leaf node and remove it from the frontier
 *     if the node contains a goal state then return the corresponding solution
 *     expand the chosen node, adding the resulting nodes to the frontier
 * </pre>
 *
 * Figure ?? An informal description of the general tree-search algorithm.
 *
 * @author Ciaran O'Reilly
 */
public class TreeQueueSearch<A, S> extends AbstractQueueSearchForActions<A, S> {
	// function TREE-SEARCH(problem) returns a solution, or failure
	@Override
	public List<A> apply(Problem<A, S> problem) {
		// initialize the frontier using the initial state of the problem
		Queue<Node<A, S>> frontier = newFrontier(problem.initialState());
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
			// expand the chosen node, adding the resulting nodes to the
			// frontier
			for (A action : problem.actions(node.state())) {
				frontier.add(newChildNode(problem, node, action));
			}
		}
		return failure();
	}
}