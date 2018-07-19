package aima.core.search.basic.informed;

import aima.core.search.api.Node;
import aima.core.search.api.NodeFactory;
import aima.core.search.api.Problem;
import aima.core.search.api.SearchForActionsFunction;
import aima.core.search.basic.SearchUtils;
import aima.core.search.basic.support.BasicNodeFactory;
import aima.core.search.basic.uninformedsearch.GenericSearchInterface;

import java.util.*;
import java.util.function.ToDoubleFunction;

/**
 * <pre>
 * function GREEDY-BEST-FIRST-SEARCH(problem) returns a solution, or failure
 *  if problem's initial state is a goal then return empty path to initial state
 *  frontier ← a priority queue ordered by h(n), with a node for the initial state
 *  reached ← a table of {state: the best path that reached state}; initially empty
 *  solution ← failure
 *  while frontier is not empty and top(frontier) is cheaper than solution do
 *    parent ← pop(frontier)
 *    for child in successors(parent) do
 *      s ← child.state
 *      if s is not in reached or child is a cheaper path than reached[s] then
 *        reached[s] ← child
 *        add child to the frontier
 *        if child is a goal and is cheaper than solution then
 *          solution = child
 *  return solution
 * </pre>
 *
 * @author samagra
 */
public class GreedyBestFirstSearch<A, S> implements GenericSearchInterface<A, S>, SearchForActionsFunction<A, S> {

    // The heuristic function
    protected ToDoubleFunction<Node<A, S>> h;
    // The helper class to generate nodes.
    protected NodeFactory<A, S> nodeFactory = new BasicNodeFactory<>();
    // frontier ← a priority queue ordered by h(n), with a node for the initial state
    PriorityQueue<Node<A, S>> frontier = new PriorityQueue<>(new Comparator<Node<A, S>>() {
        @Override
        public int compare(Node<A, S> o1, Node<A, S> o2) {
            return (int) (h.applyAsDouble(o1) - h.applyAsDouble(o2));
        }
    });

    // Constructor for the class which takes in a heuristic function.
    public GreedyBestFirstSearch(ToDoubleFunction<Node<A, S>> h) {
        this.h = h;
    }

    @Override
    public Node<A, S> search(Problem<A, S> problem) {
        if (problem.isGoalState(problem.initialState())) {
            return nodeFactory.newRootNode(problem.initialState());
        }

        frontier.clear();
        frontier.add(nodeFactory.newRootNode(problem.initialState()));
        // reached ← a table of {state: the best path that reached state}; initially empty
        HashMap<S, Node<A, S>> reached = new HashMap<>();
        Node<A, S> solution = null;
        while (!frontier.isEmpty() &&
                (solution == null || h.applyAsDouble(frontier.peek())
                        < h.applyAsDouble(solution))) {
            Node<A, S> parent = frontier.poll();
            for (Node<A, S> child :
                    SearchUtils.successors(problem, parent)) {
                S s = child.state();
                // if s is not in reached or child is a cheaper path than reached[s] then
                if (!reached.containsKey(s) ||
                        h.applyAsDouble(child) < h.applyAsDouble(reached.get(s))) {
                    reached.put(s, child);
                    frontier.add(child);
                    if (problem.isGoalState(s) &&
                            (solution == null || h.applyAsDouble(child) < h.applyAsDouble(solution))) {
                        solution = child;
                    }
                }
            }
        }
        return solution;
    }


    /**
     * Returns the list of actions that need to be taken in order to achieve the goal.
     *
     * @param problem The search problem.
     * @return The list of actions.
     */
    @Override
    public List<A> apply(Problem<A, S> problem) {
        Node<A, S> solution = this.search(problem);
        if (solution == null)
            return new ArrayList<>();
        else
            return SearchUtils.generateActions(solution);
    }

}
