package aima.test.core.unit.learning.learners;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import aima.core.learning.framework.DataSet;
import aima.core.learning.framework.DataSetFactory;
import aima.core.learning.inductive.DLTest;
import aima.core.learning.inductive.DLTestFactory;
import aima.core.learning.learners.CurrentBestLearner;
import aima.core.learning.learners.DecisionListLearner;
import aima.core.learning.learners.DecisionTreeLearner;
import aima.core.learning.learners.MajorityLearner;
import aima.test.core.unit.learning.framework.MockDataSetSpecification;
import aima.test.core.unit.learning.inductive.MockDLTestFactory;

/**
 * @author Ravi Mohan
 * 
 */
public class LearnerTests {

	@Test
	public void testMajorityLearner() throws Exception {
		MajorityLearner learner = new MajorityLearner();
		DataSet ds = DataSetFactory.getRestaurantDataSet();
		learner.train(ds);
		int[] result = learner.test(ds);
		Assert.assertEquals(6, result[0]);
		Assert.assertEquals(6, result[1]);
	}

	@Test
	public void testDefaultUsedWhenTrainingDataSetHasNoExamples()
			throws Exception {
		// tests RecursionBaseCase#1
		DataSet ds = DataSetFactory.getRestaurantDataSet();
		DecisionTreeLearner learner = new DecisionTreeLearner();

		DataSet ds2 = ds.emptyDataSet();
		Assert.assertEquals(0, ds2.size());

		learner.train(ds2);
		Assert.assertEquals("Unable To Classify", learner.predict(ds
				.getExample(0)));
	}

	@Test
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
		Assert.assertEquals("Yes", learner.predict(ds.getExample(0)));
	}

	@Test
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
		Assert.assertEquals("Yes", learner.predict(ds.getExample(1)));
	}

	@Test
	public void testInducedTreeClassifiesDataSetCorrectly() throws Exception {
		DataSet ds = DataSetFactory.getRestaurantDataSet();
		DecisionTreeLearner learner = new DecisionTreeLearner();
		learner.train(ds);
		int[] result = learner.test(ds);
		Assert.assertEquals(12, result[0]);
		Assert.assertEquals(0, result[1]);
	}

	@Test
	public void testDecisionListLearnerReturnsNegativeDLWhenDataSetEmpty()
			throws Exception {
		// tests first base case of DL Learner
		DecisionListLearner learner = new DecisionListLearner("Yes", "No",
				new MockDLTestFactory(null));
		DataSet ds = DataSetFactory.getRestaurantDataSet();
		DataSet empty = ds.emptyDataSet();
		learner.train(empty);
		Assert.assertEquals("No", learner.predict(ds.getExample(0)));
		Assert.assertEquals("No", learner.predict(ds.getExample(1)));
		Assert.assertEquals("No", learner.predict(ds.getExample(2)));
	}

	@Test
	public void testDecisionListLearnerReturnsFailureWhenTestsEmpty()
			throws Exception {
		// tests second base case of DL Learner
		DecisionListLearner learner = new DecisionListLearner("Yes", "No",
				new MockDLTestFactory(new ArrayList<DLTest>()));
		DataSet ds = DataSetFactory.getRestaurantDataSet();
		learner.train(ds);
		Assert.assertEquals(DecisionListLearner.FAILURE, learner.predict(ds
				.getExample(0)));
	}

	@Test
	public void testDecisionListTestRunOnRestaurantDataSet() throws Exception {
		DataSet ds = DataSetFactory.getRestaurantDataSet();
		DecisionListLearner learner = new DecisionListLearner("Yes", "No",
				new DLTestFactory());
		learner.train(ds);

		int[] result = learner.test(ds);
		Assert.assertEquals(12, result[0]);
		Assert.assertEquals(0, result[1]);
	}

	@Test
	public void testCurrentBestLearnerOnRestaurantDataSet() throws Exception {
		DataSet ds = DataSetFactory.getRestaurantDataSet();
		CurrentBestLearner learner = new CurrentBestLearner("Yes");
		learner.train(ds);

		int[] result = learner.test(ds);
		Assert.assertEquals(12, result[0]);
		Assert.assertEquals(0, result[1]);
	}
}
