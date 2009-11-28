package aima.core.probability.reasoning;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import aima.core.probability.RandomVariable;
import aima.core.probability.Randomizer;

/**
 * @author Ravi Mohan
 * 
 */
public class ParticleSet {

	private List<Particle> particles;

	private HiddenMarkovModel hmm;

	public ParticleSet(HiddenMarkovModel hmm) {
		particles = new ArrayList<Particle>();
		this.hmm = hmm;
	}

	// Use these two to get the filtered set directly. This is the only method a
	// user class needs to call.
	// The other methods are public only to be accessible to tests.

	public ParticleSet filter(String perception, Randomizer r) {
		return generateParticleSetForPredictedState(r).perceptionUpdate(
				perception, r);
	}

	public ParticleSet filter(String action, String perception, Randomizer r) {
		return generateParticleSetForPredictedState(action, r)
				.perceptionUpdate(perception, r);
	}

	// these are internal methods. public only to facilitate testing
	public int numberOfParticlesWithState(String state) {
		int total = 0;
		for (Particle p : particles) {
			if (p.hasState(state)) {
				total += 1;
			}
		}
		return total;
	}

	public void add(Particle particle) {
		particles.add(particle);

	}

	public int size() {
		return particles.size();
	}

	public RandomVariable toRandomVariable() {
		List<String> states = new ArrayList<String>();
		Hashtable<String, Integer> stateCount = new Hashtable<String, Integer>();
		for (Particle p : particles) {
			String state = p.getState();
			if (!(states.contains(state))) {
				states.add(state);
				stateCount.put(state, 0);
			}

			stateCount.put(state, stateCount.get(state).intValue() + 1);

		}

		RandomVariable result = new RandomVariable(states);
		for (String state : stateCount.keySet()) {
			result.setProbabilityOf(state,
					((double) stateCount.get(state) / particles.size()));
		}
		return result;
	}

	public ParticleSet generateParticleSetForPredictedState(
			Randomizer randomizer) {
		return generateParticleSetForPredictedState(HmmConstants.DO_NOTHING,
				randomizer);
	}

	public ParticleSet generateParticleSetForPredictedState(String action,
			Randomizer randomizer) {
		ParticleSet predictedParticleSet = new ParticleSet(this.hmm);
		for (Particle p : particles) {
			String newState = hmm.transitionModel().getStateForProbability(
					p.getState(), action, randomizer.nextDouble());

			Particle generatedParticle = new Particle(newState);
			predictedParticleSet.add(generatedParticle);
		}
		return predictedParticleSet;
	}

	public ParticleSet perceptionUpdate(String perception, Randomizer r) {
		// compute Particle Weight
		for (Particle p : particles) {
			double particleWeight = hmm.sensorModel().get(p.getState(),
					perception);
			p.setWeight(particleWeight);
		}

		// weighted sample to create new ParticleSet
		ParticleSet result = new ParticleSet(hmm);
		while (result.size() != size()) {
			for (Particle p : particles) {
				double probability = r.nextDouble();
				if (probability <= p.getWeight()) {
					if (result.size() < size()) {
						result.add(new Particle(p.getState(), p.getWeight()));
					}
				}
			}

		}
		return result;
	}

	public Particle getParticle(int i) {
		return particles.get(i);
	}
}
