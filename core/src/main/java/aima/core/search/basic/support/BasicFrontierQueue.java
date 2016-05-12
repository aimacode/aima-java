package aima.core.search.basic.support;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import aima.core.search.api.Node;

/**
 * @author Ciaran O'Reilly
 */
public class BasicFrontierQueue<A, S> extends LinkedList<Node<A, S>> {
	private static final long serialVersionUID = 1L;
	//
	private Set<S> states = new HashSet<>();

    // add
    @Override
    public boolean add(Node<A, S> node) {
        if (!states.add(node.state())) {
            throw new IllegalArgumentException("Node's state is already in the frontier: " + node);
        }
        return super.add(node);
    }

    @Override
    public Node<A, S> remove() {
        Node<A, S> result = super.remove();
        if (result != null) {
            states.remove(result.state());
        }
        return result;
    }

    @Override
    public boolean contains(Object o) {
        if (o instanceof Node) {
            return super.contains(o);
        }
        return states.contains(o);
    }
}

