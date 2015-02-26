package aima.core.search.support;

import aima.core.api.search.Node;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * @author Ciaran O'Reilly
 */
public class BasicFrontierQueue<S> extends LinkedList<Node<S>> {
    private Set<S> states = new HashSet<>();

    // add
    @Override
    public boolean add(Node<S> node) {
        if (!states.add(node.state())) {
            throw new IllegalArgumentException("Node's state is already in the frontier: " + node);
        }
        return super.add(node);
    }

    @Override
    public Node<S> remove() {
        Node<S> result = super.remove();
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

