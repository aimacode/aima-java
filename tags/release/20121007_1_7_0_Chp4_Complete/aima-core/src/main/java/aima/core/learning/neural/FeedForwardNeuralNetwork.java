package aima.core.learning.neural;

import aima.core.learning.framework.DataSet;
import aima.core.util.math.Matrix;
import aima.core.util.math.Vector;

/**
 * @author Ravi Mohan
 * 
 */
public class FeedForwardNeuralNetwork implements FunctionApproximator {

	public static final String UPPER_LIMIT_WEIGHTS = "upper_limit_weights";
	public static final String LOWER_LIMIT_WEIGHTS = "lower_limit_weights";
	public static final String NUMBER_OF_OUTPUTS = "number_of_outputs";
	public static final String NUMBER_OF_HIDDEN_NEURONS = "number_of_hidden_neurons";
	public static final String NUMBER_OF_INPUTS = "number_of_inputs";
	//
	private final Layer hiddenLayer;
	private final Layer outputLayer;

	private NNTrainingScheme trainingScheme;

	/*
	 * constructor to be used for non testing code.
	 */
	public FeedForwardNeuralNetwork(NNConfig config) {

		int numberOfInputNeurons = config
				.getParameterAsInteger(NUMBER_OF_INPUTS);
		int numberOfHiddenNeurons = config
				.getParameterAsInteger(NUMBER_OF_HIDDEN_NEURONS);
		int numberOfOutputNeurons = config
				.getParameterAsInteger(NUMBER_OF_OUTPUTS);

		double lowerLimitForWeights = config
				.getParameterAsDouble(LOWER_LIMIT_WEIGHTS);
		double upperLimitForWeights = config
				.getParameterAsDouble(UPPER_LIMIT_WEIGHTS);

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

	public void trainOn(NNDataSet innds, int numberofEpochs) {
		for (int i = 0; i < numberofEpochs; i++) {
			innds.refreshDataset();
			while (innds.hasMoreExamples()) {
				NNExample nne = innds.getExampleAtRandom();
				processInput(nne.getInput());
				Vector error = getOutputLayer()
						.errorVectorFrom(nne.getTarget());
				processError(error);
			}
		}

	}

	public Vector predict(NNExample nne) {
		return processInput(nne.getInput());
	}

	public int[] testOnDataSet(NNDataSet nnds) {
		int[] result = new int[] { 0, 0 };
		nnds.refreshDataset();
		while (nnds.hasMoreExamples()) {
			NNExample nne = nnds.getExampleAtRandom();
			Vector prediction = predict(nne);
			if (nne.isCorrect(prediction)) {
				result[0] = result[0] + 1;
			} else {
				result[1] = result[1] + 1;
			}
		}
		return result;
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
