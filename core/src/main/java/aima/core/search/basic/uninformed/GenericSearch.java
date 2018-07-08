package aima.core.search.basic.uninformed;

import aima.core.search.api.Node;
import aima.core.search.api.NodeFactory;
import aima.core.search.api.Problem;
import aima.core.search.basic.support.BasicNodeFactory;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): Figure ??, page ??.
 * <br>
 * <br>
 *
 * <pre>
 * function GENERIC-SEARCH(problem) returns a solution, or failure
 *  frontier ← a queue initially containing one path, for the problem's initial state
 *  reached ← a table of {state: the best path that reached state}; initially empty
 *  solution ← failure
 *  while frontier is not empty and solution can possibly be improved do
 *    parent ← some node that we choose to remove from frontier
 *    for child in successors(parent) do
 *      s ← child.state
 *      if s is not in reached or child is a cheaper path than reached[s] then
 *        reached[s] ← child
 *        add child to frontier
 *        if child is a goal and is cheaper than solution then
 *          solution = child
 *  return solution
 * </pre>
 *
 * Figure ?? In the GENERIC-SEARCH algorithm, we keep track of the best
 * solution found so far, as well as a set of states that we have already
 * reached, and a frontier of paths from which we will choose the next path
 * to expand. In any specific search algorithm, we specify (1) the criteria
 * for ordering the paths in the frontier, and (2) the procedure for
 * determining when it is no longer possible to improve on a solution.
 *
 *
 * @author samagra
 */

public abstract class GenericSearch<A, S> {
    protected NodeFactory<A, S> nodeFactory = new BasicNodeFactory<>();
    HashMap<S, Node<A, S>> reached = new HashMap<>();

    /**
     * function GENERIC-SEARCH(problem) returns a solution, or failure
     * @param problem
     *  The search problem.
     * @return
     *  The solution to the search problem.
     */
    public Node<A, S> genericSearch(Problem<A, S> problem) {
        //  frontier ← a queue initially containing one path, for the problem's initial state
        Queue<Node<A, S>> frontier = newFrontier(problem.initialState());
        reached.clear();
        Node<A, S> solution = null;
        while (!frontier.isEmpty() && this.canImprove(reached, solution)) {
            Node<A, S> parent = frontier.remove();
            for (A action :
                    problem.actions(parent.state())) {
                Node<A, S> child = nodeFactory.newChildNode(problem, parent, action);
                S s = child.state();
                if (!reached.containsKey(s) || (child.pathCost() < reached.get(s).pathCost())) {
                    reached.put(s, child);
                    frontier = this.addToFrontier(child, frontier);
                    if (problem.isGoalState(child.state()) && (child.pathCost() < solution.pathCost())) {
                        solution = child;
                    }
                }
            }
        }
        return solution;
    }

    abstract Queue<Node<A, S>> addToFrontier(Node<A, S> child,
                                             Queue<Node<A, S>> frontier);

    abstract boolean canImprove(HashMap<S, Node<A, S>> reached,
                                Node<A, S> solution);

    public Queue<Node<A, S>> newFrontier(S initialState) {
        Queue<Node<A, S>> frontier = new LinkedList<>();
        frontier.add(nodeFactory.newRootNode(initialState));
        return frontier;
    }
}
