package aima.test.core.unit.search.csp;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.search.csp.Assignment;
import aima.core.search.csp.TreeCSPSolver;
import aima.core.search.csp.CSP;
import aima.core.search.csp.TreeCSP;

/**
 * 
 * @author Peter Grubmair
 *
 */
public class TreeCSPTest {
	private CSP csp;

	@Before
	public void setUp() {
		csp = new TreeCSP();
	}
	
	@Test
	public void testTreeCSPSolver() {
		Assignment results = new TreeCSPSolver().solve(csp);
		Assert.assertNotNull(results);

		Assert.assertEquals(TreeCSP.RED, results.getAssignment(TreeCSP.A));
		Assert.assertEquals(TreeCSP.GREEN, results.getAssignment(TreeCSP.B));
		Assert.assertEquals(TreeCSP.RED, results.getAssignment(TreeCSP.C));
		Assert.assertEquals(TreeCSP.RED, results.getAssignment(TreeCSP.D));
		Assert.assertEquals(TreeCSP.GREEN, results.getAssignment(TreeCSP.E));
		Assert.assertEquals(TreeCSP.GREEN, results.getAssignment(TreeCSP.F));

	}
}
