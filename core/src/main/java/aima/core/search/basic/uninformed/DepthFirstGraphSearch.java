package aima.core.search.basic.uninformed;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.function.Supplier;

import aima.core.search.api.NodeFactory;
import aima.core.search.basic.GraphSearch;
import aima.core.search.basic.support.BasicFrontierQueueWithStateTracking;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): page ??.<br>
 * <br>
 * The depth-first algorithm is an instance of the graph-search algorithm
 * in Figure ??; whereas breadth-first-search uses a FIFO queue, depth-first
 * search uses a LIFO queue.
 *
 * @author Ciaran O'Reilly
 */
public class DepthFirstGraphSearch<A, S> extends GraphSearch<A, S> {
	public DepthFirstGraphSearch() {
		super(() -> new BasicFrontierQueueWithStateTracking<A, S>(() -> Collections.asLifoQueue(new LinkedList<>()), HashSet::new));
	}
	
	public DepthFirstGraphSearch(NodeFactory<A, S> nodeFactory, Supplier<Set<S>> exploredSupplier) {
		super(nodeFactory, () -> new BasicFrontierQueueWithStateTracking<A, S>(() -> Collections.asLifoQueue(new LinkedList<>()), HashSet::new), exploredSupplier);
	}
}
