package aima.core.search.online;

import java.util.HashMap;
import java.util.Set;

import aima.core.api.agent.Agent;
import aima.core.search.framework.HeuristicFunction;
import aima.core.search.framework.PerceptToStateFunction;
import aima.core.util.datastructure.TwoKeyHashMap;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): Figure ?? , page
 * ?? .<br>
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
 * Figure ?? LRTA*-AGENT selects an action according to the value of
 * neighboring states, which are updated as the agent moves about the state
 * space.<br>
 * <br>
 * <b>Note:</b> This algorithm fails to exit if the goal does not exist (e.g.
 * A<->B Goal=X), this could be an issue with the implementation. Comments
 * welcome.
 * 
 * @author Subham Mishra
 * @author Ciaran O'Reilly
 * @author Mike Stampone
 */

public class LRTAStarAgent<A,P> implements Agent<A,P>{

	   private boolean alive = true;
	   private OnlineSearchProblem<A> problem;
	   private PerceptToStateFunction<P> ptsFunction;
	   private HeuristicFunction hf;
	// persistent: result, a table, indexed by state and action, initially empty
		private final TwoKeyHashMap<Object, A, Object> result = new TwoKeyHashMap<Object, A, Object>();
		// H, a table of cost estimates indexed by state, initially empty
		private final HashMap<Object, Double> H = new HashMap<Object, Double>();
		// s, a, the previous state and action, initially null
		private Object s = null;
		private A a = null;
		/**
		 * Constructs a LRTA* agent with the specified search problem, percept to
		 * state function, and heuristic function.
		 * 
		 * @param problem
		 *            an online search problem for this agent to solve.
		 * @param ptsFunction
		 *            a function which returns the problem state associated with a
		 *            given Percept.
		 * @param hf
		 *            heuristic function <em>h(n)</em>, which estimates the cost of
		 *            the cheapest path from the state at node <em>n</em> to a goal
		 *            state.
		 */
		public LRTAStarAgent(OnlineSearchProblem<A> problem,
				PerceptToStateFunction<P> ptsFunction, HeuristicFunction hf) {
			setProblem(problem);
			setPerceptToStateFunction(ptsFunction);
			setHeuristicFunction(hf);
		}
		/**
		 * Returns the search problem of this agent.
		 * 
		 * @return the search problem of this agent.
		 */
		public OnlineSearchProblem<A> getProblem() {
			return problem;
		}
		/**
		 * Sets the search problem for this agent to solve.
		 * 
		 * @param problem
		 *            the search problem for this agent to solve.
		 */
		public void setProblem(OnlineSearchProblem<A> problem) {
			this.problem = problem;
			init();
		}

		/**
		 * Returns the percept to state function of this agent.
		 * 
		 * @return the percept to state function of this agent.
		 */
		public PerceptToStateFunction<P> getPerceptToStateFunction() {
			return ptsFunction;
		}

		/**
		 * Sets the percept to state function of this agent.
		 * 
		 * @param ptsFunction
		 *            a function which returns the problem state associated with a
		 *            given Percept.
		 */
		public void setPerceptToStateFunction(PerceptToStateFunction<P> ptsFunction) {
			this.ptsFunction = ptsFunction;
		}

		/**
		 * Returns the heuristic function of this agent.
		 */
		public HeuristicFunction getHeuristicFunction() {
			return hf;
		}

		/**
		 * Sets the heuristic function of this agent.
		 * 
		 * @param hf
		 *            heuristic function <em>h(n)</em>, which estimates the cost of
		 *            the cheapest path from the state at node <em>n</em> to a goal
		 *            state.
		 */
		public void setHeuristicFunction(HeuristicFunction hf) {
			this.hf = hf;
		}

		// function LRTA*-AGENT(s') returns an action
		// inputs: s', a percept that identifies the current state
		public A execute(P psDelta) {
			Object sDelta = ptsFunction.getState(psDelta);
			// if GOAL-TEST(s') then return stop
			if (goalTest(sDelta)) {
				a = null; //no operation
			} else {
				// if s' is a new state (not in H) then H[s'] <- h(s')
				if (!H.containsKey(sDelta)) {
					H.put(sDelta, getHeuristicFunction().h(sDelta));
				}
				// if s is not null
				if (null != s) {
					// result[s, a] <- s'
					result.put(s, a, sDelta);

					// H[s] <- min LRTA*-COST(s, b, result[s, b], H)
					// b (element of) ACTIONS(s)
					double min = Double.MAX_VALUE;
					for (A b : actions(s)) {
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
				for (A b : actions(sDelta)) {
					double cost = lrtaCost(sDelta, b, result.get(sDelta, b));
					if (cost < min) {
						min = cost;
						a = b;
					}
				}
			}

			// s <- s'
			s = sDelta;

			if (goalTest(sDelta)) {
				// I'm either at the Goal or can't get to it,
				// which in either case I'm finished so just die.
				setAlive(false);
			}
			// return a
			return a;
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

		private boolean goalTest(Object state) {
			return getProblem().isGoalState(state);
		}

		// function LRTA*-COST(s, a, s', H) returns a cost estimate
		private double lrtaCost(Object s, A action, Object sDelta) {
			// if s' is undefined then return h(s)
			if (null == sDelta) {
				return getHeuristicFunction().h(s);
			}
			// else return c(s, a, s') + H[s']
			return getProblem().getStepCostFunction().c(s, action, sDelta)
					+ H.get(sDelta);
		}
		private Set<A> actions(Object state) {
			return problem.getActionsFunction().actions(state);
		}
		/**
		 * Life-cycle indicator as to the liveness of an Agent.
		 * 
		 * @return true if the Agent is to be considered alive, false otherwise.
		 */
		public boolean isAlive() {
			return alive;
		}
		/**
		 * Set the current liveness of the Agent.
		 * 
		 * @param alive
		 *            set to true if the Agent is to be considered alive, false
		 *            otherwise.
		 */
		public void setAlive(boolean alive) {
			this.alive = alive;
		}
		//
		//SUPPORTING CODE
		//
		public A perceive(P percept)
		{
			return null;
		}
}