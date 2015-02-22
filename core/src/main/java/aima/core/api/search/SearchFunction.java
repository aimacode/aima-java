package aima.core.api.search;

import aima.core.api.agent.Action;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

/**
 * Description of a Search Function. Provides basic API methods for different specializations
 * (e.g. Tree and Graph search).
 *
 * @param <S> the type of the state space
 *
 * @author Ciaran O'Reilly
 */
public interface SearchFunction<S> extends Function<Problem<S>, List<Action>> {

    Node<S> newNode(S state);
    Node<S> newNode(S state, double pathCost);
    Node<S> childNode(Problem<S> problem, Node<S> parent, Action action);

    default List<Action> failure() {
        // represented by an empty list
        return Collections.<Action>emptyList();
    }

    default List<Action> solution(Node<S> node) {
        // Use a LinkedList so we can insert into the front efficiently
        LinkedList<Action> result = new LinkedList<>();
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
