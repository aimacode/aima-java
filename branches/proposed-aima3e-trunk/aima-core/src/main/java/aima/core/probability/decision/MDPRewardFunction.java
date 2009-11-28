package aima.core.probability.decision;

import java.util.Hashtable;

/**
 * @author Ravi Mohan
 * 
 */
public class MDPRewardFunction<STATE_TYPE> {
	Hashtable<STATE_TYPE, Double> stateToReward;

	public MDPRewardFunction() {
		stateToReward = new Hashtable<STATE_TYPE, Double>();
	}

	public double getRewardFor(STATE_TYPE state) {
		return stateToReward.get(state);
	}

	public void setReward(STATE_TYPE state, Double reward) {
		stateToReward.put(state, reward);
	}

	@Override
	public String toString() {
		return stateToReward.toString();
	}

	public MDPUtilityFunction<STATE_TYPE> asUtilityFunction() {
		MDPUtilityFunction<STATE_TYPE> uf = new MDPUtilityFunction<STATE_TYPE>();
		for (STATE_TYPE state : stateToReward.keySet()) {
			uf.setUtility(state, getRewardFor(state));
		}
		return uf;
	}
}
