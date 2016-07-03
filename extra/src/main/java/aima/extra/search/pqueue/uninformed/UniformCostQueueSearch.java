package aima.extra.search.pqueue.uninformed;

import java.util.PriorityQueue;

import aima.extra.search.pqueue.QueueSearchForActions;
import aima.extra.search.pqueue.QueueSearchForActionsWrapper;

/**
 * 
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 */
public class UniformCostQueueSearch<A, S> extends QueueSearchForActionsWrapper<A, S> {
	public UniformCostQueueSearch() {
		this(new GraphPriorityQueueSearch<>());
	}
	
	public UniformCostQueueSearch(QueueSearchForActions<A, S> qsearchImpl) {
		super(qsearchImpl);
		setNodeCostFunction(node -> node.pathCost());
		setFrontierSupplier(() -> new PriorityQueue<>(qsearchImpl.getNodeComparator()));
	}
}