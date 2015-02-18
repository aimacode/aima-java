package aima.core.search.uninformed;

import aima.core.api.search.Node;
import aima.core.api.search.uninformed.GraphSearch;

import java.util.*;

/**
 * @author Ciaran O'Reilly
 */
public class BasicGraphSearch<S> extends BasicSearchFunction<S> implements GraphSearch<S> {

    @Override
    public Queue<Node<S>> newFrontier() {
        return new FrontierQueue();
    }

    @Override
    public Set<S> newExplored() {
        return new HashSet<>();
    }

    private class FrontierQueue extends LinkedList<Node<S>> {
        Set<S> states = new HashSet<>();

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
            Node<S> result = super.poll();
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
}
