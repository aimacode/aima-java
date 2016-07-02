package aima.core.search.basic.uninformed;

import java.util.LinkedList;

import aima.core.search.basic.queue.QueueSearchForActions;
import aima.core.search.basic.queue.QueueSearchForActionsWrapper;

/**
 * 
 *
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 */
public class BreadthFirstQueueSearch<A, S> extends QueueSearchForActionsWrapper<A, S> {
	public BreadthFirstQueueSearch(QueueSearchForActions<A, S> qsearchImpl) {
		super(qsearchImpl);
		// LinkedList by default is a FIFO queue.
		setFrontierSupplier(LinkedList::new);
	}
}