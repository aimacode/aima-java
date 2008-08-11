package aima.test.search;

import java.util.List;

import junit.framework.TestCase;
import aima.search.framework.Node;

/**
 * @author Ravi Mohan
 * 
 */

public class NodeTest extends TestCase {
	public void testRootNode() {
		Node node1 = new Node("state1");
		assertTrue(node1.isRootNode());
		Node node2 = new Node(node1, "state2");
		assertTrue(node1.isRootNode());
		assertFalse(node2.isRootNode());
		assertEquals(node1, node2.getParent());
	}

	public void testGetPathFromRoot() {
		Node node1 = new Node("state1");
		Node node2 = new Node(node1, "state2");
		Node node3 = new Node(node2, "state3");
		List path = node3.getPathFromRoot();
	}

}
