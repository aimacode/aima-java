package aima.learning.neural;

import java.util.ArrayList;
import java.util.List;

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

}
