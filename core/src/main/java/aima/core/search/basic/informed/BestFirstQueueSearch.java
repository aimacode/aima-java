package aima.core.search.basic.informed;

import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.function.ToDoubleFunction;

import aima.core.search.api.Node;
import aima.core.search.basic.queue.QueueSearchForActions;
import aima.core.search.basic.queue.QueueSearchForActionsWrapper;
import aima.core.search.basic.support.BasicFrontierQueue;

/**
 *
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 * @author Mike Stampone
 * 
 */
public class BestFirstQueueSearch<A, S> extends QueueSearchForActionsWrapper<A, S> {

	public BestFirstQueueSearch(QueueSearchForActions<A, S> qsearchImpl, ToDoubleFunction<Node<A, S>> f) {
		super(qsearchImpl);
		qsearchImpl.getNodeFactory().setNodeCostFunction(f);
		qsearchImpl.setFrontierSupplier(() -> new BasicFrontierQueue<>(
				() -> new PriorityQueue<>(getQueueSearchForActionsImpl().getNodeFactory()), HashMap::new));
	}

	public ToDoubleFunction<Node<A, S>> getEvaluationFunctionF() {
		return getQueueSearchForActionsImpl().getNodeFactory().getNodeCostFunction();
	}
}