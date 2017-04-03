package aima.extra.search.uninformed;

import aima.core.search.api.BidirectionalSearchResult;
import aima.core.search.api.NodeFactory;
import aima.core.search.api.Node;
import aima.core.search.api.Problem;
import aima.core.search.api.SearchForActionsBidirectionallyFunction;
import aima.core.search.basic.support.BasicBidirectionalSearchResult;
import aima.core.search.basic.support.BasicFrontierQueue;
import aima.core.search.basic.support.BasicNodeFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): Figure ??, page ??.
 * <br>
 * <br>
 *
 * <pre>
 * The strategy of this search implementation is inspired by the description of
 * the bidirectional search algorithm i.e. Problem is reversed and search is performed from both
 * frontiers.
 * 
 * @author wormi
 */
public class BidirectionalSearchGW<A, S> implements SearchForActionsBidirectionallyFunction<A, S> {


    @Override
    public BidirectionalSearchResult<A> apply(Problem<A, S> originalProblem,
                                              Problem<A, S> reverseProblem) {

        if (originalProblem.isGoalState(originalProblem.initialState())) {
            return new BasicBidirectionalSearchResult<>();
        }

        Node<A, S> node = newRootNode(originalProblem.initialState(), 0);
        Node<A, S> goalNode = newRootNode(reverseProblem.initialState(), 0);
        Queue<Node<A, S>> front = newFIFOQueue(node);
        Queue<Node<A, S>> back = newFIFOQueue(goalNode);
        Set<S> exploredFront = newExploredSet(node.state());
        Set<S> exploredBack = newExploredSet(goalNode.state());

        while (!front.isEmpty() || !back.isEmpty()) {

            Node<A, S> next = front.poll();
            //expand node from front
            List<A> actions = originalProblem.actions(next.state());
            for (A action : actions) {
                final Node<A, S> child = newChildNode(originalProblem, next, action);
                if (exploredBack.contains(child.state())) {
                    return buildResult(child, back, originalProblem, false);
                }
                if (!exploredFront.contains(child.state())) {
                    exploredFront.add(child.state());
                    front.add(child);
                }
            }

            next = back.poll();
            //expand node from back
            actions = reverseProblem.actions(next.state());
            for (A action : actions) {
                final Node<A, S> child = newChildNode(reverseProblem, next, action);
                if (exploredFront.contains(child.state())) {
                    return buildResult(child, front, reverseProblem, true);
                }
                if (!exploredBack.contains(child.state())) {
                    exploredBack.add(child.state());
                    back.add(child);
                }
            }
        }
        return null;
    }

    /**
     * merge the startNode with the one originating from the goal by
     * applying the necessary actions to get one node to the root state of the other
     */
    public BasicBidirectionalSearchResult<A> buildResult(Node<A, S> node,
                                                         Queue<Node<A, S>> otherFront,
                                                         Problem<A,S> problem, boolean forward) {

        Node<A, S> meetingNode = null;
        for (Node<A, S> n : otherFront) {
            if (n.state().equals(node.state())) {
                meetingNode = n;
                break;
            }
            // handle overreaching fronts
            final Node<A, S> parent = n.parent();
            if (parent != null && parent.state().equals(node.state())) {
                meetingNode = parent;
                break;
            }
        }

        if (meetingNode == null) {
            throw new RuntimeException();
        }

        Node<A, S> fromStartNode;
        Node<A, S> fromGoalNode;
        BasicBidirectionalSearchResult<A> result = new BasicBidirectionalSearchResult<>();

        if (!forward) {
            fromStartNode = node;
            fromGoalNode = meetingNode;
        } else {
            fromStartNode = meetingNode;
            fromGoalNode = node;
        }

        result.setFromInitialStateToMeeting(extractActionList(fromStartNode));
        result.setFromGoalStateToMeeting(extractActionList(fromGoalNode));

        fromGoalNode = fromGoalNode.parent();
        while (fromGoalNode != null) {
            S state = fromStartNode.state();
            S intendedState = fromGoalNode.state();
            A actionFromStateToIntendedState = problem.actions(state).parallelStream()
                .filter(action -> intendedState.equals(problem.result(state, action))).findFirst()
                .orElseThrow(() -> new NullPointerException("can't happen if we assume that " +
                    "nodes are bidirectional linked")
                );
            fromStartNode = newChildNode(problem, fromStartNode, actionFromStateToIntendedState);
            fromGoalNode = fromGoalNode.parent();
        }
        result.setFromInitialStateToGoalState(extractActionList(fromStartNode));

        fromGoalNode = newRootNode(fromStartNode.state(), 0);
        fromStartNode = fromStartNode.parent();
        while (fromStartNode != null) {
            S state = fromGoalNode.state();
            S intendedState = fromStartNode.state();
            A actionFromStateToIntendedState = problem.actions(state).parallelStream()
                .filter(action -> intendedState.equals(problem.result(state, action))).findFirst()
                .orElseThrow(() -> new NullPointerException("can't happen if we assume that " +
                    "nodes are bidirectional linked")
                );
            fromGoalNode = newChildNode(problem, fromGoalNode, actionFromStateToIntendedState);
            fromStartNode = fromStartNode.parent();
        }
        result.setFromGoalStateToInitialState(extractActionList(fromGoalNode));
        return result;
    }

    private List<A> extractActionList(Node<A, S> fullNode) {
        List<A> actions = new ArrayList<A>();
        Node<A, S> n = fullNode;
        actions.add(n.action());
        while (n.parent() != null) {
            n = n.parent();
            if (n.action() != null) {
                actions.add(n.action());
            }
        }
        Collections.reverse(actions);
        return actions;
    }

    //Supporting Code
    protected NodeFactory<A, S> nodeFactory = new BasicNodeFactory<>();

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

    private Set<S> newExploredSet(S state) {
        Set<S> set = new HashSet<>();
        set.add(state);
        return set;
    }
}
