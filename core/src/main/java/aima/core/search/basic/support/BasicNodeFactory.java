package aima.core.search.basic.support;

import java.util.function.ToDoubleFunction;

import aima.core.search.api.Node;
import aima.core.search.api.NodeFactory;
import aima.core.search.api.Problem;

/**
 * Basic implementation of the NodeFactory interface.
 *
 * @author Ciaran O'Reilly
 */
public class BasicNodeFactory<A, S> implements NodeFactory<A, S> {
	private ToDoubleFunction<Node<A, S>> nodeCostFunction;

	@Override
	public Node<A, S> newRootNode(S state) {
		return newRootNode(state, 0);
	}

	@Override
	public Node<A, S> newRootNode(S state, double pathCost) {
		return new BasicNode<>(state, pathCost);
	}

	/**
	 * Take a parent node and an action and return the resulting child node.
	 *
	 * @param problem
	 *            the problem for which the child node is to be instantiated,
	 *            contains the function RESULT(s, a) that returns the state that
	 *            results from doing action a in state s, as well as the step
	 *            cost function for going from state s to s' using action a.
	 * @param parent
	 *            the parent node of the child.
	 * @param action
	 *            the action taken in the parent node, which results in the
	 *            child node.
	 * @param <A>
	 *            the action type.
	 * @param <S>
	 *            the type of the state that node contains.
	 * @return the resulting child node.
	 */
	@Override
	public Node<A, S> newChildNode(Problem<A, S> problem, Node<A, S> parent, A action) {
		S sPrime = problem.result(parent.state(), action);
		return new BasicNode<>(sPrime, parent, action,
				parent.pathCost() + problem.stepCost(parent.state(), action, sPrime));
	}

	@Override
	public ToDoubleFunction<Node<A, S>> getNodeCostFunction() {
		if (nodeCostFunction == null) {
			// If not provided default to using the node's path cost.
			nodeCostFunction = n -> n.pathCost();
		}
		return nodeCostFunction;
	}

	@Override
	public void setNodeCostFunction(ToDoubleFunction<Node<A, S>> nodeCostFunction) {
		this.nodeCostFunction = nodeCostFunction;
	}
}
