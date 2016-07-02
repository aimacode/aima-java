package aima.core.search.basic.queue;

import java.util.HashMap;
import java.util.List;
import java.util.Queue;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;

import aima.core.search.api.Node;
import aima.core.search.api.Problem;
import aima.core.search.api.SearchForActionsFunction;
import aima.core.search.basic.support.BasicFrontierQueue;

public class QueueSearchForActionsWrapper<A, S> implements SearchForActionsFunction<A, S> {
	private QueueSearchForActions<A, S> qsearchImpl;

	public QueueSearchForActionsWrapper(QueueSearchForActions<A, S> qsearchImpl) {
		this.qsearchImpl = qsearchImpl;
	}

	@Override
	public List<A> apply(Problem<A, S> problem) {
		return getQueueSearchForActionsImpl().apply(problem);
	}

	public QueueSearchForActions<A, S> getQueueSearchForActionsImpl() {
		return qsearchImpl;
	}

	public void setFrontierSupplier(Supplier<Queue<Node<A, S>>> frontierSupplier) {
		if (qsearchImpl instanceof QueueSearchForActions.DoesStateContainmentCheckingOnFrontier) {
			// Ensure we set a supplier that supports node state containment checking
			qsearchImpl.setFrontierSupplier(() -> new BasicFrontierQueue<A, S>(frontierSupplier, HashMap::new));
		}
		else {
			qsearchImpl.setFrontierSupplier(frontierSupplier);
		}
	}
	
	public ToDoubleFunction<Node<A, S>> getNodeCostFunction() {
		return getQueueSearchForActionsImpl().getNodeFactory().getNodeCostFunction();
	}
	
	public void setNodeCostFunction(ToDoubleFunction<Node<A, S>> nodeCostFunction) {
		qsearchImpl.getNodeFactory().setNodeCostFunction(nodeCostFunction);
	}
}