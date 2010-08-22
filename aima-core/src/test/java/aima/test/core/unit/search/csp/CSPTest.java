package aima.test.core.unit.search.csp;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.search.csp.CSP;
import aima.core.search.csp.Constraint;
import aima.core.search.csp.Domain;
import aima.core.search.csp.NotEqualConstraint;
import aima.core.search.csp.Variable;

/**
 * @author Ruediger Lunde
 */
public class CSPTest {
	private static final Variable X = new Variable("x");
	private static final Variable Y = new Variable("y");
	private static final Variable Z = new Variable("z");

	private static final Constraint C1 = new NotEqualConstraint(X, Y);
	private static final Constraint C2 = new NotEqualConstraint(X, Y);

	private Domain colors;
	private Domain animals;

	private List<Variable> variables;

	@Before
	public void setUp() {
		variables = new ArrayList<Variable>();
		variables.add(X);
		variables.add(Y);
		variables.add(Z);
		colors = new Domain(new Object[] { "red", "green", "blue" });
		animals = new Domain(new Object[] { "cat", "dog" });
	}

	@Test
	public void testConstraintNetwork() {
		CSP csp = new CSP(variables);
		csp.addConstraint(C1);
		csp.addConstraint(C2);
		Assert.assertNotNull(csp.getConstraints());
		Assert.assertEquals(2, csp.getConstraints().size());
		Assert.assertNotNull(csp.getConstraints(X));
		Assert.assertEquals(2, csp.getConstraints(X).size());
		Assert.assertNotNull(csp.getConstraints(Y));
		Assert.assertEquals(2, csp.getConstraints(Y).size());
		Assert.assertNotNull(csp.getConstraints(Z));
		Assert.assertEquals(0, csp.getConstraints(Z).size());
	}

	@Test
	public void testDomainChanges() {
		Domain colors2 = new Domain(colors.asList());
		Assert.assertEquals(colors, colors2);

		CSP csp = new CSP(variables);
		csp.addConstraint(C1);
		Assert.assertNotNull(csp.getDomain(X));
		Assert.assertEquals(0, csp.getDomain(X).size());
		Assert.assertNotNull(csp.getConstraints(X));

		csp.setDomain(X, colors);
		Assert.assertEquals(colors, csp.getDomain(X));
		Assert.assertEquals(3, csp.getDomain(X).size());
		Assert.assertEquals("red", csp.getDomain(X).get(0));

		CSP cspCopy = csp.copyDomains();
		Assert.assertNotNull(cspCopy.getDomain(X));
		Assert.assertEquals(3, cspCopy.getDomain(X).size());
		Assert.assertEquals("red", cspCopy.getDomain(X).get(0));
		Assert.assertNotNull(cspCopy.getDomain(Y));
		Assert.assertEquals(0, cspCopy.getDomain(Y).size());
		Assert.assertNotNull(cspCopy.getConstraints(X));
		Assert.assertEquals(C1, cspCopy.getConstraints(X).get(0));

		cspCopy.removeValueFromDomain(X, "red");
		Assert.assertEquals(2, cspCopy.getDomain(X).size());
		Assert.assertEquals("green", cspCopy.getDomain(X).get(0));
		Assert.assertEquals(3, csp.getDomain(X).size());
		Assert.assertEquals("red", csp.getDomain(X).get(0));

		cspCopy.setDomain(X, animals);
		Assert.assertEquals(2, cspCopy.getDomain(X).size());
		Assert.assertEquals("cat", cspCopy.getDomain(X).get(0));
		Assert.assertEquals(3, csp.getDomain(X).size());
		Assert.assertEquals("red", csp.getDomain(X).get(0));
	}
}
