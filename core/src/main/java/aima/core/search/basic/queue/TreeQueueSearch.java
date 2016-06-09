package aima.core.search.basic.queue;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import aima.core.search.api.Node;
import aima.core.search.api.Problem;

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
		while (getSearchController().isExecuting()) {
			// if the frontier is empty then return failure
			if (frontier.isEmpty()) { return getSearchController().failure(); }
			// choose a leaf node and remove it from the frontier
			Node<A, S> node = frontier.remove();
			// if the node contains a goal state then return the corresponding solution
			if (getSearchController().isGoalState(node, problem)) { return getSearchController().solution(node);}
			// expand the chosen node, adding the resulting nodes to the frontier
			for (A action : problem.actions(node.state())) {
				frontier.add(getNodeFactory().newChildNode(problem, node, action));
			}
		}
		return getSearchController().failure();
	}
	
	//
	// Supporting Code
	public TreeQueueSearch() {
		// We default to a LinkedList instead of the parent classes default of a queue that supports
		// state tracking as it is not required in this instance.
		setFrontierSupplier(LinkedList::new);
	}
}