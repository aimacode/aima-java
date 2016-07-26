package aima.core.environment.wumpusworld;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import aima.core.agent.api.Agent;
import aima.core.environment.wumpusworld.action.Climb;
import aima.core.environment.wumpusworld.action.Forward;
import aima.core.environment.wumpusworld.action.Grab;
import aima.core.environment.wumpusworld.action.Shoot;
import aima.core.environment.wumpusworld.action.TurnLeft;
import aima.core.environment.wumpusworld.action.WWAction;
import aima.core.logic.basic.propositional.parsing.PLParser;
import aima.core.search.api.GoalTestPredicate;
import aima.core.search.basic.informed.AStarSearch;
import aima.core.search.basic.support.BasicProblem;
import aima.core.util.SetOps;

/**
 * Artificial Intelligence WWAction Modern Approach (4th Edition): page ???.<br>
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
 *      unvisited <- {[x, y] : ASK(KB, L<sup>t'</sup><sub>x,y</sub>) = false for all t' &le; t}
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
 *   return WWAction*-GRAPH-SEARCH(problem)
 * </code>
 * </pre>
 * 
 * Figure 7.20 WWAction hybrid agent program for the wumpus world. It uses a
 * propositional knowledge base to infer the state of the world, and a
 * combination of problem-solving search and domain-specific code to decide what
 * actions to take
 * 
 * @author Federico Baron
 * @author Alessandro Daniele
 * @author Ciaran O'Reilly
 * @author Anurag Rai
 */
public class HybridWumpusAgent implements Agent<WWAction, AgentPercept> {

	// persistent: KB, a knowledge base, initially the atemporal
	// "wumpus physics"
	private WumpusKnowledgeBase<WWAction> kb = null;
	// t, a counter, initially 0, indicating time
	private int t = 0;
	// plan, an action sequence, initially empty
	private Queue<WWAction> plan = new LinkedList<>();

	/**
	 * function HYBRID-WUMPUS-AGENT(percept) returns an action<br>
	 * 
	 * @param percept
	 *            a list, [stench, breeze, glitter, bump, scream]
	 * 
	 * @return an action the agent should take.
	 */
	@Override
	public WWAction perceive(AgentPercept percept) {
		// TELL(KB, MAKE-PERCEPT-SENTENCE(percept, t))
		kb.makePerceptSentence(percept, t);
		// TELL the KB the temporal "physics" sentences for time t
		kb.tellTemporalPhysicsSentences(t);

		AgentPosition current = kb.askCurrentPosition(t);

		// safe <- {[x, y] : ASK(KB, OK<sup>t</sup><sub>x,y</sub>) = true}
		Set<Room> safe = kb.askSafeRooms(t);

		// if ASK(KB, Glitter<sup>t</sup>) = true then
		if (kb.askGlitter(t)) {
			// plan <- [Grab] + PLAN-ROUTE(current, {[1,1]}, safe) + [Climb]
			Set<Room> goals = new LinkedHashSet<>();
			goals.add(new Room(1, 1));

			plan.add(new Grab());
			plan.addAll(planRoute(current, goals, safe));
			plan.add(new Climb());
		}

		// if plan is empty then
		// unvisited <- {[x, y] : ASK(KB, L<sup>t'</sup><sub>x,y</sub>) = false
		// for all t' &le; t}
		Set<Room> unvisited = kb.askUnvisitedRooms(t);
		if (plan.isEmpty()) {
			// plan <- PLAN-ROUTE(current, unvisited &cap; safe, safe)
			plan.addAll(planRoute(current, SetOps.intersection(unvisited, safe), safe));
		}

		// if plan is empty and ASK(KB, HaveArrow<sup>t</sup>) = true then
		if (plan.isEmpty() && kb.askHaveArrow(t)) {
			// possible_wumpus <- {[x, y] : ASK(KB, ~W<sub>x,y</sub>) = false}
			Set<Room> possibleWumpus = kb.askPossibleWumpusRooms(t);
			// plan <- PLAN-SHOT(current, possible_wumpus, safe)
			plan.addAll(planShot(current, possibleWumpus, safe));
		}

		// if plan is empty then //no choice but to take a risk
		if (plan.isEmpty()) {
			// not_unsafe <- {[x, y] : ASK(KB, ~OK<sup>t</sup><sub>x,y</sub>) =
			// false}
			Set<Room> notUnsafe = kb.askNotUnsafeRooms(t);
			// plan <- PLAN-ROUTE(current, unvisited &cap; not_unsafe, safe)
			plan.addAll(planRoute(current, SetOps.intersection(unvisited, notUnsafe), safe));
		}

		// if plan is empty then
		if (plan.isEmpty()) {
			// plan PLAN-ROUTE(current, {[1,1]}, safe) + [Climb]
			Set<Room> start = new LinkedHashSet<>();
			start.add(new Room(1, 1));
			plan.addAll(planRoute(current, start, safe));
			plan.add(new Climb());
		}
		// action <- POP(plan)
		WWAction action = plan.remove();
		// TELL(KB, MAKE-ACTION-SENTENCE(action, t))
		kb.makeActionSentence(action, t);
		// t <- t+1
		t = t + 1;
		// return action
		return action;
	}

	/**
	 * Returns a sequence of actions using WWAction* Search.
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
	 */
	public List<WWAction> planRoute(AgentPosition current, Set<Room> goals, Set<Room> allowed) {

		// Every square represent 4 possible positions for the agent, it could
		// be in different orientations. For every square in allowed and goals
		// sets we add 4 squares.
		Set<AgentPosition> allowedPositions = new LinkedHashSet<>();
		for (Room allowedRoom : allowed) {
			int x = allowedRoom.getX();
			int y = allowedRoom.getY();

			allowedPositions.add(new AgentPosition(x, y, AgentPosition.Orientation.FACING_WEST));
			allowedPositions.add(new AgentPosition(x, y, AgentPosition.Orientation.FACING_EAST));
			allowedPositions.add(new AgentPosition(x, y, AgentPosition.Orientation.FACING_NORTH));
			allowedPositions.add(new AgentPosition(x, y, AgentPosition.Orientation.FACING_SOUTH));
		}
		final Set<AgentPosition> goalPositions = new LinkedHashSet<>();
		for (Room goalRoom : goals) {
			int x = goalRoom.getX();
			int y = goalRoom.getY();

			goalPositions.add(new AgentPosition(x, y, AgentPosition.Orientation.FACING_WEST));
			goalPositions.add(new AgentPosition(x, y, AgentPosition.Orientation.FACING_EAST));
			goalPositions.add(new AgentPosition(x, y, AgentPosition.Orientation.FACING_NORTH));
			goalPositions.add(new AgentPosition(x, y, AgentPosition.Orientation.FACING_SOUTH));
		}

		WumpusCave cave = new WumpusCave(kb.getCaveXDimension(), kb.getCaveYDimension(), allowedPositions);

		GoalTestPredicate<AgentPosition> goalTest = state -> {
			if (goalPositions.contains(state)) {
				return true;
			}
			return false;
		};

		BasicProblem<WWAction, AgentPosition> problem = new BasicProblem<>(current,
				WumpusFunctionFactory.getActionsFunction(cave), WumpusFunctionFactory.getResultFunction(), goalTest);

		AStarSearch<WWAction, AgentPosition> search = new AStarSearch<>(new ManhattanHeuristicFunction(goals));

		List<WWAction> actions = null;
		try {
			actions = search.apply(problem);
			// Search agent can return a NoOp if already at goal,
			// in the context of this agent we will just return
			// no actions.
			if (actions.size() == 1 && (actions.get(0) == null)) {
				actions = new ArrayList<>();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return actions;
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
	 */
	public List<WWAction> planShot(AgentPosition current, Set<Room> possibleWumpus, Set<Room> allowed) {

		Set<AgentPosition> shootingPositions = new LinkedHashSet<>();

		for (Room p : possibleWumpus) {
			int x = p.getX();
			int y = p.getY();

			for (int i = 1; i <= kb.getCaveXDimension(); i++) {
				if (i < x) {
					shootingPositions.add(new AgentPosition(i, y, AgentPosition.Orientation.FACING_EAST));
				}
				if (i > x) {
					shootingPositions.add(new AgentPosition(i, y, AgentPosition.Orientation.FACING_WEST));
				}
				if (i < y) {
					shootingPositions.add(new AgentPosition(x, i, AgentPosition.Orientation.FACING_NORTH));
				}
				if (i > y) {
					shootingPositions.add(new AgentPosition(x, i, AgentPosition.Orientation.FACING_SOUTH));
				}
			}
		}

		// Can't have a shooting position from any of the rooms the wumpus could
		// reside
		for (Room p : possibleWumpus) {
			for (AgentPosition.Orientation orientation : AgentPosition.Orientation.values()) {
				shootingPositions.remove(new AgentPosition(p.getX(), p.getY(), orientation));
			}
		}

		Iterator<AgentPosition> it = shootingPositions.iterator();
		Set<Room> shootingPositionsArray = new LinkedHashSet<>();
		while (it.hasNext()) {
			AgentPosition tmp = it.next();
			shootingPositionsArray.add(new Room(tmp.getX(), tmp.getY()));
		}

		List<WWAction> actions = planRoute(current, shootingPositionsArray, allowed);

		AgentPosition newPos = current;
		if (actions.size() > 0) {
			newPos = ((Forward) actions.get(actions.size() - 1)).getToPosition();
		}

		while (!shootingPositions.contains(newPos)) {
			TurnLeft tLeft = new TurnLeft(newPos.getOrientation());
			newPos = new AgentPosition(newPos.getX(), newPos.getY(), tLeft.getToOrientation());
			actions.add(tLeft);
		}

		actions.add(new Shoot());
		return actions;
	}

	//
	// SUPPORTING CODE
	//
	public HybridWumpusAgent(PLParser plparser) {
		// i.e. default is a 4x4 world as depicted in figure 7.2
		this(4, plparser);
	}

	public HybridWumpusAgent(int caveXandYDimensions, PLParser plparser) {
		kb = new WumpusKnowledgeBase<WWAction>(caveXandYDimensions, plparser);
	}

}