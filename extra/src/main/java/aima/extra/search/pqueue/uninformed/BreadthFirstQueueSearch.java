package aima.extra.search.pqueue.uninformed;

import java.util.LinkedList;

import aima.extra.search.pqueue.QueueSearchForActions;
import aima.extra.search.pqueue.QueueSearchForActionsWrapper;

/**
 * 
 *
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 */
public class BreadthFirstQueueSearch<A, S> extends QueueSearchForActionsWrapper<A, S> {
	public BreadthFirstQueueSearch() {
		this(new GraphGoalTestedFirstQueueSearch<>());
	}
	
	public BreadthFirstQueueSearch(QueueSearchForActions<A, S> qsearchImpl) {
		super(qsearchImpl);
		// LinkedList by default is a FIFO queue.
		setFrontierSupplier(LinkedList::new);
	}
}