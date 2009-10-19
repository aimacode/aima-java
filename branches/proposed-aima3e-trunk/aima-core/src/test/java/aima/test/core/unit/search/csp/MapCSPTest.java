package aima.test.core.unit.search.csp;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.search.csp.Assignment;
import aima.core.search.csp.CSP;
import aima.core.search.csp.MapCSP;

/**
 * @author Ravi Mohan
 * 
 */
public class MapCSPTest {
	private CSP csp;

	@Before
	public void setUp() {
		csp = MapCSP.getMap();
	}

	@Test
	public void testBackTrackingSearch() {
		Assignment results = csp.backTrackingSearch();
		Assert.assertNotNull(results);
		Assert.assertEquals(MapCSP.RED, results.getAssignment(MapCSP.V));
		Assert.assertEquals(MapCSP.GREEN, results.getAssignment(MapCSP.SA));
		Assert.assertEquals(MapCSP.RED, results.getAssignment(MapCSP.T));
		Assert.assertEquals(MapCSP.BLUE, results.getAssignment(MapCSP.NT));
		Assert.assertEquals(MapCSP.RED, results.getAssignment(MapCSP.Q));
		Assert.assertEquals(MapCSP.BLUE, results.getAssignment(MapCSP.NSW));
		Assert.assertEquals(MapCSP.RED, results.getAssignment(MapCSP.WA));
	}

	@Test
	public void testMCSearch() {
		Assignment results = csp.mcSearch(100);
	}
}
