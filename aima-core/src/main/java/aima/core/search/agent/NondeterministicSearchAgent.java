package aima.core.search.agent;

import aima.core.agent.Agent;
import aima.core.agent.Notifier;
import aima.core.agent.impl.SimpleAgent;
import aima.core.search.nondeterministic.AndOrSearch;
import aima.core.search.nondeterministic.NondeterministicProblem;
import aima.core.search.nondeterministic.Plan;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * This agent traverses the nondeterministic environment using a contingency plan. See page 135, AIMA3e.
 *
 * @param <P> The type used to represent percepts
 * @param <S> The type used to represent states
 * @param <A> The type of the actions to be used to navigate through the state space
 * @author Ruediger Lunde
 * @author Andrew Brown
 */
public class NondeterministicSearchAgent<P, S, A> extends SimpleAgent<P, A> {
	/** Maps percepts to states. */
	private Function<P, S> ptsFunction;
	/** Is informed about every computed contingency plan. */
	private Notifier notifier;

	private NondeterministicProblem<S, A> problem;
	private Plan<S, A> contingencyPlan;
	private int currStep;

	public NondeterministicSearchAgent(Function<P, S> ptsFn) {
		this.ptsFunction = ptsFn;
	}

	public NondeterministicSearchAgent(BiFunction<P, Agent<P, A>, S> ptsFn) {
		this.ptsFunction = percept -> ptsFn.apply(percept, this);
	}

	public NondeterministicSearchAgent(BiFunction<P, Agent<P, A>, S> ptsFn, Notifier notifier) {
		this(ptsFn);
		this.notifier = notifier;
	}

	/**
	 * Computes a contingency plan for the given problem and prepares plan execution.
	 * @param problem
	 *            The search problem for this agent to solve.
	 */
	public void makePlan(NondeterministicProblem<S, A> problem) {
		this.problem = problem;
		setAlive(true);
		AndOrSearch<S, A> andOrSearch = new AndOrSearch<>();
		Optional<Plan<S, A>> plan = andOrSearch.search(problem);
		contingencyPlan = plan.orElse(null);
		currStep = -1;
		if (notifier != null)
			notifier.notify("Contingency plan: " + contingencyPlan);
	}

	/**
	 * Selects next action from the contingency plan.
	 * @param percept A percept.
	 * @return An action from the contingency plan.
	 */
	@Override
	public final Optional<A> act(P percept) {
		S state = ptsFunction.apply(percept);
		// at goal or no plan?
		if (problem.testGoal(state) || contingencyPlan == null)
			return Optional.empty();

		currStep++;
		while (true) {
			// end of plan reached?
			if (currStep == contingencyPlan.size()) {
				contingencyPlan = null;
				return Optional.empty();
			}

			// next step is action step?
			if (contingencyPlan.isActionStep(currStep))
				return Optional.of(contingencyPlan.getAction(currStep));

			// determine next sub-plan and execute it!
			contingencyPlan = contingencyPlan.getPlan(currStep, state);
			currStep = 0;
		}
	}

	/**
	 * Returns the search problem for this agent.
	 * @return The search problem for this agent.
	 */
	public NondeterministicProblem<S, A> getProblem() {
		return problem;
	}

	/**
	 * Returns the contingency plan of the agent.
	 * @return The plan the agent uses to clean the vacuum world or null.
	 */
	public Plan<S, A> getPlan() {
		return contingencyPlan;
	}
}
