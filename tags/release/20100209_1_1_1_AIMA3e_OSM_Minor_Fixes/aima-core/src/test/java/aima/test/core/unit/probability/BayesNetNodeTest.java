package aima.test.core.unit.probability;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.probability.BayesNetNode;

/**
 * @author Ravi Mohan
 * 
 */
public class BayesNetNodeTest {
	private BayesNetNode a, b, c, d, e;

	@Before
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

	@Test
	public void testInfluenceSemantics() {
		Assert.assertEquals(1, a.getChildren().size());
		Assert.assertTrue(a.getChildren().contains(c));
		Assert.assertEquals(0, a.getParents().size());

		Assert.assertEquals(1, b.getChildren().size());
		Assert.assertTrue(b.getChildren().contains(c));
		Assert.assertEquals(0, b.getParents().size());

		Assert.assertEquals(2, c.getChildren().size());
		Assert.assertTrue(c.getChildren().contains(d));
		Assert.assertTrue(c.getChildren().contains(e));
		Assert.assertEquals(2, c.getParents().size());
		Assert.assertTrue(c.getParents().contains(a));
		Assert.assertTrue(c.getParents().contains(b));

		Assert.assertEquals(0, d.getChildren().size());
		Assert.assertEquals(1, d.getParents().size());
		Assert.assertTrue(d.getParents().contains(c));

		Assert.assertEquals(0, e.getChildren().size());
		Assert.assertEquals(1, e.getParents().size());
		Assert.assertTrue(e.getParents().contains(c));
	}
}