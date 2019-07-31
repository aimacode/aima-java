package aima.core.search.agent;

import aima.core.agent.impl.SimpleAgent;
import aima.core.search.framework.problem.Problem;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure 3.1, page 67.<br>
 * <br>
 * <pre>
 * function SIMPLE-PROBLEM-SOLVING-AGENT(percept) returns an action
 *   persistent: seq, an action sequence, initially empty
 *               state, some description of the current world state
 *               goal, a goal, initially null
 *               problem, a problem formulation
 *           
 *   state &lt;- UPDATE-STATE(state, percept)
 *   if seq is empty then
 *     goal    &lt;- FORMULATE-GOAL(state)
 *     problem &lt;- FORMULATE-PROBLEM(state, goal)
 *     seq     &lt;- SEARCH(problem)
 *     if seq = failure then return a null action
 *   action &lt;- FIRST(seq)
 *   seq &lt;- REST(seq)
 *   return action
 * </pre>
 * 
 * Figure 3.1 A simple problem-solving agent. It first formulates a goal and a
 * problem, searches for a sequence of actions that would solve the problem, and
 * then executes the actions one at a time. When this is complete, it formulates
 * another goal and starts over.<br>
 *
 * @param <P> The type used to represent percepts
 * @param <S> The type used to represent states
 * @param <A> The type of the actions to be used to navigate through the state space
 *
 * @author Ciaran O'Reilly
 * @author Mike Stampone
 * @author Ruediger Lunde
 */
public abstract class SimpleProblemSolvingAgent<P, S, A> extends SimpleAgent<P, A> {

	/// seq, an action sequence, initially empty
	private Queue<A> seq = new LinkedList<>();

	/// function SIMPLE-PROBLEM-SOLVING-AGENT(percept) returns an action
	/**
	 * Template method which decides about the action to perform next taking into account the current percept.
	 * @param percept The current percept
	 * @return An action or empty if started at the goal, current goal unreachable, or no further goal exists
	 */
	@Override
	public Optional<A> act(P percept) {
		/// state <- UPDATE-STATE(state, percept)
		updateState(percept);
		/// if seq is empty then do
		if (seq.isEmpty()) {
			/// goal <- FORMULATE-GOAL(state)
			Optional<Object> goal = formulateGoal();
			if (goal.isPresent()) {
				/// problem <- FORMULATE-PROBLEM(state, goal)
				Problem<S, A> problem = formulateProblem(goal.get());
				/// seq <- SEARCH(problem)
				Optional<List<A>> actions = search(problem);
				// actions is empty if goal is unreachable
				// actions contains empty list of actions if agent is at the goal
				actions.ifPresent(as -> seq.addAll(as));
			} else {
				handleNoGoal();
			}
		}
		/// if seq = failure then return a null action
		/// action <- FIRST(seq)
		/// seq <- REST(seq)
		return Optional.ofNullable(!seq.isEmpty() ? seq.remove() : null);
	}

	protected abstract void updateState(P percept);

	/**
	 * Primitive operation, responsible for goal generation. Implementations are
	 * allowed to return empty to indicate that the agent has finished the job.
	 */
	protected abstract Optional<Object> formulateGoal();

	protected abstract Problem<S, A> formulateProblem(Object goal);

	protected abstract Optional<List<A>> search(Problem<S, A> problem);

	/**
	 * Primitive operation, which decides what to do if goal formulation failed.
	 * In this default implementation the agent dies.
	 */
	protected void handleNoGoal() {
		setAlive(false);
	}
}