package aima.core.api.search.uninformed;

import aima.core.api.agent.Action;
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
 * function UNIFORM-COST-SEARCH(problem) returns a solution, or failure
 *   node &lt;- a node with STATE = problem.INITIAL-STATE, PATH-COST = 0
 *   frontier &lt;- a priority queue ordered by PATH-COST, with node as the only element
 *   explored &lt;- an empty set
 *   loop do
 *      if EMPTY?(frontier) then return failure
 *      node &lt;- POP(frontier) // chooses the lowest-cost node in frontier
 *      if problem.GOAL-TEST(node.STATE) then return SOLUTION(node)
 *      add node.STATE to explored
 *      for each action in problem.ACTIONS(node.STATE) do
 *          child &lt;- CHILD-NODE(problem, node, action)
 *          if child.STATE is not in explored or frontier then
 *             frontier &lt;- INSERT(child, frontier)
 *          else if child.STATE is in frontier with higher PATH-COST then
 *             replace that frontier node with child
 * </pre>
 *
 * Figure ?? Uniform-cost search on a graph. The algorithm is identical to the
 * general graph search algorithm in Figure 3.7, except for the use of a
 * priority queue and the addition of an extra check in case a shorter path to a
 * frontier state is discovered. The data structure for frontier needs to
 * support efficient membership testing, so it should combine the capabilities
 * of a priority queue and a hash table.
 *
 * @author Ciaran O'Reilly
 */
public interface UniformCostSearch <S> extends GraphSearch<S> {

    // function UNIFORM-COST-SEARCH(problem) returns a solution, or failure
    @Override
    default List<Action> apply(Problem<S> problem) {
        // node <- a node with STATE = problem.INITIAL-STATE, PATH-COST = 0
        Node<S> node = newNode(problem.initialState(), 0);
        // frontier <- a priority queue ordered by PATH-COST, with node as the only element
        Queue<Node<S>> frontier = newFrontier();
        frontier.add(node);
        // explored <- an empty set
        Set<S> explored = newExplored();
        // loop do
        while (true) {
            // if EMPTY?(frontier) then return failure
            if (frontier.isEmpty()) { return failure(); }
            // node <- POP(frontier) // chooses the lowest-cost node in frontier
            node = frontier.remove();
            // if problem.GOAL-TEST(node.STATE) then return SOLUTION(node)
            if (problem.isGoalState(node.state())) { return solution(node); }
            // add node.STATE to explored
            explored.add(node.state());
            // for each action in problem.ACTIONS(node.STATE) do
            for (Action action : problem.actions(node.state())) {
                // child <- CHILD-NODE(problem, node, action)
                Node<S> child = childNode(problem, node, action);
                // if child.STATE is not in explored or frontier then
                boolean childStateInFrontier = frontier.contains(child.state());
                if (!(childStateInFrontier || explored.contains(child.state()))) {
                    // frontier <- INSERT(child, frontier)
                    frontier.add(child);
                } // else if child.STATE is in frontier with higher PATH-COST then
                else if (childStateInFrontier && frontier.removeIf(n -> n.state().equals(child.state()) && n.pathCost() > child.pathCost())) {
                    // replace that frontier node with child
                    frontier.add(child);
                }
            }
        }
    }
}
