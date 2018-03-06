package aima.core.search.online;

import java.util.HashMap;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;

import aima.core.agent.Action;
import aima.core.agent.Percept;
import aima.core.agent.impl.AbstractAgent;
import aima.core.agent.impl.NoOpAction;
import aima.core.search.framework.problem.OnlineSearchProblem;
import aima.core.util.datastructure.TwoKeyHashMap;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): Figure 4.24, page
 * 152.<br>
 * <br>
 * 
 * <pre>
 * function LRTA*-AGENT(s') returns an action
 *   inputs: s', a percept that identifies the current state
 *   persistent: result, a table, indexed by state and action, initially empty
 *               H, a table of cost estimates indexed by state, initially empty
 *               s, a, the previous state and action, initially null
 *           
 *   if GOAL-TEST(s') then return stop
 *   if s' is a new state (not in H) then H[s'] &lt;- h(s')
 *   if s is not null
 *     result[s, a] &lt;- s'
 *     H[s] &lt;-        min LRTA*-COST(s, b, result[s, b], H)
 *             b (element of) ACTIONS(s)
 *   a &lt;- an action b in ACTIONS(s') that minimizes LRTA*-COST(s', b, result[s', b], H)
 *   s &lt;- s'
 *   return a
 *   
 * function LRTA*-COST(s, a, s', H) returns a cost estimate
 *   if s' is undefined then return h(s)
 *   else return c(s, a, s') + H[s']
 * </pre>
 * 
 * Figure 4.24 LRTA*-AGENT selects an action according to the value of
 * neighboring states, which are updated as the agent moves about the state
 * space.<br>
 * <br>
 * <b>Note:</b> This algorithm fails to exit if the goal does not exist (e.g.
 * A<->B Goal=X), this could be an issue with the implementation. Comments
 * welcome.
 * 
 * @author Ciaran O'Reilly
 * @author Mike Stampone
 * @author Ruediger Lunde
 */
public class LRTAStarAgent<S, A extends Action> extends AbstractAgent {

	private OnlineSearchProblem<S, A> problem;
	private Function<Percept, S> ptsFn;
	private ToDoubleFunction<S> h;
	// persistent: result, a table, indexed by state and action, initially empty
	private final TwoKeyHashMap<S, A, S> result = new TwoKeyHashMap<>();
	// H, a table of cost estimates indexed by state, initially empty
	private final HashMap<S, Double> H = new HashMap<>();
	// s, a, the previous state and action, initially null
	private S s = null;
	private A a = null;

	/**
	 * Constructs a LRTA* agent with the specified search problem, percept to
	 * state function, and heuristic function.
	 * 
	 * @param problem
	 *            an online search problem for this agent to solve.
	 * @param ptsFn
	 *            a function which returns the problem state associated with a
	 *            given Percept.
	 * @param h
	 *            heuristic function <em>h(n)</em>, which estimates the cost of
	 *            the cheapest path from the state at node <em>n</em> to a goal
	 *            state.
	 */
	public LRTAStarAgent(OnlineSearchProblem<S, A> problem, Function<Percept, S> ptsFn, ToDoubleFunction<S> h) {
		setProblem(problem);
		setPerceptToStateFunction(ptsFn);
		setHeuristicFunction(h);
	}

	/**
	 * Returns the search problem of this agent.
	 * 
	 * @return the search problem of this agent.
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
	 * Sets the percept to state function of this agent.
	 * 
	 * @param ptsFn
	 *            a function which returns the problem state associated with a
	 *            given Percept.
	 */
	public void setPerceptToStateFunction(Function<Percept, S> ptsFn) {
		this.ptsFn = ptsFn;
	}

	/**
	 * Returns the heuristic function of this agent.
	 */
	public ToDoubleFunction<S> getHeuristicFunction() {
		return h;
	}

	/**
	 * Sets the heuristic function of this agent.
	 * 
	 * @param h
	 *            heuristic function <em>h(n)</em>, which estimates the cost of
	 *            the cheapest path from the state at node <em>n</em> to a goal
	 *            state.
	 */
	public void setHeuristicFunction(ToDoubleFunction<S> h) {
		this.h = h;
	}

	// function LRTA*-AGENT(s') returns an action
	// inputs: s', a percept that identifies the current state
	@Override
	public Action execute(Percept psPrimed) {
		S sPrimed = ptsFn.apply(psPrimed);
		// if GOAL-TEST(s') then return stop
		if (problem.testGoal(sPrimed)) {
			a = null;
		} else {
			// if s' is a new state (not in H) then H[s'] <- h(s')
			if (!H.containsKey(sPrimed)) {
				H.put(sPrimed, getHeuristicFunction().applyAsDouble(sPrimed));
			}
			// if s is not null
			if (null != s) {
				// result[s, a] <- s'
				result.put(s, a, sPrimed);

				// H[s] <- min LRTA*-COST(s, b, result[s, b], H)
				// b (element of) ACTIONS(s)
				double min = Double.MAX_VALUE;
				for (A b : problem.getActions(s)) {
					double cost = lrtaCost(s, b, result.get(s, b));
					if (cost < min) {
						min = cost;
					}
				}
				H.put(s, min);
			}
			// a <- an action b in ACTIONS(s') that minimizes LRTA*-COST(s', b,
			// result[s', b], H)
			double min = Double.MAX_VALUE;
			// Just in case no actions
			a = null;
			for (A b : problem.getActions(sPrimed)) {
				double cost = lrtaCost(sPrimed, b, result.get(sPrimed, b));
				if (cost < min) {
					min = cost;
					a = b;
				}
			}
		}

		// s <- s'
		s = sPrimed;

		if (a == null) {
			// I'm either at the Goal or can't get to it,
			// which in either case I'm finished so just die.
			setAlive(false);
		}
		// return a
		return a != null ? a : NoOpAction.NO_OP;
	}

	//
	// PRIVATE METHODS
	//
	private void init() {
		setAlive(true);
		result.clear();
		H.clear();
		s = null;
		a = null;
	}

	// function LRTA*-COST(s, a, s', H) returns a cost estimate
	private double lrtaCost(S s, A action, S sDelta) {
		// if s' is undefined then return h(s)
		if (null == sDelta) {
			return h.applyAsDouble(s);
		}
		// else return c(s, a, s') + H[s']
		return problem.getStepCosts(s, action, sDelta)
				+ H.get(sDelta);
	}
}
