package aima.core.agent.api;

/**
 * A belief as a state with a probability
 * @author wormi
 */
public interface Belief<S> {

  double getWeight();

  S getState();

}
