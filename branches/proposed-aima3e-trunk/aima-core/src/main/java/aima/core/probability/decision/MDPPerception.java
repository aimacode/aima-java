package aima.core.probability.decision;

/**
 * @author Ravi Mohan
 * 
 */
public class MDPPerception<STATE_TYPE> {

	private STATE_TYPE state;

	private double reward;

	public MDPPerception(STATE_TYPE state, double reward) {
		this.state = state;
		this.reward = reward;
	}

	public double getReward() {
		return reward;
	}

	public STATE_TYPE getState() {
		return state;
	}

	@Override
	public String toString() {
		return "[ " + state.toString() + " , " + reward + " ] ";
	}
}
