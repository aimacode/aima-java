package aima.core.search.basic.uninformedsearch;

import aima.core.search.api.Node;
import aima.core.search.api.NodeFactory;
import aima.core.search.api.Problem;
import aima.core.search.basic.SearchUtils;
import aima.core.search.basic.support.BasicNodeFactory;

import java.util.Stack;

/**
 * <pre>
 *  function DEPTH-LIMITED-SEARCH(problem, l) returns a solution, or failure, or cutoff
 *  frontier ← a FIFO queue initially containing one path, for the problem's initial state
 *  solution ← failure
 *  while frontier is not empty do
 *    parent ← pop(frontier)
 *    if depth(parent) > l then
 *      solution ← cutoff
 *    else
 *        for child in successors(parent) do
 *          if child is a goal then
 *            return child
 *          add child to frontier
 *  return solution
 * </pre>
 * <p>
 * Figure 3.14 An implementation of depth-limited tree search. The
 * algorithm has two different ways to signal failure to find a solution:
 * it returns failure when it has exhausted all paths and proved there is no
 * solution at any depth, and returns cutoff to mean there might be a solution
 * at a deeper depth than l. Note that this algorithm does not keep track of
 * reached states, and thus might visit the same state multiple times on different paths.
 *
 * @param <A> The generic object representing actions.
 * @param <S> The generic object representing state.
 * @author samagra
 */
public class DepthLimitedSearch<A, S> {

    // A helper class to generate new nodes.
    NodeFactory<A, S> nodeFactory = new BasicNodeFactory<>();

    /**
     * function DEPTH-LIMITED-SEARCH(problem, l) returns a solution, or failure, or cutoff
     *
     * @param problem The search problem.
     * @param l       The cutoff limit.
     * @return The goal state if exists/found else null
     */
    public Node<A, S> search(Problem<A, S> problem, int l) {
        Stack<Node<A, S>> frontier = new Stack<>();
        frontier.push(nodeFactory.newRootNode(problem.initialState()));
        Node<A, S> solution = null;
        while (!frontier.isEmpty()) {
            Node<A, S> parent = frontier.pop();
            if (SearchUtils.depth(parent) > l) {
                solution = null;
            } else {
                for (Node<A, S> child :
                        SearchUtils.successors(problem, parent)) {
                    if (problem.isGoalState(child.state())) {
                        return child;
                    }
                    frontier.push(child);
                }
            }
        }
        return solution;
    }
}
