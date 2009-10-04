package aima.learning.neural;

import java.util.ArrayList;
import java.util.List;

import aima.util.Matrix;

public class LayerSensitivity {
	/*
	 * contains sensitivity matrices and related calculations for each layer.
	 * Used for backprop learning
	 */

	private Matrix sensitivityMatrix;
	private final Layer layer;

	public LayerSensitivity(Layer layer) {
		Matrix weightMatrix = layer.getWeightMatrix();
		this.sensitivityMatrix = new Matrix(weightMatrix.getRowDimension(),
				weightMatrix.getColumnDimension());
		this.layer = layer;

	}

	public Matrix getSensitivityMatrix() {
		return sensitivityMatrix;
	}

	public Matrix sensitivityMatrixFromErrorMatrix(Vector errorVector) {
		Matrix derivativeMatrix = createDerivativeMatrix(layer
				.getLastInducedField());
		Matrix calculatedSensitivityMatrix = derivativeMatrix
				.times(errorVector).times(-2.0);
		sensitivityMatrix = calculatedSensitivityMatrix.copy();
		return calculatedSensitivityMatrix;
	}

	public Matrix sensitivityMatrixFromSucceedingLayer(
			LayerSensitivity nextLayerSensitivity) {
		Layer nextLayer = nextLayerSensitivity.getLayer();
		Matrix derivativeMatrix = createDerivativeMatrix(layer
				.getLastInducedField());
		Matrix weightTranspose = nextLayer.getWeightMatrix().transpose();
		Matrix calculatedSensitivityMatrix = derivativeMatrix.times(
				weightTranspose).times(
				nextLayerSensitivity.getSensitivityMatrix());
		sensitivityMatrix = calculatedSensitivityMatrix.copy();
		return sensitivityMatrix;
	}

	private Matrix createDerivativeMatrix(Vector lastInducedField) {
		List<Double> lst = new ArrayList<Double>();
		for (int i = 0; i < lastInducedField.size(); i++) {
			lst.add(new Double(layer.getActivationFunction().deriv(
					lastInducedField.getValue(i))));
		}
		return Matrix.createDiagonalMatrix(lst);
	}

	public Layer getLayer() {
		return layer;
	}

}
