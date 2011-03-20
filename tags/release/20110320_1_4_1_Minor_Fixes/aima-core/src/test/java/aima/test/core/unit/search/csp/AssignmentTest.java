package aima.test.core.unit.search.csp;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.search.csp.Assignment;
import aima.core.search.csp.Variable;

/**
 * @author Ravi Mohan
 * 
 */
public class AssignmentTest {
	private static final Variable X = new Variable("x");
	private static final Variable Y = new Variable("y");

	private List<Variable> variables;
	private Assignment assignment;

	@Before
	public void setUp() {
		variables = new ArrayList<Variable>();
		variables.add(X);
		variables.add(Y);
		assignment = new Assignment();
	}

	@Test
	public void testAssignmentCompletion() {
		Assert.assertFalse(assignment.isComplete(variables));
		assignment.setAssignment(X, "Ravi");
		Assert.assertFalse(assignment.isComplete(variables));
		assignment.setAssignment(Y, "AIMA");
		Assert.assertTrue(assignment.isComplete(variables));
		assignment.removeAssignment(X);
		Assert.assertFalse(assignment.isComplete(variables));
	}

	// @Test
	// public void testAssignmentDefaultVariableSelection() {
	// Assert.assertEquals(X, assignment.selectFirstUnassignedVariable(csp));
	// assignment.setAssignment(X, "Ravi");
	// Assert.assertEquals(Y, assignment.selectFirstUnassignedVariable(csp));
	// assignment.setAssignment(Y, "AIMA");
	// Assert.assertEquals(null, assignment.selectFirstUnassignedVariable(csp));
	// }
}