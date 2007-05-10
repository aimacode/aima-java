package aima.learning.reinforcement;

import aima.probability.Randomizer;
import aima.probability.decision.MDP;
import aima.probability.decision.MDPPerception;

public class MDPEnvironment<STATE_TYPE, ACTION_TYPE> {	

	private MDP<STATE_TYPE, ACTION_TYPE> mdp;
	private STATE_TYPE currentState;
	private double currentReward;

	public MDPEnvironment(MDP<STATE_TYPE, ACTION_TYPE> mdp){
		this.mdp = mdp;
		this.currentState = mdp.getInitialState();
		this.currentReward = mdp.getRewardFor(currentState);
	}
	
	public MDPPerception<STATE_TYPE> execute(ACTION_TYPE action,Randomizer r){
		MDPPerception<STATE_TYPE> result = mdp.execute(currentState, action, r);
		currentState = result.getState();
		currentReward = result.getReward();
		return result;
	}
	
	public MDPPerception<STATE_TYPE> passive_adp_learn(ACTION_TYPE action){
		return null;
	}


}
