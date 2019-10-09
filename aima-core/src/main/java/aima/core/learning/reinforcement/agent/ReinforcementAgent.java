package aima.core.learning.reinforcement.agent;

import java.util.Map;
import java.util.Optional;

import aima.core.agent.impl.SimpleAgent;
import aima.core.learning.reinforcement.PerceptStateReward;

/**
 * An abstract base class for creating reinforcement based agents.
 * 
 * @param <S>
 *            the state type.
 * @param <A>
 *            the action type.
 * 
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 */
public abstract class ReinforcementAgent<S, A> extends SimpleAgent<PerceptStateReward<S>, A> {

	/**
	 * Default Constructor.
	 */
	public ReinforcementAgent() {
	}

	/**
	 * Map the given percept to an Agent action.
	 * 
	 * @param percept
	 *            a percept indicating the current state s' and reward signal r'
	 * @return the action to take.
	 */
	public abstract Optional<A> act(PerceptStateReward<S> percept);
	
	/**
	 * Get a vector of the currently calculated utilities for states of type S
	 * in the world.
	 * 
	 * @return a Map of the currently learned utility values for the states in
	 *         the environment (Note: this map may not contain all of the states
	 *         in the environment, i.e. the agent has not seen them yet).
	 */
	public abstract Map<S, Double> getUtility();
	
	/**
	 * Reset the agent back to its initial state before it has learned anything
	 * about its environment.
	 */
	public abstract void reset();
}
