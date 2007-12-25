/*
 * Created on Jul 25, 2005
 *
 */
package aima.test.learningtest;

import java.util.ArrayList;

import junit.framework.TestCase;
import aima.learning.framework.DataSet;
import aima.learning.framework.DataSetFactory;
import aima.learning.inductive.DLTest;
import aima.learning.inductive.DLTestFactory;
import aima.learning.learners.DecisionListLearner;
import aima.learning.learners.DecisionTreeLearner;
import aima.learning.learners.MajorityLearner;

/**
 * @author Ravi Mohan
 * 
 */

public class LearnerTests extends TestCase {
	public void testMajorityLearner() throws Exception {
		MajorityLearner learner = new MajorityLearner();
		DataSet ds = DataSetFactory.getRestaurantDataSet();
		learner.train(ds);
		int[] result = learner.test(ds);
		assertEquals(6, result[0]);
		assertEquals(6, result[1]);
	}

	public void testDefaultUsedWhenTrainingDataSetHasNoExamples()
			throws Exception {
		// tests RecursionBaseCase#1
		DataSet ds = DataSetFactory.getRestaurantDataSet();
		DecisionTreeLearner learner = new DecisionTreeLearner();

		DataSet ds2 = ds.emptyDataSet();
		assertEquals(0, ds2.size());

		learner.train(ds2);
		assertEquals("Unable To Classify", learner.predict(ds.getExample(0)));

	}

	public void testClassificationReturnedWhenAllExamplesHaveTheSameClassification()
			throws Exception {
		// tests RecursionBaseCase#2
		DataSet ds = DataSetFactory.getRestaurantDataSet();
		DecisionTreeLearner learner = new DecisionTreeLearner();

		DataSet ds2 = ds.emptyDataSet();

		// all 3 examples have the same classification (willWait = yes)
		ds2.add(ds.getExample(0));
		ds2.add(ds.getExample(2));
		ds2.add(ds.getExample(3));

		learner.train(ds2);
		assertEquals("Yes", learner.predict(ds.getExample(0)));

	}

	public void testMajorityReturnedWhenAttributesToExamineIsEmpty()
			throws Exception {
		// tests RecursionBaseCase#2
		DataSet ds = DataSetFactory.getRestaurantDataSet();
		DecisionTreeLearner learner = new DecisionTreeLearner();

		DataSet ds2 = ds.emptyDataSet();

		// 3 examples have classification = "yes" and one ,"no"
		ds2.add(ds.getExample(0));
		ds2.add(ds.getExample(1));// "no"
		ds2.add(ds.getExample(2));
		ds2.add(ds.getExample(3));
		ds2.setSpecification(new MockDataSetSpecification("will_wait"));

		learner.train(ds2);
		assertEquals("Yes", learner.predict(ds.getExample(1)));

	}

	public void testInducedTreeClassifiesDataSetCorrectly() throws Exception {
		DataSet ds = DataSetFactory.getRestaurantDataSet();
		DecisionTreeLearner learner = new DecisionTreeLearner();
		learner.train(ds);
		int[] result = learner.test(ds);
		assertEquals(12, result[0]);
		assertEquals(0, result[1]);
	}

	public void testDecisionListLearnerReturnsNegativeDLWhenDataSetEmpty()
			throws Exception {
		// tests first base case of DL Learner
		DecisionListLearner learner = new DecisionListLearner("Yes", "No",
				new MockDLTestFactory(null));
		DataSet ds = DataSetFactory.getRestaurantDataSet();
		DataSet empty = ds.emptyDataSet();
		learner.train(empty);
		assertEquals("No", learner.predict(ds.getExample(0)));
		assertEquals("No", learner.predict(ds.getExample(1)));
		assertEquals("No", learner.predict(ds.getExample(2)));
	}

	public void testDecisionListLearnerReturnsFailureWhenTestsEmpty()
			throws Exception {
		// tests second base case of DL Learner
		DecisionListLearner learner = new DecisionListLearner("Yes", "No",
				new MockDLTestFactory(new ArrayList<DLTest>()));
		DataSet ds = DataSetFactory.getRestaurantDataSet();
		learner.train(ds);
		assertEquals(DecisionListLearner.FAILURE, learner.predict(ds
				.getExample(0)));
	}

	public void testDecisionListTestRunOnRestaurantDataSet() throws Exception {
		DataSet ds = DataSetFactory.getRestaurantDataSet();
		DecisionListLearner learner = new DecisionListLearner("Yes", "No",
				new DLTestFactory());
		learner.train(ds);
		// System.out.println(learner.getDecisionList());
		int[] result = learner.test(ds);
		assertEquals(12, result[0]);
		assertEquals(0, result[1]);
	}

}
