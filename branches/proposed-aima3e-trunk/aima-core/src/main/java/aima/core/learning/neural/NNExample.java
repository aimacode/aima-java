package aima.core.learning.neural;

import java.util.ArrayList;
import java.util.List;

import aima.core.util.math.Vector;

/**
 * @author Ravi Mohan
 * 
 */
public class NNExample {
	private final List<Double> normalizedInput, normalizedTarget;

	public NNExample(List<Double> normalizedInput, List<Double> normalizedTarget) {
		this.normalizedInput = normalizedInput;
		this.normalizedTarget = normalizedTarget;
	}

	public NNExample copyExample() {
		List<Double> newInput = new ArrayList<Double>();
		List<Double> newTarget = new ArrayList<Double>();
		for (Double d : normalizedInput) {
			newInput.add(new Double(d.doubleValue()));
		}
		for (Double d : normalizedTarget) {
			newTarget.add(new Double(d.doubleValue()));
		}
		return new NNExample(newInput, newTarget);
	}

	public Vector getInput() {
		Vector v = new Vector(normalizedInput);
		return v;

	}

	public Vector getTarget() {
		Vector v = new Vector(normalizedTarget);
		return v;

	}

	public boolean isCorrect(Vector prediction) {
		/*
		 * compares the index having greatest value in target to indec having
		 * greatest value in prediction. Ifidentical, correct
		 */
		return getTarget().indexHavingMaxValue() == prediction
				.indexHavingMaxValue();
	}
}
