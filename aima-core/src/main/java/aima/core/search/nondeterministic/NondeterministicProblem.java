package aima.core.search.nondeterministic;

import aima.core.search.framework.problem.ActionsFunction;
import aima.core.search.framework.problem.GoalTest;
import aima.core.search.framework.problem.StepCostFunction;

/**
 * Non-deterministic problems may have multiple results for a given state and
 * action; this class handles these results by mimicking Problem and replacing
 * ResultFunction (one result) with ResultsFunction (a set of results).
 * 
 * @author Andrew Brown
 * @author Ruediger Lunde
 */
public class NondeterministicProblem<S, A> {

	protected S initialState;
	protected ActionsFunction<S, A> actionsFn;
	protected GoalTest<S> goalTest;
	protected StepCostFunction<S, A> stepCostFn;
	protected ResultsFunction<S, A> resultsFn;

	/**
	 * Constructor
	 */
	public NondeterministicProblem(S initialState,
			ActionsFunction<S, A> actionsFn, ResultsFunction<S, A> resultsFn,
			GoalTest<S> goalTest) {
		this(initialState, actionsFn, resultsFn, goalTest, (s, a, sPrimed) -> 1.0);
	}

	/**
	 * Constructor
	 */
	public NondeterministicProblem(S initialState,
			ActionsFunction<S, A> actionsFn, ResultsFunction<S, A> resultsFn,
			GoalTest<S> goalTest, StepCostFunction<S, A> stepCostFn) {
		this.initialState = initialState;
		this.actionsFn = actionsFn;
		this.resultsFn = resultsFn;
		this.goalTest = goalTest;
		this.stepCostFn = stepCostFn;
	}

	/**
	 * Returns the initial state of the agent.
	 * 
	 * @return the initial state of the agent.
	 */
	public S getInitialState() {
		return initialState;
	}

	/**
	 * Returns <code>true</code> if the given state is a goal state.
	 * 
	 * @return <code>true</code> if the given state is a goal state.
	 */
	public boolean testGoal(S state) {
		return goalTest.test(state);
	}

	/**
	 * Returns the goal test.
	 * 
	 * @return the goal test.
	 */
	public GoalTest<S> getGoalTest() {
		return goalTest;
	}

	/**
	 * Returns the description of the possible actions available to the agent.
	 * 
	 * @return the description of the possible actions available to the agent.
	 */
	public ActionsFunction<S, A> getActionsFn() {
		return actionsFn;
	}

	/**
	 * Return the description of what each action does.
	 * 
	 * @return the description of what each action does.
	 */
	public ResultsFunction<S, A> getResultsFn() {
		return this.resultsFn;
	}

	/**
	 * Returns the path cost function.
	 * 
	 * @return the path cost function.
	 */
	public StepCostFunction<S, A> getStepCostFn() {
		return stepCostFn;
	}
}
