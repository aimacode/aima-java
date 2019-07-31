package aima.core.environment.wumpusworld;

import aima.core.agent.Notifier;
import aima.core.agent.impl.SimpleAgent;
import aima.core.logic.propositional.inference.DPLL;
import aima.core.logic.propositional.inference.DPLLSatisfiable;
import aima.core.search.framework.Metrics;
import aima.core.search.framework.SearchForActions;
import aima.core.search.framework.problem.GeneralProblem;
import aima.core.search.framework.problem.Problem;
import aima.core.search.framework.qsearch.GraphSearch;
import aima.core.search.informed.AStarSearch;
import aima.core.util.SetOps;

import java.util.*;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 270.<br>
 * <br>
 * 
 * <pre>
 * <code>
 * function HYBRID-WUMPUS-AGENT(percept) returns an action
 *   inputs: percept, a list, [stench, breeze, glitter, bump, scream]
 *   persistent: KB, a knowledge base, initially the temporal "wumpus physics"
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
 * @author Ruediger Lunde
 */
public class HybridWumpusAgent extends SimpleAgent<WumpusPercept, WumpusAction> {

	/// persistent: KB, a knowledge base, initially the atemporal "wumpus physics"
	private WumpusKnowledgeBase kb = null;
	protected AgentPosition start;
	/** The agent's current position. */
	protected AgentPosition currentPosition;
	/// t, a counter, initially 0, indicating time
	protected int t = 0;
	/// plan, an action sequence, initially empty
	protected Queue<WumpusAction> plan = new LinkedList<>(); // FIFOQueue
	private Notifier notifier;

	public HybridWumpusAgent() {
		// i.e. default is a 4x4 world as depicted in figure 7.2
		this(4, 4, new AgentPosition(1, 1, AgentPosition.Orientation.FACING_NORTH));
	}

	public HybridWumpusAgent(int caveXDim, int caveYDim, AgentPosition start) {
		this(caveXDim, caveYDim, start, new DPLLSatisfiable(), null);
	}

	public HybridWumpusAgent(int caveXDim, int caveYDim, AgentPosition start, DPLL satSolver,
							 Notifier notifier) {
		this(caveXDim, caveYDim, start, new WumpusKnowledgeBase(caveXDim, caveYDim, start, satSolver), notifier);
	}

	public HybridWumpusAgent(int caveXDim, int caveYDim, AgentPosition start, WumpusKnowledgeBase kb,
							 Notifier notifier) {
		this.kb = kb;
		this.start = start;
		this.currentPosition = start;
		this.notifier = notifier;
	}

	public WumpusKnowledgeBase getKB() {
		return kb;
	}

	/**
	 * function HYBRID-WUMPUS-AGENT(percept) returns an action<br>
	 * 
	 * @param percept
	 *            a list, [stench, breeze, glitter, bump, scream]
	 * 
	 * @return an action the agent should take.
	 */
	@Override
	public Optional<WumpusAction> act(WumpusPercept percept) {

		/// TELL(KB, MAKE-PERCEPT-SENTENCE(percept, t))
		kb.makePerceptSentence(percept, t);
		/// TELL the KB the temporal "physics" sentences for time t
		kb.tellTemporalPhysicsSentences(t);

		Set<Room> safe = null;
		Set<Room> unvisited = null;

		// Optimization: Do not ask anything during plan execution (different from pseudo-code)
		if (plan.isEmpty()) {
			notifyViews("Reasoning (t=" + t + ", Percept=" + percept + ") ...");
			currentPosition = kb.askCurrentPosition(t);
			notifyViews("Ask position -> " + currentPosition);
			/// safe <- {[x, y] : ASK(KB, OK<sup>t</sup><sub>x,y</sub>) = true}
			safe = kb.askSafeRooms(t);
			notifyViews("Ask safe -> " + safe);
		}

		/// if ASK(KB, Glitter<sup>t</sup>) = true then (can only be true when plan is empty!)
		if (plan.isEmpty() && kb.askGlitter(t)) {
			/// plan <- [Grab] + PLAN-ROUTE(current, {[1,1]}, safe) + [Climb]
			Set<Room> goals = new LinkedHashSet<>();
			goals.add(start.getRoom());
			plan.add(WumpusAction.GRAB);
			plan.addAll(planRouteToRooms(goals, safe));
			plan.add(WumpusAction.CLIMB);
		}

		/// if plan is empty then
		if (plan.isEmpty()) {
			/// unvisited <- {[x, y] : ASK(KB, L<sup>t'</sup><sub>x,y</sub>) = false for all t' &le; t}
			unvisited = kb.askUnvisitedRooms(t);
			notifyViews("Ask unvisited -> " + unvisited);
			/// plan <- PLAN-ROUTE(current, unvisited &cap; safe, safe)
			plan.addAll(planRouteToRooms(SetOps.intersection(unvisited, safe), safe));
		}

		/// if plan is empty and ASK(KB, HaveArrow<sup>t</sup>) = true then
		if (plan.isEmpty() && kb.askHaveArrow(t)) {
			/// possible_wumpus <- {[x, y] : ASK(KB, ~W<sub>x,y</sub>) = false}
			Set<Room> possibleWumpus = kb.askPossibleWumpusRooms(t);
			notifyViews("Ask possible Wumpus positions -> " + possibleWumpus);
			/// plan <- PLAN-SHOT(current, possible_wumpus, safe)
			plan.addAll(planShot(possibleWumpus, safe));
		}

		/// if plan is empty then (no choice but to take a risk)
		if (plan.isEmpty()) {
			/// not_unsafe <- {[x, y] : ASK(KB, ~OK<sup>t</sup><sub>x,y</sub>) = false}
			Set<Room> notUnsafe = kb.askNotUnsafeRooms(t);
			notifyViews("Ask not unsafe -> " + notUnsafe);
			/// plan <- PLAN-ROUTE(current, unvisited &cap; not_unsafe, safe)
			// Correction: Last argument must be not_unsafe!
			plan.addAll(planRouteToRooms(unvisited, notUnsafe));
		}

		/// if plan is empty then
		if (plan.isEmpty()) {
			notifyViews("Going home.");
			/// plan PLAN-ROUTE(current, {[1,1]}, safe) + [Climb]
			Set<Room> goal = new LinkedHashSet<>();
			goal.add(start.getRoom());
			plan.addAll(planRouteToRooms(goal, safe));
			plan.add(WumpusAction.CLIMB);
		}
		/// action <- POP(plan)
		WumpusAction action = plan.remove();
		/// TELL(KB, MAKE-ACTION-SENTENCE(action, t))
		kb.makeActionSentence(action, t);
		/// t <- t+1
		t = t + 1;
		/// return action
		return Optional.of(action);
	}

	/**
	 * Returns a sequence of actions using A* Search.
	 *
	 * @param goals
	 *            a set of squares; try to plan a route to one of them
	 * @param allowed
	 *            a set of squares that can form part of the route
	 * 
	 * @return the best sequence of actions that the agent have to do to reach a
	 *         goal from the current position.
	 */
	public List<WumpusAction> planRouteToRooms(Set<Room> goals, Set<Room> allowed) {
		final Set<AgentPosition> goalPositions = new LinkedHashSet<>();
		for (Room goalRoom : goals) {
			int x = goalRoom.getX();
			int y = goalRoom.getY();
			for (AgentPosition.Orientation orientation : AgentPosition.Orientation.values())
				goalPositions.add(new AgentPosition(x, y, orientation));
		}
		return planRoute(goalPositions, allowed);
	}

	/**
	 * Returns a sequence of actions using A* Search.
	 *
	 * @param goals
	 *            a set of agent positions; try to plan a route to one of them
	 * @param allowed
	 *            a set of squares that can form part of the route
	 *
	 * @return the best sequence of actions that the agent have to do to reach a
	 *         goal from the current position.
	 */
	public List<WumpusAction> planRoute(Set<AgentPosition> goals, Set<Room> allowed) {

		WumpusCave cave = new WumpusCave(kb.getCaveXDimension(), kb.getCaveYDimension()).setAllowed(allowed);
		Problem<AgentPosition, WumpusAction> problem = new GeneralProblem<>(currentPosition,
				WumpusFunctions.createActionsFunction(cave),
				WumpusFunctions.createResultFunction(cave), goals::contains);
		SearchForActions<AgentPosition, WumpusAction> search =
				new AStarSearch<>(new GraphSearch<>(), WumpusFunctions.createManhattanDistanceFunction(goals));
		Optional<List<WumpusAction>> actions = search.findActions(problem);

		return actions.orElse(Collections.emptyList());
	}

	/**
	 * @param possibleWumpus
	 *            a set of squares where we don't know that there isn't the
	 *            wumpus.
	 * @param allowed
	 *            a set of squares that can form part of the route
	 * 
	 * @return the sequence of actions to reach the nearest square that is in
	 *         line with a possible wumpus position. The last action is a shot.
	 */
	public List<WumpusAction> planShot(Set<Room> possibleWumpus, Set<Room> allowed) {

		Set<AgentPosition> shootingPositions = new LinkedHashSet<>();

		for (Room room : possibleWumpus) {
			int x = room.getX();
			int y = room.getY();

			for (int i = 1; i <= kb.getCaveXDimension(); i++) {
				if (i < x)
					shootingPositions.add(new AgentPosition(i, y, AgentPosition.Orientation.FACING_EAST));
				if (i > x)
					shootingPositions.add(new AgentPosition(i, y, AgentPosition.Orientation.FACING_WEST));
			}
			for (int i = 1; i <= kb.getCaveYDimension(); i++) {
				if (i < y)
					shootingPositions.add(new AgentPosition(x, i, AgentPosition.Orientation.FACING_NORTH));
				if (i > y)
					shootingPositions.add(new AgentPosition(x, i, AgentPosition.Orientation.FACING_SOUTH));
			}
		}

		// Can't have a shooting position from any of the rooms the wumpus could reside
		for (Room room : possibleWumpus)
			for (AgentPosition.Orientation orientation : AgentPosition.Orientation.values())
				shootingPositions.remove(new AgentPosition(room.getX(), room.getY(), orientation));

		List<WumpusAction> actions = new ArrayList<>(planRoute(shootingPositions, allowed));
		actions.add(WumpusAction.SHOOT);
		return actions;
	}

	public Metrics getMetrics() {
		return kb.getMetrics();
	}

	protected void notifyViews(String msg) {
		if (notifier != null)
			notifier.notify(msg);
	}
}