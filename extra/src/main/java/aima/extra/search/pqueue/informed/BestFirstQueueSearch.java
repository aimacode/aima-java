package aima.extra.search.pqueue.informed;

import java.util.PriorityQueue;
import java.util.function.ToDoubleFunction;

import aima.core.search.api.Node;
import aima.extra.search.pqueue.QueueSearchForActions;
import aima.extra.search.pqueue.QueueSearchForActionsWrapper;
import aima.extra.search.pqueue.uninformed.GraphPriorityQueueSearch;

/**
 *
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 * @author Mike Stampone
 * 
 */
public class BestFirstQueueSearch<A, S> extends QueueSearchForActionsWrapper<A, S> {
	
	public BestFirstQueueSearch(ToDoubleFunction<Node<A, S>> f) {
		this(new GraphPriorityQueueSearch<>(), f);
	}

	public BestFirstQueueSearch(QueueSearchForActions<A, S> qsearchImpl, ToDoubleFunction<Node<A, S>> f) {
		super(qsearchImpl);
		setNodeCostFunction(f);
		setFrontierSupplier(() -> new PriorityQueue<>(qsearchImpl.getNodeComparator()));
	}

	public ToDoubleFunction<Node<A, S>> getEvaluationFunctionF() {
		return getNodeCostFunction();
	}
}