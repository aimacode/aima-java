package aima.test.core.unit.search.csp;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.search.csp.Assignment;

/**
 * @author Ravi Mohan
 * 
 */
public class AssignmentTest {
	private Assignment assignment;

	@Before
	public void setUp() {
		List<String> l = new ArrayList<String>();
		l.add("x");
		l.add("y");
		assignment = new Assignment(l);
	}

	@Test
	public void testAssignmentCompletion() {
		Assert.assertFalse(assignment.isComplete());
		assignment.setAssignment("x", "Ravi");
		Assert.assertFalse(assignment.isComplete());
		assignment.setAssignment("y", "AIMA");
		Assert.assertTrue(assignment.isComplete());
		assignment.remove("x");
		Assert.assertFalse(assignment.isComplete());
	}

	@Test
	public void testAssignmentDefaultVariableSelection() {
		Assert.assertEquals("x", assignment.selectFirstUnassignedVariable());
		assignment.setAssignment("x", "Ravi");
		Assert.assertEquals("y", assignment.selectFirstUnassignedVariable());
		assignment.setAssignment("y", "AIMA");
		Assert.assertEquals(null, assignment.selectFirstUnassignedVariable());
	}
}