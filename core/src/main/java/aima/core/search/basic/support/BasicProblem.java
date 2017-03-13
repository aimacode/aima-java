package aima.core.search.basic.support;

import aima.core.search.api.NondeterministicProblem;
import aima.core.search.api.OnlineSearchProblem;
import aima.core.search.api.Problem;
import aima.core.search.api.ActionsFunction;
import aima.core.search.api.ResultFunction;
import aima.core.search.api.GoalTestPredicate;
import aima.core.search.api.StepCostFunction;
import aima.core.search.api.ResultsFunction;
import java.util.List;

/**
 * Basic implementation of the Problem, NondeterministicProblem, and
 * OnlineSearchProblem interfaces.
 *
 * @author Ciaran O'Reilly
 */
public class BasicProblem<A, S> implements Problem<A, S>, NondeterministicProblem<A, S>, OnlineSearchProblem<A, S> {
	private S initialState;
	private ActionsFunction<A, S> actionsFn;
	private ResultFunction<A, S> resultFn;
	private ResultsFunction<A, S> resultsFn;
	private GoalTestPredicate<S> goalTestPredicate;
	private StepCostFunction<A, S> stepCostFn;

	// Problem constructor
	public BasicProblem(S initialState, ActionsFunction<A, S> actionsFn, ResultFunction<A, S> resultFn,
			GoalTestPredicate<S> goalTestPredicate) {
		// Default step cost function.
		this(initialState, actionsFn, resultFn, goalTestPredicate, (s, a, sPrime) -> 1.0);
	}

	// Problem constructor
	public BasicProblem(S initialState, ActionsFunction<A, S> actionsFn, ResultFunction<A, S> resultFn,
			GoalTestPredicate<S> goalTestPredicate, StepCostFunction<A, S> stepCostFn) {
		this.initialState = initialState;
		this.actionsFn = actionsFn;
		this.resultFn = resultFn;
		this.goalTestPredicate = goalTestPredicate;
		this.stepCostFn = stepCostFn;
	}

	// NondeterministicProblem constructor
	public BasicProblem(S initialState, ActionsFunction<A, S> actionsFn, ResultsFunction<A, S> resultsFn,
			GoalTestPredicate<S> goalTestPredicate) {
		// Default step cost function.
		this(initialState, actionsFn, resultsFn, goalTestPredicate, (s, a, sPrime) -> 1.0);
	}

	// NondeterministicProblem constructor
	public BasicProblem(S initialState, ActionsFunction<A, S> actionsFn, ResultsFunction<A, S> resultsFn,
			GoalTestPredicate<S> goalTestPredicate, StepCostFunction<A, S> stepCostFn) {
		this.initialState = initialState;
		this.actionsFn = actionsFn;
		this.resultsFn = resultsFn;
		this.goalTestPredicate = goalTestPredicate;
		this.stepCostFn = stepCostFn;
	}

	// OnlineSearchProblem constructor
	public BasicProblem(ActionsFunction<A, S> actionsFn, GoalTestPredicate<S> goalTestPredicate) {
		// Default step cost function.
		this(actionsFn, goalTestPredicate, (s, a, sPrime) -> 1.0);
	}

	// OnlineSearchProblem constructor
	public BasicProblem(ActionsFunction<A, S> actionsFn, GoalTestPredicate<S> goalTestPredicate,
			StepCostFunction<A, S> stepCostFn) {
		this.actionsFn = actionsFn;
		this.goalTestPredicate = goalTestPredicate;
		this.stepCostFn = stepCostFn;
	}

	@Override
	public S initialState() {
		return initialState;
	}

	@Override
	public List<A> actions(S s) {
		return actionsFn.actions(s);
	}

	@Override
	public S result(S s, A a) {
		return resultFn.result(s, a);
	}

	@Override
	public List<S> results(S s, A a) {
		return resultsFn.results(s, a);
	}

	@Override
	public boolean isGoalState(S state) {
		return goalTestPredicate.isGoalState(state);
	}

	@Override
	public double stepCost(S s, A a, S sPrime) {
		return stepCostFn.stepCost(s, a, sPrime);
	}

}
