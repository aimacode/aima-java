package aima.core.search;

import aima.core.api.search.Node;
import aima.core.api.search.Problem;
import aima.core.api.search.SearchFunction;

/**
 * @author Ciaran O'Reilly
 */
public abstract class BasicSearchFunction<A, S> implements SearchFunction<A, S> {
    @Override
    public Node<A, S> newNode(S state) {
        return BasicNode.rootNode(state);
    }

    @Override
    public Node<A, S> newNode(S state, double pathCost) {
        return BasicNode.rootNode(state, pathCost);
    }

    @Override
    public Node<A, S> childNode(Problem<A, S> problem, Node<A, S> parent, A action) {
        return BasicNode.childNode(problem, parent, action);
    }
}
