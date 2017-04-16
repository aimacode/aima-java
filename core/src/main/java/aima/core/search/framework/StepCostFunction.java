package aima.core.search.framework;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): page ??.<br>
 * <br>
 * The <b>step cost</b> of taking action a in state s to reach state s' is
 * denoted by c(s, a, s').
 * 
 * @author Subham Mishra
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public interface StepCostFunction<A>{
	/**
	 * Calculate the step cost of taking action a in state s to reach state s'.
	 * 
	 * @param s
	 *            the state from which action a is to be performed.
	 * @param a
	 *            the action to be taken.
	 * 
	 * @param sDelta
	 *            the state reached by taking the action.
	 * @return the cost of taking action a in state s to reach state s'.
	 */
	double c(Object s, A a, Object sDelta);
}
