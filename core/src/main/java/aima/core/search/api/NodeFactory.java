package aima.core.search.api;

import java.util.Comparator;
import java.util.function.ToDoubleFunction;

/**
 * A Node Factory, used to construct implementations of the Node interface.
 * 
 * @param <A>
 *            the type of the action.
 * @param <S>
 *            the type of the state that node contains.
 *
 * @author Ciaran O'Reilly
 */
public interface NodeFactory<A, S> {
	Node<A, S> newRootNode(S state);

	Node<A, S> newRootNode(S state, double pathCost);

	Node<A, S> newChildNode(Problem<A, S> problem, Node<A, S> parent, A action);

	ToDoubleFunction<Node<A, S>> getNodeCostFunction();

	void setNodeCostFunction(ToDoubleFunction<Node<A, S>> nodeCostFunction);

	default Comparator<Node<A, S>> getNodeComparator() {
		// Default to comparing using path costs if an explicit node cost
		// function isn't provided
		return Comparator.comparingDouble(
				n -> getNodeCostFunction() == null ? n.pathCost() : getNodeCostFunction().applyAsDouble(n));
	}
}