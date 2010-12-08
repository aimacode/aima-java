package aima.core.search.online;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aima.core.agent.Action;
import aima.core.agent.Percept;
import aima.core.agent.impl.AbstractAgent;
import aima.core.agent.impl.NoOpAction;
import aima.core.search.framework.PerceptToStateFunction;
import aima.core.util.TwoKeyHashMap;
import aima.core.util.datastructure.Pair;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure 4.21, page 150.
 * <code>
 * function ONLINE-DFS-AGENT(s') returns an action
 *   inputs: s', a percept that identifies the current state
 *   persistent: result, a table, indexed by state and action, initially empty
 *               untried, a table that lists, for each state, the actions not yet tried
 *               unbacktracked, a table that lists, for each state, the backtracks not yet tried
 *               s, a, the previous state and action, initially null
 *    
 *   if GOAL-TEST(s') then return stop
 *   if s' is a new state (not in untried) then untried[s'] <- ACTIONS(s')
 *   if s is not null then
 *       result[s, a] <- s'
 *       add s to the front of the unbacktracked[s']
 *   if untried[s'] is empty then
 *       if unbacktracked[s'] is empty then return stop
 *       else a <- an action b such that result[s', b] = POP(unbacktracked[s'])
 *   else a <- POP(untried[s'])
 *   s <- s'
 *   return a
 * </code>
 * Figure 4.21 An online search agent that uses depth-first exploration. The agent is
 * applicable only in state spaces in which every action can be "undone" by some other action.<br>
 */

/**
 * @author Ciaran O'Reilly
 * 
 */
public class OnlineDFSAgent extends AbstractAgent {

	private OnlineSearchProblem problem;
	private PerceptToStateFunction ptsFunction;
	// persistent: result, a table, indexed by state and action, initially empty
	private final TwoKeyHashMap<Object, Action, Object> result = new TwoKeyHashMap<Object, Action, Object>();
	// untried, a table that lists, for each state, the actions not yet tried
	private final Map<Object, List<Action>> untried = new HashMap<Object, List<Action>>();
	// unbacktracked, a table that lists,
	// for each state, the backtracks not yet tried
	private final Map<Object, List<Object>> unbacktracked = new HashMap<Object, List<Object>>();
	// s, a, the previous state and action, initially null
	private Object s = null;
	private Action a = null;

	public OnlineDFSAgent(OnlineSearchProblem problem,
			PerceptToStateFunction ptsFunction) {
		setProblem(problem);
		setPerceptToStateFunction(ptsFunction);
	}

	public OnlineSearchProblem getProblem() {
		return problem;
	}

	public void setProblem(OnlineSearchProblem problem) {
		this.problem = problem;
		init();
	}

	public PerceptToStateFunction getPerceptToStateFunction() {
		return ptsFunction;
	}

	public void setPerceptToStateFunction(PerceptToStateFunction ptsFunction) {
		this.ptsFunction = ptsFunction;
	}

	// function ONLINE-DFS-AGENT(s') returns an action
	// inputs: s', a percept that identifies the current state
	@Override
	public Action execute(Percept psPrime) {
		Object sPrime = ptsFunction.getState(psPrime);
		// if GOAL-TEST(s') then return stop
		if (goalTest(sPrime)) {
			a = NoOpAction.NO_OP;
		} else {
			// if s' is a new state (not in untried) then untried[s'] <-
			// ACTIONS(s')
			if (!untried.containsKey(sPrime)) {
				untried.put(sPrime, actions(sPrime));
			}

			// if s is not null then do
			if (null != s) {
				// Note: If I've already seen the result of this
				// [s, a] then don't put it back on the unbacktracked
				// list otherwise you can keep oscillating
				// between the same states endlessly.
				if (!(sPrime.equals(result.get(s, a)))) {
					// result[s, a] <- s'
					result.put(s, a, sPrime);

					// Ensure the unbacktracked always has a list for s'
					if (!unbacktracked.containsKey(sPrime)) {
						unbacktracked.put(sPrime, new ArrayList<Object>());
					}

					// add s to the front of the unbacktracked[s']
					unbacktracked.get(sPrime).add(0, s);
				}
			}
			// if untried[s'] is empty then
			if (untried.get(sPrime).isEmpty()) {
				// if unbacktracked[s'] is empty then return stop
				if (unbacktracked.get(sPrime).isEmpty()) {
					a = NoOpAction.NO_OP;
				} else {
					// else a <- an action b such that result[s', b] =
					// POP(unbacktracked[s'])
					Object popped = unbacktracked.get(sPrime).remove(0);
					for (Pair<Object, Action> sa : result.keySet()) {
						if (sa.getFirst().equals(sPrime)
								&& result.get(sa).equals(popped)) {
							a = sa.getSecond();
							break;
						}
					}
				}
			} else {
				// else a <- POP(untried[s'])
				a = untried.get(sPrime).remove(0);
			}
		}

		if (a.isNoOp()) {
			// I'm either at the Goal or can't get to it,
			// which in either case I'm finished so just die.
			setAlive(false);
		}

		// s <- s'
		s = sPrime;
		// return a
		return a;
	}

	//
	// PRIVATE METHODS
	//

	private void init() {
		setAlive(true);
		result.clear();
		untried.clear();
		unbacktracked.clear();
		s = null;
		a = null;
	}

	private boolean goalTest(Object state) {
		return getProblem().isGoalState(state);
	}

	private List<Action> actions(Object state) {
		return new ArrayList<Action>(problem.getActionsFunction()
				.actions(state));
	}
}
