package aima.test.core.unit.search.csp;

import aima.core.search.csp.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.search.csp.examples.MapCSP;

/**
 * @author Ravi Mohan
 * 
 */
public class MapCSPTest {
	private CSP csp;

	@Before
	public void setUp() {
		csp = new MapCSP();
	}

	@Test
	public void testBackTrackingSearch() {
		Assignment results = new FlexibleBacktrackingSolver().solve(csp);
		Assert.assertNotNull(results);
		Assert.assertEquals(MapCSP.GREEN, results.getValue(MapCSP.WA));
		Assert.assertEquals(MapCSP.RED, results.getValue(MapCSP.NT));
		Assert.assertEquals(MapCSP.BLUE, results.getValue(MapCSP.SA));
		Assert.assertEquals(MapCSP.GREEN, results.getValue(MapCSP.Q));
		Assert.assertEquals(MapCSP.RED, results.getValue(MapCSP.NSW));
		Assert.assertEquals(MapCSP.GREEN, results.getValue(MapCSP.V));
		Assert.assertEquals(MapCSP.RED, results.getValue(MapCSP.T));
	}

	@Test
	public void testMCSearch() {
		new MinConflictsSolver(100).solve(csp);
	}
}
