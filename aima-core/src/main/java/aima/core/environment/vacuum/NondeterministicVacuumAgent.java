package aima.core.environment.vacuum;

import java.util.LinkedList;

import aima.core.agent.Action;
import aima.core.agent.Percept;
import aima.core.agent.impl.AbstractAgent;
import aima.core.agent.impl.NoOpAction;
import aima.core.search.framework.PerceptToStateFunction;
import aima.core.search.nondeterministic.AndOrSearch;
import aima.core.search.nondeterministic.IfStateThenPlan;
import aima.core.search.nondeterministic.NondeterministicProblem;
import aima.core.search.nondeterministic.Plan;

/**
 * This agent traverses the NondeterministicVacuumEnvironment using a
 * contingency plan. See page 135, AIMA3e.
 * 
 * @author Andrew Brown
 */
public class NondeterministicVacuumAgent extends AbstractAgent {
	private NondeterministicProblem problem;
	private PerceptToStateFunction ptsFunction;
	private Plan contingencyPlan;
	private LinkedList<Object> stack = new LinkedList<Object>();

	public NondeterministicVacuumAgent(PerceptToStateFunction ptsFunction) {
		setPerceptToStateFunction(ptsFunction);
	}

	/**
	 * Returns the search problem for this agent.
	 * 
	 * @return the search problem for this agent.
	 */
	public NondeterministicProblem getProblem() {
		return problem;
	}

	/**
	 * Sets the search problem for this agent to solve.
	 * 
	 * @param problem
	 *            the search problem for this agent to solve.
	 */
	public void setProblem(NondeterministicProblem problem) {
		this.problem = problem;
		init();
	}

	/**
	 * Returns the percept to state function of this agent.
	 * 
	 * @return the percept to state function of this agent.
	 */
	public PerceptToStateFunction getPerceptToStateFunction() {
		return ptsFunction;
	}

	/**
	 * Sets the percept to state functino of this agent.
	 * 
	 * @param ptsFunction
	 *            a function which returns the problem state associated with a
	 *            given Percept.
	 */
	public void setPerceptToStateFunction(PerceptToStateFunction ptsFunction) {
		this.ptsFunction = ptsFunction;
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
	 * @param percept
	 * @return an action from the contingency plan.
	 */
	@Override
	public Action execute(Percept percept) {
		// check if goal state
		VacuumEnvironmentState state = (VacuumEnvironmentState) this
				.getPerceptToStateFunction().getState(percept);
		if (state.getLocationState(VacuumEnvironment.LOCATION_A) == VacuumEnvironment.LocationState.Clean
				&& state.getLocationState(VacuumEnvironment.LOCATION_B) == VacuumEnvironment.LocationState.Clean) {
			return NoOpAction.NO_OP;
		}
		// check stack size
		if (this.stack.size() < 1) {
			if (this.contingencyPlan.size() < 1) {
				return NoOpAction.NO_OP;
			} else {
				this.stack.push(this.getContingencyPlan().removeFirst());
			}
		}
		// pop...
		Object currentStep = this.stack.peek();
		// push...
		if (currentStep instanceof Action) {
			return (Action) this.stack.pop();
		} // case: next step is a plan
		else if (currentStep instanceof Plan) {
			Plan newPlan = (Plan) currentStep;
			if (newPlan.size() > 0) {
				this.stack.push(newPlan.removeFirst());
			} else {
				this.stack.pop();
			}
			return this.execute(percept);
		} // case: next step is an if-then
		else if (currentStep instanceof IfStateThenPlan) {
			IfStateThenPlan conditional = (IfStateThenPlan) this.stack.pop();
			this.stack.push(conditional.ifStateMatches(percept));
			return this.execute(percept);
		} // case: ignore next step if null
		else if (currentStep == null) {
			this.stack.pop();
			return this.execute(percept);
		} else {
			throw new RuntimeException("Unrecognized contingency plan step.");
		}
	}

	//
	// PRIVATE METHODS
	//
	private void init() {
		setAlive(true);
		stack.clear();
		AndOrSearch andOrSearch = new AndOrSearch();
		this.contingencyPlan = andOrSearch.search(this.problem);
	}
}
