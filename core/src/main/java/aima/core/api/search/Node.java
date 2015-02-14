package aima.core.api.search;

import aima.core.api.agent.Action;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): Figure ??, page ??.<br>
 *
 * Figure ?? Nodes are the data structures from which the search tree is
 * constructed. Each has a parent, a state, and various bookkeeping fields.
 * Arrows point from child to parent.<br>
 * <br>
 * Search algorithms require a data structure to keep track of the search tree
 * that is being constructed. For each node n of the tree, we have a structure
 * that contains four components:
 * <ul>
 * <li>n.STATE: the state in the state space to which the node corresponds;</li>
 * <li>n.PARENT: the node in the search tree that generated this node;</li>
 * <li>n.ACTION: the action that was applied to the parent to generate the node;
 * </li>
 * <li>n.PATH-COST: the cost, traditionally denoted by g(n), of the path from
 * the initial state to the node, as indicated by the parent pointers.</li>
 * </ul>
 *
 * @param <S> the type of the state that node contains.
 *
 * @author Ciaran O'Reilly
 */
public interface Node<S> {
    S state();
    Node<S> parent();
    Action action();
    double pathCost();
}
