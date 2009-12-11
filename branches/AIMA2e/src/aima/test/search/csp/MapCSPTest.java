/*
 * Created on Sep 21, 2004
 *
 */
package aima.test.search.csp;

import junit.framework.TestCase;
import aima.search.csp.Assignment;
import aima.search.csp.CSP;
import aima.search.csp.MapCSP;

/**
 * @author Ravi Mohan
 * 
 */
public class MapCSPTest extends TestCase {
	private CSP csp;

	@Override
	public void setUp() {
		csp = MapCSP.getMap();
	}

	public void testBackTrackingSearch() {
		Assignment results = csp.backTrackingSearch();
		assertNotNull(results);
		assertEquals(MapCSP.RED, results.getAssignment(MapCSP.V));
		assertEquals(MapCSP.GREEN, results.getAssignment(MapCSP.SA));
		assertEquals(MapCSP.RED, results.getAssignment(MapCSP.T));
		assertEquals(MapCSP.BLUE, results.getAssignment(MapCSP.NT));
		assertEquals(MapCSP.RED, results.getAssignment(MapCSP.Q));
		assertEquals(MapCSP.BLUE, results.getAssignment(MapCSP.NSW));
		assertEquals(MapCSP.RED, results.getAssignment(MapCSP.WA));
		// System.out.println(results);
	}

	public void testMCSearch() {
		Assignment results = csp.mcSearch(100);

	}
}
