package aima.core.agent.basic;

import aima.core.Probability.BeliefState;
import aima.core.agent.api.Agent;
import aima.core.util.datastructure.Pair;

import java.util.List;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): Figure ?.?, page
 * ???.<br>
 * <br>
 *
 * <pre>
 * function DT-AGENT(percept) returns an action
 *   persistent: belief_state, probabilistic beliefs about the current state of the world
 *               action, the agent's action
 *
 *   update belief_state based on action and percept
 *   calculate outcome probabilities for actions
 *      given action descriptions and current belief state
 *   select action with highest expected utility
 *      given probabilities of outcomes and utility information
 *   return action
 *
 * </pre>
 *
 * @author samagra
 */
 public abstract class DecisionTheoreticAgent<A,P> implements Agent<A,P> {

    // persistent:
     BeliefState<A, P> belief_state;//belief_state, probabilistic beliefs about the current state of the world
     A action;              //action, the agent's action

    //utility (EXTRA)
     List<A> action_descriptions; //list of action descriptions

    //function DT-AGENT(percept) returns an action
    @Override
    public A perceive(P percept) {
        //update belief_state based on action and percept
        belief_state.update(action,percept);
        //calculate outcome probabilities for actions
        //              given action descriptions and current belief state
        List<Pair<A,Double>> action_probabilities = calc_action_probabilities(action_descriptions,belief_state);
        //select action with highest expected utility
       //          given probabilities of outcomes and utility information
        A finalAction = get_action_with_highest_expected_utility(action_probabilities);

        return finalAction;
    }

    /**
     * Calculates outcome probabilities for actions
     * @param action_description Refers to the permissible action descriptions
     * @param belief_state  Refers to the current belief state of the agent about the world
     * @return The next action to be taken.
     */
    abstract List<Pair<A,Double>> calc_action_probabilities(List<A> action_description, BeliefState belief_state);

    /**
     * Selects action with the highest expected utility (Utility calculation is determined by the problem)
     * @param action_probabilities Probabilities of various outcomes
     * @return Action with the highest probability.
     */
    abstract A get_action_with_highest_expected_utility(List<Pair<A,Double>> action_probabilities);
}
