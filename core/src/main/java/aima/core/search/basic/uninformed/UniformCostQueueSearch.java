package aima.core.search.basic.uninformed;

import java.util.HashSet;
import java.util.PriorityQueue;

import aima.core.search.basic.queue.QueueSearchForActions;
import aima.core.search.basic.queue.QueueSearchForActionsWrapper;
import aima.core.search.basic.support.BasicFrontierQueue;

/**
 * 
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 */
public class UniformCostQueueSearch<A, S> extends QueueSearchForActionsWrapper<A, S> {
	public UniformCostQueueSearch(QueueSearchForActions<A, S> qsearchImpl) {
		super(qsearchImpl);
		qsearchImpl.getNodeFactory().setNodeCostFunction(node -> node.pathCost());
		qsearchImpl.setFrontierSupplier(() -> new BasicFrontierQueue<>(() -> new PriorityQueue<>(getQueueSearchForActionsImpl().getNodeFactory()), HashSet::new));
    }
}