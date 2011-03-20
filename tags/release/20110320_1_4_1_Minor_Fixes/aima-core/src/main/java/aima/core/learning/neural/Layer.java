package aima.core.learning.neural;

import aima.core.util.Util;
import aima.core.util.math.Matrix;
import aima.core.util.math.Vector;

/**
 * @author Ravi Mohan
 * 
 */
public class Layer {
	// vectors are represented by n * 1 matrices;
	private final Matrix weightMatrix;

	Vector biasVector, lastBiasUpdateVector;

	private final ActivationFunction activationFunction;

	private Vector lastActivationValues, lastInducedField;

	private Matrix lastWeightUpdateMatrix;

	private Matrix penultimateWeightUpdateMatrix;

	private Vector penultimateBiasUpdateVector;

	private Vector lastInput;

	public Layer(Matrix weightMatrix, Vector biasVector, ActivationFunction af) {

		activationFunction = af;
		this.weightMatrix = weightMatrix;
		lastWeightUpdateMatrix = new Matrix(weightMatrix.getRowDimension(),
				weightMatrix.getColumnDimension());
		penultimateWeightUpdateMatrix = new Matrix(weightMatrix
				.getRowDimension(), weightMatrix.getColumnDimension());

		this.biasVector = biasVector;
		lastBiasUpdateVector = new Vector(biasVector.getRowDimension());
		penultimateBiasUpdateVector = new Vector(biasVector.getRowDimension());
	}

	public Layer(int numberOfNeurons, int numberOfInputs,
			double lowerLimitForWeights, double upperLimitForWeights,
			ActivationFunction af) {

		activationFunction = af;
		this.weightMatrix = new Matrix(numberOfNeurons, numberOfInputs);
		lastWeightUpdateMatrix = new Matrix(weightMatrix.getRowDimension(),
				weightMatrix.getColumnDimension());
		penultimateWeightUpdateMatrix = new Matrix(weightMatrix
				.getRowDimension(), weightMatrix.getColumnDimension());

		this.biasVector = new Vector(numberOfNeurons);
		lastBiasUpdateVector = new Vector(biasVector.getRowDimension());
		penultimateBiasUpdateVector = new Vector(biasVector.getRowDimension());

		initializeMatrix(weightMatrix, lowerLimitForWeights,
				upperLimitForWeights);
		initializeVector(biasVector, lowerLimitForWeights, upperLimitForWeights);
	}

	public Vector feedForward(Vector inputVector) {
		lastInput = inputVector;
		Matrix inducedField = weightMatrix.times(inputVector).plus(biasVector);

		Vector inducedFieldVector = new Vector(numberOfNeurons());
		for (int i = 0; i < numberOfNeurons(); i++) {
			inducedFieldVector.setValue(i, inducedField.get(i, 0));
		}

		lastInducedField = inducedFieldVector.copyVector();
		Vector resultVector = new Vector(numberOfNeurons());
		for (int i = 0; i < numberOfNeurons(); i++) {
			resultVector.setValue(i, activationFunction
					.activation(inducedFieldVector.getValue(i)));
		}
		// set the result as the last activation value
		lastActivationValues = resultVector.copyVector();
		return resultVector;
	}

	public Matrix getWeightMatrix() {
		return weightMatrix;
	}

	public Vector getBiasVector() {
		return biasVector;
	}

	public int numberOfNeurons() {
		return weightMatrix.getRowDimension();
	}

	public int numberOfInputs() {
		return weightMatrix.getColumnDimension();
	}

	public Vector getLastActivationValues() {
		return lastActivationValues;
	}

	public Vector getLastInducedField() {
		return lastInducedField;
	}

	public Matrix getLastWeightUpdateMatrix() {
		return lastWeightUpdateMatrix;
	}

	public void setLastWeightUpdateMatrix(Matrix m) {
		lastWeightUpdateMatrix = m;
	}

	public Matrix getPenultimateWeightUpdateMatrix() {
		return penultimateWeightUpdateMatrix;
	}

	public void setPenultimateWeightUpdateMatrix(Matrix m) {
		penultimateWeightUpdateMatrix = m;
	}

	public Vector getLastBiasUpdateVector() {
		return lastBiasUpdateVector;
	}

	public void setLastBiasUpdateVector(Vector v) {
		lastBiasUpdateVector = v;
	}

	public Vector getPenultimateBiasUpdateVector() {
		return penultimateBiasUpdateVector;
	}

	public void setPenultimateBiasUpdateVector(Vector v) {
		penultimateBiasUpdateVector = v;
	}

	public void updateWeights() {
		weightMatrix.plusEquals(lastWeightUpdateMatrix);
	}

	public void updateBiases() {
		Matrix biasMatrix = biasVector.plusEquals(lastBiasUpdateVector);
		Vector result = new Vector(biasMatrix.getRowDimension());
		for (int i = 0; i < biasMatrix.getRowDimension(); i++) {
			result.setValue(i, biasMatrix.get(i, 0));
		}
		biasVector = result;
	}

	public Vector getLastInputValues() {

		return lastInput;

	}

	public ActivationFunction getActivationFunction() {

		return activationFunction;
	}

	public void acceptNewWeightUpdate(Matrix weightUpdate) {
		/*
		 * penultimate weightupdates maintained only to implement VLBP later
		 */
		setPenultimateWeightUpdateMatrix(getLastWeightUpdateMatrix());
		setLastWeightUpdateMatrix(weightUpdate);
	}

	public void acceptNewBiasUpdate(Vector biasUpdate) {
		setPenultimateBiasUpdateVector(getLastBiasUpdateVector());
		setLastBiasUpdateVector(biasUpdate);
	}

	public Vector errorVectorFrom(Vector target) {
		return target.minus(getLastActivationValues());

	}

	//
	// PRIVATE METHODS
	//
	private static void initializeMatrix(Matrix aMatrix, double lowerLimit,
			double upperLimit) {
		for (int i = 0; i < aMatrix.getRowDimension(); i++) {
			for (int j = 0; j < aMatrix.getColumnDimension(); j++) {
				double random = Util.generateRandomDoubleBetween(lowerLimit,
						upperLimit);
				aMatrix.set(i, j, random);
			}
		}

	}

	private static void initializeVector(Vector aVector, double lowerLimit,
			double upperLimit) {
		for (int i = 0; i < aVector.size(); i++) {

			double random = Util.generateRandomDoubleBetween(lowerLimit,
					upperLimit);
			aVector.setValue(i, random);
		}
	}
}