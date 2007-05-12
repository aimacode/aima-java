package aima.learning.reinforcement;

import aima.probability.decision.MDPPerception;

public interface RILearningStrategy<STATE_TYPE, ACTION_TYPE> {
	
	 MDPPerception<STATE_TYPE> learn(ACTION_TYPE action);

}
