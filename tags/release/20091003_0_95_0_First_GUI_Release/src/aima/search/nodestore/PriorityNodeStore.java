package aima.search.nodestore;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;

import aima.search.framework.Node;
import aima.search.framework.NodeStore;

/**
 * @author Ravi Mohan
 * 
 */

public class PriorityNodeStore implements NodeStore {

	PriorityQueue<Node> queue;

	public PriorityNodeStore(Comparator<Node> comparator) {

		queue = new PriorityQueue<Node>(5, comparator);
		// queue = new PriorityQueue(comparator);
	}

	public void add(Node anItem) {

		queue.add(anItem);
	}

	public Node remove() {

		return queue.remove();
	}

	public void add(List<Node> nodes) {
		for (Object n : nodes) {
			queue.add((Node) n);
		}

	}

	public boolean isEmpty() {

		return queue.isEmpty();
	}

	public int size() {
		return queue.size();
	}

	@Override
	public String toString() {
		return queue.toString();
	}

}