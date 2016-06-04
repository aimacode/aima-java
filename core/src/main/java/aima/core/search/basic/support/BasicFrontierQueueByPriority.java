package aima.core.search.basic.support;

import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.function.Supplier;

import aima.core.search.api.Node;
import aima.core.search.api.FrontierQueueByPriority;

/**
 *
 * @author Ciaran O'Reilly
 */
public class BasicFrontierQueueByPriority<A, S> extends BasicFrontierQueueWithStateTracking<A, S> implements FrontierQueueByPriority<A, S> {
	private static final long serialVersionUID = 1L;

	private Comparator<Node<A, S>> comparator;
	
    public BasicFrontierQueueByPriority(Comparator<Node<A, S>> comparator) {
    	this(comparator, HashSet::new);
    }
    
    public BasicFrontierQueueByPriority(Comparator<Node<A, S>> comparator, Supplier<Set<S>> stateMembershipSupplier) {
    	super(() -> new PriorityQueue<>(comparator), stateMembershipSupplier);
    	this.comparator = comparator;
    }
    
    @Override
    public Comparator<Node<A, S>> getComparator() {
    	return comparator;
    }
}
