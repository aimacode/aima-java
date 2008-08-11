package aima.search.nodestore;

import java.util.Hashtable;
import java.util.List;

import aima.search.framework.Node;
import aima.search.framework.NodeStore;

/**
 * A simple wrapper class which delegates NodeStore interface
 * requests to the NodeStore it was created with. However,
 * adds additional capability to keep track of nodes and look
 * them up based on their states.
 */

/**
 * @author Ciaran O'Reilly
 * 
 */
public class CachedStateNodeStore implements NodeStore {

	private NodeStore nodeStore;
	private Hashtable<Object, Node> cachedState = new Hashtable<Object, Node>();

	public CachedStateNodeStore(NodeStore aNodeStore) {
		nodeStore = aNodeStore;
	}

	public boolean containsNodeBasedOn(Object state) {
		return cachedState.containsKey(state);
	}

	public Node getNodeBasedOn(Object state) {
		return cachedState.get(state);
	}

	//
	// START Interface - NodeStore
	public void add(Node anItem) {
		nodeStore.add(anItem);
		cachedState.put(anItem.getState(), anItem);
	}

	public Node remove() {
		Node n = nodeStore.remove();
		cachedState.remove(n.getState());

		return n;
	}

	public void add(List<Node> nodes) {
		nodeStore.add(nodes);
		for (Node n : nodes) {
			cachedState.put(n.getState(), n);
		}
	}

	public boolean isEmpty() {
		return nodeStore.isEmpty();
	}

	public int size() {
		return nodeStore.size();
	}
	// END Interface - NodeStore
	//
}
