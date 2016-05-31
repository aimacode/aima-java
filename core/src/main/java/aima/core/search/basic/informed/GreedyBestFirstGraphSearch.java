package aima.core.search.basic.informed;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;

import aima.core.search.api.Node;
import aima.core.search.api.NodeFactory;
import aima.core.search.api.SearchController;
import aima.core.search.basic.support.BasicNodeFactory;
import aima.core.search.basic.support.BasicSearchController;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): Figure ??, page ??.<br>
 * <br>
 *
 * Greedy best-first graph search tries to expand the node that is closest to the goal, on the grounds
 * that this is likely to lead to a solution quickly. Thus, it evaluates nodes by using just the heuristic
 * function; that is <em>f(n) = h(n)</em>.
 *
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 * @author Mike Stampone
 * 
 */
public class GreedyBestFirstGraphSearch<A, S> extends BestFirstGraphSearch<A, S> {
	private ToDoubleFunction<Node<A, S>> h;
	
	public GreedyBestFirstGraphSearch(ToDoubleFunction<Node<A, S>> h) {
		this(new BasicSearchController<>(), new BasicNodeFactory<>(), h, HashSet::new);
	}

	public GreedyBestFirstGraphSearch(SearchController<A, S> searchController, NodeFactory<A, S> nodeFactory, ToDoubleFunction<Node<A, S>> h, Supplier<Set<S>> exploredSupplier) {
	    super(searchController, nodeFactory, h, exploredSupplier);
	    this.h = h;
	}
	
	public ToDoubleFunction<Node<A, S>> getHeuristifcFunctionH() {
	  return h;
	}
}
