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
    private boolean wumpusAlive = true;
    private boolean scream;
    /** We assume that only one agent is added to the environment! */
    private boolean hasArrow;
    private boolean bump;
    private Map<Agent, AgentPosition> agentPositions = new HashMap<>();

    public WumpusEnvironment(WumpusCave cave) {
        this.cave = cave;
    }

    @Override
    public void addAgent(Agent agent) {
        super.addAgent(agent);
        agentPositions.put(agent, cave.getStart());
    }

    @Override
    public void executeAction(Agent agent, Action action) {
        AgentPosition pos = agentPositions.get(agent);
        if (action == WumpusAction.FORWARD) {
            AgentPosition newPos = cave.moveForward(pos);
            agentPositions.put(agent, newPos);
            if (newPos.equals(pos))
                bump = true;
            else if (cave.isPit(pos.getRoom()) || pos.getRoom().equals(cave.getWumpus()) && wumpusAlive)
                agent.setAlive(false);
        } else if (action == WumpusAction.TURN_LEFT) {
            agentPositions.put(agent, cave.turnLeft(pos));
        } else if (action == WumpusAction.TURN_RIGHT) {
            agentPositions.put(agent, cave.turnRight(pos));
        } else if (action == WumpusAction.GRAB) {
            if (pos.getRoom().equals(cave.getGold()))
                cave.setGold(null);
        } else if (action == WumpusAction.SHOOT) {
            if (isAgentFacingWumpus(pos)) {
                wumpusAlive = false;
                scream = true;
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
        if (bump) {
            result.setBump();
            bump = false;
        }
        if (scream) {
            result.setScream();
            scream = false;
        }
        return result;
    }
}
