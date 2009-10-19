package aima.core.search.nodestore;

import java.util.List;

import aima.core.search.framework.Node;
import aima.core.search.framework.NodeStore;
import aima.core.util.datastructure.FIFOQueue;

/**
 * @author Ravi Mohan
 * 
 */

public class FIFONodeStore implements NodeStore {

	FIFOQueue queue;

	public FIFONodeStore() {
		queue = new FIFOQueue();
	}

	public void add(Node anItem) {
		queue.add(anItem);

	}

	public Node remove() {
		return (Node) queue.remove();
	}

	public void add(List nodes) {
		queue.add(nodes);

	}

	public boolean isEmpty() {
		return queue.isEmpty();
	}

	public int size() {
		return queue.size();

	}

}