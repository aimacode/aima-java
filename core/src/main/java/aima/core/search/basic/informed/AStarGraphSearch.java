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
 * The algorithm is identical to UNIFORM-COST graph search except that A* uses <em>g + h</em> instead of g.
 *
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 * @author Ruediger Lunde
 * @author Mike Stampone
 */
public class AStarGraphSearch<A, S> extends UniformCostGraphSearch<A, S> {

	private ToDoubleFunction<Node<A, S>> h;
	
	public AStarGraphSearch(ToDoubleFunction<Node<A, S>> h) {
		this(new BasicNodeFactory<>(), h, HashSet::new);
	}
	
	public AStarGraphSearch(NodeFactory<A, S> nodeFactory, ToDoubleFunction<Node<A, S>> h, Supplier<Set<S>> exploredSupplier) {
		super(nodeFactory, () -> new BasicPriorityFrontierQueueWithStateTracking<>((n1, n2) -> Double.compare(n1.pathCost()+h.applyAsDouble(n1), n2.pathCost()+h.applyAsDouble(n2))), exploredSupplier);
	}
	
	public ToDoubleFunction<Node<A, S>> getHeuristicFunctionH() {
		return h;
	}
}