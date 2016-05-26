package aima.core.search.basic.informed;

import java.util.function.ToDoubleFunction;

import aima.core.search.api.Node;
import aima.core.search.api.NodeFactory;
import aima.core.search.basic.TreeShortestPathPrioritySearch;
import aima.core.search.basic.support.BasicNodeFactory;
import aima.core.search.basic.support.BasicPriorityFrontierQueue;

/**
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 * @author Mike Stampone
 * 
 */
public class BestFirstTreeSearch<A, S> extends TreeShortestPathPrioritySearch<A, S> {

	private ToDoubleFunction<Node<A, S>> f;
	
	public BestFirstTreeSearch(ToDoubleFunction<Node<A, S>> f) {
		this(new BasicNodeFactory<>(), f);
	}
	
	public BestFirstTreeSearch(NodeFactory<A, S> nodeFactory, ToDoubleFunction<Node<A, S>> f) {
		super(nodeFactory, () -> new BasicPriorityFrontierQueue<>((n1, n2) -> Double.compare(f.applyAsDouble(n1), f.applyAsDouble(n2))));
		this.f = f;
	}
	
	public ToDoubleFunction<Node<A, S>> getEvaluationFunctionF() {
		return f;
	}
}