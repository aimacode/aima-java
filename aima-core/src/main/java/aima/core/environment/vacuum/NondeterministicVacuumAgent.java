package aima.core.environment.vacuum;

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
 * This agent traverses the NondeterministicVacuumEnvironment using a
 * contingency plan. See page 135, AIMA3e.
 *
 * @author Ruediger Lunde
 * @author Andrew Brown
 */
public class NondeterministicVacuumAgent extends AbstractAgent {
	private Function<Percept, Object> ptsFunction;
	private EnvironmentViewNotifier notifier;

	private NondeterministicProblem<VacuumEnvironmentState, Action> problem;
	private Plan<VacuumEnvironmentState, Action> contingencyPlan;
	private int currStep;

	public NondeterministicVacuumAgent(Function<Percept, Object> ptsFunction) {
		this.ptsFunction = ptsFunction;
	}

	public NondeterministicVacuumAgent(Function<Percept, Object> ptsFunction, EnvironmentViewNotifier notifier) {
		this.ptsFunction = ptsFunction;
		this.notifier = notifier;
	}

	/**
	 * Computes a contingency plan for the given problem and prepares plan execution.
	 * 
	 * @param problem
	 *            The search problem for this agent to solve.
	 */
	public void makePlan(NondeterministicProblem<VacuumEnvironmentState, Action> problem) {
		this.problem = problem;
		setAlive(true);
		AndOrSearch<VacuumEnvironmentState, Action> andOrSearch = new AndOrSearch<>();
		Optional<Plan<VacuumEnvironmentState, Action>> plan = andOrSearch.search(problem);
		contingencyPlan = plan.isPresent() ? plan.get() : null;
		currStep = -1;
		if (notifier != null)
			notifier.notifyViews("   Contingency plan: " + contingencyPlan);
	}

	/**
	 * Returns the search problem for this agent.
	 *
	 * @return The search problem for this agent.
	 */
	public NondeterministicProblem<VacuumEnvironmentState, Action> getProblem() {
		return problem;
	}

	/**
	 * Returns the contingency plan of the agent.
	 * 
	 * @return The plan the agent uses to clean the vacuum world or null.
	 */
	public Plan getContingencyPlan() {
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
		VacuumEnvironmentState state = (VacuumEnvironmentState) ptsFunction.apply(percept);
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
