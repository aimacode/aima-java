package aima.core.search.api;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): page ??.<br>
 * <br>
 * An online search problem must be solved by an agent executing actions, rather
 * than by pure computation. We assume a deterministic and fully observable
 * environment (Chapter ?? relaxes these assumptions), but we stipulate that the
 * agent knows only the following: <br>
 * <ul>
 * <li>ACTIONS(s), which returns a list of actions allowed in state s;</li>
 * <li>The step-cost function c(s, a, s') - note that this cannot be used until
 * the agent knows that s' is the outcome; and</li>
 * <li>GOAL-TEST(s).</li>
 * </ul>
 * 
 * @param <A>
 *            the type of the actions that can be performed.
 * @param <S>
 *            the type of the states that the agent encounters.
 * 
 * @author Ciaran O'Reilly
 * @author Mike Stampone
 */
public interface OnlineSearchProblem<A, S> extends
		// List<A> actions(S s)
		ActionsFunction<A, S>,
		// boolean isGoalState(S s)
		GoalTestPredicate<S>,
		// double stepCost(S s, A a, S s')
		StepCostFunction<A, S> {
}