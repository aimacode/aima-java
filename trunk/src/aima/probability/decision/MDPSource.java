package aima.probability.decision;

public interface MDPSource<STATE_TYPE, ACTION_TYPE>  {
	MDP<STATE_TYPE, ACTION_TYPE> asMdp();

}
