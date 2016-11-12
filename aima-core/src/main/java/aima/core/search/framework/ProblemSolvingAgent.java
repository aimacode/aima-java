package aima.core.search.framework;

import java.util.ArrayList;
import java.util.List;

import aima.core.agent.Action;
import aima.core.agent.Percept;
import aima.core.agent.impl.AbstractAgent;
import aima.core.agent.impl.NoOpAction;
import aima.core.search.framework.problem.Problem;
import aima.core.util.Util;

/**
 * Modified copy of class
 * {@link aima.search.framework.SimpleProblemSolvingAgent} which can be used for
 * online search, too. Here, attribute {@link #plan} (original:
 * <code>seq</code>) is protected. Static pseudo code variable state is used in
 * a more general sense including world state as well as agent state aspects.
 * This allows the agent to change the plan, if unexpected percepts are
 * observed. In the concrete java code, state corresponds with the agent
 * instance itself (this).
 * 
 * <pre>
 * <code>
 * function PROBLEM-SOLVING-AGENT(percept) returns an action
 *   inputs: percept, a percept
 *   static: state, some description of current agent and world state
 *           
 *   state <- UPDATE-STATE(state, percept)
 *   while (state.plan is empty) do
 *     goal <- FORMULATE-GOAL(state)
 *     if (goal != null) then
 *       problem    <- FORMULATE-PROBLEM(state, goal)
 *       state.plan <- SEARCH(problem)
 *       if (state.plan is empty and !tryWithAnotherGoal()) then
 *         add NO_OP to plan         // failure
 *     else
 *       add NO_OP to plan           // success
 *   action <- FIRST(state.plan)
 *   plan <- REST(state.plan)
 *   return action
 * </code>
 * </pre>
 * 
 * @author Ruediger Lunde
 * 
 */
public abstract class ProblemSolvingAgent extends AbstractAgent {

	/** Plan, an action sequence, initially empty. */
	protected List<Action> plan = new ArrayList<Action>();

	public ProblemSolvingAgent() {
	}

	/**
	 * Template method, which corresponds to pseudo code function
	 * <code>PROBLEM-SOLVING-AGENT(percept)</code>.
	 * 
	 * @return an action
	 */
	public Action execute(Percept p) {
		Action action;
		// state <- UPDATE-STATE(state, percept)
		updateState(p);
		// if plan is empty then do
		while (plan.isEmpty()) {
			// state.goal <- FORMULATE-GOAL(state)
			Object goal = formulateGoal();
			if (goal != null) {
				// problem <- FORMULATE-PROBLEM(state, goal)
				Problem problem = formulateProblem(goal);
				// state.plan <- SEARCH(problem)
				plan.addAll(search(problem));
				if (plan.isEmpty() && !tryWithAnotherGoal()) {
					// unable to identify a path
					plan.add(NoOpAction.NO_OP);
					setAlive(false);
				}
			} else {
				// no further goal to achieve
				plan.add(NoOpAction.NO_OP);
				setAlive(false);
			}
		}
		// action <- FIRST(plan)
		action = Util.first(plan);
		// plan <- REST(plan)
		plan = Util.rest(plan);

		return action;
	}

	/**
	 * Primitive operation, which decides after a search for a plan failed,
	 * whether to stop the whole task with a failure, or to go on with
	 * formulating another goal. This implementation always returns false. If
	 * the agent defines local goals to reach an externally specified global
	 * goal, it might be interesting, not to stop when the first local goal
	 * turns out to be unreachable.
	 */
	protected boolean tryWithAnotherGoal() {
		return false;
	}

	//
	// ABSTRACT METHODS
	//
	/**
	 * Primitive operation, responsible for updating the state of the agent with
	 * respect to latest feedback from the world. In this version,
	 * implementations have access to the agent's current goal and plan, so they
	 * can modify them if needed. For example, if the plan didn't work because
	 * the model of the world proved to be wrong, implementations could update
	 * the model and also clear the plan.
	 */
	protected abstract Object updateState(Percept p);

	/**
	 * Primitive operation, responsible for goal generation. In this version,
	 * implementations are allowed to return null to indicate that the agent has
	 * finished the job an should die. Implementations can access the current
	 * goal (which is a possibly modified version of the last formulated goal).
	 * This might be useful in situations in which plan execution has failed.
	 */
	protected abstract Object formulateGoal();

	/**
	 * Primitive operation, responsible for search problem generation.
	 */
	protected abstract Problem formulateProblem(Object goal);

	/**
	 * Primitive operation, responsible for the generation of an action list
	 * (plan) for the given search problem.
	 */
	protected abstract List<Action> search(Problem problem);
}