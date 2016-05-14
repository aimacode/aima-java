package aima.core.search.basic.support;

import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

import aima.core.search.api.Node;

/**
 * NOTE: 'The data structure for frontier needs to support efficient membership testing, 
 * so it should combine the capabilities of a priority queue and a hash table.'
 * 
 * @author Ciaran O'Reilly
 */
public class BasicPriorityFrontierQueue<A, S> extends PriorityQueue<Node<A, S>> {
	private static final long serialVersionUID = 1L;
	//
	private Set<S> states = new HashSet<>();

    public BasicPriorityFrontierQueue(Comparator<Node<A, S>> comparator) {
        super(comparator);
    }

    @Override
    public boolean add(Node<A, S> node) {
        if (!states.add(node.state())) {
            throw new IllegalArgumentException("Node's state is already in the frontier: " + node);
        }
        return super.add(node);
    }

    @Override
    public Node<A, S> remove() {
        Node<A, S> result = super.poll();
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
