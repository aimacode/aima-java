package aima.core.search.basic.informed;

import java.util.function.ToDoubleFunction;

import aima.core.search.api.Node;
import aima.core.search.api.NodeFactory;
import aima.core.search.api.SearchController;
import aima.core.search.basic.TreePrioritySearch;
import aima.core.search.basic.support.BasicNodeFactory;
import aima.core.search.basic.support.BasicPriorityFrontierQueue;
import aima.core.search.basic.support.BasicSearchController;

/**
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 * @author Mike Stampone
 * 
 */
public class BestFirstTreeSearch<A, S> extends TreePrioritySearch<A, S> {

	private ToDoubleFunction<Node<A, S>> f;
	
	public BestFirstTreeSearch(ToDoubleFunction<Node<A, S>> f) {
		this(new BasicSearchController<>(), new BasicNodeFactory<>(), f);
	}
	
	public BestFirstTreeSearch(SearchController<A, S> searchController, NodeFactory<A, S> nodeFactory, ToDoubleFunction<Node<A, S>> f) {
		super(searchController, nodeFactory, () -> new BasicPriorityFrontierQueue<>((n1, n2) -> Double.compare(f.applyAsDouble(n1), f.applyAsDouble(n2))));
		this.f = f;
	}
	
	public ToDoubleFunction<Node<A, S>> getEvaluationFunctionF() {
		return f;
	}
}