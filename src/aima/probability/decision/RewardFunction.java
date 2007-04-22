package aima.probability.decision;

public interface RewardFunction<STATE_TYPE> {
     double   getRewardFor(STATE_TYPE state);
}
