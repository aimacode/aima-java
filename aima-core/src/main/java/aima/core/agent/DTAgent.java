package aima.core.agent;

<<<<<<< HEAD
import aima.core.probability.BeliefState;
import aima.core.agent.Agent;
=======

import aima.core.agent.Action;
import aima.core.agent.Percept;
import aima.core.agent.impl.AbstractAgent;
import aima.core.probability.BeliefState;

import java.util.List;
>>>>>>> 8d5210cb29ce8c07c0ddec2fec04c88ed7e2e882

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure 13.1, page 484.<br>
 * 
 * Figure 13.1  A decision-theoretic agent that selects rational actions.
 * 
 * @author Ritwik Sharma
 */
<<<<<<< HEAD
public abstract class DTAgent<Action,Percept> implements Agent<Action,Percept> {
=======
public abstract class DTAgent extends AbstractAgent {
>>>>>>> 8d5210cb29ce8c07c0ddec2fec04c88ed7e2e882
	/**
	 * @param belief_state
	 *            		 probabilistic beliefs about the current state of the world.
	 * @param action
	 *             the agent's action
	 */
<<<<<<< HEAD
	BeliefState<Action, Percept> belief_state;
=======
	BeliefState<Action,Percept> belief_state;
>>>>>>> 8d5210cb29ce8c07c0ddec2fec04c88ed7e2e882
	Action action;
	List<Action> action_descriptions;

	public Action perceive(Percept percept) {

		// update belief_state based on action and percept
		belief_state.update(action,percept);
		/** 
		 * calculate outcome probabilities for actions,
<<<<<<< HEAD
	   	 *		given action descriptions and current belief_state 	
		 *
		 * select action with highest expected utility
	   	 *		given probabilities of outcomes and utility information
		 */
		Action faction; /** @return faction
                                *                 final action to be returned.  
		                */ 
		return faction;
	}
	
=======
	   	 *		given action descriptions and current belief_state 
		*/	
		List<Action> action_probabilities = cal_action_probabilities(action_descriptions,belief_state);
		/**  
		 * select action with highest expected utility
	   	 *		given probabilities of outcomes and utility information
		 */
		Action finalAction = action_with_highest_expected_utility(action_probabilities);

		return finalAction;
	}

	/**
     * @param action_description 
     *						   permissible action descriptions
     * @param belief_state  
     *					 current belief state of the agent about the world
     * @return The next action to be taken.
     */
	 abstract List<Action> cal_action_probabilities(List<Action> action_description, BeliefState belief_state);

    /**
     * @param action_probabilities 
     *							 Probabilities of various outcomes
     * @return Action with the highest probability.
     */
	public abstract Action action_with_highest_expected_utility(List<Action> action_probabilities);
>>>>>>> 8d5210cb29ce8c07c0ddec2fec04c88ed7e2e882
}
