package aima.test.core.unit.search;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import aima.core.search.framework.Node;

/**
 * @author Ravi Mohan
 * 
 */
public class NodeTest {

	@Test
	public void testRootNode() {
		Node node1 = new Node("state1");
		Assert.assertTrue(node1.isRootNode());
		Node node2 = new Node(node1, null, "state2");
		Assert.assertTrue(node1.isRootNode());
		Assert.assertFalse(node2.isRootNode());
		Assert.assertEquals(node1, node2.getParent());
	}

	@Test
	public void testGetPathFromRoot() {
		Node node1 = new Node("state1");
		Node node2 = new Node(node1, null, "state2");
		Node node3 = new Node(node2, null, "state3");
		List<Node> path = node3.getPathFromRoot();
		Assert.assertEquals(node1, path.get(0));
		Assert.assertEquals(node2, path.get(1));
		Assert.assertEquals(node3, path.get(2));
	}
}
