/*
 * Created on Aug 1, 2005
 *
 */
package aima.test.learningtest;

import java.util.List;

import junit.framework.TestCase;
import aima.learning.framework.DataSet;
import aima.learning.framework.DataSetFactory;
import aima.learning.framework.Example;
import aima.learning.inductive.DLTest;
import aima.learning.inductive.DLTestFactory;

/**
 * @author Ravi Mohan
 * 
 */

public class DLTestTestCase extends TestCase {
	public void testDecisionList() throws Exception {
		DataSet ds = DataSetFactory.getRestaurantDataSet();
		List<DLTest> dlTests = new DLTestFactory()
				.createDLTestsWithAttributeCount(ds, 1);
		assertEquals(26, dlTests.size());
	}

	public void testDLTestMatchSucceedsWithMatchedExample() throws Exception {
		DataSet ds = DataSetFactory.getRestaurantDataSet();
		Example e = ds.getExample(0);
		DLTest test = new DLTest();
		test.add("type", "French");
		assertTrue(test.matches(e));
	}

	public void testDLTestMatchFailsOnMismatchedExample() throws Exception {
		DataSet ds = DataSetFactory.getRestaurantDataSet();
		Example e = ds.getExample(0);
		DLTest test = new DLTest();
		test.add("type", "Thai");
		assertFalse(test.matches(e));
	}

	public void testDLTestMatchesEvenOnMismatchedTargetAttributeValue()
			throws Exception {
		DataSet ds = DataSetFactory.getRestaurantDataSet();
		Example e = ds.getExample(0);
		DLTest test = new DLTest();
		test.add("type", "French");
		assertTrue(test.matches(e));
	}

	public void testDLTestReturnsMatchedAndUnmatchedExamplesCorrectly()
			throws Exception {
		DataSet ds = DataSetFactory.getRestaurantDataSet();
		DLTest test = new DLTest();
		test.add("type", "Burger");

		DataSet matched = test.matchedExamples(ds);
		assertEquals(4, matched.size());

		DataSet unmatched = test.unmatchedExamples(ds);
		assertEquals(8, unmatched.size());
	}

}
