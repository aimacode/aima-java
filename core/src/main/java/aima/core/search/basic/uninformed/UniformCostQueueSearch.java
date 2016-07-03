package aima.core.search.basic.uninformed;

import java.util.PriorityQueue;

import aima.core.search.basic.queue.GraphPriorityQueueSearch;
import aima.core.search.basic.queue.QueueSearchForActions;
import aima.core.search.basic.queue.QueueSearchForActionsWrapper;

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