package aima.core.probability.hmm;

import aima.core.probability.RandomVariable;

/**
 * 
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 */
public class HMMForwardBackward {

	private RandomVariable stateVariable = null;

	public HMMForwardBackward(RandomVariable stateVariable) {
		if (!stateVariable.getDomain().isFinite()) {
			throw new IllegalArgumentException(
					"State Variable for HHM must be finite.");
		}
		this.stateVariable = stateVariable;
	}
}
