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
 *  function UNIFORM-COST-SEARCH(problem) returns a solution, or failure
 *  if problem's initial state is a goal then return empty path to initial state
 *  frontier ← a priority queue ordered by pathCost, with a node for the initial state
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
 * <p>
 * Figure 3.11 Uniform-cost search on a graph. Finds optimal
 * paths for problems with varying step costs.
 *
 * @param <A> The generic class representing action.
 * @param <S> The generic class representing states.
 * @author samagra
 */
public class UniformCostSearch<A, S> implements GenericSearchInterface<A, S>, SearchForActionsFunction<A, S> {

    // frontier ← a priority queue ordered by pathCost, with a node for the initial state
    PriorityQueue<Node<A, S>> frontier = new PriorityQueue<>(new Comparator<Node<A, S>>() {
        @Override
        public int compare(Node<A, S> o1, Node<A, S> o2) {
            return (int) (o1.pathCost() - o2.pathCost());
        }
    });

    private NodeFactory<A, S> nodeFactory = new BasicNodeFactory<>();

    @Override
    public Node<A, S> search(Problem<A, S> problem) {
        if (problem.isGoalState(problem.initialState())) {
            return nodeFactory.newRootNode(problem.initialState());
        }
        // frontier ← a priority queue ordered by pathCost, with a node for the initial state
        frontier.clear();
        frontier.add(nodeFactory.newRootNode(problem.initialState()));
        HashMap<S, Node<A, S>> reached = new HashMap<>();
        Node<A, S> solution = null;
        // while frontier is not empty and top(frontier) is cheaper than solution do
        while (!frontier.isEmpty() &&
                (solution == null || frontier.peek().pathCost() < solution.pathCost())) {
            Node<A, S> parent = frontier.poll();
            for (Node<A, S> child :
                    SearchUtils.successors(problem, parent)) {
                S s = child.state();
                // if s is not in reached or child is a cheaper path than reached[s] then
                if (!reached.containsKey(s) ||
                        child.pathCost() < reached.get(s).pathCost()) {
                    reached.put(s, child);
                    frontier.add(child);
                    // if child is a goal and is cheaper than solution then
                    if (problem.isGoalState(s) && (solution == null || child.pathCost() < solution.pathCost())) {
                        solution = child;
                    }
                }
            }
        }
        return solution;
    }

    /**
     * Returns a list of actions to be taken to reach the goal state.
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
