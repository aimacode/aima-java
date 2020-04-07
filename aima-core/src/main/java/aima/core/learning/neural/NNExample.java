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
		List<Double> newInput = new ArrayList<>(normalizedInput);
		List<Double> newTarget = new ArrayList<>(normalizedTarget);
		return new NNExample(newInput, newTarget);
	}

	public Vector getInput() {
		return new Vector(normalizedInput);
	}

	public Vector getTarget() {
		return new Vector(normalizedTarget);
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
