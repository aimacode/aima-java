package aima.core.search.basic.uninformed;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;

import aima.core.search.basic.queue.QueueSearchForActions;
import aima.core.search.basic.queue.QueueSearchForActionsWrapper;
import aima.core.search.basic.support.BasicFrontierQueue;

/**
 *
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 * @author Ravi Mohan
 */
public class DepthFirstQueueSearch<A, S> extends QueueSearchForActionsWrapper<A, S> {
	public DepthFirstQueueSearch(QueueSearchForActions<A, S> qsearchImpl) {
		super(qsearchImpl);
		qsearchImpl.setFrontierSupplier(
				() -> new BasicFrontierQueue<A, S>(() -> Collections.asLifoQueue(new LinkedList<>()), HashSet::new));
	}
}