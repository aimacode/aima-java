package aima.core.search.api;

/**
 * The <b>step cost</b> of taking action a in state s to reach state s' is
 * denoted by c(s,a,s')</li>
 *
 * @param <A>
 *            the type of the actions that can be performed.
 * @param <S>
 *            the type of the states that the agent encounters.
 * 
 * @author Ciaran O'Reilly
 * 
 */
@FunctionalInterface
public interface StepCostFunction<A, S> {
	/**
	 * Calculates the step-cost between two states. Used to assign a numeric
	 * cost to each path.
	 *
	 * @param s
	 *            the starting state.
	 * @param a
	 *            the action performed in state s.
	 * @param sPrime
	 *            the resulting state from performing action a.
	 * @return the step cost of taking action a in state s to reach state s'.
	 */
	double stepCost(S s, A a, S sPrime);
}
