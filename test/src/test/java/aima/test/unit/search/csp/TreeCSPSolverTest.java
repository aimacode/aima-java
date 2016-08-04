package aima.test.unit.search.csp;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;

import aima.core.environment.support.CSPFactory;
import aima.core.search.api.Assignment;
import aima.core.search.api.CSP;
import aima.core.search.basic.csp.TreeCSPSolver;

public class TreeCSPSolverTest {

	@Test
	public void testIsTree() {
		Assert.assertFalse(CSPFactory.mapColoringTerritoriesOfAustraliaCSP().isTree());
		Assert.assertTrue(CSPFactory.aima3eFig6_10_treeCSP().isTree());
	}
	
	@Test
	public void testTopoligicalSort() {
		TreeCSPSolver tcs = new TreeCSPSolver();
		
		TreeCSPSolver.TopologicalSort topologicalSort = tcs.topologicalSort(CSPFactory.aima3eFig6_10_treeCSP(), "A");
		Assert.assertEquals(Arrays.asList("A", "B", "C", "D", "E", "F"), topologicalSort.variables);
		Assert.assertEquals(5, topologicalSort.parents.size());
		Assert.assertEquals("A", topologicalSort.parents.get("B"));
		Assert.assertEquals("B", topologicalSort.parents.get("C"));
		Assert.assertEquals("B", topologicalSort.parents.get("D"));
		Assert.assertEquals("D", topologicalSort.parents.get("E"));
		Assert.assertEquals("D", topologicalSort.parents.get("F"));
	}
	
	@Test
	public void testShouldFindAssignment() {
		TreeCSPSolver tcs = new TreeCSPSolver();
		
		CSP csp = CSPFactory.aima3eFig6_10_treeCSP();
		Assignment assignment = tcs.apply(csp);
		Assert.assertNotNull(assignment);
		Assert.assertTrue(assignment.isSolution(csp));
		
		csp = CSPFactory.aima3eFig6_10_treeCSP(new String[] {"red", "green"});
		assignment = tcs.apply(csp);
		Assert.assertNotNull(assignment);
		Assert.assertTrue(assignment.isSolution(csp));
	}
	
	@Test
	public void testShouldNotFindAssignment() {
		TreeCSPSolver tcs = new TreeCSPSolver();
		
		CSP csp = CSPFactory.aima3eFig6_10_treeCSP(new String[] {"red"});
		Assignment assignment = tcs.apply(csp);
		Assert.assertNull(assignment);
	}
}
