package aima.core.search.basic.uninformed;

import aima.core.search.api.SearchController;
import aima.core.search.api.NodeFactory;
import aima.core.search.api.BidirectionalActions;
import aima.core.search.api.Node;
import aima.core.search.api.Problem;
import aima.core.search.api.SearchForActionsBidirectionallyFunction;
import aima.core.search.basic.support.BasicBidirectionalActions;
import aima.core.search.basic.support.BasicFrontierQueue;
import aima.core.search.basic.support.BasicNodeFactory;
import aima.core.search.basic.support.BasicSearchController;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): Figure ??, page ??.
 * <br>
 * <br>
 *
 * <pre>
 * The strategy of this search implementation is inspired by the description of
 * the bidirectional search algorithm i.e. Problem is reversed and search is performed from both
 * frontiers.
 * @author manthan
 */
public class BidirectionalSearch<A, S> implements SearchForActionsBidirectionallyFunction<A, S> {
    private BidirectionalActions<A> bidirectionalActions;
    private List<A> fromInitialStatePartList = new ArrayList<>();
    private List<A> fromGoalStatePartList = new ArrayList<>();
    private Node<A, S> previousMeetingOfTwoFrontiers;
    private Node<A, S> meetingOfTwoFrontiers;
    private Node<A, S> nextNodeToBeEvaluated;
    private boolean fromFront;
    @Override

    public BidirectionalActions<A> apply(Problem<A, S> originalProblem, Problem<A, S> reverseProblem) {
        Node<A, S> node = newRootNode(originalProblem.initialState(), 0);
        if (originalProblem.isGoalState(node.state())) {
            this.previousMeetingOfTwoFrontiers = node;
            this.nextNodeToBeEvaluated = null;
            bidirectionalActions = new BasicBidirectionalActions<>(fromInitialStatePart(), fromGoalStatePart());
            return bidirectionalActions;
        }
        Node<A, S> revNode = newRootNode(reverseProblem.initialState(), 0);
        Queue<Node<A, S>> front = newFIFOQueue(node);
        Queue<Node<A, S>> back = newFIFOQueue(revNode);
        Map<S, Node<A, S>> exploredFront = newExploredMap(node.state());
        Map<S, Node<A, S>> exploredBack = newExploredMap(revNode.state());

        while (!front.isEmpty() || !back.isEmpty()) {

            if (front.isEmpty() || back.isEmpty()) {
                this.previousMeetingOfTwoFrontiers = null;
                bidirectionalActions = new BasicBidirectionalActions<>(fromInitialStatePart(), fromGoalStatePart());
                return bidirectionalActions;
            }
            // Existence of path is checked from both ends of the problem.
            if (isSolution(pathExistsBidirectional(front, exploredFront, exploredBack, originalProblem), true)) {
                bidirectionalActions = new BasicBidirectionalActions<>(fromInitialStatePart(), fromGoalStatePart());
                return bidirectionalActions;
            }
            if (isSolution(pathExistsBidirectional(back, exploredBack, exploredFront, reverseProblem), false)) {
                bidirectionalActions = new BasicBidirectionalActions<>(fromInitialStatePart(), fromGoalStatePart());
                return bidirectionalActions;
            }
        }
        this.previousMeetingOfTwoFrontiers = null;
        bidirectionalActions = new BasicBidirectionalActions<>(fromInitialStatePart(), fromGoalStatePart());
        return bidirectionalActions;
    }

    private Node<A, S> pathExistsBidirectional(Queue<Node<A, S>> queue, Map<S, Node<A, S>> exploredFront, Map<S, Node<A, S>> exploredBack, Problem<A, S> problem) {
        if (!queue.isEmpty()) {
            Node<A, S> next = queue.remove();
            for (A action : problem.actions(next.state())) {
                Node<A, S> child = newChildNode(problem, next, action);
                if (exploredBack.containsKey(child.state())) {
                    this.previousMeetingOfTwoFrontiers = next;
                    this.nextNodeToBeEvaluated = exploredBack.get(child.state());
                    return child;
                }
                if (!(exploredFront.containsKey(child.state()) || queue.contains(child.state()))) {
                    if (problem.isGoalState(child.state())) {
                        this.previousMeetingOfTwoFrontiers = next;
                        this.nextNodeToBeEvaluated = exploredBack.get(child.state());
                        return child;
                    }
                    exploredFront.put(child.state(), next);
                    queue.add(child);
                }
            }
        }
        this.previousMeetingOfTwoFrontiers = null;
        return null;
    }

    private boolean isSolution(Node<A, S> solutionNode, boolean fromFront) {
        if (solutionNode != null) {
            this.meetingOfTwoFrontiers = solutionNode;
            this.fromFront = fromFront;
            return true;
        }
        return false;
    }
    //
    //Supporting Code
    protected NodeFactory<A, S> nodeFactory = new BasicNodeFactory<>();
    protected SearchController<A, S> searchController = new BasicSearchController<>();

    public Node<A, S> newRootNode(S initialState, double pathCost) {
        return nodeFactory.newRootNode(initialState, pathCost);
    }

    public Node<A, S> newChildNode(Problem<A, S> problem, Node<A, S> node, A action) {
        return nodeFactory.newChildNode(problem, node, action);
    }

    private Queue<Node<A, S>> newFIFOQueue(Node<A, S> initialNode) {
        Queue<Node<A, S>> frontier = new BasicFrontierQueue<>();
        frontier.add(initialNode);
        return frontier;
    }

    private Map<S, Node<A, S>> newExploredMap(S state) {
        Map<S, Node<A, S>> exploredMap = new HashMap<>();
        exploredMap.put(state, null);
        return exploredMap;
    }

    public List<A> solution(Node<A, S> node) {
        return searchController.solution(node);
    }

    public List<A> failure() {
        return searchController.failure();
    }

    public BidirectionalSearch() {

    }

    public Node<A, S> getMeetingOfTwoFrontiers() {
        return this.meetingOfTwoFrontiers;
    }

    private List<A> fromInitialStatePart() {
        if (this.previousMeetingOfTwoFrontiers == null || this.nextNodeToBeEvaluated == null)
            return failure();
        if (this.fromFront) {
            fromInitialStatePartList = searchController.solution(this.previousMeetingOfTwoFrontiers);
            fromInitialStatePartList.add(this.meetingOfTwoFrontiers.action());
            return fromInitialStatePartList;
        } else {
            fromInitialStatePartList = searchController.solution(this.nextNodeToBeEvaluated);
            return fromInitialStatePartList;
        }
    }

    private List<A> fromGoalStatePart() {
        if (this.previousMeetingOfTwoFrontiers == null || this.nextNodeToBeEvaluated == null)
            return failure();
        if (!this.fromFront) {
            fromGoalStatePartList = searchController.solution(this.previousMeetingOfTwoFrontiers);
            fromGoalStatePartList.add(this.meetingOfTwoFrontiers.action());
            return fromGoalStatePartList;
        } else {
            fromGoalStatePartList = searchController.solution(this.nextNodeToBeEvaluated);
            return fromGoalStatePartList;
        }
    }
}
