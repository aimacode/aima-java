package aima.core.search.basic.uninformed;

import aima.core.search.api.BidirectionalProblem;
import aima.core.search.api.NodeFactory;
import aima.core.search.api.SearchController;
import aima.core.search.api.Node;
import aima.core.search.api.Problem;
import aima.core.search.api.SearchForActionsFunction;
import aima.core.search.basic.support.BasicNodeFactory;
import aima.core.search.basic.support.BasicSearchController;

import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.LinkedList;
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
 * Created by manthan on 1/3/17.
 */
public class BidirectionalSearch<A, S> implements SearchForActionsFunction<A, S> {

    @Override
    public List<A> apply(Problem<A, S> problem) {
        Node<A, S> node = newRootNode(problem.initialState(), 0);
        if (problem.isGoalState(node.state())) {
            return solution(node);
        }
        Node<A, S> revNode = newRootNode(((BidirectionalProblem<A, S>) problem).getReverseProblem().initialState(), 0);
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
            solutionNode = pathExistsBidirectional(front, exploredFront, exploredBack, problem);
            if (solutionNode != null) {
                List<A> sol = solution(solutionNode);
                Collections.reverse(sol);
                return sol;
            }
            solutionNode = pathExistsBidirectional(back, exploredBack, exploredFront, problem);
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
                if (!(exploredFront.contains(child.state()) || containsState(queue, child.state()))) {
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
        Queue<Node<A, S>> frontier = new LinkedList<>();
        frontier.add(initialNode);
        return frontier;
    }

    public Set<S> newExploredSet() {
        return new HashSet<>();
    }

    public BidirectionalSearch() {

    }

    public boolean containsState(Queue<Node<A, S>> frontier, S state) {
        return frontier.stream().anyMatch(frontierNode -> frontierNode.state().equals(state));
    }

}
