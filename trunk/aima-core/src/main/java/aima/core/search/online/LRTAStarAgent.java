package aima.core.search.online;

import java.util.HashMap;
import java.util.Set;

import aima.core.agent.Action;
import aima.core.agent.Percept;
import aima.core.agent.impl.AbstractAgent;
import aima.core.agent.impl.NoOpAction;
import aima.core.search.framework.HeuristicFunction;
import aima.core.search.framework.PerceptToStateFunction;
import aima.core.util.TwoKeyHashMap;

/**
 * Artificial Intelligence A Modern Approach 3rdd Edition): Figure 4.24, page 152.<br>
 * <code>
 * function LRTA*-AGENT(s') returns an action
 *   inputs: s', a percept that identifies the current state
 *   persistent: result, a table, indexed by state and action, initially empty
 *               H, a table of cost estimates indexed by state, initially empty
 *               s, a, the previous state and action, initially null
 *           
 *   if GOAL-TEST(s') then return stop
 *   if s' is a new state (not in H) then H[s'] <- h(s')
 *   if s is not null
 *     result[s, a] <- s'
 *     H[s] <-        min LRTA*-COST(s, b, result[s, b], H)
 *             b (element of) ACTIONS(s)
 *   a <- an action b in ACTIONS(s') that minimizes LRTA*-COST(s', b, result[s', b], H)
 *   s <- s'
 *   return a
 *   
 * function LRTA*-COST(s, a, s', H) returns a cost estimate
 *   if s' is undefined then return h(s)
 *   else return c(s, a, s') + H[s']
 * </code>
 * 
 * Figure 4.24 LRTA*-AGENT selects an action according to the value of
 * neighboring states, which are updated as the agent moves about the state
 * space.<br>
 * Note: This algorithm fails to exit if the goal does not exist (e.g. A<->B Goal=X),
 * this could be an issue with the implementation. Comments welcome.
 */

/**
 * @author Ciaran O'Reilly
 * 
 */
public class LRTAStarAgent extends AbstractAgent {

	private OnlineSearchProblem problem;
	private PerceptToStateFunction ptsFunction;
	private HeuristicFunction hf;
	// persistent: result, a table, indexed by state and action, initially empty
	private final TwoKeyHashMap<Object, Action, Object> result = new TwoKeyHashMap<Object, Action, Object>();
	// H, a table of cost estimates indexed by state, initially empty
	private final HashMap<Object, Double> H = new HashMap<Object, Double>();
	// s, a, the previous state and action, initially null
	private Object s = null;
	private Action a = null;

	public LRTAStarAgent(OnlineSearchProblem problem,
			PerceptToStateFunction ptsFunction, HeuristicFunction hf) {
		setProblem(problem);
		setPerceptToStateFunction(ptsFunction);
		setHeuristicFunction(hf);
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

	public HeuristicFunction getHeuristicFunction() {
		return hf;
	}

	public void setHeuristicFunction(HeuristicFunction hf) {
		this.hf = hf;
	}

	// function LRTA*-AGENT(s') returns an action
	// inputs: s', a percept that identifies the current state
	@Override
	public Action execute(Percept psPrime) {
		Object sPrime = ptsFunction.getState(psPrime);
		// if GOAL-TEST(s') then return stop
		if (goalTest(sPrime)) {
			a = NoOpAction.NO_OP;
		} else {
			// if s' is a new state (not in H) then H[s'] <- h(s')
			if (!H.containsKey(sPrime)) {
				H.put(sPrime, getHeuristicFunction().h(sPrime));
			}
			// if s is not null
			if (null != s) {
				// result[s, a] <- s'
				result.put(s, a, sPrime);

				// H[s] <- min LRTA*-COST(s, b, result[s, b], H)
				// b (element of) ACTIONS(s)
				double min = Double.MAX_VALUE;
				for (Action b : actions(s)) {
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
			a = NoOpAction.NO_OP;
			for (Action b : actions(sPrime)) {
				double cost = lrtaCost(sPrime, b, result.get(sPrime, b));
				if (cost < min) {
					min = cost;
					a = b;
				}
			}
		}

		// s <- s'
		s = sPrime;

		if (a.isNoOp()) {
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
	private double lrtaCost(Object s, Action action, Object sPrime) {
		// if s' is undefined then return h(s)
		if (null == sPrime) {
			return getHeuristicFunction().h(s);
		}
		// else return c(s, a, s') + H[s']
		return getProblem().getStepCostFunction().c(s, action, sPrime)
				+ H.get(sPrime);
	}

	private Set<Action> actions(Object state) {
		return problem.getActionsFunction().actions(state);
	}
}
