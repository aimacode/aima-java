package aima.core.search.api;

/**
 * A Node Factory, used to construct implementations of the Node interface.
 *  
 * @param <A> the type of the action.
 * @param <S> the type of the state that node contains.
 *
 * @author Ciaran O'Reilly
 */
public interface NodeFactory<A, S> {
    Node<A, S> newRootNode(S state);
    Node<A, S> newRootNode(S state, double pathCost);
    Node<A, S> newChildNode(Problem<A, S> problem, Node<A, S> parent, A action);
}