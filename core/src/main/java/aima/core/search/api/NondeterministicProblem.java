package aima.core.search.api;

/**
 * Non-deterministic problems may have multiple results for a given state and
 * action; this interface handles these results by mimicking Problem interface
 * but replacing ResultFunction (one result) with ResultsFunction (a set of
 * results).
 *
 * @param <A>
 *            the type of the actions that can be performed.
 * @param <S>
 *            the type of the states that the agent encounters.
 * 
 * @author Ciaran O'Reilly
 * @author Andrew Brown
 */
public interface NondeterministicProblem<A, S> extends
		// S initialState()
		InitialStateFunction<S>,
		// List<A> actions(S s)
		ActionsFunction<A, S>,
		// List<S> results(S s, A a)
		ResultsFunction<A, S>,
		// boolean isGoalState(S s)
		GoalTestPredicate<S>,
		// double stepCost(S s, A a, S s')
		StepCostFunction<A, S> {
}