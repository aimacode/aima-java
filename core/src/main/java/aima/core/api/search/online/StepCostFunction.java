package aima.core.api.search.online;

/**
 * Calculate the step cost of taking action a in state s to reach state s'.
 * 
 * @author Anurag Rai
 */

import aima.core.api.agent.Action;

@FunctionalInterface
public interface StepCostFunction<S> {
	/**
	 * @param stateFrom
	 *            the state from which action a is to be performed.
	 * @param action
	 *            the action to be taken.
	 * @param stateTo
	 *            the state reached by taking the action.
	 *            
	 * @return the cost of taking action a in state stateFrom to reach state stateTo.
	 * 
	 */
	double apply(S stateFrom, Action action, S stateTo);
}
