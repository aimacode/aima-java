package aima.core.learning.learners;

import java.util.List;

import aima.core.learning.framework.DataSet;
import aima.core.learning.framework.Example;
import aima.core.learning.framework.Learner;
import aima.core.learning.inductive.DLTest;
import aima.core.learning.inductive.DLTestFactory;
import aima.core.learning.inductive.DecisionList;

/**
 * @author Ravi Mohan
 * 
 */
public class DecisionListLearner implements Learner {
	public static final String FAILURE = "Failure";

	private DecisionList decisionList;

	private String positive, negative;

	private DLTestFactory testFactory;

	public DecisionListLearner(String positive, String negative,
			DLTestFactory testFactory) {
		this.positive = positive;
		this.negative = negative;
		this.testFactory = testFactory;
	}

	public void train(DataSet ds) {
		this.decisionList = decisionListLearning(ds);
	}

	public String predict(Example e) {
		if (decisionList == null) {
			throw new RuntimeException(
					"learner has not been trained with dataset yet!");
		}
		return decisionList.predict(e);
	}

	public int[] test(DataSet ds) {
		int[] results = new int[] { 0, 0 };

		for (Example e : ds.examples) {
			if (e.targetValue().equals(decisionList.predict(e))) {
				results[0] = results[0] + 1;
			} else {
				results[1] = results[1] + 1;
			}
		}
		return results;
	}

	/**
	 * @return Returns the decisionList.
	 */
	public DecisionList getDecisionList() {
		return decisionList;
	}

	//
	// PRIVATE METHODS
	//
	private DecisionList decisionListLearning(DataSet ds) {
		if (ds.size() == 0) {
			return new DecisionList(positive, negative);
		}
		List<DLTest> possibleTests = testFactory
				.createDLTestsWithAttributeCount(ds, 1);
		DLTest test = getValidTest(possibleTests, ds);
		if (test == null) {
			return new DecisionList(null, FAILURE);
		}
		// at this point there is a test that classifies some subset of examples
		// with the same target value
		DataSet matched = test.matchedExamples(ds);
		DecisionList list = new DecisionList(positive, negative);
		list.add(test, matched.getExample(0).targetValue());
		return list.mergeWith(decisionListLearning(test.unmatchedExamples(ds)));
	}

	private DLTest getValidTest(List<DLTest> possibleTests, DataSet ds) {
		for (DLTest test : possibleTests) {
			DataSet matched = test.matchedExamples(ds);
			if (!(matched.size() == 0)) {
				if (allExamplesHaveSameTargetValue(matched)) {
					return test;
				}
			}

		}
		return null;
	}

	private boolean allExamplesHaveSameTargetValue(DataSet matched) {
		// assumes at least i example in dataset
		String targetValue = matched.getExample(0).targetValue();
		for (Example e : matched.examples) {
			if (!(e.targetValue().equals(targetValue))) {
				return false;
			}
		}
		return true;
	}
}
