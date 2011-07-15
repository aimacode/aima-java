package aima.core.probability.hmm;

import java.util.List;

import aima.core.probability.CategoricalDistribution;
import aima.core.probability.RandomVariable;
import aima.core.probability.proposition.AssignmentProposition;
import aima.core.probability.temporal.ForwardBackwardInference;

/**
 * 
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 */
public class HMMForwardBackward implements ForwardBackwardInference {

	private RandomVariable stateVariable = null;

	public HMMForwardBackward(RandomVariable stateVariable) {
		if (!stateVariable.getDomain().isFinite()) {
			throw new IllegalArgumentException(
					"State Variable for HHM must be finite.");
		}
		this.stateVariable = stateVariable;
	}
	
	//
	// START-ForwardBackwardInference
	@Override
	public List<CategoricalDistribution> forwardBackward(
			List<List<AssignmentProposition>> ev, CategoricalDistribution prior) {
		throw new UnsupportedOperationException("TODO");
	}
	
	@Override
	public CategoricalDistribution forward(CategoricalDistribution f1_t,
			List<AssignmentProposition> e_tp1) {
		throw new UnsupportedOperationException("TODO");
	}
	
	@Override
	public CategoricalDistribution backward(CategoricalDistribution b_kp2t,
			List<AssignmentProposition> e_kp1) {
		throw new UnsupportedOperationException("TODO");
	}
	
	// END-ForwardBackwardInference
	//
}
