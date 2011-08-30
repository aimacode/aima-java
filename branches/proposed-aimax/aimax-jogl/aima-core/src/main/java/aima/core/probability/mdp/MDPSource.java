package aima.core.probability.mdp;

import java.util.List;

import aima.core.util.Randomizer;

/**
 * @author Ravi Mohan
 * 
 */
public interface MDPSource<STATE_TYPE, ACTION_TYPE> {
	MDP<STATE_TYPE, ACTION_TYPE> asMdp();

	STATE_TYPE getInitialState();

	MDPTransitionModel<STATE_TYPE, ACTION_TYPE> getTransitionModel();

	MDPRewardFunction<STATE_TYPE> getRewardFunction();

	List<STATE_TYPE> getNonFinalStates();

	List<STATE_TYPE> getFinalStates();

	MDPPerception<STATE_TYPE> execute(STATE_TYPE state, ACTION_TYPE action,
			Randomizer r);

	List<ACTION_TYPE> getAllActions();
}
