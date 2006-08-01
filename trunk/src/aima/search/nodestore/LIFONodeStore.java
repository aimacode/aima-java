/*
 * Created on Jul 17, 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package aima.search.nodestore;

import java.util.List;
import aima.datastructures.LIFOQueue;
import aima.search.framework.Node;
import aima.search.framework.NodeStore;

/**
 * @author Ravi Mohan
 * 
 * To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Generation - Code and Comments
 */
public class LIFONodeStore implements NodeStore {
	LIFOQueue queue;

	public LIFONodeStore() {
		queue = new LIFOQueue();
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