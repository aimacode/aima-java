package aima.core.search;

import aima.core.api.search.SearchFunction;
import aima.core.search.api.Node;
import aima.core.search.api.Problem;
import aima.core.search.basic.support.BasicNodeFactory;

/**
 * @author Ciaran O'Reilly
 */
public abstract class BasicSearchFunction<A, S> implements SearchFunction<A, S> {
	protected BasicNodeFactory<A, S> nodeFactory = new BasicNodeFactory<>();
	
    @Override
    public Node<A, S> newNode(S state) {
        return nodeFactory.newRootNode(state);
    }

    @Override
    public Node<A, S> newNode(S state, double pathCost) {
        return nodeFactory.newRootNode(state, pathCost);
    }

    @Override
    public Node<A, S> childNode(Problem<A, S> problem, Node<A, S> parent, A action) {
        return nodeFactory.newChildNode(problem, parent, action);
    }
}
