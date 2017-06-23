package aima.core.environment.vacuum;

import java.util.LinkedList;
import java.util.function.Function;

import aima.core.agent.Action;
import aima.core.agent.EnvironmentViewNotifier;
import aima.core.agent.Percept;
import aima.core.agent.impl.AbstractAgent;
import aima.core.agent.impl.NoOpAction;
import aima.core.search.nondeterministic.AndOrSearch;
import aima.core.search.nondeterministic.IfStateThenPlan;
import aima.core.search.nondeterministic.NondeterministicProblem;
import aima.core.search.nondeterministic.Plan;

/**
 * This agent traverses the NondeterministicVacuumEnvironment using a
 * contingency plan. See page 135, AIMA3e.
 * 
 * @author Andrew Brown
 * @author Ruediger Lunde
 */
public class NondeterministicVacuumAgent extends AbstractAgent {
	private Function<Percept, Object> ptsFunction;
	private EnvironmentViewNotifier notifier;

	private NondeterministicProblem<VacuumEnvironmentState, Action> problem;
	private Plan contingencyPlan;
	private LinkedList<Object> stack = new LinkedList<>();

	public NondeterministicVacuumAgent(Function<Percept, Object> ptsFunction) {
		this.ptsFunction = ptsFunction;
	}

	public NondeterministicVacuumAgent(Function<Percept, Object> ptsFunction, EnvironmentViewNotifier notifier) {
		this.ptsFunction = ptsFunction;
		this.notifier = notifier;
	}

	/**
	 * Sets the search problem for this agent to solve.
	 * 
	 * @param problem
	 *            the search problem for this agent to solve.
	 */
	public void setProblem(NondeterministicProblem<VacuumEnvironmentState, Action> problem) {
		this.problem = problem;
		setAlive(true);
		stack.clear();
		AndOrSearch<VacuumEnvironmentState, Action> andOrSearch = new AndOrSearch<>();
		contingencyPlan = andOrSearch.search(problem);
		if (notifier != null)
			notifier.notifyViews("   Contingency plan: " + contingencyPlan);
	}

	/**
	 * Returns the search problem for this agent.
	 *
	 * @return the search problem for this agent.
	 */
	public NondeterministicProblem<VacuumEnvironmentState, Action> getProblem() {
		return problem;
	}

	/**
	 * Return the agent contingency plan
	 * 
	 * @return the plan the agent uses to clean the vacuum world
	 */
	public Plan getContingencyPlan() {
		if (this.contingencyPlan == null) {
			throw new RuntimeException("Contingency plan not set.");
		}
		return this.contingencyPlan;
	}

	/**
	 * Execute an action from the contingency plan
	 * 
	 * @param percept a percept.
	 * @return an action from the contingency plan.
	 */
	@Override
	public Action execute(Percept percept) {
		// check if goal state
		VacuumEnvironmentState state = (VacuumEnvironmentState) ptsFunction.apply(percept);
		if (state.getLocationState(VacuumEnvironment.LOCATION_A) == VacuumEnvironment.LocationState.Clean
				&& state.getLocationState(VacuumEnvironment.LOCATION_B) == VacuumEnvironment.LocationState.Clean) {
			return NoOpAction.NO_OP;
		}
		// check stack size
		if (stack.size() < 1) {
			if (contingencyPlan.size() < 1) {
				return NoOpAction.NO_OP;
			} else {
				stack.push(this.getContingencyPlan().removeFirst());
			}
		}
		// pop...
		Object currentStep = stack.peek();
		// push...
		if (currentStep instanceof Action) {
			return (Action) stack.remove();
		} // case: next step is a plan
		else if (currentStep instanceof Plan) {
			Plan newPlan = (Plan) currentStep;
			if (newPlan.size() > 0)
				stack.push(newPlan.removeFirst());
			else
				stack.remove();
			return execute(percept);
		} // case: next step is an if-then
		else if (currentStep instanceof IfStateThenPlan) {
			IfStateThenPlan conditional = (IfStateThenPlan) stack.remove();
			stack.push(conditional.ifStateMatches(percept));
			return execute(percept);
		} // case: ignore next step if null
		else if (currentStep == null) {
			stack.remove();
			return execute(percept);
		} else {
			throw new RuntimeException("Unrecognized contingency plan step.");
		}
	}
}
