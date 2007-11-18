package aima.search.online;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import aima.basic.Agent;
import aima.basic.Percept;

import aima.search.framework.Problem;
import aima.search.framework.Successor;

/**
 * Artificial Intelligence A Modern Approach (2nd Edition): Figure 4.20, page 126.
 * <code>
 * function ONLINE-DFS-AGENT(s') returns an action
 *   inputs: s', a percept that identifies the current state
 *   static: result, a table, indexed by action and state, initially empty
 *           unexplored, a table that lists, for each visited state, the actions not yet tried
 *           unbacktracked, a table that lists, for each visited state, the backtracks not yet tried
 *           s, a, the previous state and action, initially null
 *   
 *   if GOAL-TEST(s') then return stop
 *   if s' is a new state then unexplored[s'] <- ACTIONS(s')
 *   if s is not null then do
 *       result[a, s] <- s'
 *       add s to the front of the unbacktracked[s']
 *   if unexplored[s'] is empty then
 *       if unbacktracked[s'] is empty then return stop
 *       else a <- an action b such that result[b, s'] = POP(unbacktracked[s'])
 *   else a <- POP(unexplored[s'])
 *   s <- s'
 *   return a
 * </code>
 * Figure 4.20 An online search agent that uses depth-first exploration. The agent is
 * applicable only in bidirectional search spaces.<br>
 * <br>
 * Note: This algorithm fails to exit if the goal does not exist (e.g. A<->B Goal=X),
 * this could be an issue with the implementation. Comments welcome.
 */

/**
 * @author Ciaran O'Reilly
 * 
 */
public class OnlineDFSAgent extends Agent {

	private Problem problem;
	// static: result, a table, indexed by action and state, initially empty
	private final Hashtable<ActionState, Percept> result = new Hashtable<ActionState, Percept>();
	// unexplored, a table that lists, for each visited state, the actions not
	// yet tried
	private final Hashtable<Percept, List<Object>> unexplored = new Hashtable<Percept, List<Object>>();
	// unbacktracked, a table that lists,
	// for each visited state, the backtracks not yet tried
	private final Hashtable<Percept, List<Percept>> unbacktracked = new Hashtable<Percept, List<Percept>>();
	// s, a, the previous state and action, initially null
	private Percept s = null;
	private Object a = null;

	public OnlineDFSAgent(Problem problem) {
		setProblem(problem);
	}

	public Problem getProblem() {
		return problem;
	}

	public void setProblem(Problem problem) {
		this.problem = problem;
		init();
	}

	// function ONLINE-DFS-AGENT(s') returns an action
	// inputs: s', a percept that identifies the current state
	@Override
	public String execute(Percept sComma) {
		// if GOAL-TEST(s') then return stop
		if (!goalTest(sComma)) {
			// if s' is a new state then unexplored[s'] <- ACTIONS(s')
			if (!unexplored.containsKey(sComma)) {
				unexplored.put(sComma, actions(sComma));
			}

			// if s is not null then do
			if (null != s) {
				// result[a, s] <- s'
				result.put(new ActionState(a, s), sComma);

				// Ensure the unbacktracked always has a list for s'
				if (!unbacktracked.containsKey(sComma)) {
					unbacktracked.put(sComma, new ArrayList<Percept>());
				}

				// add s to the front of the unbacktracked[s']
				unbacktracked.get(sComma).add(s);
			}
			// if unexplored[s'] is empty then
			if (unexplored.get(sComma).size() == 0) {
				// if unbacktracked[s'] is empty then return stop
				if (unbacktracked.get(sComma).size() == 0) {
					a = Agent.NO_OP;
				} else {
					// else a <- an action b such that result[b, s'] =
					// POP(unbacktracked[s'])
					Percept popped = unbacktracked.get(sComma).remove(
							unbacktracked.get(sComma).size() - 1);
					for (ActionState as : result.keySet()) {
						if (as.state.equals(sComma)
								&& result.get(as).equals(popped)) {
							a = as.action;
							break;
						}
					}
				}
			} else {
				// else a <- POP(unexplored[s'])
				a = unexplored.get(sComma).remove(
						unexplored.get(sComma).size() - 1);
			}
		} else {
			a = Agent.NO_OP;
		}

		if (Agent.NO_OP.equals(a)) {
			// I'm either at the Goal or can't get to it,
			// which in either case I'm finished so just die.
			die();
		}

		// s <- s'
		s = sComma;
		// return a
		return a.toString();
	}

	//
	// PRIVATE METHODS
	//

	private void init() {
		result.clear();
		unexplored.clear();
		unbacktracked.clear();
		s = null;
		a = null;
	}

	private boolean goalTest(Percept state) {
		return getProblem().isGoalState(state);
	}

	private List<Object> actions(Percept state) {
		List<Object> actions = new ArrayList<Object>();

		List<Successor> successors = getProblem().getSuccessorFunction()
				.getSuccessors(state);

		for (Successor s : successors) {
			actions.add(s.getAction());
		}

		return actions;
	}

	private class ActionState {
		private final Object action;
		private final Percept state;

		public ActionState(Object action, Percept state) {
			this.action = action;
			this.state = state;
		}

		@Override
		public boolean equals(Object o) {
			if (o == null || !(o instanceof ActionState)) {
				return super.equals(o);
			}
			return (action.equals(((ActionState) o).action) && state
					.equals(((ActionState) o).state));
		}

		@Override
		public int hashCode() {
			return action.hashCode() + state.hashCode();
		}

		@Override
		public String toString() {
			return "(" + action + ", " + state + ")";
		}
	}
}
