package aima.learning.neural;

import aima.util.Matrix;

public class LayerSensitivity {
	/*
	 * contains sensitivity matrices and related calculations for each layer.
	 * Used for backprop learning
	 */

	private final Matrix sensitivityMatrix;

	public LayerSensitivity(Layer layer) {
		Matrix weightMatrix = layer.getWeightMatrix();
		this.sensitivityMatrix = new Matrix(weightMatrix.getRowDimension(),
				weightMatrix.getColumnDimension());

	}

	public Matrix getSensitivityMatrix() {
		return sensitivityMatrix;
	}

}
