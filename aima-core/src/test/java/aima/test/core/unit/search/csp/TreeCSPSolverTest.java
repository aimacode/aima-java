package aima.test.core.unit.search.csp;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.search.csp.Assignment;
import aima.core.search.csp.CSP;
import aima.core.search.csp.Constraint;
import aima.core.search.csp.Domain;
import aima.core.search.csp.TreeCSPSolver;
import aima.core.search.csp.Variable;
import aima.core.search.csp.examples.NotEqualConstraint;

public class TreeCSPSolverTest {
	private static final Variable WA = new Variable("wa");
	private static final Variable NT = new Variable("nt");
	private static final Variable Q = new Variable("q");
	private static final Variable NSW = new Variable("nsw");
	private static final Variable V = new Variable("v");

	private static final Constraint C1 = new NotEqualConstraint(WA, NT);
	private static final Constraint C2 = new NotEqualConstraint(NT, Q);
	private static final Constraint C3 = new NotEqualConstraint(Q, NSW);
	private static final Constraint C4 = new NotEqualConstraint(NSW, V);
	
	private Domain colors;

	private List<Variable> variables;

	@Before
	public void setUp() {
		variables = new ArrayList<Variable>();
		variables.add(WA);
		variables.add(NT);
		variables.add(Q);
		variables.add(NSW);
		variables.add(V);
		colors = new Domain(new Object[] { "red", "green", "blue" });
	}

	@Test
	public void testConstraintNetwork() {
		CSP csp = new CSP(variables);
		csp.addConstraint(C1);
		csp.addConstraint(C2);
		csp.addConstraint(C3);
		csp.addConstraint(C4);
		Assert.assertNotNull(csp.getConstraints());
		Assert.assertEquals(4, csp.getConstraints().size());
		Assert.assertNotNull(csp.getConstraints(WA));
		Assert.assertEquals(1, csp.getConstraints(WA).size());
		Assert.assertNotNull(csp.getConstraints(NT));
		Assert.assertEquals(2, csp.getConstraints(NT).size());
		Assert.assertNotNull(csp.getConstraints(Q));
		Assert.assertEquals(2, csp.getConstraints(Q).size());
		Assert.assertNotNull(csp.getConstraints(NSW));
		Assert.assertEquals(2, csp.getConstraints(NSW).size());
	}

	@Test
	public void testDomainChanges() {
		Domain colors2 = new Domain(colors.asList());
		Assert.assertEquals(colors, colors2);

		CSP csp = new CSP(variables);
		csp.addConstraint(C1);
		Assert.assertNotNull(csp.getDomain(WA));
		Assert.assertEquals(0, csp.getDomain(WA).size());
		Assert.assertNotNull(csp.getConstraints(WA));

		csp.setDomain(WA, colors);
		Assert.assertEquals(colors, csp.getDomain(WA));
		Assert.assertEquals(3, csp.getDomain(WA).size());
		Assert.assertEquals("red", csp.getDomain(WA).get(0));

		CSP cspCopy = csp.copyDomains();
		Assert.assertNotNull(cspCopy.getDomain(WA));
		Assert.assertEquals(3, cspCopy.getDomain(WA).size());
		Assert.assertEquals("red", cspCopy.getDomain(WA).get(0));
		Assert.assertNotNull(cspCopy.getDomain(NT));
		Assert.assertEquals(0, cspCopy.getDomain(NT).size());
		Assert.assertNotNull(cspCopy.getConstraints(NT));
		Assert.assertEquals(C1, cspCopy.getConstraints(NT).get(0));

		cspCopy.removeValueFromDomain(WA, "red");
		Assert.assertEquals(2, cspCopy.getDomain(WA).size());
		Assert.assertEquals("green", cspCopy.getDomain(WA).get(0));
		Assert.assertEquals(3, csp.getDomain(WA).size());
		Assert.assertEquals("red", csp.getDomain(WA).get(0));
	}
	
	@Test
	public void testCSPSolver() {
		
		CSP csp = new CSP(variables);
		csp.addConstraint(C1);
		csp.addConstraint(C2);
		csp.addConstraint(C3);
		csp.addConstraint(C4);
		
		csp.setDomain(WA, colors);
		csp.setDomain(NT, colors);
		csp.setDomain(Q, colors);
		csp.setDomain(NSW, colors);
		csp.setDomain(V, colors);
		
		TreeCSPSolver treeCSPSolver = new TreeCSPSolver();
		Assignment assignment = treeCSPSolver.solve(csp);
		Assert.assertNotNull(assignment);
		Assert.assertTrue(assignment.isComplete(csp.getVariables()));
		Assert.assertTrue(assignment.isSolution(csp));
	}
}
