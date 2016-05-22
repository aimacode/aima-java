package aima.core.search.basic.informed;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;

import aima.core.search.api.Node;
import aima.core.search.api.NodeFactory;
import aima.core.search.basic.support.BasicNodeFactory;
import aima.core.search.basic.support.BasicPriorityFrontierQueueWithStateTracking;
import aima.core.search.basic.uninformed.UniformCostGraphSearch;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): Figure ??, page ??.<br>
 * <br>
 *
 * Best-first graph search is an instance of the general GRAPH-SEARCH algorithm in
 * which a node is selected for expansion based on an evaluation function, <em>f(n)</em>.
 * The evaluation function is construed as a cost estimate, so the node with the lowest
 * evaluation is expanded first. The implementation of best-first graph search is identical
 * to that for uniform-cost search (Figure ?.??), except for the use of <em>f</em> instead
 * of <em>g</em> to order the priority queue.
 *
 * @author Ciaran O'Reilly
 */
public class BestFirstGraphSearch<A, S> extends UniformCostGraphSearch<A, S> {

	private ToDoubleFunction<Node<A, S>> f;
	
	public BestFirstGraphSearch(ToDoubleFunction<Node<A, S>> f) {
		this(new BasicNodeFactory<>(), f, HashSet::new);
	}
	
	public BestFirstGraphSearch(NodeFactory<A, S> nodeFactory, ToDoubleFunction<Node<A, S>> f, Supplier<Set<S>> exploredSupplier) {
		super(nodeFactory, () -> new BasicPriorityFrontierQueueWithStateTracking<>((n1, n2) -> Double.compare(f.applyAsDouble(n1), f.applyAsDouble(n2))), exploredSupplier);
	}
	
	public ToDoubleFunction<Node<A, S>> getEvaluationFunctionF() {
		return f;
	}
}