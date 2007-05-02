package aima.probability.decision;

import java.util.List;

public class MDP<STATE_TYPE, ACTION_TYPE> {
    private STATE_TYPE initialState;

    private MDPTransitionModel<STATE_TYPE, ACTION_TYPE> transitionModel;

    private RewardFunction<STATE_TYPE> rewardFunction;
    
    private List<STATE_TYPE> states;

    public MDP(STATE_TYPE initialState,
	    MDPTransitionModel<STATE_TYPE, ACTION_TYPE> transitionModel,
	    RewardFunction<STATE_TYPE> rewardFunction, List<STATE_TYPE> states) {
	this.initialState = initialState;
	this.transitionModel = transitionModel;
	this.rewardFunction = rewardFunction;
	this.states = states;
    }
    
    public UtilityFunction<STATE_TYPE> valueIteration(double gamma,double error, double delta){
    	UtilityFunction<STATE_TYPE> U = new UtilityFunction<STATE_TYPE>();
    	UtilityFunction<STATE_TYPE> U_dash = new UtilityFunction<STATE_TYPE>();
    	double  delta_max = (error * gamma)/(1-gamma);
    	while (!(delta < delta_max )){
    		U = U_dash.copy(); 
    		delta = 0.0;
    		for (STATE_TYPE s:states){
    			double utility =  rewardFunction.getRewardFor(s) + (gamma * maxTransition(s)) ; //implement max transition
    			U_dash.setUtility(s, utility);
    		}
    	}
    	return U;
    	
    }

	private double maxTransition(STATE_TYPE s) {
		// TODO Auto-generated method stub
		return 0;
	}
}
