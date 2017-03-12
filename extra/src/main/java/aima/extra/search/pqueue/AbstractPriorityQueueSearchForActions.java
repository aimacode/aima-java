package aima.extra.search.pqueue;

import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.function.Supplier;

import aima.core.search.api.Node;
import aima.core.search.basic.support.BasicFrontierQueue;

public abstract class AbstractPriorityQueueSearchForActions<A, S> extends AbstractQueueSearchForActions<A, S> {
	@Override
	public Supplier<Queue<Node<A, S>>> getDefaultFrontierSupplierWithStateContainmentCheckingSupport() {
		// Basic frontier queue supports doing containment checking of a node's state.
		// And we configure it internally to use a priority queue
		return () -> new BasicFrontierQueue<A, S>(() -> new PriorityQueue<>(getNodeComparator()), HashMap::new);
	}
	
	@Override
	public Supplier<Queue<Node<A, S>>> getDefaultFrontierSupplierWithoutStateContainmentCheckingSupport() {
		// We want to use a priority queue in thie instance
		return () -> new PriorityQueue<>(getNodeComparator());
	}
}