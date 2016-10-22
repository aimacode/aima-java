package aima.core.search.basic.support;

import aima.core.search.api.BidirectionalProblem;
import aima.core.search.api.Node;
import aima.core.search.api.NodeFactory;
import aima.core.search.api.Problem;
import aima.core.search.api.SearchController;

import java.util.AbstractMap.SimpleEntry;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.function.ToDoubleFunction;

/**
 * solves a {@link aima.core.search.api.BidirectionalProblem} with different {@link Strategy}
 * @author wormi
 */
public class BidirectionalSearch<A, S> {

  private final String forwardStrategy;
  private final String backwardStrategy;
  private final ToDoubleFunction<Node<A, S>> forwardHeuristicFunction;
  private final ToDoubleFunction<Node<A, S>> backwardHeuristicFunction;
  private NodeFactory<A, S> nodeFactory = new BasicNodeFactory<>();
  private SearchController<A, S> searchController = new BasicSearchController<>();

  public BidirectionalSearch(String forwardStrategy, String backwardStrategy,
                             ToDoubleFunction<Node<A, S>> forwardHeuristicFunction,
                             ToDoubleFunction<Node<A, S>> backwardHeuristicFunction
  ) {
    this.forwardStrategy = forwardStrategy;
    this.backwardStrategy = backwardStrategy;
    this.forwardHeuristicFunction = forwardHeuristicFunction;
    this.backwardHeuristicFunction = backwardHeuristicFunction;
  }

  public List<A> findSolution(BidirectionalProblem<A, S> bidirectionalProblem)
      throws ExecutionException, InterruptedException {
    // build everything needed for the two searches
    Set<S> exploredFromStart = concurrentStateSet();
    Set<S> exploredFromGoal = concurrentStateSet();
    Queue<Node<A, S>> fromStartFrontier = getQueue(forwardStrategy, forwardHeuristicFunction);
    Queue<Node<A, S>> fromGoalFrontier = getQueue(backwardStrategy, backwardHeuristicFunction);
    final Problem<A, S> originalProblem = bidirectionalProblem.getOriginalProblem();
    final List<Problem<A, S>> reverseProblems = bidirectionalProblem.getReverseProblems();

    final S initialState = originalProblem.initialState();
    Node<A, S> startNode = newRootNode(initialState, 0);
    fromStartFrontier.add(startNode);
    reverseProblems.forEach(problem -> {
      final S state = problem.initialState();
      fromGoalFrontier.add(newRootNode(state, 0));
    });

    // build Searches
    List<SearchDirection> searches = new LinkedList<>();
    searches.add(new SearchDirection(exploredFromStart, fromGoalFrontier, fromStartFrontier,
        true, originalProblem));
    searches.add(new SearchDirection(exploredFromGoal, fromStartFrontier,
        fromGoalFrontier, false, reverseProblems.get(0)));


    // create runner
    Callable<List<A>> task = () ->
        searches.stream().parallel().map(searchDirection -> {
          final Problem<A, S> problem = searchDirection.problem;
          Node<A, S> nextNodeFromFrontier = searchDirection.frontier.peek();
          // initial check
          if (problem.isGoalState(nextNodeFromFrontier.state())) {
            return solution(nextNodeFromFrontier);
          }
          // the whole work is done here, expand the node, add and remove frontier nodes and check
          // if our parallel searches meet and calculate the resulting path
          while (nextNodeFromFrontier != null) {
            Node<A, S> nodeToExpand = nextNodeFromFrontier;
            Optional<SimpleEntry<Node<A, S>, Node<A, S>>> _solution =
                findMeetingNodes(problem, searchDirection, nodeToExpand);
            if (_solution.isPresent()) {
              return solution(buildResultPath(_solution.get().getKey(), _solution.get().getValue(),
                  problem));
            }
            searchDirection.frontier.remove(nodeToExpand);
            nextNodeFromFrontier = searchDirection.frontier.peek();
          }
          return null;
        }).filter(result -> result != null).findFirst().orElse(failure());

    // limit concurrency to two threads
    ForkJoinPool forkJoinPool = new ForkJoinPool(2);

    // execute search
    return forkJoinPool.submit(task).get();

  }

  private Set<S> concurrentStateSet() {
    return new ConcurrentSkipListSet<S>() {
      @Override
      public Comparator<? super S> comparator() {
        return (Comparator<S>) (o1, o2) -> o1.toString().compareTo(o2.toString());
      }
    };
  }

  public Queue<Node<A, S>> getQueue(String strategy, ToDoubleFunction<Node<A, S>> h) {
    if (Strategy.BFS.equals(strategy)) {
      return newFIFOQueue();
    }
    int initialCapacity = 10;
    if (Strategy.UCS.equals(strategy)) {
      return new PriorityBlockingQueue<>(initialCapacity, (o1, o2) -> Double.compare(o1.pathCost
          (), o2.pathCost()));
    }
    if (Strategy.ASTAR.equals(strategy)) {
      if (h == null) {
        throw new IllegalArgumentException("A* needs a heuristic");
      }
      return new PriorityBlockingQueue<>(initialCapacity, (o1, o2) ->
          Double.compare(o1.pathCost() + h.applyAsDouble(o1), o2.pathCost() + h.applyAsDouble(o2)));
    }
    throw new IllegalArgumentException("did not recognize strategy");
  }

  public Node<A, S> newRootNode(S initialState, double pathCost) {
    return nodeFactory.newRootNode(initialState, pathCost);
  }

  public List<A> failure() {
    return searchController.failure();
  }

  public List<A> solution(Node<A, S> child) {
    return searchController.solution(child);
  }

  private Node<A, S> findNodeWithEqualState(Node<A, S> leaf, Queue<Node<A, S>> otherFrontier,
                                            boolean direction) {
    return otherFrontier.parallelStream()
        .filter(node -> node.state().equals(leaf.state()))
        .findFirst()
        .orElse(null);
  }

  private SimpleEntry<Node<A, S>, Node<A, S>> meetingNodes(Node<A, S> leaf, Queue<Node<A, S>>
      otherFrontier, boolean forward) {
    Node<A, S> nodeWithEqualState = findNodeWithEqualState(leaf, otherFrontier, forward);
    boolean nodesMeet = nodeWithEqualState != null;
    if (nodesMeet) {
      if (forward) {
        return new SimpleEntry<>(leaf, nodeWithEqualState);
      } else {
        return new SimpleEntry<>(nodeWithEqualState, leaf);
      }
    }
    return null;
  }

  /**
   * merge the startNode with the one originating from the goal by
   * applying the necessary actions to get one node to the root state of the other
   */
  public Node<A, S> buildResultPath(Node<A, S> fromStartNode, Node<A, S> fromGoalNode,
                                    Problem<A, S> problem) {

    if (!fromGoalNode.state().equals(fromStartNode.state())) {
      throw new UnsupportedOperationException("can build result path from same state only");
    }

    Node<A, S> result = fromStartNode;
    Node<A, S> nextNode = fromGoalNode.parent();
    while (nextNode != null) {
      S state = result.state();
      S intendedState = nextNode.state();
      A actionFromStateToIntendedState = problem.actions(state).parallelStream()
          .filter(action -> intendedState.equals(problem.result(state, action))).findFirst()
          .orElseThrow(() -> new NullPointerException("can't happen if we assume that nodes are " +
              "bidirectional linked")
          );
      result = newLeafNode(problem, result, actionFromStateToIntendedState);
      nextNode = nextNode.parent();
    }
    return result;
  }

  private Optional<SimpleEntry<Node<A, S>, Node<A, S>>> findMeetingNodes(Problem<A, S> problem,
                                                                         SearchDirection searchDirection,
                                                                         Node<A, S> nodeToExpand) {
    Queue<Node<A, S>> frontier = searchDirection.frontier;
    List<A> actions = problem.actions(nodeToExpand.state());
    Set<S> exploredStates = searchDirection.exploredStates;
    Queue<Node<A, S>> otherFrontier = searchDirection.otherFrontier;

    return actions.parallelStream()
        .map(action -> meetingNodes(exploredStates, frontier, otherFrontier,
            newLeafNode(problem, nodeToExpand, action), searchDirection.forwardOrBackward))
        .filter(o -> o != null)
        .findFirst();
  }

  /**
   * add a new node to the frontier if the state is not explored
   *
   * @return true when the new node reaches one of the other searchDirections branch
   */
  private SimpleEntry<Node<A, S>, Node<A, S>> meetingNodes(Set<S> exploredStates,
                                                           Queue<Node<A, S>> thisFrontier,
                                                           Queue<Node<A, S>> otherFrontier,
                                                           Node<A, S> leaf,
                                                           boolean direction) {
    if (!exploredStates.contains(leaf.state())) {
      thisFrontier.add(leaf);
    }
    if (exploredStates.contains(leaf.state())) {
      return null;
    } else {
      exploredStates.add(leaf.state());
    }
    return meetingNodes(leaf, otherFrontier, direction);
  }

  private Node<A, S> newLeafNode(Problem<A, S> problem, Node<A, S>
      node, A action) {
    return nodeFactory.newChildNode(problem, node, action);
  }

  public Queue<Node<A, S>> newFIFOQueue() {
    return new ConcurrentLinkedQueue<>();
  }


  public static class Strategy {
    public static final String BFS = "breadth first search";
    public static final String UCS = "uniform cost search";
    public static String ASTAR = "A*";
  }

  private class SearchDirection {
    final Set<S> exploredStates;
    final Queue<Node<A, S>> otherFrontier;
    final Queue<Node<A, S>> frontier;
    final boolean forwardOrBackward;
    final Problem<A, S> problem;

    /**
     * @param exploredStates    remembers all explored states from this direction
     * @param otherFrontier     frontierNodes of the other direction to check against
     * @param frontier          our frontier
     * @param forwardOrBackward indicator if a solution path needs to be reversed
     * @param problem           to solve
     */
    SearchDirection(Set<S> exploredStates, Queue<Node<A, S>> otherFrontier, Queue<Node<A, S>>
        frontier, boolean forwardOrBackward, Problem<A, S> problem) {
      this.exploredStates = exploredStates;
      this.otherFrontier = otherFrontier;
      this.frontier = frontier;
      this.forwardOrBackward = forwardOrBackward;
      this.problem = problem;
    }
  }
}
