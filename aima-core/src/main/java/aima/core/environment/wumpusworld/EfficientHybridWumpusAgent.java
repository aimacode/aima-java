package aima.core.environment.wumpusworld;

import aima.core.agent.Notifier;
import aima.core.logic.propositional.inference.DPLL;
import aima.core.logic.propositional.inference.DPLLSatisfiable;
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
 * actions to take.<br><br>
 *
 * This is a tuned version of the {@link HybridWumpusAgent}. It uses a model cave
 * not only for routing but also for position and visited location tracking.
 * The knowledge base grows significant slower than in the original version
 * and response times are much faster.
 *
 * @author Ruediger Lunde
 */
public class EfficientHybridWumpusAgent extends HybridWumpusAgent {

    private WumpusCave modelCave;
    private Set<Room> visitedRooms = new HashSet<>();

    public EfficientHybridWumpusAgent(int caveXDim, int caveYDim, AgentPosition start) {
        this(caveXDim, caveYDim, start, new DPLLSatisfiable(), null);
    }

    public EfficientHybridWumpusAgent(int caveXDim, int caveYDim, AgentPosition start, DPLL satSolver,
                                      Notifier notifier) {
        this(caveXDim, caveYDim, start, new WumpusKnowledgeBase(caveXDim, caveYDim, start, satSolver), notifier);
    }

    public EfficientHybridWumpusAgent(int caveXDim, int caveYDim, AgentPosition start, WumpusKnowledgeBase kb,
                                      Notifier notifier) {
        super(caveXDim, caveYDim, start, kb, notifier);
        getKB().disableNavSentences(); // Optimization: Verbosity of produced sentences is reduced.
        modelCave = new WumpusCave(caveXDim, caveYDim);
        visitedRooms.add(currentPosition.getRoom());
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

        // TELL(KB, MAKE-PERCEPT-SENTENCE(percept, t))
        getKB().makePerceptSentence(percept, t);
        // TELL the KB the temporal "physics" sentences for time t
        // Optimization: The agent is aware of it's position - the KB can profit from that!
        getKB().tellTemporalPhysicsSentences(t, currentPosition);

        Set<Room> safe = null;
        Set<Room> unvisited = null;

        // Optimization: Do not ask anything during plan execution (different from pseudo-code)
        if (plan.isEmpty()) {
            notifyViews("Reasoning (t=" + t + ", Percept=" + percept + ", Pos=" + currentPosition + ") ...");
            // safe <- {[x, y] : ASK(KB, OK<sup>t</sup><sub>x,y</sub>) = true}
            safe = getKB().askSafeRooms(t, visitedRooms);
            notifyViews("Ask safe -> " + safe);
        }

        // if ASK(KB, Glitter<sup>t</sup>) = true then
        // Optimization: Use percept (condition can only be true if plan is empty).
        if (plan.isEmpty() && percept.isGlitter()) {
            // plan <- [Grab] + PLAN-ROUTE(current, {[1,1]}, safe) + [Climb]
            Set<Room> goals = new LinkedHashSet<>();
            goals.add(modelCave.getStart().getRoom());
            plan.add(WumpusAction.GRAB);
            plan.addAll(planRouteToRooms(goals, safe));
            plan.add(WumpusAction.CLIMB);
        }

        // if plan is empty then
        if (plan.isEmpty()) {
            // unvisited <- {[x, y] : ASK(KB, L<sup>t'</sup><sub>x,y</sub>) = false for all t' &le; t}
            // Optimization: Agent remembers visited locations, no need to ask.
            unvisited = SetOps.difference(modelCave.getAllRooms(), visitedRooms);
            // plan <- PLAN-ROUTE(current, unvisited &cap; safe, safe)
            plan.addAll(planRouteToRooms(unvisited, safe));
        }

        // if plan is empty and ASK(KB, HaveArrow<sup>t</sup>) = true then
        if (plan.isEmpty() && getKB().askHaveArrow(t)) {
            // possible_wumpus <- {[x, y] : ASK(KB, ~W<sub>x,y</sub>) = false}
            Set<Room> possibleWumpus = getKB().askPossibleWumpusRooms(t);
            notifyViews("Ask possible Wumpus positions -> " + possibleWumpus);
            // plan <- PLAN-SHOT(current, possible_wumpus, safe)
            plan.addAll(planShot(possibleWumpus, safe));
        }

        // if plan is empty then //no choice but to take a risk
        if (plan.isEmpty()) {
            // not_unsafe <- {[x, y] : ASK(KB, ~OK<sup>t</sup><sub>x,y</sub>) = false}
            // Optimization: Do not check visited rooms again.
            Set<Room> notUnsafe = getKB().askNotUnsafeRooms(t, visitedRooms);
            notifyViews("Ask not unsafe -> " + notUnsafe);
            // plan <- PLAN-ROUTE(current, unvisited &cap; not_unsafe, safe)
            // Correction: Last argument must be not_unsafe!
            plan.addAll(planRouteToRooms(unvisited, notUnsafe));
        }

        // if plan is empty then
        if (plan.isEmpty()) {
            notifyViews("Going home.");
            // plan PLAN-ROUTE(current, {[1,1]}, safe) + [Climb]
            Set<Room> goal = new LinkedHashSet<>();
            goal.add(modelCave.getStart().getRoom());
            plan.addAll(planRouteToRooms(goal, safe));
            plan.add(WumpusAction.CLIMB);
        }
        // action <- POP(plan)
        WumpusAction action = plan.remove();
        // TELL(KB, MAKE-ACTION-SENTENCE(action, t))
        getKB().makeActionSentence(action, t);
        // t <- t+1
        t = t + 1;
        updateAgentPosition(action);
        visitedRooms.add(currentPosition.getRoom());
        // return action
        return Optional.of(action);
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
        modelCave.setAllowed(allowed);
        Problem<AgentPosition, WumpusAction> problem = new GeneralProblem<>(currentPosition,
                WumpusFunctions.createActionsFunction(modelCave),
                WumpusFunctions.createResultFunction(modelCave), goals::contains);
        SearchForActions<AgentPosition, WumpusAction> search =
                new AStarSearch<>(new GraphSearch<>(), WumpusFunctions.createManhattanDistanceFunction(goals));
        Optional<List<WumpusAction>> actions = search.findActions(problem);

        return actions.orElse(Collections.emptyList());
    }

    /**
     * Uses the model cave to update the current agent position.
     */
    private void updateAgentPosition(WumpusAction action) {
        modelCave.setAllowed(modelCave.getAllRooms());
        switch (action) {
            case FORWARD:
                currentPosition = modelCave.moveForward(currentPosition);
                break;
            case TURN_LEFT:
                currentPosition = modelCave.turnLeft(currentPosition);
                break;
            case TURN_RIGHT:
                currentPosition = modelCave.turnRight(currentPosition);
                break;
        }
    }
}