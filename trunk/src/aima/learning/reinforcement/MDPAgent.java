package aima.learning.reinforcement;

import aima.probability.Randomizer;
import aima.probability.decision.MDP;
import aima.probability.decision.MDPPerception;

public class MDPAgent<STATE_TYPE, ACTION_TYPE> {	

	protected MDP<STATE_TYPE, ACTION_TYPE> mdp;
	protected STATE_TYPE currentState;
	protected Double currentReward;
	protected RILearningStrategy<STATE_TYPE, ACTION_TYPE> strategy;
	

	public MDPAgent(MDP<STATE_TYPE, ACTION_TYPE> mdp){
		this.mdp = mdp;
		this.currentState = mdp.getInitialState();
		this.currentReward = mdp.getRewardFor(currentState);
		
	}
	
	public MDPPerception<STATE_TYPE> execute(ACTION_TYPE action,Randomizer r){
		MDPPerception<STATE_TYPE> perception = mdp.execute(currentState, action, r);
		updateFromPerception(perception);
		return perception;
	}
	
	public void updateFromPerception(MDPPerception<STATE_TYPE> perception){
		currentState = perception.getState();
		currentReward = perception.getReward();
	}
	



}
