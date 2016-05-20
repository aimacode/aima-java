package aima.core.search.basic.support;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;

import aima.core.search.api.FrontierQueue;
import aima.core.search.api.Node;

/**
 * An implementation of the FrontierQueue interface that wraps an underlying queue but
 * tracks the state of all nodes contained in the underlying queue.
 * 
 * @author Ciaran O'Reilly
 */
public class BasicFrontierQueue<A, S> implements FrontierQueue<A, S>, Serializable {
	private static final long serialVersionUID = 1L;
	//
	private Queue<Node<A, S>> queue;
// TODO - replace with a state membership tracker type of interface, so that the decision as to how many times 
// a state can be repeated in the frontier is left up to it. 	
	private Set<S> states;
	
	public BasicFrontierQueue() {
		this(LinkedList::new, HashSet::new);
	}
	
	public BasicFrontierQueue(Supplier<Queue<Node<A, S>>> underlyingQueueSupplier, Supplier<Set<S>> stateMembershipSupplier) {
		this.queue  = underlyingQueueSupplier.get();
		this.states = stateMembershipSupplier.get();
	}
	
	//
	// FrontierQueue
	@Override
	public boolean containsState(S state) {
		return states.contains(state);
	}

	//
	// Queue
    @Override
    public boolean add(Node<A, S> node) {       
        boolean inserted = queue.add(node);
        if (inserted) {
        	if (!states.add(node.state())) {
                throw new IllegalArgumentException("Node's state is already in the frontier: " + node);
            }
        }
        
        return inserted;
    }
    
    @Override
    public boolean offer(Node<A, S> node) {
        boolean inserted = queue.offer(node);
        if (inserted) {
            if (!states.add(node.state())) {
                throw new IllegalArgumentException("Node's state is already in the frontier: " + node);
            }
        }
        return inserted;
    }

    @Override
    public Node<A, S> remove() {
        Node<A, S> result = queue.remove();
        if (result != null) {
            states.remove(result.state());
        }
        return result;
    }
    
    @Override
    public Node<A, S> poll() {
        Node<A, S> result = queue.poll();
        if (result != null) {
            states.remove(result.state());
        }
        return result;
    }
    
    @Override public Node<A, S> element() { return queue.element(); }
    @Override public Node<A, S> peek() { return queue.peek(); }
    
    //
    // Collection
    @Override public int size() { return queue.size(); }
    @Override public boolean isEmpty() { return queue.isEmpty(); }
    @Override public boolean contains(Object o) { return queue.contains(o); }
    @Override public Iterator<Node<A, S>> iterator() { return queue.iterator(); }
    @Override public Object[] toArray() { return queue.toArray(); }
    @Override public <T> T[] toArray(T[] a) { return queue.toArray(a); }
    @Override 
    public boolean remove(Object o) {
// TODO - add support for    	
    	throw new UnsupportedOperationException("Use remove()");
    }
    @Override public boolean containsAll(Collection<?> c) { return queue.containsAll(c); }
    @Override 
    public boolean addAll(Collection<? extends Node<A, S>> c) {
// TODO - add support for    	
    	throw new UnsupportedOperationException("Use add(node)");
    }
    @Override 
    public boolean removeAll(Collection<?> c) {
// TODO - add support for    	
    	throw new UnsupportedOperationException("Use remove()");    	
    }
    @Override 
    public boolean removeIf(Predicate<? super Node<A, S>> filter) {
    	 Objects.requireNonNull(filter);
         boolean removed = false;
         final Iterator<Node<A, S>> each = iterator();
         while (each.hasNext()) {
        	 Node<A, S> node = each.next();
             if (filter.test(node)) {
                 each.remove();
                 removed = true;
                 states.remove(node.state());
             }
         }
         return removed;	
    }
    @Override 
    public boolean retainAll(Collection<?> c) {
// TODO - add support for    	
    	throw new UnsupportedOperationException("Not supported currently");     
    }
    @Override 
    public void clear() { 
    	queue.clear(); 
    	states.clear();
    }
    @Override public boolean equals(Object o) { return queue.equals(o); }
    @Override public int hashCode() { return queue.hashCode(); }
}