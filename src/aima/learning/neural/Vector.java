package aima.learning.neural;

import aima.util.Matrix;

public class Vector extends Matrix {

	// Vector is modelled as a matrix with a single column;
	public Vector(int size) {
		super(size, 1);
	}

	public double getValue(int i) {
		return super.get(i, 0);
	}

	public void setValue(int index, double value) {
		super.set(index, 0, value);
	}

	public Vector copyVector() {
		Vector result = new Vector(getRowDimension());
		for (int i = 0; i < getRowDimension(); i++) {
			result.setValue(i, getValue(i));
		}
		return result;
	}

	public int size() {
		return getRowDimension();
	}

}
