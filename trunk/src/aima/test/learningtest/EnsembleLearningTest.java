/*
 * Created on Jul 31, 2005
 *
 */
package aima.test.learningtest;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import aima.learning.framework.DataSet;
import aima.learning.framework.DataSetFactory;
import aima.learning.framework.Learner;
import aima.learning.inductive.DecisionTree;
import aima.learning.learners.AdaBoostLearner;
import aima.learning.learners.StumpLearner;

/**
 * @author Ravi Mohan
 * 
 */

public class EnsembleLearningTest extends TestCase {
	private static final String UNABLE_TO_CLASSIFY = "Unable to Classify";

	private static final String YES = "Yes";

	public void testAdaBoostEnablesCollectionOfStumpsToClassifyDataSetAccurately()
			throws Exception {
		DataSet ds = DataSetFactory.getRestaurantDataSet();
		List stumps = DecisionTree.getStumpsFor(ds, YES, "No");
		List<Learner> learners = new ArrayList<Learner>();
		for (Object stump : stumps) {
			DecisionTree sl = (DecisionTree) stump;
			StumpLearner stumpLearner = new StumpLearner(sl, "No");
			learners.add(stumpLearner);
		}
		AdaBoostLearner learner = new AdaBoostLearner(learners, ds);
		learner.train(ds);
		int[] result = learner.test(ds);
		assertEquals(12, result[0]);
		assertEquals(0, result[1]);
	}

}
