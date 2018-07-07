package aima.core.probability.mdp;

import aima.core.agent.Action;

import java.util.Set;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 658.<br>
 * <br>
 * <p>
 * <p>
 * A POMDP has the same elements as an MDP—the transition model P (s' | s, a), actions A(s), and reward function
 * R(s)—but, like the partially observable search problems, it also has a sensor
 * model P (e | s).
 *
 * @param <S> the state type.
 * @param <A> the action type.
 * @author samagra
 */
public interface POMDP<S, A extends Action> extends MarkovDecisionProcess<S, A> {

    /**
     * @return The discount on future rewards i.e gamma.
     */
    double getDiscount();

    /**
     * Return the probability of observing a state e while being present in the state s based
     * on the underlying sendor model P(e | s).
     *
     * @param observedState the observed state e.
     * @param actualState   the state s in which the agent is actually present.
     * @return the probability of observing state e while being in state s.
     */
    double sensorModel(S observedState, S actualState);

    /**
     * @return All the actions available in the environment.
     */
    Set<A> getAllActions();

}
