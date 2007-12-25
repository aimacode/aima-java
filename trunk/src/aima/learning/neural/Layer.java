package aima.learning.neural;

import java.util.ArrayList;
import java.util.List;

import aima.learning.statistics.ActivationFunction;
import aima.util.Matrix;
import aima.util.Util;

public class Layer {
	// vectors are represented by n* 1 matrices;
	private final Matrix weightMatrix;

	Vector biasVector, lastBiasUpdateVector;

	private final ActivationFunction activationFunction;

	private Vector lastActivationValues, lastInducedField;

	private Matrix lastSensitivityMatrix;

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
		// sensitivityMatrix = new Matrix(weightMatrix.getRowDimension(),
		// weightMatrix.getColumnDimension());
		this.biasVector = biasVector;
		lastBiasUpdateVector = new Vector(biasVector.getRowDimension());
		penultimateBiasUpdateVector = new Vector(biasVector.getRowDimension());
	}

	public Layer(int numberOfNeurons, int numberOfInputs,
			double lowerLimitForWeights, double upperLimitForWeights,
			ActivationFunction af) {
		// sensitivityMatrix = new Matrix(numberOfNeurons, numberOfInputs);
		activationFunction = af;
		this.weightMatrix = new Matrix(numberOfNeurons, numberOfInputs());
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

	public Matrix sensitivityMatrixFromErrorMatrix(Vector errorVector) {
		Matrix derivativeMatrix = createDerivativeMatrix(lastInducedField);
		Matrix sensitivityMatrix = derivativeMatrix.times(errorVector).times(
				-2.0);
		lastSensitivityMatrix = sensitivityMatrix.copy();
		return sensitivityMatrix;
	}

	public Matrix sensitivityMatrixFromSucceedingLayer(Layer nextLayer) {
		Matrix derivativeMatrix = createDerivativeMatrix(lastInducedField);
		Matrix weightTranspose = nextLayer.weightMatrix.transpose();
		Matrix sensitivityMatrix = derivativeMatrix.times(weightTranspose)
				.times(nextLayer.getSensitivityMatrix());
		lastSensitivityMatrix = sensitivityMatrix.copy();
		return sensitivityMatrix;
	}

	private Matrix getSensitivityMatrix() {

		return lastSensitivityMatrix;
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

	private static void initializeMatrix(Matrix aMatrix, double lowerLimit,
			double upperLimit) {
		for (int i = 0; i < aMatrix.getRowDimension(); i++) {
			for (int j = 0; j < aMatrix.getColumnDimension(); i++) {
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

	private Matrix createDerivativeMatrix(Vector lastInducedField) {
		List<Double> lst = new ArrayList<Double>();
		for (int i = 0; i < lastInducedField.size(); i++) {
			lst.add(new Double(activationFunction.deriv(lastInducedField
					.getValue(i))));
		}
		return Matrix.createDiagonalMatrix(lst);
	}

	public Matrix calculateWeightUpdates(Vector previousLayerActivationOrInput,
			double alpha) {
		Matrix activationTranspose = previousLayerActivationOrInput.transpose();
		Matrix weightUpdateMatrix = lastSensitivityMatrix.times(
				activationTranspose).times(alpha).times(-1.0);
		penultimateWeightUpdateMatrix = lastWeightUpdateMatrix.copy();
		lastWeightUpdateMatrix = weightUpdateMatrix.copy();
		return weightUpdateMatrix;
	}

	public Matrix calculateWeightUpdates(Vector previousLayerActivationOrInput,
			double alpha, double momentum) {
		Matrix activationTranspose = previousLayerActivationOrInput.transpose();
		Matrix momentumLessUpdate = lastSensitivityMatrix.times(
				activationTranspose).times(alpha).times(-1.0);
		Matrix updateWithMomentum = lastWeightUpdateMatrix.times(momentum)
				.plus(momentumLessUpdate.times(1.0 - momentum));
		penultimateWeightUpdateMatrix = lastWeightUpdateMatrix.copy(); // done
		// only
		// to
		// implement
		// VLBP
		// later
		lastWeightUpdateMatrix = updateWithMomentum.copy();
		return updateWithMomentum;
	}

	public Matrix getLastWeightUpdateMatrix() {
		return lastWeightUpdateMatrix;
	}

	public Matrix getPenultimateWeightUpdateMatrix() {
		return penultimateWeightUpdateMatrix;
	}

	public Vector calculateBiasUpdates(double alpha) {
		Matrix biasUpdateMatrix = lastSensitivityMatrix.times(alpha)
				.times(-1.0);

		Vector result = new Vector(biasUpdateMatrix.getRowDimension());
		for (int i = 0; i < biasUpdateMatrix.getRowDimension(); i++) {
			result.setValue(i, biasUpdateMatrix.get(i, 0));
		}
		penultimateBiasUpdateVector = lastBiasUpdateVector.copyVector();
		lastBiasUpdateVector = result.copyVector();
		return result;
	}

	public Vector calculateBiasUpdates(double alpha, double momentum) {
		Matrix biasUpdateMatrixWithoutMomentum = lastSensitivityMatrix.times(
				alpha).times(-1.0);
		;
		Matrix biasUpdateMatrixWithMomentum = lastBiasUpdateVector.times(
				momentum).plus(
				biasUpdateMatrixWithoutMomentum.times(1.0 - momentum));
		Vector result = new Vector(biasUpdateMatrixWithMomentum
				.getRowDimension());
		for (int i = 0; i < biasUpdateMatrixWithMomentum.getRowDimension(); i++) {
			result.setValue(i, biasUpdateMatrixWithMomentum.get(i, 0));
		}
		penultimateBiasUpdateVector = lastBiasUpdateVector.copyVector();
		lastBiasUpdateVector = result.copyVector();
		return result;
	}

	public Vector getLastBiasUpdateVector() {
		return lastBiasUpdateVector;
	}

	public Vector getPenultimateBiasUpdateVector() {
		return penultimateBiasUpdateVector;
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
}