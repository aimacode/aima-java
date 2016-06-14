package aima.core.search.basic.uninformed;

import java.util.HashMap;
import java.util.LinkedList;

import aima.core.search.basic.queue.QueueSearchForActions;
import aima.core.search.basic.queue.QueueSearchForActionsWrapper;
import aima.core.search.basic.support.BasicFrontierQueue;

/**
 * 
 *
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 */
public class BreadthFirstQueueSearch<A, S> extends QueueSearchForActionsWrapper<A, S> {
	public BreadthFirstQueueSearch(QueueSearchForActions<A, S> qsearchImpl) {
		super(qsearchImpl);
		qsearchImpl.setFrontierSupplier(() -> new BasicFrontierQueue<A, S>(LinkedList::new, HashMap::new));
	}
}