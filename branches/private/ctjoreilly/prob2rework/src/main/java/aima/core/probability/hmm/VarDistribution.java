package aima.core.probability.hmm;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import aima.core.util.Util;
import aima.core.util.math.Matrix;

/**
 * @author Ravi Mohan
 * 
 */
public class VarDistribution {
	private String name;

	private Hashtable<String, Double> distribution;

	private List<String> states;

	public VarDistribution(List<String> states) {
		this("HiddenState", states);
	}

	public VarDistribution(String name, List<String> states) {
		this.name = name;
		this.states = states;
		this.distribution = new Hashtable<String, Double>();
		int numberOfStates = states.size();
		double initialProbability = 1.0 / numberOfStates;
		for (String s : states) {
			distribution.put(s, initialProbability);
		}
	}

	private VarDistribution(String name, List<String> states,
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

	public VarDistribution duplicate() {
		Hashtable<String, Double> probs = new Hashtable<String, Double>();
		for (String key : distribution.keySet()) {
			probs.put(key, distribution.get(key));
		}
		return new VarDistribution(name, states, probs);

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

	public Matrix asMatrix() {
		Matrix m = new Matrix(states.size(), 1);
		for (int i = 0; i < states.size(); i++) {
			m.set(i, 0, distribution.get(states.get(i)));
		}
		return m;

	}

	public void updateFrom(Matrix aMatrix) {
		for (int i = 0; i < states.size(); i++) {
			distribution.put(states.get(i), aMatrix.get(i, 0));
		}

	}

	public VarDistribution createUnitBelief() {
		VarDistribution result = duplicate();
		for (String s : states()) {
			result.setProbabilityOf(s, 1.0);
		}
		return result;
	}

	@Override
	public String toString() {
		return asMatrix().toString();
	}
	/**
	 * <pre>
	 * public ParticleSet toParticleSet(HiddenMarkovModel hmm, Randomizer randomizer,
	 * 		int numberOfParticles) {
	 * 	ParticleSet result = new ParticleSet(hmm);
	 * 	for (int i = 0; i &lt; numberOfParticles; i++) {
	 * 		double rvalue = randomizer.nextDouble();
	 * 		String state = getStateForRandomNumber(rvalue);
	 * 		result.add(new Particle(state, 0));
	 * 	}
	 * 	return result;
	 * }
	 * 
	 * private String getStateForRandomNumber(double rvalue) {
	 * 	double total = 0.0;
	 * 	for (String s : states) {
	 * 		total = total + distribution.get(s);
	 * 		if (total &gt;= rvalue) {
	 * 			return s;
	 * 		}
	 * 	}
	 * 	throw new RuntimeException(&quot;cannot handle &quot; + rvalue);
	 * }
	 * </pre>
	 */
}
