package aima.learning.neural;

import aima.learning.framework.DataSet;
import aima.util.Matrix;

public class FeedForwardNeuralNetwork implements FunctionApproximator {

	private final Layer hiddenLayer;
	private final Layer outputLayer;

	private NNTrainingScheme trainingScheme;

	/*
	 * constructor to be used for non testing code.
	 */
	public FeedForwardNeuralNetwork(NNConfig config) {

		int numberOfInputNeurons = config
				.getParameterAsInteger("number_of_inputs");
		int numberOfHiddenNeurons = config
				.getParameterAsInteger("number_of_hidden_neurons");
		int numberOfOutputNeurons = config
				.getParameterAsInteger("number_of_outputs");

		double lowerLimitForWeights = config
				.getParameterAsDouble("lower_limit_weights");
		double upperLimitForWeights = config
				.getParameterAsDouble("upper_limit_weights");

		hiddenLayer = new Layer(numberOfHiddenNeurons, numberOfInputNeurons,
				lowerLimitForWeights, upperLimitForWeights,
				new LogSigActivationFunction());

		outputLayer = new Layer(numberOfOutputNeurons, numberOfHiddenNeurons,
				lowerLimitForWeights, upperLimitForWeights,
				new PureLinearActivationFunction());

	}

	/*
	 * ONLY for testing to set up a network with known weights in future use to
	 * deserialize networks after adding variables for pen weightupdate,
	 * lastnput etc
	 */
	public FeedForwardNeuralNetwork(Matrix hiddenLayerWeights,
			Vector hiddenLayerBias, Matrix outputLayerWeights,
			Vector outputLayerBias) {

		hiddenLayer = new Layer(hiddenLayerWeights, hiddenLayerBias,
				new LogSigActivationFunction());
		outputLayer = new Layer(outputLayerWeights, outputLayerBias,
				new PureLinearActivationFunction());

	}

	public void processError(Vector error) {

		trainingScheme.processError(this, error);

	}

	public Vector processInput(Vector input) {
		return trainingScheme.processInput(this, input);
	}

	public void testOn(DataSet ds) {
		// TODO Auto-generated method stub
	}

	public Matrix getHiddenLayerWeights() {

		return hiddenLayer.getWeightMatrix();
	}

	public Vector getHiddenLayerBias() {

		return hiddenLayer.getBiasVector();
	}

	public Matrix getOutputLayerWeights() {

		return outputLayer.getWeightMatrix();
	}

	public Vector getOutputLayerBias() {

		return outputLayer.getBiasVector();
	}

	public Layer getHiddenLayer() {
		return hiddenLayer;
	}

	public Layer getOutputLayer() {
		return outputLayer;
	}

	public void setTrainingScheme(NNTrainingScheme trainingScheme) {
		this.trainingScheme = trainingScheme;
		trainingScheme.setNeuralNetwork(this);
	}

}
