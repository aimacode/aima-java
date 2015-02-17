package aima.core.search.uninformed;

import aima.core.api.search.Node;
import aima.core.api.search.uninformed.UniformCostSearch;

import java.util.*;

/**
 * @author Ciaran O'Reilly
 */
public class BasicUniformCostSearch<S> extends BasicGraphSearch<S> implements UniformCostSearch<S> {

    @Override
    public Queue<Node<S>> newFrontier() {
        return new PriorityFrontierQueue();
    }

    private class PriorityFrontierQueue extends PriorityQueue<Node<S>> {
        Set<S> states = new HashSet<>();

        PriorityFrontierQueue() {
            super((n1, n2) -> {
                if (n1.pathCost() < n2.pathCost()) {
                    return -1;
                }
                else if (n1.pathCost() > n2.pathCost()) {
                    return 1;
                }
                return 0;
            });
        }

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
