package aima.core.agent;


import aima.core.agent.impl.SimpleAgent;
import aima.core.probability.BeliefState;
import aima.core.util.datastructure.Pair;

import java.util.List;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure 13.1, page 484.<br>
 * 
 * Figure 13.1  A decision-theoretic agent that selects rational actions.
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
 *  @author Ritwik Sharma
 *  @author samagra
 */
public abstract class DTAgent extends SimpleAgent {
	/**
	 * @param belief_state
	 *            	      probabilistic beliefs about the current state of the world.
	 * @param action
	 *              the agent's action
	 */
	BeliefState<Action,Percept> belief_state;
	Action action;
	List<Action> action_descriptions;

	public Action perceive(Percept percept) {

		// update belief_state based on action and percept
		belief_state.update(action,percept);
		/** 
		 * calculate outcome probabilities for actions,
	   	 *		given action descriptions and current belief_state 
		*/	
		List<Pair<Action,Double>> action_probabilities = cal_action_probabilities(action_descriptions,belief_state);
		/**  
		 * select action with highest expected utility
	   	 *		given probabilities of outcomes and utility information
		 */
		Action finalAction = action_with_highest_expected_utility(action_probabilities);

		return finalAction;
	}

	/**
     * @param action_description 
     *			        permissible action descriptions
     * @param belief_state  
     *			  current belief state of the agent about the world
     * @return The next action to be taken.
     */
	 abstract List<Pair<Action,Double>> cal_action_probabilities(List<Action> action_description, BeliefState belief_state);

    /**
     * @param action_probabilities 
     *			          Probabilities of various outcomes
     * @return Action with the highest probability.
     */
	public abstract Action action_with_highest_expected_utility(List<Pair<Action,Double>> action_probabilities);
}
