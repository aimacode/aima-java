package aima.core.agent.api;

import java.util.List;

/**
 * @param <A> agents action
 * @param <S> agents state
 * @param <P> percept from the environment
 */
public interface MonteCarloLocalizationAgent<A, S, P> extends Agent<A, P> {

  default List<Belief<S>> localize(List<Belief<S>> beliefs, A action, P percept) {
    List<Belief<S>> newBeliefs = motionUpdate(beliefs, action);
    newBeliefs = sensorUpdate(newBeliefs, percept);
    newBeliefs = resample(newBeliefs, percept);
    return newBeliefs;
  }

  List<Belief<S>> motionUpdate(List<Belief<S>> beliefs, A action);

  List<Belief<S>> sensorUpdate(List<Belief<S>> beliefs, P percept);

  List<Belief<S>> resample(List<Belief<S>> beliefs, P percept);

}


