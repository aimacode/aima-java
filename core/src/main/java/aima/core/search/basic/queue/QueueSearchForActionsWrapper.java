package aima.core.search.basic.queue;

import java.util.List;

import aima.core.search.api.Problem;
import aima.core.search.api.SearchForActionsFunction;

public class QueueSearchForActionsWrapper<A, S> implements SearchForActionsFunction<A, S> {
	private QueueSearchForActions<A, S> qsearchImpl;

	public QueueSearchForActionsWrapper(QueueSearchForActions<A, S> qsearchImpl) {
		setQueueSearchForActionsImpl(qsearchImpl);
	}

	@Override
	public List<A> apply(Problem<A, S> problem) {
		return getQueueSearchForActionsImpl().apply(problem);
	}

	public QueueSearchForActions<A, S> getQueueSearchForActionsImpl() {
		return qsearchImpl;
	}

	public void setQueueSearchForActionsImpl(QueueSearchForActions<A, S> qsearchImpl) {
		this.qsearchImpl = qsearchImpl;
	}
}
