package aima.core.learning.learners;

import java.util.Hashtable;
import java.util.List;

import aima.core.learning.framework.DataSet;
import aima.core.learning.framework.Example;
import aima.core.learning.framework.Learner;
import aima.core.util.Util;
import aima.core.util.datastructure.Table;

/**
 * @author Ravi Mohan
 * 
 */
public class AdaBoostLearner implements Learner {

	private List<Learner> learners;

	private DataSet dataSet;

	private double[] exampleWeights;

	private Hashtable<Learner, Double> learnerWeights;

	public AdaBoostLearner(List<Learner> learners, DataSet ds) {
		this.learners = learners;
		this.dataSet = ds;

		initializeExampleWeights(ds.examples.size());
		initializeHypothesisWeights(learners.size());
	}

	public void train(DataSet ds) {
		initializeExampleWeights(ds.examples.size());

		for (Learner learner : learners) {
			learner.train(ds);

			double error = calculateError(ds, learner);
			if (error < 0.0001) {
				break;
			}

			adjustExampleWeights(ds, learner, error);

			double newHypothesisWeight = learnerWeights.get(learner)
					* Math.log((1.0 - error) / error);
			learnerWeights.put(learner, newHypothesisWeight);
		}
	}

	public String predict(Example e) {
		return weightedMajority(e);
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

	//
	// PRIVATE METHODS
	//

	private String weightedMajority(Example e) {
		List<String> targetValues = dataSet.getPossibleAttributeValues(dataSet
				.getTargetAttributeName());

		Table<String, Learner, Double> table = createTargetValueLearnerTable(
				targetValues, e);
		return getTargetValueWithTheMaximumVotes(targetValues, table);
	}

	private Table<String, Learner, Double> createTargetValueLearnerTable(
			List<String> targetValues, Example e) {
		// create a table with target-attribute values as rows and learners as
		// columns and cells containing the weighted votes of each Learner for a
		// target value
		// Learner1 Learner2 Laerner3 .......
		// Yes 0.83 0.5 0
		// No 0 0 0.6

		Table<String, Learner, Double> table = new Table<String, Learner, Double>(
				targetValues, learners);
		// initialize table
		for (Learner l : learners) {
			for (String s : targetValues) {
				table.set(s, l, 0.0);
			}
		}
		for (Learner learner : learners) {
			String predictedValue = learner.predict(e);
			for (String v : targetValues) {
				if (predictedValue.equals(v)) {
					table.set(v, learner, table.get(v, learner)
							+ learnerWeights.get(learner) * 1);
				}
			}
		}
		return table;
	}

	private String getTargetValueWithTheMaximumVotes(List<String> targetValues,
			Table<String, Learner, Double> table) {
		String targetValueWithMaxScore = targetValues.get(0);
		double score = scoreOfValue(targetValueWithMaxScore, table, learners);
		for (String value : targetValues) {
			double scoreOfValue = scoreOfValue(value, table, learners);
			if (scoreOfValue > score) {
				targetValueWithMaxScore = value;
				score = scoreOfValue;
			}
		}
		return targetValueWithMaxScore;
	}

	private void initializeExampleWeights(int size) {
		if (size == 0) {
			throw new RuntimeException(
					"cannot initialize Ensemble learning with Empty Dataset");
		}
		double value = 1.0 / (1.0 * size);
		exampleWeights = new double[size];
		for (int i = 0; i < size; i++) {
			exampleWeights[i] = value;
		}
	}

	private void initializeHypothesisWeights(int size) {
		if (size == 0) {
			throw new RuntimeException(
					"cannot initialize Ensemble learning with Zero Learners");
		}

		learnerWeights = new Hashtable<Learner, Double>();
		for (Learner le : learners) {
			learnerWeights.put(le, 1.0);
		}
	}

	private double calculateError(DataSet ds, Learner l) {
		double error = 0.0;
		for (int i = 0; i < ds.examples.size(); i++) {
			Example e = ds.getExample(i);
			if (!(l.predict(e).equals(e.targetValue()))) {
				error = error + exampleWeights[i];
			}
		}
		return error;
	}

	private void adjustExampleWeights(DataSet ds, Learner l, double error) {
		double epsilon = error / (1.0 - error);
		for (int j = 0; j < ds.examples.size(); j++) {
			Example e = ds.getExample(j);
			if ((l.predict(e).equals(e.targetValue()))) {
				exampleWeights[j] = exampleWeights[j] * epsilon;
			}
		}
		exampleWeights = Util.normalize(exampleWeights);
	}

	private double scoreOfValue(String targetValue,
			Table<String, Learner, Double> table, List<Learner> learners) {
		double score = 0.0;
		for (Learner l : learners) {
			score += table.get(targetValue, l);
		}
		return score;
	}
}
