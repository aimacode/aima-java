package aima.core.search.agent;

import aima.core.agent.Action;
import aima.core.agent.EnvironmentViewNotifier;
import aima.core.agent.Percept;
import aima.core.agent.impl.AbstractAgent;
import aima.core.agent.impl.NoOpAction;
import aima.core.search.nondeterministic.AndOrSearch;
import aima.core.search.nondeterministic.NondeterministicProblem;
import aima.core.search.nondeterministic.Plan;

import java.util.Optional;
import java.util.function.Function;

/**
 * This agent traverses the nondeterministic environment using a
 * contingency plan. See page 135, AIMA3e.
 *
 * @author Ruediger Lunde
 * @author Andrew Brown
 */
public class NondeterministicSearchAgent<S, A extends Action> extends AbstractAgent {
	private Function<Percept, S> ptsFunction;
	private EnvironmentViewNotifier notifier;

	private NondeterministicProblem<S, A> problem;
	private Plan<S, A> contingencyPlan;
	private int currStep;

	public NondeterministicSearchAgent(Function<Percept, S> ptsFn) {
		this.ptsFunction = ptsFn;
	}

	public NondeterministicSearchAgent(Function<Percept, S> ptsFn, EnvironmentViewNotifier notifier) {
		this.ptsFunction = ptsFn;
		this.notifier = notifier;
	}

	/**
	 * Computes a contingency plan for the given problem and prepares plan execution.
	 * 
	 * @param problem
	 *            The search problem for this agent to solve.
	 */
	public void makePlan(NondeterministicProblem<S, A> problem) {
		this.problem = problem;
		setAlive(true);
		AndOrSearch<S, A> andOrSearch = new AndOrSearch<>();
		Optional<Plan<S, A>> plan = andOrSearch.search(problem);
		contingencyPlan = plan.isPresent() ? plan.get() : null;
		currStep = -1;
		if (notifier != null)
			notifier.notifyViews("Contingency plan: " + contingencyPlan);
	}

	/**
	 * Returns the search problem for this agent.
	 *
	 * @return The search problem for this agent.
	 */
	public NondeterministicProblem<S, A> getProblem() {
		return problem;
	}

	/**
	 * Returns the contingency plan of the agent.
	 * 
	 * @return The plan the agent uses to clean the vacuum world or null.
	 */
	public Plan<S, A> getPlan() {
		return contingencyPlan;
	}

	/**
	 * Execute an action from the contingency plan.
	 * 
	 * @param percept A percept.
	 * @return An action from the contingency plan.
	 */
	@Override
	public Action execute(Percept percept) {
		S state = (S) ptsFunction.apply(percept);
		// at goal or no plan?
		if (problem.testGoal(state) || contingencyPlan == null)
			return NoOpAction.NO_OP;

		currStep++;
		// end of plan reached?
		if (currStep == contingencyPlan.size()) {
			contingencyPlan = null;
			return NoOpAction.NO_OP;
		}

		// next step is action step?
		if (contingencyPlan.isActionStep(currStep))
			return contingencyPlan.getAction(currStep);

		// determine next sub-plan and execute it!
		contingencyPlan = contingencyPlan.getPlan(currStep, state);
		currStep = -1;
		return execute(percept);
	}
}
