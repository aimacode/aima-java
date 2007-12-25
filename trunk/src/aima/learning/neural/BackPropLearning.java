package aima.learning.neural;

import aima.util.Matrix;

public class BackPropLearning implements NNTrainingScheme {
	private final double learningRate;
	private final double momentum;

	private final Layer hiddenLayer;
	private final Layer outputLayer;
	private final LayerSensitivity hiddenSensitivity;
	private final LayerSensitivity outputSensitivity;

	public BackPropLearning(FeedForwardNeuralNetwork network,
			double learningRate, double momentum) {

		this.hiddenLayer = network.getHiddenLayer();
		this.outputLayer = network.getOutputLayer();
		hiddenSensitivity = new LayerSensitivity(hiddenLayer);
		outputSensitivity = new LayerSensitivity(outputLayer);
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
		outputLayer.setSensitivityMatrix(outputSensitivity
				.sensitivityMatrixFromErrorMatrix(error));

		hiddenLayer.setSensitivityMatrix(hiddenSensitivity
				.sensitivityMatrixFromSucceedingLayer(outputLayer));

		// calculate weight Updates
		calculateWeightUpdates(outputSensitivity, hiddenLayer
				.getLastActivationValues(), learningRate, momentum);
		calculateWeightUpdates(hiddenSensitivity, hiddenLayer
				.getLastInputValues(), learningRate, momentum);

		// calculate Bias Updates
		calculateBiasUpdates(outputSensitivity, learningRate, momentum);
		calculateBiasUpdates(hiddenSensitivity, learningRate, momentum);

		// update weightsAndBiases
		outputLayer.updateWeights();
		outputLayer.updateBiases();

		hiddenLayer.updateWeights();
		hiddenLayer.updateBiases();

	}

	public Matrix calculateWeightUpdates(LayerSensitivity layerSensitivity,
			Vector previousLayerActivationOrInput, double alpha, double momentum) {
		Layer layer = layerSensitivity.getLayer();
		Matrix activationTranspose = previousLayerActivationOrInput.transpose();
		Matrix momentumLessUpdate = layer.getSensitivityMatrix().times(
				activationTranspose).times(alpha).times(-1.0);
		Matrix updateWithMomentum = layer.getLastWeightUpdateMatrix().times(
				momentum).plus(momentumLessUpdate.times(1.0 - momentum));
		layer.setPenultimateWeightUpdateMatrix(layer
				.getLastWeightUpdateMatrix().copy()); // done
		// only
		// to
		// implement
		// VLBP
		// later
		layer.setLastWeightUpdateMatrix(updateWithMomentum.copy());
		return updateWithMomentum;
	}

	public static Matrix calculateWeightUpdates(
			LayerSensitivity layerSensitivity,
			Vector previousLayerActivationOrInput, double alpha) {
		Layer layer = layerSensitivity.getLayer();
		Matrix activationTranspose = previousLayerActivationOrInput.transpose();
		Matrix weightUpdateMatrix = layerSensitivity.getSensitivityMatrix()
				.times(activationTranspose).times(alpha).times(-1.0);
		layer.setPenultimateWeightUpdateMatrix(layer
				.getLastWeightUpdateMatrix().copy());
		layer.setLastWeightUpdateMatrix(weightUpdateMatrix.copy());
		return weightUpdateMatrix;
	}

	public Vector calculateBiasUpdates(LayerSensitivity layerSensitivity,
			double alpha, double momentum) {
		Layer layer = layerSensitivity.getLayer();
		Matrix biasUpdateMatrixWithoutMomentum = layer.getSensitivityMatrix()
				.times(alpha).times(-1.0);

		Matrix biasUpdateMatrixWithMomentum = layer.getLastBiasUpdateVector()
				.times(momentum).plus(
						biasUpdateMatrixWithoutMomentum.times(1.0 - momentum));
		Vector result = new Vector(biasUpdateMatrixWithMomentum
				.getRowDimension());
		for (int i = 0; i < biasUpdateMatrixWithMomentum.getRowDimension(); i++) {
			result.setValue(i, biasUpdateMatrixWithMomentum.get(i, 0));
		}
		layer.setPenultimateBiasUpdateVector(layer.getLastBiasUpdateVector()
				.copyVector());
		layer.setLastBiasUpdateVector(result.copyVector());
		return result;
	}

	public static Vector calculateBiasUpdates(
			LayerSensitivity layerSensitivity, double alpha) {
		Layer layer = layerSensitivity.getLayer();
		Matrix biasUpdateMatrix = layer.getSensitivityMatrix().times(alpha)
				.times(-1.0);

		Vector result = new Vector(biasUpdateMatrix.getRowDimension());
		for (int i = 0; i < biasUpdateMatrix.getRowDimension(); i++) {
			result.setValue(i, biasUpdateMatrix.get(i, 0));
		}
		layer.setPenultimateBiasUpdateVector(layer.getLastBiasUpdateVector()
				.copyVector());
		layer.setLastBiasUpdateVector(result.copyVector());
		return result;
	}
}
