package aima.probability.decision;

public class MDP<STATE_TYPE, ACTION_TYPE> {
    private STATE_TYPE initialState;

    private MDPTransitionModel<STATE_TYPE, ACTION_TYPE> transitionModel;

    private RewardFunction<STATE_TYPE> rewardFunction;

    public MDP(STATE_TYPE initialState,
	    MDPTransitionModel<STATE_TYPE, ACTION_TYPE> transitionModel,
	    RewardFunction<STATE_TYPE> rewardFunction) {
	this.initialState = initialState;
	this.transitionModel = transitionModel;
	this.rewardFunction = rewardFunction;
    }
}
