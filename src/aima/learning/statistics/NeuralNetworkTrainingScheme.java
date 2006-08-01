/*
 * Created on Aug 5, 2005
 *
 */
package aima.learning.statistics;

import java.util.List;


public interface NeuralNetworkTrainingScheme {
	void backPropogate(FeedForwardNetwork network,List<Double> input, List<Double> correctOutput);
	void updateWeightsAndBiases(FeedForwardNetwork network);
	double error(List<Double> expectedOutput,FeedForwardNetwork network);

}
