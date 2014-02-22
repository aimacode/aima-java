package aima.core.environment.wumpusworld;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import aima.core.agent.Action;
import aima.core.agent.Percept;
import aima.core.agent.impl.AbstractAgent;
import aima.core.agent.impl.DynamicAction;
import aima.core.environment.wumpusworld.action.Forward;
import aima.core.environment.wumpusworld.action.Shoot;
import aima.core.environment.wumpusworld.action.TurnLeft;
import aima.core.search.framework.GoalTest;
import aima.core.search.framework.GraphSearch;
import aima.core.search.framework.HeuristicFunction;
import aima.core.search.framework.Problem;
import aima.core.search.framework.Search;
import aima.core.search.framework.SearchAgent;
import aima.core.search.informed.AStarSearch;
import aima.core.util.datastructure.FIFOQueue;
import aima.core.util.datastructure.Queue;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 270.<br>
 * <br>
 * 
 * <pre>
 * <code>
 * function HYBRID-WUMPUS-AGENT(percept) returns an action
 *   inputs: percept, a list, [stench, breeze, glitter, bump, scream]
 *   persistent: KB, a knowledge base, initially the atemporal "wumpus physics"
 *               t, a counter, initially 0, indicating time
 *               plan, an action sequence, initially empty
 * 
 *   TELL(KB, MAKE-PERCEPT-SENTENCE(percept, t))
 *   TELL the KB the temporal "physics" sentences for time t
 *   safe <- {[x, y] : ASK(KB, OK<sup>t</sup><sub>x,y</sub>) = true}
 *   if ASK(KB, Glitter<sup>t</sup>) = true then
 *      plan <- [Grab] + PLAN-ROUTE(current, {[1,1]}, safe) + [Climb]
 *   if plan is empty then
 *      unvisited <- {[x, y] : ASK(KB, L<sup>t'</sup><sub>x,y</sub>) = false for all t' < t}
 *      plan <- PLAN-ROUTE(current, unvisited &cap; safe, safe)
 *   if plan is empty and ASK(KB, HaveArrow<sup>t</sup>) = true then
 *      possible_wumpus <- {[x, y] : ASK(KB, ~W<sub>x,y</sub>) = false}
 *      plan <- PLAN-SHOT(current, possible_wumpus, safe)
 *   if plan is empty then //no choice but to take a risk
 *      not_unsafe <- {[x, y] : ASK(KB, ~OK<sup>t</sup><sub>x,y</sub>) = false}
 *      plan <- PLAN-ROUTE(current, unvisited &cap; not_unsafe, safe)
 *   if plan is empty then
 *      plan <- PLAN-ROUTE(current, {[1,1]}, safe) + [Climb]
 *   action <- POP(plan)
 *   TELL(KB, MAKE-ACTION-SENTENCE(action, t))
 *   t <- t+1
 *   return action
 * 
 * --------------------------------------------------------------------------------
 * 
 * function PLAN-ROUTE(current, goals, allowed) returns an action sequence
 *   inputs: current, the agent's current position
 *           goals, a set of squares; try to plan a route to one of them
 *           allowed, a set of squares that can form part of the route 
 *    
 *   problem <- ROUTE-PROBLEM(current, goals, allowed)
 *   return A*-GRAPH-SEARCH(problem)
 * </code>
 * </pre>
 * 
 * Figure 7.20 A hybrid agent program for the wumpus world. It uses a
 * propositional knowledge base to infer the state of the world, and a
 * combination of problem-solving search and domain-specific code to decide what
 * actions to take
 * 
 * @author Federico Baron
 * @author Alessandro Daniele
 * @author Ciaran O'Reilly
 */
public class HybridWumpusAgent extends AbstractAgent {

	// persistent: KB, a knowledge base, initially the atemporal
	// "wumpus physics"
	// i.e. default is a 4x4 world as depicted in figure 7.2
	private WumpusKnowledgeBase kb = new WumpusKnowledgeBase(4);
	// t, a counter, initially 0, indicating time
	private int t = 0;
	// plan, an action sequence, initially empty
	private Queue<Action> plan = new FIFOQueue<Action>();
	// the agents current location (default to same as figure 7.2)
	private AgentPosition current = new AgentPosition(1, 1,
			AgentPosition.Orientation.FACING_RIGHT);

	/**
	 * function HYBRID-WUMPUS-AGENT(percept) returns an action<br>
	 * 
	 * @param percept
	 *            a list, [stench, breeze, glitter, bump, scream]
	 * 
	 * @return an action the agent should take.
	 */
	@Override
	public Action execute(Percept percept) {

		// TELL(KB, MAKE-PERCEPT-SENTENCE(percept, t))
		kb.makePerceptSentence((AgentPercept) percept, t);
		// TELL the KB the temporal "physics" sentences for time t
		kb.addTemporalSentences(t);

		// safe <- {[x, y] : ASK(KB, OK<sup>t</sup><sub>x,y</sub>) = true}
		Room tmp = null;
		for (int x = 1; x <= kb.getCaveXDimension(); x++) {
			for (int y = 1; tmp == null && y <= kb.getCaveYDimension(); y++) {
				if (kb.ask("L" + t + "_" + x + "_" + y) == true) {
					tmp = new Room(x, y);
				}
			}
		}

		if (kb.ask("FacingNorth" + t)) {
			current = new AgentPosition((int) tmp.getX(), (int) tmp.getY(),
					AgentPosition.Orientation.FACING_UP);
		} else if (kb.ask("FacingEast" + t)) {
			current = new AgentPosition((int) tmp.getX(), (int) tmp.getY(),
					AgentPosition.Orientation.FACING_RIGHT);
		} else if (kb.ask("FacingSouth" + t)) {
			current = new AgentPosition((int) tmp.getX(), (int) tmp.getY(),
					AgentPosition.Orientation.FACING_DOWN);
		} else if (kb.ask("FacingWest" + t)) {
			current = new AgentPosition((int) tmp.getX(), (int) tmp.getY(),
					AgentPosition.Orientation.FACING_LEFT);
		}

		List<Room> safe = new ArrayList<Room>();
		for (int x = 1; x <= kb.getCaveXDimension(); x++) {
			for (int y = 1; y <= kb.getCaveYDimension(); y++) {
				if (kb.ask("OK" + t + "_" + x + "_" + y)) {
					safe.add(new Room(x, y));
				}
			}
		}

		// if ASK(KB, Glitter<sup>t</sup>) = true then
		// plan <- [Grab] + PLAN-ROUTE(current, {[1,1]}, safe) + [Climb]
		if (kb.ask("Glitter" + t)) {
			Action grab = new DynamicAction("grab");
			Action climb = new DynamicAction("climb");
			List<Room> goals = new ArrayList<Room>();
			goals.add(new Room(1, 1));

			plan.add(grab);
			plan.addAll(planRoute(current, goals, safe));
			plan.add(climb);
		}

		// if plan is empty then
		// unvisited <- {[x, y] : ASK(KB, L<sup>t'</sup><sub>x,y</sub>) = false
		// for all t' < t}
		// plan <- PLAN-ROUTE(current, unvisited &cap; safe, safe)
		if (plan.isEmpty()) {
			List<Room> unvisited = new ArrayList<Room>();
			for (int x = 1; x <= kb.getCaveXDimension(); x++) {
				for (int y = 1; y <= kb.getCaveYDimension(); y++) {
					for (int k = 0; k < t; k++) {
						if (kb.ask("L" + k + "_" + x + "_" + y)) {
							unvisited.add(new Room(x, y));
						}
					}
				}
			}

			List<Room> unvisitedAndSafe = new ArrayList<Room>();
			for (Room u : unvisited) {
				for (Room s : safe) {
					if (!unvisitedAndSafe.contains(u) && (s.getX() == u.getX())
							&& (s.getY() == u.getY())) {
						unvisitedAndSafe.add(u);
					}
				}
			}

			plan.addAll(planRoute(current, unvisitedAndSafe, safe));
		}

		// if plan is empty and ASK(KB, HaveArrow<sup>t</sup>) = true then
		// possible_wumpus <- {[x, y] : ASK(KB, ~W<sub>x,y</sub>) = false}
		// plan <- PLAN-SHOT(current, possible_wumpus, safe)
		if (plan.isEmpty() && kb.ask("HaveArrow" + t)) {
			List<Room> possibleWumpus = new ArrayList<Room>();
			for (int x = 1; x <= kb.getCaveXDimension(); x++) {
				for (int y = 1; y <= kb.getCaveYDimension(); y++) {
					if (kb.ask("W_" + x + "_" + y) == false) {
						possibleWumpus.add(new Room(x, y));
					}
				}
			}
			plan.addAll(planShot(current, possibleWumpus, safe));
		}

		// if plan is empty then //no choice but to take a risk
		// not_unsafe <- {[x, y] : ASK(KB, ~OK<sup>t</sup><sub>x,y</sub>) =
		// false}
		// plan <- PLAN-ROUTE(current, unvisited &cap; not_unsafe, safe)
		if (plan.isEmpty()) {
			List<Room> notUnsafe = new ArrayList<Room>();
			for (int x = 1; x <= kb.getCaveXDimension(); x++) {
				for (int y = 1; y <= kb.getCaveYDimension(); y++) {
					if (kb.ask("OK" + t + "_" + x + "_" + y) == false) {
						notUnsafe.add(new Room(x, y));
					}
				}
			}
			plan.addAll(planRoute(current, notUnsafe, safe));
		}

		// if plan is empty then
		// plan PLAN-ROUTE(current, {[1,1]}, safe) + [Climb]
		if (plan.isEmpty()) {
			ArrayList<Room> start = new ArrayList<Room>();
			start.add(new Room(1, 1));
			plan.addAll(planRoute(current, start, safe));
			plan.add(new DynamicAction("climb"));
		}
		// action <- POP(plan)
		Action action = plan.pop();
		// TELL(KB, MAKE-ACTION-SENTENCE(action, t))
		kb.makeActionSentence(action, t);
		// t <- t+1
		t = t + 1;
		// return action
		return action;
	}

	/**
	 * Returns a sequence of actions using A* Search.
	 * 
	 * @param current
	 *            the agent's current position
	 * @param goals
	 *            a set of squares; try to plan a route to one of them
	 * @param allowed
	 *            a set of squares that can form part of the route
	 * 
	 * @return the best sequence of actions that the agent have to do to reach a
	 *         goal from the current position.
	 * 
	 * 
	 * @author Federico Baron
	 * @author Alessandro Daniele
	 */
	private List<Action> planRoute(AgentPosition current, List<Room> goals,
			List<Room> allowed) {

		// Every square represent 4 possible positions for the agent, it could
		// be in different orientations. For every square in allowed and goals
		// sets we add 4 squares.
		Set<AgentPosition> allowedPositions = new LinkedHashSet<AgentPosition>();
		for (int i = 0; i < allowed.size(); i++) {
			int x = (int) allowed.get(i).getX();
			int y = (int) allowed.get(i).getY();

			allowedPositions.add(new AgentPosition(x, y,
					AgentPosition.Orientation.FACING_LEFT));
			allowedPositions.add(new AgentPosition(x, y,
					AgentPosition.Orientation.FACING_RIGHT));
			allowedPositions.add(new AgentPosition(x, y,
					AgentPosition.Orientation.FACING_UP));
			allowedPositions.add(new AgentPosition(x, y,
					AgentPosition.Orientation.FACING_DOWN));
		}
		final Set<AgentPosition> goalPositions = new LinkedHashSet<AgentPosition>();
		for (int i = 0; i < goals.size(); i++) {
			int x = (int) goals.get(i).getX();
			int y = (int) goals.get(i).getY();

			goalPositions.add(new AgentPosition(x, y,
					AgentPosition.Orientation.FACING_LEFT));
			goalPositions.add(new AgentPosition(x, y,
					AgentPosition.Orientation.FACING_RIGHT));
			goalPositions.add(new AgentPosition(x, y,
					AgentPosition.Orientation.FACING_UP));
			goalPositions.add(new AgentPosition(x, y,
					AgentPosition.Orientation.FACING_DOWN));
		}

		WumpusCave cave = new WumpusCave(kb.getCaveXDimension(),
				kb.getCaveYDimension(), allowedPositions);

		GoalTest goalTest = new GoalTest() {

			@Override
			public boolean isGoalState(Object state) {
				if (goalPositions.contains(state)) {
					return true;
				} else {
					return false;
				}
			}
		};

		Problem problem = new Problem(current,
				WumpusFunctionFactory.getActionsFunction(cave),
				WumpusFunctionFactory.getResultFunction(), goalTest);

		HeuristicFunction hf = new ManhattanHeuristicFunction(goals);

		Search search = new AStarSearch(new GraphSearch(), hf);
		SearchAgent agent = null;
		try {
			agent = new SearchAgent(problem, search);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return agent.getActions();
	}

	//
	// SUPPORTING CODE
	//
	public HybridWumpusAgent(int caveXandYDimensions) {
		kb = new WumpusKnowledgeBase(caveXandYDimensions);
	}

	/**
	 * 
	 * @param current
	 *            the agent's current position
	 * @param possibleWumpus
	 *            a set of squares where we don't know that there isn't the
	 *            wumpus.
	 * @param allowed
	 *            a set of squares that can form part of the route
	 * 
	 * @return the sequence of actions to reach the nearest square that is in
	 *         line with a possible wumpus position. The last action is a shot.
	 * 
	 * 
	 * @author Federico Baron
	 * @author Alessandro Daniele
	 */
	private List<Action> planShot(AgentPosition current,
			List<Room> possibleWumpus, List<Room> allowed) {

		Set<AgentPosition> shootingPositions = new LinkedHashSet<AgentPosition>();

		for (Room p : possibleWumpus) {
			int x = (int) p.getX();
			int y = (int) p.getY();

			for (int i = 1; i <= kb.getCaveXDimension(); i++) {
				if (i < x) {
					shootingPositions.add(new AgentPosition(i, y,
							AgentPosition.Orientation.FACING_RIGHT));
				}
				if (i > x) {
					shootingPositions.add(new AgentPosition(i, y,
							AgentPosition.Orientation.FACING_LEFT));
				}
				if (i < y) {
					shootingPositions.add(new AgentPosition(x, i,
							AgentPosition.Orientation.FACING_UP));
				}
				if (i > y) {
					shootingPositions.add(new AgentPosition(x, i,
							AgentPosition.Orientation.FACING_DOWN));
				}
			}
		}

		for (Room p : possibleWumpus) {
			for (AgentPosition.Orientation orientation : AgentPosition.Orientation
					.values())
				shootingPositions.remove(new AgentPosition((int) p.getX(),
						(int) p.getY(), orientation));
		}

		Iterator<AgentPosition> it = shootingPositions.iterator();
		List<Room> shootingPositionsArray = new ArrayList<Room>();
		while (it.hasNext()) {
			AgentPosition tmp = it.next();
			shootingPositionsArray.add(new Room(tmp.getX(), tmp.getY()));
		}

		List<Action> actions = planRoute(current, shootingPositionsArray,
				allowed);

		AgentPosition newPos;
		if (actions.get(actions.size() - 1).isNoOp()) {
			newPos = current;
			actions.clear();
		} else {
			newPos = ((Forward) actions.get(actions.size() - 1))
					.getToPosition();
		}

		while (!shootingPositions.contains(newPos)) {
			TurnLeft tLeft = new TurnLeft(newPos.getOrientation());
			newPos = new AgentPosition(newPos.getX(), newPos.getY(),
					tLeft.getToOrientation());
			actions.add(tLeft);
		}

		actions.add(new Shoot());
		return actions;
	}

}
