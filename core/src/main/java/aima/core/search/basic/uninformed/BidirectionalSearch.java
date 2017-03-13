package aima.core.search.basic.uninformed;

import aima.core.search.api.*;
import aima.core.search.basic.support.BasicFrontierQueue;
import aima.core.search.basic.support.BasicNodeFactory;
import aima.core.search.basic.support.BasicSearchController;
import aima.core.util.datastructure.Pair;

import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.HashSet;

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

    @Override
    public List<A> apply(Problem<A, S> originalProblem, Problem<A, S> reverseProblem) {
        Node<A, S> node = newRootNode(originalProblem.initialState(), 0);
        if (originalProblem.isGoalState(node.state())) {
            return solution(node);
        }
        Node<A, S> revNode = newRootNode(reverseProblem.initialState(), 0);
        Queue<Node<A, S>> front = newFIFOQueue(node);
        Queue<Node<A, S>> back = newFIFOQueue(revNode);
        Set<S> exploredFront = newExploredSet();
        Set<S> exploredBack = newExploredSet();

        exploredFront.add(node.state());
        exploredBack.add(revNode.state());
        Node<A, S> solutionNode;
        while (!front.isEmpty() || !back.isEmpty()) {

            if (front.isEmpty() || back.isEmpty()) {
                return failure();
            }
            // Existence of path is checked from both ends of the problem.
            solutionNode = pathExistsBidirectional(front, exploredFront, exploredBack, originalProblem);
            if (solutionNode != null) {
                List<A> sol = solution(solutionNode);
                Collections.reverse(sol);
                return sol;
            }
            solutionNode = pathExistsBidirectional(back, exploredBack, exploredFront, originalProblem);
            if (solutionNode != null) {
                List<A> sol = solution(solutionNode);
                Collections.reverse(sol);
                return sol;
            }
        }
        return failure();
    }

    public Node<A, S> pathExistsBidirectional(Queue<Node<A, S>> queue, Set<S> exploredFront, Set<S> exploredBack, Problem<A, S> problem) {
        if (!queue.isEmpty()) {
            Node<A, S> next = queue.remove();
            for (A action : problem.actions(next.state())) {
                Node<A, S> child = newChildNode(problem, next, action);
                if (exploredBack.contains(child.state())) {
                    return child;
                }
                if (!(exploredFront.contains(child.state()) || queue.contains(child.state()))) {
                    if (problem.isGoalState(child.state())) {
                        return child;
                    }
                    exploredFront.add(child.state());
                    queue.add(child);
                }
            }
        }
        return null;
    }
    //
    //Supporting Code
    protected NodeFactory<A, S> nodeFactory = new BasicNodeFactory<>();
    protected SearchController<A, S> searchController = new BasicSearchController<A, S>();

    public Node<A, S> newRootNode(S initialState, double pathCost) {
        return nodeFactory.newRootNode(initialState, pathCost);
    }

    public Node<A, S> newChildNode(Problem<A, S> problem, Node<A, S> node, A action) {
        return nodeFactory.newChildNode(problem, node, action);
    }

    public List<A> solution(Node<A, S> node) {
        return searchController.solution(node);
    }

    public List<A> failure() {
        return searchController.failure();
    }

    public Queue<Node<A, S>> newFIFOQueue(Node<A, S> initialNode) {
        Queue<Node<A, S>> frontier = new BasicFrontierQueue<>();
        frontier.add(initialNode);
        return frontier;
    }

    public Set<S> newExploredSet() {
        return new HashSet<>();
    }

    public BidirectionalSearch() {

    }
}
