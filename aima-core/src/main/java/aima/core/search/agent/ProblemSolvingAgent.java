package aima.core.search.agent;

import aima.core.agent.impl.AbstractAgent;
import aima.core.search.framework.problem.Problem;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

/**
 * Modified copy of class {@link SimpleProblemSolvingAgent} which can be used for
 * online search, too. Here, attribute {@link #plan} (original:
 * <code>seq</code>) is protected. Static pseudo code variable state is used in
 * a more general sense including world state as well as agent state aspects.
 * This allows the agent to remove the current plan and mark the current goal
 * as not yet reached if unexpected percepts are observed in
 * {@link #updateState(Object)}.
 * 
 * <pre>
 * <code>
 * function PROBLEM-SOLVING-AGENT(percept) returns an action
 *   inputs: percept, a percept
 *   static: state, some description of current agent and world state
 *           
 *   state <- UPDATE-STATE(state, percept)
 *   while (state.plan is empty && agent is alive) do
 *     goal <- FORMULATE-GOAL(state)
 *     if (goal exists) then
 *       problem <- FORMULATE-PROBLEM(state, goal)
 *       actions <- SEARCH(problem)
 *       if (actions != failure) then
 *         state.plan = actions
 *       else
 *         handleGoalUnreachable(goal)
 *     else
 *       die
 *   if (state.plan is empty)
 *      action = do nothing
 *   else
 *      action <- FIRST(state.plan)
 *      state.plan <- REST(state.plan)
 *   return action
 * </code>
 * </pre>
 *
 * @param <P> The type used to represent percepts
 * @param <S> The type used to represent states
 * @param <A> The type of the actions to be used to navigate through the state space
 *
 * @author Ruediger Lunde
 */
public abstract class ProblemSolvingAgent<P, S, A> extends AbstractAgent<P, A> {

	/** Plan, an action sequence, initially empty. */
	protected Queue<A> plan = new LinkedList<>();


	/**
	 * Template method, which corresponds to pseudo code function
	 * <code>PROBLEM-SOLVING-AGENT(percept)</code>.
	 *
	 * In this implementation, the agent does not necessarily give up, if search fails for one goal as
	 * long as there are other goals. But the agent dies if no further goal is left.
	 * 
	 * @return an action or empty if at a new goal or no further goal left.
	 */
	public Optional<A> execute(P p) {
		updateState(p);
		// never give up
		while (plan.isEmpty() && isAlive()) {
			Optional<Object> goal = formulateGoal();
			if (goal.isPresent()) {
				Problem<S, A> problem = formulateProblem(goal.get());
				Optional<List<A>> actions = search(problem);
				if (actions.isPresent())
					// list is empty if agent is at the goal
					plan.addAll(actions.get());
				else
					handleGoalUnreachable(goal.get());
			} else {
				// no further goal
				setAlive(false);
			}
		}
		return Optional.ofNullable(!plan.isEmpty() ? plan.remove() : null);
	}

	/**
	 * Primitive operation, responsible for updating the state of the agent with
	 * respect to latest feedback from the world. In this version,
	 * implementations have access to the agent's current goal and plan, so they
	 * can modify them if needed. For example, if the plan didn't work because
	 * the model of the world proved to be wrong, implementations could update
	 * the model and also clear the plan.
	 */
	protected abstract void updateState(P p);

	/**
	 * Primitive operation, responsible for goal generation. Implementations are
	 * allowed to return empty to indicate that the agent has finished the job an
	 * should die.
	 */
	protected abstract Optional<Object> formulateGoal();

	/**
	 * Primitive operation, responsible for search problem generation.
	 */
	protected abstract Problem<S, A> formulateProblem(Object goal);

	/**
	 * Primitive operation, responsible for the generation of a list of actions
	 * (plan) for the given search problem.
	 */
	protected abstract Optional<List<A>> search(Problem<S, A> problem);

	/**
	 * Primitive operation, which decides what to do after the search for a plan failed.
	 * Implementations can influence next goal or problem formulation.
	 * This implementation does nothing.
	 * @param goal The goal which has turned out to be unreachable.
	 */
	protected void handleGoalUnreachable(Object goal) { }
}