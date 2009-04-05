package aima.learning.learners;

import java.util.ArrayList;
import java.util.List;

import aima.learning.framework.DataSet;
import aima.learning.framework.Example;
import aima.learning.framework.Learner;
import aima.learning.knowledge.CurrentBestLearning;
import aima.learning.knowledge.FOLDataSetDomain;
import aima.learning.knowledge.FOLExample;
import aima.learning.knowledge.Hypothesis;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class CurrentBestLearner implements Learner {
	private String trueGoalValue = null;

	//
	// PUBLIC METHODS
	//
	public CurrentBestLearner(String trueGoalValue) {
		this.trueGoalValue = trueGoalValue;
	}

	//
	// START-Learner
	public void train(DataSet ds) {
		FOLDataSetDomain folDSDomain = new FOLDataSetDomain(ds.specification,
				trueGoalValue);
		List<FOLExample> folExamples = new ArrayList<FOLExample>();
		int egNo = 1;
		for (Example e : ds.examples) {
			folExamples.add(new FOLExample(folDSDomain, e, egNo));
			egNo++;
		}

		CurrentBestLearning cbl = new CurrentBestLearning(folDSDomain);

		Hypothesis h = cbl.currentBestLearning(folExamples);

		// TODO
	}

	public String predict(Example e) {
		// TODO
		return null;
	}

	public int[] test(DataSet ds) {
		int[] results = new int[] { 0, 0 };

		for (Example e : ds.examples) {
			if (e.targetValue().equals(predict(e))) {
				results[0] = results[0] + 1;
			} else {
				results[1] = results[1] + 1;
			}
		}
		return results;
	}
	// END-Learner
	//
}
