package aima.core.search.basic.informed;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;

import aima.core.search.api.Node;
import aima.core.search.api.NodeFactory;
import aima.core.search.basic.support.BasicNodeFactory;

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
public class AStarGraphSearch<A, S> extends BestFirstGraphSearch<A, S> {

	private ToDoubleFunction<Node<A, S>> h;
	
	public AStarGraphSearch(ToDoubleFunction<Node<A, S>> h) {
		this(new BasicNodeFactory<>(), h, HashSet::new);
	}
	
	public AStarGraphSearch(NodeFactory<A, S> nodeFactory, ToDoubleFunction<Node<A, S>> h, Supplier<Set<S>> exploredSupplier) {
		super(nodeFactory, (node) -> node.pathCost()+h.applyAsDouble(node), exploredSupplier);
		this.h = h;
	}
	
	public ToDoubleFunction<Node<A, S>> getHeuristicFunctionH() {
		return h;
	}
}