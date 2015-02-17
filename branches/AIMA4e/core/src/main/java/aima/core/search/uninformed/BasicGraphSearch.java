package aima.core.search.uninformed;

import aima.core.api.agent.Action;
import aima.core.api.search.Node;
import aima.core.api.search.Problem;
import aima.core.api.search.uninformed.GraphSearch;
import aima.core.search.BasicNode;

import java.util.*;

/**
 * @author Ciaran O'Reilly
 */
public class BasicGraphSearch<S> implements GraphSearch<S> {
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
        return new FrontierQueue();
    }

    @Override
    public Set<S> newExplored() {
        return new HashSet<>();
    }

    @Override
    public List<Action> solution(Node<S> node) {
        // Use a LinkedList so we can insert into the front efficiently
        LinkedList<Action> result = new LinkedList<>();
        if (node.parent() == null) {
            // This should be an Action.NoOp
            result.add(node.action());
        }
        else {
            // This loop will skip the root node's action, as
            // we only want to include action from the root
            // and not the default assigned to the root
            // (i.e. usually an Action.NoOp)
            while (node.parent() != null) {

                result.addFirst(node.action());
                node = node.parent();
            }
        }


        return result;
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
