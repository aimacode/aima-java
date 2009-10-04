/*
 * Created on Aug 2, 2005
 *
 */
package aima.test.learningtest;

import junit.framework.TestCase;
import aima.learning.framework.DataSet;
import aima.learning.framework.DataSetFactory;
import aima.learning.inductive.DLTest;
import aima.learning.inductive.DecisionList;

/**
 * @author Ravi Mohan
 * 
 */
public class DecisionListTest extends TestCase {
	public void testDecisonListWithNoTestsReturnsDefaultValue()
			throws Exception {
		DecisionList dlist = new DecisionList("Yes", "No");
		DataSet ds = DataSetFactory.getRestaurantDataSet();
		assertEquals("No", dlist.predict(ds.getExample(0)));
	}

	public void testDecisionListWithSingleTestReturnsTestValueIfTestSuccessful()
			throws Exception {
		DecisionList dlist = new DecisionList("Yes", "No");
		DataSet ds = DataSetFactory.getRestaurantDataSet();

		DLTest test = new DLTest();
		test.add("type", "French");

		dlist.add(test, "test1success");

		assertEquals("test1success", dlist.predict(ds.getExample(0)));
	}

	public void testDecisionListFallsThruToNextTestIfOneDoesntMatch()
			throws Exception {
		DecisionList dlist = new DecisionList("Yes", "No");
		DataSet ds = DataSetFactory.getRestaurantDataSet();

		DLTest test1 = new DLTest();
		test1.add("type", "Thai"); // doesn't match first example
		dlist.add(test1, "test1success");

		DLTest test2 = new DLTest();
		test2.add("type", "French");
		dlist.add(test2, "test2success");// matches first example

		assertEquals("test2success", dlist.predict(ds.getExample(0)));
	}

	public void testDecisionListFallsThruToDefaultIfNoTestMatches()
			throws Exception {
		DecisionList dlist = new DecisionList("Yes", "No");
		DataSet ds = DataSetFactory.getRestaurantDataSet();

		DLTest test1 = new DLTest();
		test1.add("type", "Thai"); // doesn't match first example
		dlist.add(test1, "test1success");

		DLTest test2 = new DLTest();
		test2.add("type", "Burger");
		dlist.add(test2, "test2success");// doesn't match first example

		assertEquals("No", dlist.predict(ds.getExample(0)));
	}

	public void testDecisionListHandlesEmptyDataSet() throws Exception {
		// tests first base case of recursion
		DecisionList dlist = new DecisionList("Yes", "No");
		DataSet ds = DataSetFactory.getRestaurantDataSet();

		DLTest test1 = new DLTest();
		test1.add("type", "Thai"); // doesn't match first example
		dlist.add(test1, "test1success");
	}

	public void testDecisionListMerge() throws Exception {
		DecisionList dlist1 = new DecisionList("Yes", "No");
		DecisionList dlist2 = new DecisionList("Yes", "No");
		DataSet ds = DataSetFactory.getRestaurantDataSet();

		DLTest test1 = new DLTest();
		test1.add("type", "Thai"); // doesn't match first example
		dlist1.add(test1, "test1success");

		DLTest test2 = new DLTest();
		test2.add("type", "French");
		dlist2.add(test2, "test2success");// matches first example

		DecisionList dlist3 = dlist1.mergeWith(dlist2);
		assertEquals("test2success", dlist3.predict(ds.getExample(0)));
	}

}
