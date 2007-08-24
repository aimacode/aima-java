/*
 * Created on Jul 31, 2005
 *
 */
package aima.learning.learners;

import aima.learning.framework.DataSet;
import aima.learning.inductive.DecisionTree;

/**
 * @author Ravi Mohan
 * 
 */

public class StumpLearner extends DecisionTreeLearner {

	public StumpLearner(DecisionTree sl, String unable_to_classify) {
		super(sl, unable_to_classify);
	}

	@Override
	public void train(DataSet ds) {
		// System.out.println("Stump learner training");
		// do nothing the stump is not inferred from the dataset
	}

}
