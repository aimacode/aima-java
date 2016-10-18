package aima.core.search.basic.uninformed;

import aima.core.search.api.BidirectionalProblem;
import aima.core.search.api.Node;
import aima.core.search.api.NodeFactory;
import aima.core.search.api.Problem;
import aima.core.search.api.SearchController;
import aima.core.search.basic.support.BasicNodeFactory;
import aima.core.search.basic.support.BasicSearchController;
import aima.core.search.basic.support.StateActionTimeLine;

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

/**
 * find a solution by searching from a start and goals in parallel. When multiple solutions
 * exist, a random one is returned, see unit test arad2oreda. The processes expand states
 * parallel and check each others frontiers if they meet.
 * <p>
 * An optional {@link BiDirectionalBreadthFirstSearch#history} can be used for println debugging
 * but may slow down execution significantly
 *
 * @param <A> actions to take
 * @param <S> states which exist
 */
public class BiDirectionalBreadthFirstSearch<A, S> {

  private NodeFactory<A, S> nodeFactory = new BasicNodeFactory<>();
  private SearchController<A, S> searchController = new BasicSearchController<>();
  private StateActionTimeLine<String, String> history;
  private final double pathCost = 0;

  /**
   * @param biDirectionalProblem to solve
   * @return list of actions to solve the problem
   */
  public List<A> findSolution(BidirectionalProblem<A, S> biDirectionalProblem) throws ExecutionException,
      InterruptedException {

    // build everything needed for the two searches
    Set<S> exploredFromStart = concurrentStateSet();
    Set<S> exploredFromGoal = concurrentStateSet();
    Queue<Node<A, S>> fromStartFrontier = newFIFOQueue();
    Queue<Node<A, S>> fromGoalFrontier = newFIFOQueue();
    final Problem<A, S> originalProblem = biDirectionalProblem.getOriginalProblem();
    final List<Problem<A, S>> reverseProblems = biDirectionalProblem.getReverseProblems();

    final S initialState = originalProblem.initialState();
    Node<A, S> startNode = newRootNode(initialState, pathCost);
    //rememberEvent("addToFrontier: " + startNode.state(), true);
    fromStartFrontier.add(startNode);
    reverseProblems.forEach(problem -> {
      final S state = problem.initialState();
      rememberEvent("addToFrontier: " + state, false);
      fromGoalFrontier.add(newRootNode(state, pathCost));
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
          // the whole work is done here, expand the node, add and remove frontier nodes and check if
          // our parallel searches meet
          while (nextNodeFromFrontier != null) {
            Node<A, S> nodeToExpand = nextNodeFromFrontier;
            Optional<SimpleEntry<Node<A, S>, Node<A, S>>> _solution =
                findMeetingNodes(problem, searchDirection, nodeToExpand);
            if (_solution.isPresent()) {
              return solution(buildResultPath(_solution.get().getKey(), _solution.get().getValue(),
                  problem));
            }
            rememberEvent("remove from frontier " + nodeToExpand.state(), searchDirection.forwardOrBackward);
            searchDirection.frontier.remove(nodeToExpand);
            nextNodeFromFrontier = searchDirection.frontier.peek();
          }
          rememberEvent("abort search, frontier is empty", searchDirection.forwardOrBackward);
          return null;
        }).filter(result -> result != null).findFirst().orElse(failure());

    // limit concurrency to two threads
    ForkJoinPool forkJoinPool = new ForkJoinPool(2);

    // execute search
    return forkJoinPool.submit(task).get();
  }


  private Optional<SimpleEntry<Node<A, S>, Node<A, S>>> findMeetingNodes(Problem<A, S> problem,
                                                                         SearchDirection
      searchDirection, Node<A, S> nodeToExpand) {

    Queue<Node<A, S>> frontier = searchDirection.frontier;
    List<A> actions = problem.actions(nodeToExpand.state());
    Set<S> exploredStates = searchDirection.exploredStates;
    Queue<Node<A, S>> otherFrontier = searchDirection.otherFrontier;

    rememberEvent("expand " + nodeToExpand.state(), searchDirection.forwardOrBackward);
    return actions.parallelStream()
        .map(action -> meetingNodes(exploredStates, frontier, otherFrontier,
            newLeafNode(problem, nodeToExpand, action), searchDirection.forwardOrBackward))
        .filter(o -> o != null)
        .findFirst();
  }

  private void rememberEvent(String action, boolean direction) {
    if (history != null) {
      rememberEvent(direction ? "forward" : "backward", action);
    }
  }

  private void rememberEvent(String state, String action) {
    history.add(state, action);
  }

  /**
   * add a new node to the frontier if the state is not explored
   * @return true when the new node reaches one of the other searchDirections branch
   */
  private SimpleEntry<Node<A, S>, Node<A, S>> meetingNodes(Set<S> exploredStates,
                                                           Queue<Node<A, S>> thisFrontier,
                                                           Queue<Node<A, S>> otherFrontier,
                                                           Node<A, S> leaf,
                                                           boolean direction) {
    if (!exploredStates.contains(leaf.state())) {
      rememberEvent("addToFrontier: " + leaf.state(), direction);
      thisFrontier.add(leaf);
    }
    if (exploredStates.contains(leaf.state())) {
      return null;
    } else {
      exploredStates.add(leaf.state());
    }
    return meetingNodes(leaf, otherFrontier, direction);
  }

  /**
   * @return node with equal state from the other frontier or null if none exists
   */
  private Node<A, S> findNodeWithEqualState(Node<A, S> leaf, Queue<Node<A, S>> otherFrontier,
                                            boolean direction) {
    rememberEvent("search for " + leaf.state() + "in other frontier", direction);
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

  public Node<A, S> newRootNode(S initialState, double pathCost) {
    return nodeFactory.newRootNode(initialState, pathCost);
  }

  private Node<A, S> newLeafNode(Problem<A, S> problem, Node<A, S>
      node, A action) {
    return nodeFactory.newChildNode(problem, node, action);
  }

  public Queue<Node<A, S>> newFIFOQueue() {
    return new ConcurrentLinkedQueue<>();
  }

  private Set<S> concurrentStateSet() {
    return new ConcurrentSkipListSet<S>() {
      @Override
      public Comparator<? super S> comparator() {
        return (Comparator<S>) (o1, o2) -> o1.toString().compareTo(o2.toString());
      }
    };
  }

  public List<A> failure() {
    return searchController.failure();
  }

  public List<A> solution(Node<A, S> child) {
    return searchController.solution(child);
  }

  public void register(StateActionTimeLine<String, String> timeLine) {
    history = timeLine;
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
