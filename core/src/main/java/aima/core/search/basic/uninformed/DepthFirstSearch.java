package aima.core.search.basic.uninformed;

import aima.core.search.api.Node;
import aima.core.search.api.NodeFactory;
import aima.core.search.api.Problem;
import aima.core.search.api.SearchController;
import aima.core.search.api.SearchForActionsFunction;
import aima.core.search.basic.support.BasicNodeFactory;
import aima.core.search.basic.support.BasicSearchController;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 * <pre>
 * function DEPTH-FIRST-SEARCH(problem) returns a solution, or failure
 *   node &larr; a node with STATE = problem.INITIAL-STATE, PATH-COST=0
 *   if problem.GOAL-TEST(node.STATE) then return SOLUTION(node)
 *   frontier &larr; a LIFO queue with node as the only element
 *   explored &larr; an empty set
 *   loop do
 *       if EMPTY?(frontier) then return failure
 *       node &larr; POP(frontier) // chooses the deepest node in the frontier
 *       add node.STATE to explored
 *       for each action in problem.ACTIONS(node.STATE) do
 *           child &larr; CHILD-NODE(problem, node, action)
 *           if child.STATE is not in explored or frontier then
 *               if problem.GOAL-TEST(child.STATE) then return SOLUTION(child)
 *               frontier &larr; INSERT(child, frontier)
 * </pre>
 *
 * <b>Figure ??</b> Depth-first graph search.
 *
 * <p>The depth-first search algorithm is an instance of the graph-search algorithm;
 * whereas breadth-first search uses a FIFO queue, depth-first-search uses a LIFO
 * queue which means that the most recently generated node is chosen for expansion.
 * This must be the deepest unexpanded node when it was selected. The graph-search
 * version avoids repeated states and redundant paths and is complete in finite
 * state spaces because it will eventually expand every node. There is one slight
 * tweak on the general graph-search algorithm, which is that the goal test is
 * applied to each node when it is generated rather than when it is selected for
 * expansion.
 */
public class DepthFirstSearch<A, S> implements SearchForActionsFunction<A, S> {

  @Override
  public List<A> apply(Problem<A, S> problem) {
    Node<A, S> node = newRootNode(problem.initialState(), 0);
    if (isGoalState(node, problem)) {
      return solution(node);
    }
    Queue<Node<A, S>> frontier = newLIFOQueue(node);
    Set<S> explored = newExploredSet();
    while (true) {
      if (frontier.isEmpty()) {
        return failure();
      }
      node = frontier.remove();
      explored.add(node.state());
      for (A action : problem.actions(node.state())) {
        Node<A, S> child = newChildNode(problem, node, action);
        if (!(explored.contains(child.state()) || containsState(frontier, child.state()))) {
          if (isGoalState(child, problem)) {
            return solution(child);
          }
          frontier.add(child);
        }
      }
    }
  }

  /* Supporting code */
  private NodeFactory<A, S> nodeFactory = new BasicNodeFactory<>();
  private SearchController<A, S> searchController = new BasicSearchController<>();

  private Node<A, S> newRootNode(S initialState, double pathCost) {
    return nodeFactory.newRootNode(initialState, pathCost);
  }

  private Node<A, S> newChildNode(Problem<A, S> problem, Node<A, S> node, A action) {
    return nodeFactory.newChildNode(problem, node, action);
  }

  private boolean isGoalState(Node<A, S> node, Problem<A, S> problem) {
    return searchController.isGoalState(node, problem);
  }

  private List<A> solution(Node<A, S> node) {
    return searchController.solution(node);
  }

  private List<A> failure() {
    return searchController.failure();
  }

  private Queue<Node<A,S>> newLIFOQueue(Node<A, S> node) {
    Queue<Node<A, S>> frontier = Collections.asLifoQueue(new LinkedList<>());
    frontier.add(node);
    return frontier;
  }

  private Set<S> newExploredSet() {
    return new HashSet<>();
  }

  private boolean containsState(Queue<Node<A, S>> frontier, S state) {
    // NOTE: Not very efficient (i.e. linear in the size of the frontier)
    return frontier.stream().anyMatch(frontierNode -> frontierNode.state().equals(state));
  }
}
