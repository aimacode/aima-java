package aima.core.search.api;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): page ??.<br>
 * <br>
 * A problem can be defined formally by five components: <br>
 * <ul>
 * <li>The <b>initial state</b> that the agent starts in.</li>
 * <li>A description of the possible <b>actions</b> available to the agent.
 * Given a particular state s, ACTIONS(s) returns the set of actions that can be
 * executed in s.</li>
 * <li>A description of what each action does; the formal name for this is the
 * <b>transition model, specified by a function RESULT(s, a) that returns the
 * state that results from doing action a in state s.</b></li>
 * <li>The <b>goal test</b>, which determines whether a given state is a goal
 * state.</li>
 * <li>A <b>path cost</b> function that assigns a numeric cost to each path. The
 * problem-solving agent chooses a cost function that reflects its own
 * performance measure. The <b>step cost</b> of taking action a in state s to
 * reach state s' is denoted by c(s,a,s')</li>
 * </ul>
 *
 * @param <A>
 *            the type of the actions that can be performed.
 * @param <S>
 *            the type of the states that the agent encounters.
 * 
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 * @author Mike Stampone
 * 
 */
public interface Problem<A, S> extends
		// S initialState()
		InitialStateFunction<S>,
		// List<A> actions(S s)
		ActionsFunction<A, S>,
		// S result(S s, A a)
		ResultFunction<A, S>,
		// boolean isGoalState(S s)
		GoalTestPredicate<S>,
		// double stepCost(S s, A a, S s')
		StepCostFunction<A, S> {
}
