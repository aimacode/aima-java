package aima.core.probability.decision;

import java.util.Hashtable;

/**
 * @author Ravi Mohan
 * 
 */
public class MDPUtilityFunction<STATE_TYPE> {
	private Hashtable<STATE_TYPE, Double> hash;

	public MDPUtilityFunction() {
		hash = new Hashtable<STATE_TYPE, Double>();
	}

	public Double getUtility(STATE_TYPE state) {
		Double d = hash.get(state);
		if (d == null) {
			System.out.println("no value for " + state);
		}
		return d;
	}

	public void setUtility(STATE_TYPE state, double utility) {
		hash.put(state, utility);
	}

	public MDPUtilityFunction<STATE_TYPE> copy() {
		MDPUtilityFunction<STATE_TYPE> other = new MDPUtilityFunction<STATE_TYPE>();
		for (STATE_TYPE state : hash.keySet()) {
			other.setUtility(state, hash.get(state));
		}
		return other;
	}

	@Override
	public String toString() {
		return hash.toString();
	}

	public boolean hasUtilityFor(STATE_TYPE state) {

		return hash.keySet().contains(state);
	}
}
