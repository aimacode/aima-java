package aima.core.search.basic.informed;

import java.util.function.ToDoubleFunction;

import aima.core.search.api.Node;
import aima.core.search.api.NodeFactory;
import aima.core.search.api.SearchController;
import aima.core.search.basic.support.BasicNodeFactory;
import aima.core.search.basic.support.BasicSearchController;

/**
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 * @author Ruediger Lunde
 * @author Mike Stampone
 */
public class AStarTreeSearch<A, S> extends BestFirstTreeSearch<A, S> {

	private ToDoubleFunction<Node<A, S>> h;
	
	public AStarTreeSearch(ToDoubleFunction<Node<A, S>> h) {
		this(new BasicSearchController<>(), new BasicNodeFactory<>(), h);	
	}
	
	public AStarTreeSearch(SearchController<A, S> searchController, NodeFactory<A, S> nodeFactory, ToDoubleFunction<Node<A, S>> h) {
		super(searchController, nodeFactory, (node) -> node.pathCost()+h.applyAsDouble(node));
		this.h = h;
	}
	
	public ToDoubleFunction<Node<A, S>> getHeuristicFunctionH() {
		return h;
	}
}