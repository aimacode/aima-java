package aima.test.probabilitytest;

import aima.probability.BayesNetNode;
import junit.framework.TestCase;

/**
 * @author Ravi Mohan
 * 
 */

public class BayesNetNodeTest extends TestCase {
	private BayesNetNode a, b, c, d, e;

	@Override
	public void setUp() {
		a = new BayesNetNode("A");
		b = new BayesNetNode("B");
		c = new BayesNetNode("C");
		d = new BayesNetNode("D");
		e = new BayesNetNode("E");

		c.influencedBy(a, b);
		d.influencedBy(c);
		e.influencedBy(c);
	}

	public void testInfluenceSemantics() {
		assertEquals(1, a.getChildren().size());
		assertTrue(a.getChildren().contains(c));
		assertEquals(0, a.getParents().size());

		assertEquals(1, b.getChildren().size());
		assertTrue(b.getChildren().contains(c));
		assertEquals(0, b.getParents().size());

		assertEquals(2, c.getChildren().size());
		assertTrue(c.getChildren().contains(d));
		assertTrue(c.getChildren().contains(e));
		assertEquals(2, c.getParents().size());
		assertTrue(c.getParents().contains(a));
		assertTrue(c.getParents().contains(b));

		assertEquals(0, d.getChildren().size());
		assertEquals(1, d.getParents().size());
		assertTrue(d.getParents().contains(c));

		assertEquals(0, e.getChildren().size());
		assertEquals(1, e.getParents().size());
		assertTrue(e.getParents().contains(c));
	}

}