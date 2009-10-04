/*
 * Created on Apr 14, 2005
 *
 */
package aima.learning.learners;

import java.util.ArrayList;
import java.util.List;

import aima.learning.framework.DataSet;
import aima.learning.framework.Example;
import aima.learning.framework.Learner;
import aima.util.Util;

/**
 * @author Ravi Mohan
 * 
 */
public class MajorityLearner implements Learner {

	private DataSet dataset;

	private String result;

	public void train(DataSet ds) {
		List<String> targets = new ArrayList<String>();
		for (Example e : ds.examples) {
			targets.add(e.targetValue());
		}
		result = Util.mode(targets);
	}

	public String predict(Example e) {
		return result;
	}

	public int[] test(DataSet ds) {
		int[] results = new int[] { 0, 0 };

		for (Example e : ds.examples) {
			if (e.targetValue().equals(result)) {
				results[0] = results[0] + 1;
			} else {
				results[1] = results[1] + 1;
			}
		}
		return results;
	}

}