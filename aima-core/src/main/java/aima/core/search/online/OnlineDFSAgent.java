package aima.core.search.online;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import aima.core.agent.Action;
import aima.core.agent.Percept;
import aima.core.agent.impl.AbstractAgent;
import aima.core.agent.impl.NoOpAction;
import aima.core.search.framework.problem.OnlineSearchProblem;
import aima.core.util.datastructure.Pair;
import aima.core.util.datastructure.TwoKeyHashMap;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure 4.21, page
 * 150.<br>
 * <br>
 * 
 * <pre>
 * function ONLINE-DFS-AGENT(s') returns an action
 *   inputs: s', a percept that identifies the current state
 *   persistent: result, a table, indexed by state and action, initially empty
 *               untried, a table that lists, for each state, the actions not yet tried
 *               unbacktracked, a table that lists, for each state, the backtracks not yet tried
 *               s, a, the previous state and action, initially null
 *    
 *   if GOAL-TEST(s') then return stop
 *   if s' is a new state (not in untried) then untried[s'] &lt;- ACTIONS(s')
 *   if s is not null then
 *       result[s, a] &lt;- s'
 *       add s to the front of the unbacktracked[s']
 *   if untried[s'] is empty then
 *       if unbacktracked[s'] is empty then return stop
 *       else a &lt;- an action b such that result[s', b] = POP(unbacktracked[s'])
 *   else a &lt;- POP(untried[s'])
 *   s &lt;- s'
 *   return a
 * </pre>
 * 
 * Figure 4.21 An online search agent that uses depth-first exploration. The
 * agent is applicable only in state spaces in which every action can be
 * "undone" by some other action.<br>
 * 
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 * 
 */
public class OnlineDFSAgent<S, A extends Action> extends AbstractAgent {

	private OnlineSearchProblem<S, A> problem;
	private Function<Percept, S> ptsFn;
	// persistent: result, a table, indexed by state and action, initially empty
	private final TwoKeyHashMap<S, A, S> result = new TwoKeyHashMap<>();
	// untried, a table that lists, for each state, the actions not yet tried
	private final Map<S, List<A>> untried = new HashMap<>();
	// unbacktracked, a table that lists,
	// for each state, the backtracks not yet tried
	private final Map<S, List<S>> unbacktracked = new HashMap<>();
	// s, a, the previous state and action, initially null
	private S s = null;
	private A a = null;

	/**
	 * Constructs an online DFS agent with the specified search problem and
	 * percept to state function.
	 * 
	 * @param problem
	 *            an online search problem for this agent to solve
	 * @param ptsFn
	 *            a function which returns the problem state associated with a
	 *            given Percept.
	 */
	public OnlineDFSAgent(OnlineSearchProblem<S, A> problem, Function<Percept, S> ptsFn) {
		setProblem(problem);
		setPerceptToStateFunction(ptsFn);
	}

	/**
	 * Returns the search problem for this agent.
	 * 
	 * @return the search problem for this agent.
	 */
	public OnlineSearchProblem<S, A> getProblem() {
		return problem;
	}

	/**
	 * Sets the search problem for this agent to solve.
	 * 
	 * @param problem
	 *            the search problem for this agent to solve.
	 */
	public void setProblem(OnlineSearchProblem<S, A> problem) {
		this.problem = problem;
		init();
	}

	/**
	 * Returns the percept to state function of this agent.
	 * 
	 * @return the percept to state function of this agent.
	 */
	public Function<Percept, S> getPerceptToStateFunction() {
		return ptsFn;
	}

	/**
	 * Sets the percept to state functino of this agent.
	 * 
	 * @param ptsFn
	 *            a function which returns the problem state associated with a
	 *            given Percept.
	 */
	public void setPerceptToStateFunction(Function<Percept, S> ptsFn) {
		this.ptsFn = ptsFn;
	}

	// function ONLINE-DFS-AGENT(s') returns an action
	// inputs: s', a percept that identifies the current state
	@Override
	public Action execute(Percept psPrimed) {
		S sPrimed = ptsFn.apply(psPrimed);
		// if GOAL-TEST(s') then return stop
		if (problem.testGoal(sPrimed)) {
			a = null;
		} else {
			// if s' is a new state (not in untried) then untried[s'] <-
			// ACTIONS(s')
			if (!untried.containsKey(sPrimed)) {
				untried.put(sPrimed, problem.getActions(sPrimed));
			}

			// if s is not null then do
			if (null != s) {
				// Note: If I've already seen the result of this
				// [s, a] then don't put it back on the unbacktracked
				// list otherwise you can keep oscillating
				// between the same states endlessly.
				if (!(sPrimed.equals(result.get(s, a)))) {
					// result[s, a] <- s'
					result.put(s, a, sPrimed);

					// Ensure the unbacktracked always has a list for s'
					if (!unbacktracked.containsKey(sPrimed)) {
						unbacktracked.put(sPrimed, new ArrayList<>());
					}

					// add s to the front of the unbacktracked[s']
					unbacktracked.get(sPrimed).add(0, s);
				}
			}
			// if untried[s'] is empty then
			if (untried.get(sPrimed).isEmpty()) {
				// if unbacktracked[s'] is empty then return stop
				if (unbacktracked.get(sPrimed).isEmpty()) {
					a = null;
				} else {
					// else a <- an action b such that result[s', b] =
					// POP(unbacktracked[s'])
					S popped = unbacktracked.get(sPrimed).remove(0);
					for (Pair<S, A> sa : result.keySet()) {
						if (sa.getFirst().equals(sPrimed) && result.get(sa).equals(popped)) {
							a = sa.getSecond();
							break;
						}
					}
				}
			} else {
				// else a <- POP(untried[s'])
				a = untried.get(sPrimed).remove(0);
			}
		}

		if (a == null) {
			// I'm either at the Goal or can't get to it,
			// which in either case I'm finished so just die.
			setAlive(false);
		}

		// s <- s'
		s = sPrimed;
		// return a
		return a != null ? a : NoOpAction.NO_OP;
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
}
