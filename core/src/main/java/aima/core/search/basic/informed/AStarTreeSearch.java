package aima.core.search.basic.informed;

import java.util.function.ToDoubleFunction;

import aima.core.search.api.Node;
import aima.core.search.api.NodeFactory;
import aima.core.search.basic.support.BasicNodeFactory;

/**
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 * @author Ruediger Lunde
 * @author Mike Stampone
 */
public class AStarTreeSearch<A, S> extends BestFirstTreeSearch<A, S> {

	private ToDoubleFunction<Node<A, S>> h;
	
	public AStarTreeSearch(ToDoubleFunction<Node<A, S>> h) {
		this(new BasicNodeFactory<>(), h);	
	}
	
	public AStarTreeSearch(NodeFactory<A, S> nodeFactory, ToDoubleFunction<Node<A, S>> h) {
		super(nodeFactory, (node) -> node.pathCost()+h.applyAsDouble(node));
		this.h = h;
	}
	
	public ToDoubleFunction<Node<A, S>> getHeuristicFunctionH() {
		return h;
	}
}