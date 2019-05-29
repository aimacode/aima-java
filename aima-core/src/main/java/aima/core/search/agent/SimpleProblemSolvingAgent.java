package aima.core.search.agent;

import aima.core.agent.impl.AbstractAgent;
import aima.core.search.framework.problem.Problem;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure 3.1, page 67.<br>
 * <br>
 * 
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
public abstract class SimpleProblemSolvingAgent<P, S, A> extends AbstractAgent<P, A> {

	// seq, an action sequence, initially empty
	private Queue<A> seq = new LinkedList<>();

	// function SIMPLE-PROBLEM-SOLVING-AGENT(percept) returns an action
	/**
	 * Decides which action to perform next taking into account the current percept. Here, the agent
	 * dies if no further goals can be found.
	 * @param p The current percept
	 * @return An action or empty if at the goal, current goal unreachable, or no further goal found
	 */
	@Override
	public Optional<A> execute(P p) {
		A action = null;

		// state <- UPDATE-STATE(state, percept)
		updateState(p);
		// if seq is empty then do
		if (seq.isEmpty()) {
			// goal <- FORMULATE-GOAL(state)
			Optional<Object> goal = formulateGoal();
			if (goal.isPresent()) {
				// problem <- FORMULATE-PROBLEM(state, goal)
				Problem<S, A> problem = formulateProblem(goal.get());
				// seq <- SEARCH(problem)
				Optional<List<A>> actions = search(problem);
				// actions is empty if goal is unreachable
				// actions contains empty list of actions if agent is at the goal
				actions.ifPresent(as -> seq.addAll(as));
			} else {
				// agent no longer wishes to achieve any more goals
				setAlive(false);
			}
		}

		if (!seq.isEmpty()) {
			// action <- FIRST(seq)
			// seq <- REST(seq)
			action = seq.remove();
		}

		return Optional.ofNullable(action);
	}

	//
	// PROTECTED METHODS
	//
	protected abstract void updateState(P p);

	protected abstract Optional<Object> formulateGoal();

	protected abstract Problem<S, A> formulateProblem(Object goal);

	protected abstract Optional<List<A>> search(Problem<S, A> problem);
}