package aima.core.agent;

import aima.core.probability.BeliefState;
import aima.core.agent.Agent;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure 13.1, page 484.<br>
 * 
 * Figure 13.1  A decision-theoretic agent that selects rational actions.
 * 
 * @author Ritwik Sharma
 */
public interface DTAgent extends Agent {
	/**
	 * @param belief_state
	 *            		 probabilistic beliefs about the current state of the world.
	 * @param action
	 *             the agent's action
	 */
	BeliefState<Action, Percept> belief_state;
	Action action;
	List<Action> action_descriptions;

	Action perceive(Percept percept);

	// update belief_state based on action and percept
	belief_state.update(action,percept);
	/** 
	 * calculate outcome probabilities for actions,
   	 *		given action descriptions and current belief_state 	
	 *
	 * select action with highest expected utility
   	 *		given probabilities of outcomes and utility information
	 */

	 return action;
	
}
