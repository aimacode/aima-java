package aima.core.api.search;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import aima.core.search.api.Node;
import aima.core.search.api.Problem;

/**
 * Description of a Search Function. Provides basic API methods for different specializations
 * (e.g. Tree and Graph search).
 *
 * @param <A> the type of the actions that can be performed.
 * @param <S> the type of the state space
 *
 * @author Ciaran O'Reilly
 */
public interface SearchFunction<A, S> extends Function<Problem<A, S>, List<A>> {

    Node<A, S> newNode(S state);
    Node<A, S> newNode(S state, double pathCost);
    Node<A, S> childNode(Problem<A, S> problem, Node<A, S> parent, A action);

    default boolean isGoalState(Node<A, S> node, Problem<A, S> problem) {
        return problem.isGoalState(node.state());
    }

    default List<A> failure() {
        // represented by an empty list
        return Collections.<A>emptyList();
    }

    /**
     * Default implementation of the <em>SOLUTION</em> function which returns
     * the sequence of actions obtained by following parent pointers back to
     * root.
     *
     * @param node
     *        a goal node.
     * @return the sequence of actions used to go from the root to the given node.
     */
    default List<A> solution(Node<A, S> node) {
        // Use a LinkedList so we can insert into the front efficiently
        LinkedList<A> result = new LinkedList<>();
        if (node.parent() == null) {
            // This should be an Action.NoOp as it implies we are
            // at the goal already, so there is nothing to do
            result.add(node.action());
        }
        else {
            // This loop will skip the root node's action, as
            // we only want to include action from the root
            // and not the default assigned to the root itself
            // (i.e. usually an Action.NoOp)
            while (node.parent() != null) {
                result.addFirst(node.action());
                node = node.parent();
            }
        }

        return result;
    }
}
