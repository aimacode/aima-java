package aima.core.search.api;

/**
 * The <b>goal test</b>, which determines whether a given state is a goal state.
 *
 * @param <S>
 *            the type of the states that the agent encounters.
 * 
 * @author Ciaran O'Reilly
 * 
 */
@FunctionalInterface
public interface GoalTestPredicate<S> {
	/**
	 * The goal test, which determines if a given state is a goal state.
	 *
	 * @param state
	 *            a state to be tested.
	 * @return true if the given state is a goal state, false otherwise.
	 */
	boolean isGoalState(S state);
}
