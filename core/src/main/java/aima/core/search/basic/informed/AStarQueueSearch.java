package aima.core.search.basic.informed;

import java.util.function.ToDoubleFunction;

import aima.core.search.api.Node;
import aima.core.search.basic.queue.GraphPriorityQueueSearch;
import aima.core.search.basic.queue.QueueSearchForActions;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): Figure ??, page ??.
 * <br>
 * <br>
 *
 * The algorithm is identical to UNIFORM-COST graph search except that A* uses
 * <em>g + h</em> instead of g.
 *
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 * @author Ruediger Lunde
 * @author Mike Stampone
 */
public class AStarQueueSearch<A, S> extends BestFirstQueueSearch<A, S> {
	private ToDoubleFunction<Node<A, S>> h;
	
	public AStarQueueSearch(ToDoubleFunction<Node<A, S>> h) {
		this(new GraphPriorityQueueSearch<>(), h);
	}

	public AStarQueueSearch(QueueSearchForActions<A, S> qsearchImpl, ToDoubleFunction<Node<A, S>> h) {
		super(qsearchImpl, (node) -> node.pathCost() + h.applyAsDouble(node));
		this.h = h;
	}

	public ToDoubleFunction<Node<A, S>> getHeuristicFunctionH() {
		return h;
	}
}