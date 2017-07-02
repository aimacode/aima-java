package aima.core.environment.wumpusworld;

import aima.core.agent.Action;
import aima.core.agent.Agent;
import aima.core.agent.Percept;
import aima.core.agent.impl.AbstractEnvironment;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implements an environment for the Wumpus World.
 * @author Ruediger Lunde
 */
public class WumpusEnvironment extends AbstractEnvironment {

    private WumpusCave cave;
    private boolean isWumpusAlive = true;
    private boolean isGoldGrabbed;
    private boolean screamNext;
    private boolean scream;
    // We assume that only one agent is added to the environment!
    private boolean bumpNext;
    private boolean bump;
    private boolean hasArrow =true;
    private Map<Agent, AgentPosition> agentPositions = new HashMap<>();

    public WumpusEnvironment(WumpusCave cave) {
        this.cave = cave;
    }

    public WumpusCave getCave() {
        return cave;
    }

    public boolean isWumpusAlive() {
        return isWumpusAlive;
    }

    public boolean isGoalGrabbed() {
        return isGoldGrabbed;
    }

    public AgentPosition getAgentPosition(Agent agent) {
        return agentPositions.get(agent);
    }

    @Override
    public void addAgent(Agent agent) {
        agentPositions.put(agent, cave.getStart());
        super.addAgent(agent);
    }

    @Override
    public void step() {
        super.step();
        bump = bumpNext;
        scream = screamNext;
        screamNext = false;
        bumpNext = false;
    }

    @Override
    public void executeAction(Agent agent, Action action) {
        AgentPosition pos = agentPositions.get(agent);
        if (action == WumpusAction.FORWARD) {
            AgentPosition newPos = cave.moveForward(pos);
            agentPositions.put(agent, newPos);
            if (newPos.equals(pos))
                bumpNext = true;
            else if (cave.isPit(newPos.getRoom()) || newPos.getRoom().equals(cave.getWumpus()) && isWumpusAlive)
                agent.setAlive(false);
        } else if (action == WumpusAction.TURN_LEFT) {
            agentPositions.put(agent, cave.turnLeft(pos));
        } else if (action == WumpusAction.TURN_RIGHT) {
            agentPositions.put(agent, cave.turnRight(pos));
        } else if (action == WumpusAction.GRAB) {
            if (!isGoldGrabbed && pos.getRoom().equals(cave.getGold()))
                isGoldGrabbed = true;
        } else if (action == WumpusAction.SHOOT) {
            if (hasArrow && isAgentFacingWumpus(pos)) {
                isWumpusAlive = false;
                screamNext = true;
                hasArrow = false;
            }
        } else if (action == WumpusAction.CLIMB) {
            agent.setAlive(false);
        }
    }

    private boolean isAgentFacingWumpus(AgentPosition pos) {
        Room wumpus = cave.getWumpus();
        switch (pos.getOrientation()) {
            case FACING_NORTH:
                return pos.getX() == wumpus.getX() && pos.getY() < wumpus.getY();
            case FACING_SOUTH:
                return pos.getX() == wumpus.getX() && pos.getY() > wumpus.getY();
            case FACING_EAST:
                return pos.getY() == wumpus.getY() && pos.getX() < wumpus.getX();
            case FACING_WEST:
                return pos.getY() == wumpus.getY() && pos.getX() > wumpus.getX();
        }
        return false;
    }

    @Override
    public Percept getPerceptSeenBy(Agent anAgent) {
        WumpusPercept result = new WumpusPercept();
        AgentPosition pos = agentPositions.get(anAgent);
        List<Room> adjacentRooms = Arrays.asList(
                new Room(pos.getX()-1, pos.getY()), new Room(pos.getX()+1, pos.getY()),
                new Room(pos.getX(), pos.getY()-1), new Room(pos.getX(), pos.getY()+1)
        );
        for (Room r : adjacentRooms) {
            if (r.equals(cave.getWumpus()))
                result.setStench();
            if (cave.isPit(r))
                result.setBreeze();
        }
        if (pos.getRoom().equals(cave.getGold()))
            result.setGlitter();
        if (bump)
            result.setBump();
        if (scream)
            result.setScream();
        return result;
    }
}
