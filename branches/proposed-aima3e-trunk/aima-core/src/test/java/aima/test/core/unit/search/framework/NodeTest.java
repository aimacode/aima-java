package aima.test.core.unit.search.framework;

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
		Node node2 = new Node("state2", node1, null, 1.0);
		Assert.assertTrue(node1.isRootNode());
		Assert.assertFalse(node2.isRootNode());
		Assert.assertEquals(node1, node2.getParent());
	}

	@Test
	public void testGetPathFromRoot() {
		Node node1 = new Node("state1");
		Node node2 = new Node("state2", node1, null, 1.0);
		Node node3 = new Node("state3", node2, null, 1.0);
		List<Node> path = node3.getPathFromRoot();
		Assert.assertEquals(node1, path.get(0));
		Assert.assertEquals(node2, path.get(1));
		Assert.assertEquals(node3, path.get(2));
	}
}
