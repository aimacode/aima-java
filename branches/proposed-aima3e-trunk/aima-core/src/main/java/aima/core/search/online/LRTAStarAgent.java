package aima.core.search.online;

import java.util.Hashtable;
import java.util.Set;

import aima.core.agent.Action;
import aima.core.agent.Percept;
import aima.core.agent.impl.AbstractAgent;
import aima.core.agent.impl.NoOpAction;
import aima.core.search.framework.PerceptToStateFunction;
import aima.core.search.framework.Problem;

/**
 * Artificial Intelligence A Modern Approach (2nd Edition): Figure 4.23, page
 * 128.<br>
 * <code>
 * function LRTA*AGENT(s') returns an action
 *   inputs: s', a percept that identifies the current state
 *   static: result, a table, indexed by action and state, initially empty
 *           H, a table of cost estimates indexed by state, initially empty
 *           s, a, the previous state and action, initially null
 *           
 *   if GOAL-TEST(s') then return stop
 *   if s' is new state (not in H) then H[s'] <- h(s')
 *   unless s is null
 *     result[a, s] <- s'
 *     H[s] <- min LRTA*-COST(s, b, result[b, s], H)
 *             b (element of) ACTIONS(s)
 *   a <- an action b in ACTIONS(s') that minimizes LRTA*-COST(s', b, result[b, s'], H)
 *   s <- s'
 *   return a
 *   
 * 
 * function LRTA*-COST(s, a, s', H) returns a cost estimate
 *   if s' is undefined then return h(s)
 *   else return c(s, a, s') + H[s']
 * </code>
 * 
 * Figure 4.23 LRTA*-AGENT selects an action according to the value of
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

	private Problem problem;
	private PerceptToStateFunction ptsFunction;
	// static: result, a table, indexed by action and state, initially empty
	private final Hashtable<ActionState, Percept> result = new Hashtable<ActionState, Percept>();
	// H, a table of cost estimates indexed by state, initially empty
	private final Hashtable<Percept, Double> H = new Hashtable<Percept, Double>();
	// s, a, the previous state and action, initially null
	private Percept s = null;
	private Action a = null;

	public LRTAStarAgent(Problem problem, PerceptToStateFunction ptsFunction) {
		setProblem(problem);
		setPerceptToStateFunction(ptsFunction);
	}

	public Problem getProblem() {
		return problem;
	}

	public void setProblem(Problem problem) {
		this.problem = problem;
		init();
	}

	public PerceptToStateFunction getPerceptToStateFunction() {
		return ptsFunction;
	}

	public void setPerceptToStateFunction(PerceptToStateFunction ptsFunction) {
		this.ptsFunction = ptsFunction;
	}

	// function LRTA*AGENT(s') returns an action
	// inputs: s', a percept that identifies the current state
	@Override
	public Action execute(Percept sPrime) {

		// if GOAL-TEST(s') then return stop
		if (!goalTest(sPrime)) {
			// if s' is new state (not in H) then H[s'] <- h(s')
			if (!H.containsKey(sPrime)) {
				H.put(sPrime, getProblem().getHeuristicFunction()
						.getHeuristicValue(ptsFunction.getState(sPrime)));
			}
			// unless s is null
			if (null != s) {
				// result[a, s] <- s'
				result.put(new ActionState(a, s), sPrime);

				// H[s] <- min LRTA*-COST(s, b, result[b, s], H)
				// b (element of) ACTIONS(s)
				double min = Double.MAX_VALUE;
				for (Action b : actions(s)) {
					double cost = lrtaCost(s, b, result.get(new ActionState(b,
							s)));
					if (cost < min) {
						min = cost;
					}
				}
				H.put(s, min);
			}
			// a <- an action b in ACTIONS(s') that minimizes LRTA*-COST(s', b,
			// result[b, s'], H)
			double min = Double.MAX_VALUE;
			// Just in case no actions
			a = NoOpAction.NO_OP;
			for (Action b : actions(sPrime)) {
				double cost = lrtaCost(sPrime, b, result.get(new ActionState(b,
						sPrime)));
				if (cost < min) {
					min = cost;
					a = b;
				}
			}
		} else {
			a = NoOpAction.NO_OP;
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

	private boolean goalTest(Percept state) {
		return getProblem().isGoalState(ptsFunction.getState(state));
	}

	// function LRTA*-COST(s, a, s', H) returns a cost estimate
	private double lrtaCost(Percept s, Action action, Percept sPrime) {
		// if s' is undefined then return h(s)
		if (null == sPrime) {
			return getProblem().getHeuristicFunction().getHeuristicValue(ptsFunction.getState(s));
		}
		// else return c(s, a, s') + H[s']
		return getProblem().getStepCostFunction().cost(ptsFunction.getState(s), action, ptsFunction.getState(sPrime))
				+ H.get(sPrime);
	}

	private Set<Action> actions(Percept state) {
		return problem.getActionsFunction().actions(ptsFunction.getState(state));
	}
}
