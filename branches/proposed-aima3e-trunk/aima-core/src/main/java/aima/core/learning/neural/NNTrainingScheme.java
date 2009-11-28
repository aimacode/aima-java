package aima.core.learning.neural;

import aima.core.util.math.Vector;

/**
 * @author Ravi Mohan
 * 
 */
public interface NNTrainingScheme {
	Vector processInput(FeedForwardNeuralNetwork network, Vector input);

	void processError(FeedForwardNeuralNetwork network, Vector error);

	void setNeuralNetwork(FunctionApproximator ffnn);
}
