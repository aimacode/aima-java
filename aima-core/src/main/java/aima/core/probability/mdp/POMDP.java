package aima.core.probability.mdp;

import aima.core.agent.Action;

import java.util.Set;

public interface POMDP<S, A extends Action,E> extends MarkovDecisionProcess<S,A>{
    double getDiscount();
    double sensorModel(S observedState, S actualState);
    Set<A> getAllActions();

}
