package aima.core.probability.mdp;

import aima.core.agent.Action;

public interface POMDP<S, A extends Action,E> extends MarkovDecisionProcess<S,A>{
    double getDiscount();
    double sensorModel(S sDelta, S currentState, A action);
}
