package aima.core.api.search.uninformed.graph;

import aima.core.api.search.GeneralGraphSearch;
import aima.core.api.search.Node;
import aima.core.api.search.Problem;

import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): Figure ??, page ??.<br>
 * <br>
 *
 * <pre>
 * function BREADTH-FIRST-SEARCH(problem) returns a solution, or failure
 *   node &lt;- a node with STATE = problem.INITIAL-STATE, PATH-COST=0
 *   if problem.GOAL-TEST(node.STATE) then return SOLUTION(node)
 *   frontier &lt;- a FIFO queue with node as the only element
 *   explored &lt;- an empty set
 *   loop do
 *      if EMPTY?(frontier) then return failure
 *      node &lt;- POP(frontier) // chooses the shallowest node in frontier
 *      add node.STATE to explored
 *      for each action in problem.ACTIONS(node.STATE) do
 *          child &lt;- CHILD-NODE(problem, node, action)
 *          if child.STATE is not in explored or frontier then
 *              if problem.GOAL-TEST(child.STATE) then return SOLUTION(child)
 *              frontier &lt;- INSERT(child, frontier)
 * </pre>
 *
 * Figure ?? Breadth-first search on a graph.<br>
 *
 * An instance of the general graph-search algorithm (Figure ?.?) in which the
 * shallowest unexpanded node is chosen for expansion. This is achieved very simply
 * by using a FIFO queue for the frontier. Thus, new nodes (which are always deeper
 * than their parent) go to the back of the queue, and old nodes, which are shallower
 * than the new nodes, get expanded first. There is one slight tweak on the general
 * graph-search algorithm, which is that the goal test is applied to each node
 * when it is generated rather than when it is selected for expansion.
 *
 * @author Ciaran O'Reilly
 */
public interface BreadthFirstGraphSearch<A, S> extends GeneralGraphSearch<A, S> {

    // function BREADTH-FIRST-SEARCH(problem) returns a solution, or failure
    @Override
    default List<A> apply(Problem<A, S> problem) {
        // node <- a node with STATE = problem.INITIAL-STATE, PATH-COST=0
        Node<A, S> node = newNode(problem.initialState(), 0);
        // if problem.GOAL-TEST(node.STATE) then return SOLUTION(node)
        if (isGoalState(node, problem)) { return solution(node); }
        // frontier <- a FIFO queue with node as the only element
        Queue<Node<A, S>> frontier = newFrontier();
        frontier.add(node);
        // explored <- an empty set
        Set<S> explored = newExplored();
        // loop do
        while (true) {
            // if EMPTY?(frontier) then return failure
            if (frontier.isEmpty()) { return failure(); }
            // node <- POP(frontier) // chooses the shallowest node in frontier
            node = frontier.remove();
            // add node.STATE to explored
            explored.add(node.state());
            // for each action in problem.ACTIONS(node.STATE) do
            for (A action : problem.actions(node.state())) {
                // child <- CHILD-NODE(problem, node, action)
                Node<A, S> child = childNode(problem, node, action);
                // if child.STATE is not in explored or frontier then
                if (!(explored.contains(child.state()) || frontier.contains(child.state()))) {
                    // if problem.GOAL-TEST(child.STATE) then return SOLUTION(child)
                    if (isGoalState(child, problem)) { return solution(child); }
                    // frontier <- INSERT(child, frontier)
                    frontier.add(child);
                }
            }
        }
    }
}
