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
 * function A*-SEARCH(problem) returns a solution, or failure
 *   node &larr; a node with STATE = problem.INITIAL-STATE, PATH-COST=0
 *   frontier &larr; a priority queue ordered by PATH-COST + h(NODE), with node as the only element
 *   explored &larr; an empty set
 *   loop do
 *      if EMPTY?(frontier) then return failure
 *      node &lt;- POP(frontier) // chooses the lowest-cost node in frontier
 *      if problem.GOAL-TEST(node.STATE) then return SOLUTION(node)
 *      add node.STATE to explored
 *      for each action in problem.ACTIONS(node.STATE) do
 *          child &larr; CHILD-NODE(problem, node, action)
 *          if child.STATE is not in explored or frontier then
 *             frontier &larr; INSERT(child, frontier)
 *          else if child.STATE is in frontier with higher COST then
 *             replace that frontier node with child
 * </pre>
 *
 * @author Ciaran O'Reilly
 */
public class AStarSearch<A, S> implements GenericSearchInterface<A, S>, SearchForActionsFunction<A, S> {

    // The heuristic function
    protected ToDoubleFunction<Node<A, S>> h;
    // A helper class to generate new nodes.
    protected NodeFactory<A, S> nodeFactory = new BasicNodeFactory<>();

    // frontier ← a priority queue ordered by f(n) = h(n)+g(n), with a node for the initial state
    PriorityQueue<Node<A, S>> frontier = new PriorityQueue<>(new Comparator<Node<A, S>>() {
        @Override
        public int compare(Node<A, S> o1, Node<A, S> o2) {
            return (int) (h.applyAsDouble(o1) - h.applyAsDouble(o2));
        }
    });


    public AStarSearch(ToDoubleFunction<Node<A, S>> h) {
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
        // solution ← failure
        Node<A, S> solution = null;
        // while frontier is not empty and top(frontier) is cheaper than solution do
        while (!frontier.isEmpty() &&
                (solution == null || getCostValue(frontier.peek()) < getCostValue(solution))) {
            // parent ← pop(frontier)
            Node<A, S> parent = frontier.poll();
            // for child in successors(parent) do
            for (Node<A, S> child :
                    SearchUtils.successors(problem, parent)) {
                // s ← child.state
                S s = child.state();
                // if s is not in reached or child is a cheaper path than reached[s] then
                if (!reached.containsKey(s) ||
                        getCostValue(child) < getCostValue(reached.get(s))) {
                    // reached[s] ← child
                    reached.put(s, child);
                    // add child to the frontier
                    frontier.add(child);
                    // if child is a goal and is cheaper than solution then
                    if (problem.isGoalState(s) &&
                            (solution == null || getCostValue(child) < getCostValue(solution))) {
                        // solution = child
                        solution = child;
                    }
                }
            }
        }
        // return solution
        return solution;
    }

    @Override
    public List<A> apply(Problem<A, S> problem) {
        Node<A, S> solution = this.search(problem);
        if (solution == null)
            return new ArrayList<>();
        else
            return SearchUtils.generateActions(solution);
    }

    private double getCostValue(Node<A, S> node) {
        return node.pathCost() + h.applyAsDouble(node);
    }
}
