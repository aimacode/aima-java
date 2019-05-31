package aima.core.search.nondeterministic;

import aima.core.search.framework.problem.StepCostFunction;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

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
	protected Function<S, List<A>> actionsFn;
	protected Predicate<S> goalTest;
	protected StepCostFunction<S, A> stepCostFn;
	protected ResultsFunction<S, A> resultsFn;

	/**
	 * Constructor
	 */
	public NondeterministicProblem(S initialState, Function<S, List<A>> actionsFn, ResultsFunction<S, A> resultsFn,
								   Predicate<S> goalTest) {
		this(initialState, actionsFn, resultsFn, goalTest, (s, a, sPrimed) -> 1.0);
	}

	/**
	 * Constructor
	 */
	public NondeterministicProblem(S initialState, Function<S, List<A>> actionsFn, ResultsFunction<S, A> resultsFn,
								   Predicate<S> goalTest, StepCostFunction<S, A> stepCostFn) {
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
	 * Returns the description of the possible actions available to the agent.
	 */
	List<A> getActions(S state) {
		return actionsFn.apply(state);
	}

	/**
	 * Return the description of what each action does.
	 * 
	 * @return the description of what each action does.
	 */
	public List<S> getResults(S state, A action) {
		return this.resultsFn.results(state, action);
	}

	/**
	 * Returns the <b>step cost</b> of taking action <code>action</code> in state <code>state</code> to reach state
	 * <code>stateDelta</code> denoted by c(s, a, s').
	 */
	double getStepCosts(S state, A action, S stateDelta) {
		return stepCostFn.applyAsDouble(state, action, stateDelta);
	}
}
