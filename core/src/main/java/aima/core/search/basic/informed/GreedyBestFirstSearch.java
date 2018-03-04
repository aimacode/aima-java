package aima.core.search.basic.informed;

import aima.core.search.api.Node;
import aima.core.search.api.NodeFactory;
import aima.core.search.api.Problem;
import aima.core.search.api.SearchController;
import aima.core.search.api.SearchForActionsFunction;
import aima.core.search.basic.support.BasicNodeFactory;
import aima.core.search.basic.support.BasicSearchController;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.function.ToDoubleFunction;

/**
 * <pre>
 * function GREEDY-BEST-FIRST-SEARCH(problem) returns a solution, or failure
 *   node &larr; a node with STATE = problem.INITIAL-STATE, PATH-COST=0
 *   frontier &larr; a priority queue ordered by h(NODE), with node as the only element
 *   explored &larr; an empty set
 *   loop do
 *      if EMPTY?(frontier) then return failure
 *      node &larr; POP(frontier) // chooses the lowest-cost node in frontier
 *      if problem.GOAL-TEST(node.STATE) then return SOLUTION(node)
 *      add node.STATE to explored
 *      for each action in problem.ACTIONS(node.STATE) do
 *         child &larr; CHILD-NODE(problem, node, action)
 *         if child.STATE is not in explored or frontier then
 *            frontier &larr; INSERT(child, frontier)
 *         else if child.STATE is in frontier with higher COST then
 *            replace that frontier node with child
 * </pre>
 */
public class GreedyBestFirstSearch<A, S> implements SearchForActionsFunction<A, S> {
  // function GREEDY-BEST-FIRST-SEARCH(problem) returns a solution, or failure
  @Override
  public List<A> apply(Problem<A, S> problem) {
    // node <- a node with STATE = problem.INITIAL-STATE, PATH-COST=0
    Node<A, S> node = newRootNode(problem.initialState(), 0);
    // frontier <- a priority queue ordered by h(NODE), with node as the only element
    Queue<Node<A, S>> frontier = newPriorityQueueOrderedByH(node);
    // explored <- an empty set
    Set<S> explored = newExploredSet();
    // loop do
    while (true) {
      // if EMPTY?(frontier) then return failure
      if (frontier.isEmpty()) {
        return failure();
      }
      // node <- POP(frontier) // chooses the lowest-cost node in frontier
      node = frontier.remove();
      // if problem.GOAL-TEST(node.STATE) then return SOLUTION(node)
      if (problem.isGoalState(node.state())) {
        return solution(node);
      }
      // add node.STATE to explored
      explored.add(node.state());
      // for each action in problem.ACTIONS(node.STATE) do
      for (A action : problem.actions(node.state())) {
        // child <- CHILD-NODE(problem, node, action)
        Node<A, S> child = newChildNode(problem, node, action);
        // if child.STATE is not in explored or frontier then
        if (!(explored.contains(child.state())) || containsState(frontier, child.state())) {
          // frontier <- INSERT(child, frontier)
          frontier.add(child);
        } // else if child.STATE is in frontier with higher COST then
        else if (removedNodeFromFrontierWithSameStateAndHigherCost(child, frontier)) {
          // replace that frontier node with child
          frontier.add(child);
        }
      }
    }
  }

  /* Supporting Code */
  protected ToDoubleFunction<Node<A, S>> h;
  protected NodeFactory<A, S> nodeFactory = new BasicNodeFactory<>();
  protected SearchController<A, S> searchController = new BasicSearchController<>();

  public GreedyBestFirstSearch(ToDoubleFunction<Node<A, S>> h) {
    this.h = h;
  }

  public Node<A, S> newRootNode(S initialState, double pathCost) {
    return nodeFactory.newRootNode(initialState, pathCost);
  }

  public Queue<Node<A, S>> newPriorityQueueOrderedByH(Node<A, S> initialNode) {
    Queue<Node<A, S>> frontier = new PriorityQueue<>(
        Comparator.comparingDouble(value -> h.applyAsDouble(value)));
    frontier.add(initialNode);
    return frontier;
  }

  public Set<S> newExploredSet() {
    return new HashSet<>();
  }

  public List<A> failure() {
    return searchController.failure();
  }

  public boolean isGoalState(Node<A, S> node, Problem<A, S> problem) {
    return searchController.isGoalState(node, problem);
  }

  public List<A> solution(Node<A, S> node) {
    return searchController.solution(node);
  }

  public Node<A, S> newChildNode(Problem<A, S> problem, Node<A, S> node, A action) {
    return nodeFactory.newChildNode(problem, node, action);
  }

  public boolean containsState(Queue<Node<A, S>> frontier, S state) {
    // NOTE: Not very efficient (i.e. linear in the size of the frontier)
    return frontier.stream().anyMatch(frontierNode -> frontierNode.state().equals(state));
  }

  private boolean removedNodeFromFrontierWithSameStateAndHigherCost(Node<A, S> child,
      Queue<Node<A, S>> frontier) {
    // NOTE: Not very efficient (i.e. linear in the size of the frontier)
    return frontier.removeIf(
        n -> n.state().equals(child.state()) && h.applyAsDouble(n) > h.applyAsDouble(child));
  }
}
