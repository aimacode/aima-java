package aima.core.search.online;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.ToDoubleFunction;

import aima.core.agent.impl.SimpleAgent;
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
 * @param <P> The type used to represent percepts
 * @param <S> The type used to represent states
 * @param <A> The type of the actions to be used to navigate through the state space
 * @author Ciaran O'Reilly
 * @author Mike Stampone
 * @author Ruediger Lunde
 */
public class LRTAStarAgent<P, S, A> extends SimpleAgent<P, A> {

	private OnlineSearchProblem<S, A> problem;
	private Function<P, S> ptsFn;
	private ToDoubleFunction<S> h;
	/// persistent: result, a table, indexed by state and action, initially empty
	private final TwoKeyHashMap<S, A, S> result = new TwoKeyHashMap<>();
	/// H, a table of cost estimates indexed by state, initially empty
	private final HashMap<S, Double> H = new HashMap<>();
	/// s, a, the previous state and action, initially null
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
	 *            heuristic function <em>h(s)</em>, which estimates the cost of
	 *            the cheapest path from the state <em>s</em> to a goal state.
	 */
	public LRTAStarAgent(OnlineSearchProblem<S, A> problem, Function<P, S> ptsFn, ToDoubleFunction<S> h) {
		setProblem(problem);
		setPerceptToStateFunction(ptsFn);
		setHeuristicFunction(h);
	}

	/// function LRTA*-AGENT(s') returns an action
	/// inputs: s', a percept that identifies the current state
	@Override
	public Optional<A> act(P psPrimed) {
		S sPrimed = ptsFn.apply(psPrimed);
		/// if GOAL-TEST(s') then return stop
		if (problem.testGoal(sPrimed)) {
			a = null;
		} else {
			/// if s' is a new state (not in H) then H[s'] <- h(s')
			if (!H.containsKey(sPrimed))
				H.put(sPrimed, h.applyAsDouble(sPrimed));
			/// if s is not null
			if (s != null) {
				/// result[s, a] <- s'
				result.put(s, a, sPrimed);
				/// H[s] <- min LRTA*-COST(s, b, result[s, b], H)
				/// b (element of) ACTIONS(s)
				double min = problem.getActions(s).stream()
						.mapToDouble(b -> lrtaCost(s, b, result.get(s, b)))
						.min().orElse(Double.MAX_VALUE);
				H.put(s, min);
			}
			/// a <- an action b in ACTIONS(s') that minimizes LRTA*-COST(s', b, result[s', b], H)
			a = problem.getActions((sPrimed)).stream()
					.min(Comparator.comparingDouble(b -> lrtaCost(sPrimed, b, result.get(sPrimed, b))))
					.orElse(null);
		}
		if (a == null)
			// I'm either at the goal or can't get to it, which in either case I'm finished so just die.
			setAlive(false);

		/// s <- s'
		s = sPrimed;
		/// return a
		return Optional.ofNullable(a);
	}


	/// function LRTA*-COST(s, a, s', H) returns a cost estimate
	private double lrtaCost(S s, A action, S sDelta) {
		/// if s' is undefined then return h(s)
		if (sDelta == null)
			return h.applyAsDouble(s);
		/// else return c(s, a, s') + H[s']
		return problem.getStepCosts(s, action, sDelta) + H.get(sDelta);
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

		result.clear();
		H.clear();
		s = null;
		a = null;
		setAlive(true);
	}

	/**
	 * Returns the percept to state function of this agent.
	 * 
	 * @return the percept to state function of this agent.
	 */
	public Function<P, S> getPerceptToStateFunction() {
		return ptsFn;
	}

	/**
	 * Sets the percept to state function of this agent.
	 * 
	 * @param ptsFn
	 *            a function which returns the problem state associated with a
	 *            given Percept.
	 */
	public void setPerceptToStateFunction(Function<P, S> ptsFn) {
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
	 *            heuristic function <em>h(s)</em>, which estimates the cost of
	 *            the cheapest path from the state <em>s</em> to a goal state.
	 */
	public void setHeuristicFunction(ToDoubleFunction<S> h) {
		this.h = h;
	}
}
