package aima.core.search.basic.support;

import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Queue;
import java.util.function.Predicate;
import java.util.function.Supplier;

import aima.core.search.api.Node;

/**
 * An implementation of the Queue interface that wraps an underlying TreeSet
 * implementation to create a Priority Frontier Queue, which is a Priority 
 * queue that also, automatically replaces nodes of the same state that 
 * are already present, but with a lower priority, and does so quickly.
 * It also maintains counts of all equal elements to get results similar to a 
 * MultiSet. All operations are O(log N) or lower in complexity<br> 
 * 
 * @author Avinash Agarwal
 */
public class BasicPriorityFrontierQueue<A, S> implements Queue<Node<A, S>>, Serializable {
	
	private static final long serialVersionUID = 1L;
	
	//Red black tree representation for our queue
	private TreeSet<Node<A, S>> queue;
	
	//It maps states with their corresponding nodes (that are present in the queue) in a sorted TreeSet container.
	private Map<S, TreeSet<Node<A, S>>> stateToNodesMap;
	
	//The Comparator used for ordering in this priority queue
	private Comparator<Node<A, S>> comparator;
	
	//map to store counts of each distinct node
	private Map<Node<A, S>, Integer> nodeCounts;
	
	//the actual size of our queue (sum of counts of all distinct nodes)
	private int totalCount;
	
	public BasicPriorityFrontierQueue(Comparator<Node<A, S>> comparator) {
		this(comparator, HashMap::new);
	}
	
	public BasicPriorityFrontierQueue(Comparator<Node<A, S>> comparator,
			Supplier<Map<S, TreeSet<Node<A, S>>>> stateMembershipSupplier) {
		
		// Adding a further object equality check
		this.comparator = comparator.thenComparing((n1,n2) -> Objects.equals(n1, n2) ? 0 : 1);		
		queue = new TreeSet<Node<A, S>>(this.comparator);
		stateToNodesMap = stateMembershipSupplier.get();
		nodeCounts = new HashMap<Node<A, S>, Integer>();
		totalCount = 0;
	}

	//
	// Queue
	@Override
	public boolean add(Node<A, S> node) {		
		
		//remove lower priority nodes first
		removeNodesOfLowerPriority(node);
		
		if(contains(node)) {
			//if node is already contained in our set, we just have to increment its count
			incrementCount(node);
			return true;
		}
		
		//when node hasn't been added before
		boolean inserted = queue.add(node);
		
		if (inserted) {
			incrementCount(node);
			inserted = addToStateNodeMap(node.state(), node);
		}

		return inserted;
	}

	@Override
	public boolean offer(Node<A, S> node) {
		return add(node);
	}

	@Override
	public Node<A, S> remove() {		
		
		Node<A, S> result = queue.first();
		
		//If it is the only node of its kind in the set
		if(nodeCounts.get(result) == 1) {
			result = queue.pollFirst();
			removeFromStateNodeMap(result.state(), result);
		}
		//otherwise just decrementing count is sufficient
		decrementCount(result);
		
		return result;
	}

	@Override
	public Node<A, S> poll() {
		return remove();
	}

	@Override
	public Node<A, S> element() {
		if(queue.isEmpty()) {
			throw new NoSuchElementException();
		}
		return queue.first();
	}

	@Override
	public Node<A, S> peek() {
		if(queue.isEmpty()) {
			return null;
		}
		return queue.first();
	}

	//
	// Collection
	@Override
	public int size() {
		return totalCount;
	}

	@Override
	public boolean isEmpty() {
		return queue.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		if (o instanceof Node) {
			return nodeCounts.containsKey(o);
		} else {
			// Assume is a State
			return stateToNodesMap.containsKey(o);
		}
	}

	@Override
	public Iterator<Node<A, S>> iterator() {
		// TODO - add support for
		//TreeSet queue only has unique nodes. However, a proper iterator should iterate across all elements 
		throw new UnsupportedOperationException("Not yet supported. Use toArray() to get array of all elements");
	}

	@Override
	public Object[] toArray() {
		Object[] result = new Object[totalCount];
		int i = 0;
		//iterating over the queue to get each unique node
		for(Node<A, S> n : queue) {
			int cnt = nodeCounts.get(n);
			//include this node cnt number of times (as should be in actualy priority queue
			while(cnt-- > 0) {
				result[i++] = n;
			}
		}
		return result;
	}

	@Override
	public <T> T[] toArray(T[] a) {
		
		if(a.length < totalCount) {
			a = (T[]) new Node[totalCount];
		}
		int i = 0;
		//iterating over the queue to get each unique node
		for(Node<A, S> n : queue) {
			int cnt = nodeCounts.get(n);
			//include this node cnt number of times (as should be in actualy priority queue
			while(cnt-- > 0) {
				a[i++] = (T) n;
			}
		}
		//fill remaining with null
		while(i < totalCount) {
			a[i++] = null;
		}
		
		return a;
	}

	@Override
	public boolean remove(Object o) {
		if(o instanceof Node) {
			Node<A, S> toDelete = (Node<A, S>)o;
			boolean result = false;
			if (contains(toDelete)) {
				result = true;
				//If it is the only node of its kind in the set
				if(nodeCounts.get(toDelete) == 1) {
					result = queue.remove(toDelete);
					removeFromStateNodeMap(toDelete.state(), toDelete);
				}
				//otherwise just decrementing count is sufficient
				decrementCount(toDelete);
			}
			return result;
		}
		//Assuming o is state
		//temp contains all nodes of state o that are to be removed
		TreeSet<Node<A, S>> temp = stateToNodesMap.get(o);
		for(Node<A, S> n : temp) {
			//size decreases by number of such nodes n in the priority queue
			totalCount -= nodeCounts.get(n);
			nodeCounts.remove(n);
		}
		//remove all nodes of state o
		boolean deleted = queue.removeAll(temp);
		stateToNodesMap.remove(o);
		return deleted;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return queue.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends Node<A, S>> c) {
		boolean changed = false;
		for(Node<A, S> node : c) {
			changed |= add(node);
		}
		return changed;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		boolean changed = false;
		for(Object node : c) {
			changed |= remove(node);
		}
		return changed;
	}

	@Override
	public boolean removeIf(Predicate<? super Node<A, S>> filter) {
		return queue.removeIf(filter);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		// TODO - add support for
		throw new UnsupportedOperationException("Not supported currently");
	}

	@Override
	public void clear() {
		//reset everything
		queue.clear();
		stateToNodesMap.clear();
		nodeCounts.clear();
		totalCount = 0;
	}

	@Override
	public boolean equals(Object o) {
		return queue.equals(o);
	}

	@Override
	public int hashCode() {
		return queue.hashCode();
	}

	//adds nodes corresponding to their state into stateToNodesMap
	protected boolean addToStateNodeMap(S state, Node<A, S> node) {
		
		if(stateToNodesMap.containsKey(state)) {
			return stateToNodesMap.get(state).add(node);
		}
		else {
			TreeSet<Node<A, S>> treeSet = new TreeSet<Node<A, S>>(queue.comparator());
			boolean result = treeSet.add(node);
			stateToNodesMap.put(state, treeSet);
			return result;
		}
	}

	//modifies stateToNodesMap to reflect removal of node from priority queue
	protected boolean removeFromStateNodeMap(S state, Node<A, S> node) {
		
		if(stateToNodesMap.containsKey(state)) {
			boolean result = stateToNodesMap.get(state).remove(node);
			if(stateToNodesMap.get(state).isEmpty()) {
				stateToNodesMap.remove(state);
			}
			return result;
		}
		return false;
	}
	
	//make changes to totalCount and nodeCounts when adding node
	protected void incrementCount(Node<A, S> node) {
		++totalCount;
		nodeCounts.merge(node, 1, Integer::sum);
	}
	
	//make changes to totalCount and nodeCounts when removing node
	protected void decrementCount(Node<A, S> node) {
		if(nodeCounts.containsKey(node)) {
			--totalCount;
			nodeCounts.merge(node, -1, (oldValue, newValue) -> {
				int decrementedValue = Integer.sum(oldValue, newValue);
				if (decrementedValue == 0) {
					return null; // Causes it to be removed from the map
				}
				else {
					return decrementedValue;
				}
			});
		}
	}
	
	//removes nodes from the queue that are of priority lower (that compare higher) than the given node
	protected void removeNodesOfLowerPriority(Node<A, S> node) {
		//iterating until there is no node with same state and lower priority
		if(contains(node.state())) {
			Object[] lowerPriorityNodes = stateToNodesMap.get(node.state()).tailSet(node, false).toArray();
			for(Object n : lowerPriorityNodes) {
				remove(n);
			}
		}
	}
}