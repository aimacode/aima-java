package aima.core.probability.reasoning;

import aima.core.probability.RandomVariable;

/**
 * @author Ravi Mohan
 * 
 */
public class HMMAgent {
	private HiddenMarkovModel hmm;

	private RandomVariable belief;

	public HMMAgent(HiddenMarkovModel hmm) {
		this.hmm = hmm;
		this.belief = hmm.prior().duplicate();
	}

	public RandomVariable belief() {
		return belief;
	}

	public void act(String action) {
		belief = hmm.predict(belief, action);
	}

	public void waitWithoutActing() {
		act(HmmConstants.DO_NOTHING);
	}

	public void perceive(String perception) {
		belief = hmm.perceptionUpdate(belief, perception);
	}
}
