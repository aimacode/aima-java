package aima.core.search.informed;

import aima.core.search.framework.Node;

import java.util.function.ToDoubleFunction;

/**
 * Super class for all evaluation functions which make use of heuristics.
 * Informed search algorithms use heuristics to estimate remaining costs to
 * reach a goal state from a given node. Their evaluation functions only differ
 * in the way how they combine the estimated remaining costs with the costs of
 * the already known path to the node.
 * 
 * @author Ruediger Lunde
 *
 */
public abstract class EvaluationFunction<S, A> implements ToDoubleFunction<Node<S, A>> {
	protected ToDoubleFunction<Node<S, A>> h;

	public EvaluationFunction() {
		this(node -> 0.0);
	}

	public EvaluationFunction(ToDoubleFunction<Node<S, A>> h) {
		this.h = h;
	}

	public ToDoubleFunction<Node<S, A>> getHeuristicFunction() {
		return h;
	}

	// Problem solving agents need to be able to change the heuristic function after formulating a new goal.
	public void setHeuristicFunction(ToDoubleFunction<Node<S, A>> h) {
		this.h = h;
	}
}
