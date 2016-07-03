package aima.extra.search.pqueue.uninformed;

import java.util.Collections;
import java.util.LinkedList;

import aima.extra.search.pqueue.QueueSearchForActions;
import aima.extra.search.pqueue.QueueSearchForActionsWrapper;


/**
 *
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 * @author Ravi Mohan
 */
public class DepthFirstQueueSearch<A, S> extends QueueSearchForActionsWrapper<A, S> {
	public DepthFirstQueueSearch() {
		this(new GraphQueueSearch<>());
	}
	
	public DepthFirstQueueSearch(QueueSearchForActions<A, S> qsearchImpl) {
		super(qsearchImpl);
		setFrontierSupplier(() -> Collections.asLifoQueue(new LinkedList<>()));
	}
}