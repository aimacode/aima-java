package aima.core.search.framework;

import aima.core.agent.Action;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page ??.
 * 
 * The <b>step cost</b> of taking action a in state s to reach state s' 
 * is denoted by c(s, a, s').
 */

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public interface StepCostFunction {
	/**
	 * Calculate the step cost of taking action a in state s1 to reach state s2.
	 * 
	 * @param s1
	 *            the state from which action a is to be performed.
	 * @param a
	 * 
	 * @param s2
	 * @return
	 */
	double cost(Object s1, Action a, Object s2);
}