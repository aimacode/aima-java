package aima.core.api.search;

import java.util.List;
import java.util.Queue;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): Figure ??, page ??.<br>
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
public interface GeneralTreeSearch<A, S> extends SearchFunction<A, S> {
    // function TREE-SEARCH(problem) returns a solution, or failure
    @Override
    default List<A> apply(Problem<A, S> problem) {
        // initialize the frontier using the initial state of the problem
        Queue<Node<A, S>> frontier = newFrontier();
        frontier.add(newNode(problem.initialState(), 0));
        // loop do
        while (true) {
            // if the frontier is empty then return failure
            if (frontier.isEmpty()) { return failure(); }
            // choose a leaf node and remove it from the frontier
            Node<A, S> node = frontier.remove();
            // if the node contains a goal state then return the corresponding solution
            if (isGoalState(node, problem)) { return solution(node); }
            // expand the chosen node, adding the resulting nodes to the frontier
            for (A action : problem.actions(node.state())) {
                frontier.add(childNode(problem, node, action));
            }
        }
    }

    Queue<Node<A, S>> newFrontier();
}
