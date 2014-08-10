package aima.test.core.unit.learning.inductive;

import org.junit.Assert;
import org.junit.Test;

import aima.core.learning.framework.DataSet;
import aima.core.learning.framework.DataSetFactory;
import aima.core.learning.inductive.DLTest;
import aima.core.learning.inductive.DecisionList;

/**
 * @author Ravi Mohan
 * 
 */
public class DecisionListTest {

	@Test
	public void testDecisonListWithNoTestsReturnsDefaultValue()
			throws Exception {
		DecisionList dlist = new DecisionList("Yes", "No");
		DataSet ds = DataSetFactory.getRestaurantDataSet();
		Assert.assertEquals("No", dlist.predict(ds.getExample(0)));
	}

	@Test
	public void testDecisionListWithSingleTestReturnsTestValueIfTestSuccessful()
			throws Exception {
		DecisionList dlist = new DecisionList("Yes", "No");
		DataSet ds = DataSetFactory.getRestaurantDataSet();

		DLTest test = new DLTest();
		test.add("type", "French");

		dlist.add(test, "test1success");

		Assert.assertEquals("test1success", dlist.predict(ds.getExample(0)));
	}

	@Test
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

		Assert.assertEquals("test2success", dlist.predict(ds.getExample(0)));
	}

	@Test
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

		Assert.assertEquals("No", dlist.predict(ds.getExample(0)));
	}

	@Test
	public void testDecisionListHandlesEmptyDataSet() throws Exception {
		// tests first base case of recursion
		DecisionList dlist = new DecisionList("Yes", "No");

		DLTest test1 = new DLTest();
		test1.add("type", "Thai"); // doesn't match first example
		dlist.add(test1, "test1success");
	}

	@Test
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
		Assert.assertEquals("test2success", dlist3.predict(ds.getExample(0)));
	}
}
