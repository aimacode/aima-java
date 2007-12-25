package aima.learning.neural;

public class BackPropLearning implements NNTrainingScheme {
	private final double learningRate;
	private final double momentum;

	private final Layer hiddenLayer;
	private final Layer outputLayer;

	public BackPropLearning(FeedForwardNeuralNetwork network,
			double learningRate, double momentum) {

		this.hiddenLayer = network.getHiddenLayer();
		this.outputLayer = network.getOutputLayer();
		this.learningRate = learningRate;
		this.momentum = momentum;
	}

	public Vector processInput(FeedForwardNeuralNetwork network, Vector input) {

		hiddenLayer.feedForward(input);
		outputLayer.feedForward(hiddenLayer.getLastActivationValues());
		return outputLayer.getLastActivationValues();
	}

	public void processError(FeedForwardNeuralNetwork network, Vector error) {
		// TODO calculate total error somewhere
		// create Sensitivity Matrices
		outputLayer.sensitivityMatrixFromErrorMatrix(error);
		hiddenLayer.sensitivityMatrixFromSucceedingLayer(network
				.getOutputLayer());

		// calculate weight Updates
		outputLayer.calculateWeightUpdates(hiddenLayer
				.getLastActivationValues(), learningRate, momentum);
		hiddenLayer.calculateWeightUpdates(hiddenLayer.getLastInputValues(),
				learningRate, momentum);

		// calculate Bias Updates
		outputLayer.calculateBiasUpdates(learningRate, momentum);
		hiddenLayer.calculateBiasUpdates(learningRate, momentum);

		// update weightsAndBiases
		outputLayer.updateWeights();
		outputLayer.updateBiases();

		hiddenLayer.updateWeights();
		hiddenLayer.updateBiases();

	}
}
