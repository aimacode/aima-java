package aima.probability;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import aima.util.Util;

public class RandomVariable {
	private String name;

	private Hashtable<String, Double> distribution;

	private List<String> states;

	public RandomVariable(String name, List<String> states) {
		this.name = name;
		this.states = states;
		this.distribution = new Hashtable<String, Double>();
		int numberOfStates = states.size();
		double initialProbability = 1.0 / numberOfStates;
		for (String s : states) {
			distribution.put(s, initialProbability);
		}
	}

	private RandomVariable(String name, List<String> states,
			Hashtable<String, Double> distribution) {
		this.name = name;
		this.states = states;
		this.distribution = distribution;
	}

	public void setProbabilityOf(String state, Double probability) {
		if (states.contains(state)) {
			distribution.put(state, probability);
		} else {
			throw new RuntimeException(state + "  is an invalid state");
		}
	}

	public double getProbabilityOf(String state) {
		if (states.contains(state)) {
			return distribution.get(state);
		} else {
			throw new RuntimeException(state + "  is an invalid state");
		}
	}

	public List<String> states() {
		return states;
	}

	public RandomVariable duplicate() {
		Hashtable<String, Double> probs = new Hashtable<String, Double>();
		for (String key : distribution.keySet()) {
			probs.put(key, distribution.get(key));
		}
		return new RandomVariable(name, states, probs);

	}

	public void normalize() {
		List<Double> probs = new ArrayList<Double>();
		for (String s : states) {
			probs.add(distribution.get(s));
		}
		List<Double> newProbs = Util.normalize(probs);
		for (int i = 0; i < states.size(); i++) {
			distribution.put(states.get(i), newProbs.get(i));
		}
	}

}
