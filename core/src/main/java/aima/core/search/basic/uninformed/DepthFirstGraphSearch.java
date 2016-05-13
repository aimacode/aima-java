package aima.core.search.basic.uninformed;

import java.util.Collections;
import java.util.Set;
import java.util.function.Supplier;

import aima.core.search.api.NodeFactory;
import aima.core.search.basic.GraphSearch;
import aima.core.search.basic.support.BasicFrontierQueue;

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
		super(() -> Collections.asLifoQueue(new BasicFrontierQueue<>()));
	}
	
	public DepthFirstGraphSearch(NodeFactory<A, S> nodeFactory, Supplier<Set<S>> exploredSupplier) {
		super(nodeFactory, () -> Collections.asLifoQueue(new BasicFrontierQueue<>()), exploredSupplier);
	}
}
