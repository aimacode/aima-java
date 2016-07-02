package aima.core.search.basic.uninformed;

import java.util.Collections;
import java.util.LinkedList;

import aima.core.search.basic.queue.QueueSearchForActions;
import aima.core.search.basic.queue.QueueSearchForActionsWrapper;


/**
 *
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 * @author Ravi Mohan
 */
public class DepthFirstQueueSearch<A, S> extends QueueSearchForActionsWrapper<A, S> {
	public DepthFirstQueueSearch(QueueSearchForActions<A, S> qsearchImpl) {
		super(qsearchImpl);
		setFrontierSupplier(() -> Collections.asLifoQueue(new LinkedList<>()));
	}
}