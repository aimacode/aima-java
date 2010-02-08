package aima.core.learning.reinforcement;

import aima.core.probability.Randomizer;
import aima.core.probability.decision.MDP;
import aima.core.probability.decision.MDPPerception;

/**
 * @author Ravi Mohan
 * 
 */
public abstract class MDPAgent<STATE_TYPE, ACTION_TYPE> {

	protected MDP<STATE_TYPE, ACTION_TYPE> mdp;

	protected STATE_TYPE currentState;

	protected Double currentReward;

	protected STATE_TYPE previousState;

	protected ACTION_TYPE previousAction;

	public MDPAgent(MDP<STATE_TYPE, ACTION_TYPE> mdp) {
		this.mdp = mdp;
		this.currentState = mdp.getInitialState();
		this.currentReward = mdp.getRewardFor(currentState);

	}

	public MDPPerception<STATE_TYPE> execute(ACTION_TYPE action, Randomizer r) {
		MDPPerception<STATE_TYPE> perception = mdp.execute(currentState,
				action, r);
		updateFromPerception(perception);
		return perception;
	}

	public void updateFromPerception(MDPPerception<STATE_TYPE> perception) {
		currentState = perception.getState();
		currentReward = perception.getReward();
	}

	public void executeTrial(Randomizer r) {
		currentState = mdp.getInitialState();
		currentReward = mdp.getRewardFor(mdp.getInitialState());
		previousState = null;
		previousAction = null;
		MDPPerception<STATE_TYPE> perception = new MDPPerception<STATE_TYPE>(
				currentState, currentReward);
		ACTION_TYPE action = null;
		do {
			action = decideAction(perception);
			if (action != null) {
				perception = execute(action, r);
			}
		} while (action != null);
	}

	public abstract ACTION_TYPE decideAction(
			MDPPerception<STATE_TYPE> perception);

	public Double getCurrentReward() {
		return currentReward;
	}

	public void setCurrentReward(Double currentReward) {
		this.currentReward = currentReward;
	}

	public ACTION_TYPE getPreviousAction() {
		return previousAction;
	}

	public void setPreviousAction(ACTION_TYPE previousAction) {
		this.previousAction = previousAction;
	}

	public STATE_TYPE getPreviousState() {
		return previousState;
	}

	public void setPreviousState(STATE_TYPE previousState) {
		this.previousState = previousState;
	}

	public STATE_TYPE getCurrentState() {
		return currentState;
	}
}
