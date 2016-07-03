package aima.extra.search.pqueue.informed;

import java.util.function.ToDoubleFunction;

import aima.core.search.api.Node;
import aima.extra.search.pqueue.QueueSearchForActions;
import aima.extra.search.pqueue.uninformed.GraphPriorityQueueSearch;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): Figure ??, page ??.
 * <br>
 * <br>
 *
 * Greedy best-first graph search tries to expand the node that is closest to
 * the goal, on the grounds that this is likely to lead to a solution quickly.
 * Thus, it evaluates nodes by using just the heuristic function; that is
 * <em>f(n) = h(n)</em>.
 *
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 * @author Mike Stampone
 * 
 */
public class GreedyBestFirstQueueSearch<A, S> extends BestFirstQueueSearch<A, S> {
	private ToDoubleFunction<Node<A, S>> h;
	
	public GreedyBestFirstQueueSearch(ToDoubleFunction<Node<A, S>> h) {
		this(new GraphPriorityQueueSearch<>(), h);
	}

	public GreedyBestFirstQueueSearch(QueueSearchForActions<A, S> qsearchImpl, ToDoubleFunction<Node<A, S>> h) {
		super(qsearchImpl, h);
		this.h = h;
	}

	public ToDoubleFunction<Node<A, S>> getHeuristicFunctionH() {
		return h;
	}
}
