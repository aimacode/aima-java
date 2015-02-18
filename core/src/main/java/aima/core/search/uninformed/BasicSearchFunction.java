package aima.core.search.uninformed;

import aima.core.api.agent.Action;
import aima.core.api.search.Node;
import aima.core.api.search.Problem;
import aima.core.api.search.uninformed.SearchFunction;
import aima.core.search.BasicNode;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Ciaran O'Reilly
 */
public abstract class BasicSearchFunction<S> implements SearchFunction<S> {
    @Override
    public Node<S> newNode(S state, double pathCost) {
        return BasicNode.rootNode(state, pathCost);
    }

    @Override
    public Node<S> childNode(Problem<S> problem, Node<S> parent, Action action) {
        return BasicNode.childNode(problem, parent, action);
    }

    @Override
    public Queue<Node<S>> newFrontier() {
        return new LinkedList<>();
    }
}
