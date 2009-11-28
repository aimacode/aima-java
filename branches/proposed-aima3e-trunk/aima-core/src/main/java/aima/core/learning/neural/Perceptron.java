package aima.core.learning.neural;

import aima.core.util.math.Matrix;
import aima.core.util.math.Vector;

/**
 * @author Ravi Mohan
 * 
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
}
