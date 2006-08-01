/*
 * Created on Aug 6, 2005
 *
 */
package aima.learning.learners;

import java.util.List;

import aima.learning.framework.DataSet;
import aima.learning.framework.Example;
import aima.learning.framework.Learner;
import aima.learning.statistics.FeedForwardNetwork;
import aima.learning.statistics.IrisDataSetNumerizer;
import aima.learning.statistics.NeuralNetworkTrainingScheme;
import aima.util.Pair;
import aima.util.Util;

public class NeuralNetLearner implements Learner {

	private FeedForwardNetwork network;

	private IrisDataSetNumerizer numerizer;

	private int numberOfIterations;

	private NeuralNetworkTrainingScheme trainingScheme;

	public NeuralNetLearner(FeedForwardNetwork network,
			IrisDataSetNumerizer numerizer,NeuralNetworkTrainingScheme scheme, int numberOfIterations) {
		this.network = network;
		this.numerizer = numerizer;
		this.numberOfIterations = numberOfIterations;
		this.trainingScheme = scheme;
	}

	public void train(DataSet ds) {
		for (int i = 0; i < numberOfIterations; i++) {
			double iterationError = 0;
			for (Example e : ds.examples) {
				Pair<List<Double>, List<Double>> io = numerizer.numerize(e);
				List<Double> input = io.getFirst();
				List<Double> expectedOutput = io.getSecond();

				
				trainingScheme.backPropogate(network, input, expectedOutput);
				trainingScheme.updateWeightsAndBiases(network);
				iterationError += trainingScheme.error(expectedOutput, network);
			}
		
		}

	}

	public String predict(Example e) {
		Pair<List<Double>, List<Double>> io = numerizer.numerize(e);
		List<Double> input = io.getFirst();
		List<Double> expectedOutput = io.getSecond();
		
		network.propogateInput(input);
		List<Double> actualOutput = network.output();
		String output = numerizer.denumerize(actualOutput);
		return output;
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

}
