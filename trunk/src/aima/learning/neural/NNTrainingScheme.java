package aima.learning.neural;

public interface NNTrainingScheme {
	Vector processInput(FeedForwardNeuralNetwork network, Vector input);

	void processError(FeedForwardNeuralNetwork network, Vector error);
}
