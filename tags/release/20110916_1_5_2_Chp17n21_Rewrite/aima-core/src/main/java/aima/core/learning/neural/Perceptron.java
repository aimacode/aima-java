package aima.core.learning.neural;

import aima.core.util.math.Matrix;
import aima.core.util.math.Vector;

/**
 * @author Ravi Mohan
 * @author Mike Stampone
 */
public class Perceptron implements FunctionApproximator {

	private final Layer layer;
	private Vector lastInput;

	public Perceptron(int numberOfNeurons, int numberOfInputs) {

		this.layer = new Layer(numberOfNeurons, numberOfInputs, 2.0, -2.0,
				new HardLimitActivationFunction());

	}

	public Vector processInput(Vector input) {
		lastInput = input;
		return layer.feedForward(input);
	}

	public void processError(Vector error) {
		Matrix weightUpdate = error.times(lastInput.transpose());
		layer.acceptNewWeightUpdate(weightUpdate);

		Vector biasUpdate = layer.getBiasVector().plus(error);
		layer.acceptNewBiasUpdate(biasUpdate);

	}

	/**
	 * Induces the layer of this perceptron from the specified set of examples
	 * 
	 * @param innds
	 *            a set of training examples for constructing the layer of this
	 *            perceptron.
	 * @param numberofEpochs
	 *            the number of training epochs to be used.
	 */
	public void trainOn(NNDataSet innds, int numberofEpochs) {
		for (int i = 0; i < numberofEpochs; i++) {
			innds.refreshDataset();
			while (innds.hasMoreExamples()) {
				NNExample nne = innds.getExampleAtRandom();
				processInput(nne.getInput());
				Vector error = layer.errorVectorFrom(nne.getTarget());
				processError(error);
			}
		}
	}

	/**
	 * Returns the outcome predicted for the specified example
	 * 
	 * @param nne
	 *            an example
	 * 
	 * @return the outcome predicted for the specified example
	 */
	public Vector predict(NNExample nne) {
		return processInput(nne.getInput());
	}

	/**
	 * Returns the accuracy of the hypothesis on the specified set of examples
	 * 
	 * @param nnds
	 *            the neural network data set to be tested on.
	 * 
	 * @return the accuracy of the hypothesis on the specified set of examples
	 */
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
}
