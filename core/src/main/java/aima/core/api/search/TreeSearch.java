package aima.core.api.search;

import aima.core.api.agent.Action;

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
public interface TreeSearch<S> extends SearchFunction<S> {
    // function TREE-SEARCH(problem) returns a solution, or failure
    @Override
    default List<Action> apply(Problem<S> problem) {
        // initialize the frontier using the initial state of the problem
        Queue<Node<S>> frontier = newFrontier();
        frontier.add(newNode(problem.initialState(), 0));
        // loop do
        while (true) {
            // if the frontier is empty then return failure
            if (frontier.isEmpty()) { return failure(); }
            // choose a leaf node and remove it from the frontier
            Node<S> node = frontier.remove();
            // if the node contains a goal state then return the corresponding solution
            if (isGoalState(node, problem)) { return solution(node); }
            // expand the chosen node, adding the resulting nodes to the frontier
            for (Action action : problem.actions(node.state())) {
                frontier.add(childNode(problem, node, action));
            }
        }
    }

    Queue<Node<S>> newFrontier();
}
