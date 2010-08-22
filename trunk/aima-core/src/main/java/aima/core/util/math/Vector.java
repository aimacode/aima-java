package aima.core.util.math;

import java.util.List;

/**
 * @author Ravi Mohan
 * 
 */
public class Vector extends Matrix {
	private static final long serialVersionUID = 1L;

	// Vector is modelled as a matrix with a single column;
	public Vector(int size) {
		super(size, 1);
	}

	public Vector(List<Double> lst) {
		super(lst.size(), 1);
		for (int i = 0; i < lst.size(); i++) {
			setValue(i, lst.get(i));
		}
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

	public Vector minus(Vector v) {
		Vector result = new Vector(size());
		for (int i = 0; i < size(); i++) {
			result.setValue(i, getValue(i) - v.getValue(i));
		}
		return result;
	}

	public Vector plus(Vector v) {
		Vector result = new Vector(size());
		for (int i = 0; i < size(); i++) {
			result.setValue(i, getValue(i) + v.getValue(i));
		}
		return result;
	}

	public int indexHavingMaxValue() {
		if (size() <= 0) {
			throw new RuntimeException("can't perform this op on empty vector");
		}
		int res = 0;
		for (int i = 0; i < size(); i++) {
			if (getValue(i) > getValue(res)) {
				res = i;
			}
		}
		return res;
	}
}
