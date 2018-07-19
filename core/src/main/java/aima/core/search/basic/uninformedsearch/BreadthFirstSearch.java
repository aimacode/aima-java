package aima.core.search.basic.uninformedsearch;

import aima.core.search.api.Node;
import aima.core.search.api.NodeFactory;
import aima.core.search.api.Problem;
import aima.core.search.api.SearchForActionsFunction;
import aima.core.search.basic.SearchUtils;
import aima.core.search.basic.support.BasicNodeFactory;

import java.util.*;

/**
 * <pre>
 *  function BREADTH-FIRST-SEARCH(problem) returns a solution, or failure
 *  if problem's initial state is a goal then return empty path to initial state
 *  frontier ← a FIFO queue initially containing one path, for the problem's initial state
 *  reached ← a set of states; initially empty
 *  solution ← failure
 *  while frontier is not empty do
 *    parent ← the first node in frontier
 *    for child in successors(parent) do
 *      s ← child.state
 *      if s is a goal then
 *        return child
 *      if s is not in reached then
 *        add s to reached
 *        add child to the end of frontier
 *  return solution
 * </pre>
 * <p>
 * Figure 3.9 Breadth-first search algorithm.
 *
 * @param <A> The generic object representing actions.
 * @param <S> The generic object representing state.
 * @author samagra
 */
public class BreadthFirstSearch<A, S> implements GenericSearchInterface<A, S>, SearchForActionsFunction<A, S> {
    // Node factory is just a helper class to generate new nodes
    // It takes the problem and current state in order to generate new nodes
    NodeFactory<A, S> nodeFactory = new BasicNodeFactory();

    public BreadthFirstSearch() {
    }

    /**
     * function BREADTH-FIRST-SEARCH(problem) returns a solution, or failure
     *
     * @param problem The search problem.
     * @return The goal node if any else null.
     */
    @Override
    public Node<A, S> search(Problem<A, S> problem) {
        if (problem.isGoalState(problem.initialState())) {
            return nodeFactory.newRootNode(problem.initialState());
        }
        // frontier ← a FIFO queue initially containing one path, for the problem's initial state
        Queue<Node> frontier = new LinkedList<>();
        ((LinkedList<Node>) frontier).add(nodeFactory.newRootNode(problem.initialState()));
        HashSet<S> reached = new HashSet<>();
        Node<A, S> solution = null;
        while (!frontier.isEmpty()) {
            Node<A, S> parent = frontier.remove();
            for (Node<A, S> child : SearchUtils.successors(problem, parent)) {
                S s = child.state();
                if (problem.isGoalState(s)) {
                    return child;
                }
                // if s is not in reached then
                if (!reached.contains(s)) {
                    reached.add(s);
                    frontier.add(child);
                }
            }
        }
        return solution;
    }

    /**
     * Extracts a list of actions from a solution state.
     *
     * @param problem
     * @return
     */
    @Override
    public List<A> apply(Problem<A, S> problem) {
        Node<A, S> solution = this.search(problem);
        if (solution == null)
            return new ArrayList<>();
        return SearchUtils.generateActions(solution);
    }
}
