package aima.core.api.search.uninformed;

import aima.core.api.agent.Action;
import aima.core.api.search.Node;
import aima.core.api.search.Problem;

import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.function.Function;

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
 * @param <S> the type of the state space
 *
 * @author Ciaran O'Reilly
 */
public interface BreadthFirstSearch<S> extends Function<Problem<S>, List<Action>> {

    // function BREADTH-FIRST-SEARCH(problem) returns a solution, or failure
    @Override
    default List<Action> apply(Problem<S> problem) {

        // node <- a node with STATE = problem.INITIAL-STATE, PATH-COST=0
        Node<S> node = newNode(problem.initialState(), 0);
        // if problem.GOAL-TEST(node.STATE) then return SOLUTION(node)
        if (problem.isGoalState(node.state())) { return solution(node); }
        // frontier <- a FIFO queue with node as the only element
        Queue<Node<S>> frontier = newFrontier();
        frontier.add(node);
        // explored <- an empty set
        Set<S> explored = newExplored();
        // loop do
        while (true) {
            // if EMPTY?(frontier) then return failure
            if (frontier.isEmpty()) { return Collections.emptyList(); }
            // node <- POP(frontier) // chooses the shallowest node in frontier
            node = frontier.poll();
            // add node.STATE to explored
            explored.add(node.state());
            // for each action in problem.ACTIONS(node.STATE) do
            for (Action action : problem.actions(node.state())) {
                // child <- CHILD-NODE(problem, node, action)
                Node<S> child = childNode(problem, node, action);
                // if child.STATE is not in explored or frontier then
                if (!(explored.contains(child.state()) || frontier.contains(node.state()))) {
                    // if problem.GOAL-TEST(child.STATE) then return SOLUTION(child)
                    if (problem.isGoalState(child.state())) { return solution(child); }
                    // frontier <- INSERT(child, frontier)
                    frontier.add(child);
                }
            }
        }
    }

    Node<S> newNode(S state, double pathCost);
    Node<S> childNode(Problem<S> problem, Node<S> parent, Action action);

    Queue<Node<S>> newFrontier();
    Set<S> newExplored();

    List<Action> solution(Node<S> node);
}
