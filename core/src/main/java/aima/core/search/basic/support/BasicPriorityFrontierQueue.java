package aima.core.search.basic.support;

import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.function.Supplier;

import aima.core.search.api.Node;

/**
 *
 * @author Ciaran O'Reilly
 */
public class BasicPriorityFrontierQueue<A, S> extends BasicFrontierQueue<A, S> {
	private static final long serialVersionUID = 1L;

    public BasicPriorityFrontierQueue(Comparator<Node<A, S>> comparator) {
    	this(comparator, HashSet::new);
    }
    
    public BasicPriorityFrontierQueue(Comparator<Node<A, S>> comparator, Supplier<Set<S>> stateMembershipSupplier) {
    	super(() -> new PriorityQueue<>(comparator), stateMembershipSupplier);
    }
}
