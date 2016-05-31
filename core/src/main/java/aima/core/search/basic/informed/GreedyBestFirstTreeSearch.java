package aima.core.search.basic.informed;

import java.util.function.ToDoubleFunction;

import aima.core.search.api.Node;
import aima.core.search.api.NodeFactory;
import aima.core.search.api.SearchController;
import aima.core.search.basic.support.BasicNodeFactory;
import aima.core.search.basic.support.BasicSearchController;

/**
 * 
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 * @author Mike Stampone
 * 
 */
public class GreedyBestFirstTreeSearch<A, S> extends BestFirstTreeSearch<A, S> {
	private ToDoubleFunction<Node<A, S>> h;

	public GreedyBestFirstTreeSearch(ToDoubleFunction<Node<A, S>> h) {
	    this(new BasicSearchController<>(), new BasicNodeFactory<>(), h);
	}
	
	public GreedyBestFirstTreeSearch(SearchController<A, S> searchController, NodeFactory<A, S> nodeFactory, ToDoubleFunction<Node<A, S>> h) {
		super(searchController, nodeFactory, h);
		this.h = h;
	}
	
	public ToDoubleFunction<Node<A, S>> getHeuristifcFunctionH() {
	  return h;
	}
}
