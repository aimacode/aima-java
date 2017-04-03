package aima.extra.search.uninformed;

import aima.core.search.api.SearchController;
import aima.core.search.api.NodeFactory;
import aima.core.search.api.BidirectionalSearchResult;
import aima.core.search.api.Node;
import aima.core.search.api.Problem;
import aima.core.search.api.SearchForActionsBidirectionallyFunction;
import aima.core.search.basic.support.BasicBidirectionalSearchResult;
import aima.core.search.basic.support.BasicFrontierQueue;
import aima.core.search.basic.support.BasicNodeFactory;
import aima.core.search.basic.support.BasicSearchController;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Objects;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): Figure ??, page ??.
 * <br>
 * <br>
 * <p>
 * <pre>
 * The strategy of this search implementation is inspired by the description of
 * the bidirectional search algorithm i.e. Problem is reversed and search is performed from both
 * frontiers.
 * @author manthan
 */
public class BidirectionalSearchMRS<A, S> implements SearchForActionsBidirectionallyFunction<A, S> {
    private List<A> fromInitialStatePartList = new ArrayList<>();
    private List<A> fromGoalStatePartList = new ArrayList<>();
    private Node<A, S> previousMeetingOfTwoFrontiers;
    private Node<A, S> meetingOfTwoFrontiers;
    private Node<A, S> nextNodeToBeEvaluated;
    private boolean fromFront;
    private Problem<A, S> originalProblem;
    private Problem<A, S> reverseProblem;

    @Override

    public BidirectionalSearchResult<A> apply(Problem<A, S> originalProblem, Problem<A, S> reverseProblem) {
        this.originalProblem = originalProblem;
        this.reverseProblem = reverseProblem;
        Node<A, S> node = newRootNode(originalProblem.initialState(), 0);
        if (originalProblem.isGoalState(node.state())) {
            return new BasicBidirectionalSearchResult<>();
        }
        Node<A, S> revNode = newRootNode(reverseProblem.initialState(), 0);
        Queue<Node<A, S>> front = newFIFOQueue(node);
        Queue<Node<A, S>> back = newFIFOQueue(revNode);
        Map<S, Node<A, S>> exploredFront = newExploredMap(node.state());
        Map<S, Node<A, S>> exploredBack = newExploredMap(revNode.state());

        while (!front.isEmpty() && !back.isEmpty()) {

            // Existence of path is checked from both ends of the problem.
            if (isSolution(pathExistsBidirectional(front, exploredFront, exploredBack, originalProblem), true)) {
                return result();
            }
            if (isSolution(pathExistsBidirectional(back, exploredBack, exploredFront, reverseProblem), false)) {
                return result();
            }
        }
        this.previousMeetingOfTwoFrontiers = null;
        return result();
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

    public BidirectionalSearchMRS() {

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
            if (this.nextNodeToBeEvaluated.state().equals(originalProblem.initialState()))
                return failure();
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
            if (this.nextNodeToBeEvaluated.state().equals(reverseProblem.initialState()))
                return failure();
            return fromGoalStatePartList;
        }
    }

    private List<A> fromMeetingStateToInitialState() {
        if (this.previousMeetingOfTwoFrontiers == null || this.nextNodeToBeEvaluated == null)
            return failure();
        if (this.nextNodeToBeEvaluated.state().equals(originalProblem.initialState())) {
            return buildPathForAdjacentNodes(originalProblem, this.nextNodeToBeEvaluated);
        }
        if (!this.fromFront) {
            return buildPath(this.nextNodeToBeEvaluated);
        } else {
            return buildPath(this.meetingOfTwoFrontiers);
        }
    }

    private List<A> fromMeetingStateToGoalState() {
        if (this.previousMeetingOfTwoFrontiers == null || this.nextNodeToBeEvaluated == null)
            return failure();
        if (this.nextNodeToBeEvaluated.state().equals(reverseProblem.initialState())) {
            return buildPathForAdjacentNodes(reverseProblem, this.nextNodeToBeEvaluated);
        }
        if (this.fromFront) {
            return buildPath(this.nextNodeToBeEvaluated);
        } else {
            return buildPath(this.meetingOfTwoFrontiers);
        }
    }

    private List<A> buildPathForAdjacentNodes(Problem<A, S> problem, Node<A, S> adjacentNode) {
        LinkedList<A> result = new LinkedList<>();
        List<A> actions = problem.actions(this.meetingOfTwoFrontiers.state());
        double pathCost = Double.POSITIVE_INFINITY;
        for (A action : actions) {
            Node<A, S> child = newChildNode(problem, this.meetingOfTwoFrontiers, action);
            if (adjacentNode.state().equals(child.state()) && child.pathCost() < pathCost) {
                result.add(action);
                pathCost = child.pathCost();
            }
        }
        return result;
    }

    //building path from node to initial or goal
    private List<A> buildPath(Node<A, S> node) {
        LinkedList<A> result = new LinkedList<>();
        result.add(node.action());
        List<A> actions;
        while (node.parent() != null) {
            actions = originalProblem.actions(node.state());
            double pathCost = Double.POSITIVE_INFINITY;
            for (A action : actions) {
                Node<A, S> child = newChildNode(originalProblem, node, action);
                if (node.parent().state().equals(child.state()) && child.pathCost() < pathCost) {
                    result.add(action);
                    pathCost = child.pathCost();
                }
            }
            node = node.parent();
        }
        return result;
    }

    private BasicBidirectionalSearchResult<A> result() {
        BasicBidirectionalSearchResult<A> bidirectionalSearchResult = new BasicBidirectionalSearchResult<>();
        List<A> fromInitialStateToMeeting = fromInitialStatePart();
        List<A> fromGoalStateToMeeting = fromGoalStatePart();
        List<A> fromInitialStateToGoalState = new ArrayList<>();
        List<A> fromGoalStateToInitialState = new ArrayList<>();

        //if initial node and goal node are adjacent
        if (this.previousMeetingOfTwoFrontiers.state().equals(originalProblem.initialState()) && this.meetingOfTwoFrontiers.state().equals(reverseProblem.initialState())) {
            fromInitialStateToGoalState.add(this.meetingOfTwoFrontiers.action());
            fromGoalStateToInitialState.addAll(buildPathForAdjacentNodes(originalProblem, this.previousMeetingOfTwoFrontiers));
        } else if (this.previousMeetingOfTwoFrontiers.state().equals(reverseProblem.initialState()) && this.meetingOfTwoFrontiers.state().equals(originalProblem.initialState())) {
            fromInitialStateToGoalState.addAll(buildPathForAdjacentNodes(reverseProblem, this.previousMeetingOfTwoFrontiers));
            fromGoalStateToInitialState.add(this.meetingOfTwoFrontiers.action());
        } else {
            if (fromInitialStateToMeeting.size() > 0) {
                fromInitialStateToMeeting.removeIf(Objects::isNull);
                fromInitialStateToGoalState.addAll(fromInitialStateToMeeting);
            }
            fromInitialStateToGoalState.addAll(fromMeetingStateToGoalState());

            if (fromGoalStateToMeeting.size() > 0) {
                fromGoalStateToMeeting.removeIf(Objects::isNull);
                fromGoalStateToInitialState.addAll(fromGoalStateToMeeting);
            }
            fromGoalStateToInitialState.addAll(fromMeetingStateToInitialState());
        }
        bidirectionalSearchResult.setFromInitialStateToGoalState(fromInitialStateToGoalState);
        bidirectionalSearchResult.setFromGoalStateToInitialState(fromGoalStateToInitialState);
        bidirectionalSearchResult.setFromInitialStateToMeeting(fromInitialStateToMeeting);
        bidirectionalSearchResult.setFromGoalStateToMeeting(fromGoalStateToMeeting);
        return bidirectionalSearchResult;
    }
}

