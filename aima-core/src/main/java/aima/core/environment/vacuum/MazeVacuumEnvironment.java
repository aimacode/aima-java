package aima.core.environment.vacuum;

import aima.core.agent.Action;
import aima.core.agent.Agent;
import aima.core.agent.impl.DynamicAction;
import aima.core.util.Util;

import java.util.*;

/**
 * A checkerboard-like world with squares to be cleaned. Movements in all 4 directions
 * are possible if not at the border and the next square does not contain obstructions.
 * Percepts tell about possible movements and the current square state.
 *
 * This environment can be used as a base for vacuum agent design competitions.
 *
 * @author Ruediger Lunde
 */
public class MazeVacuumEnvironment extends VacuumEnvironment {

	public static final DynamicAction ACTION_MOVE_UP = new DynamicAction("Up");
	public static final DynamicAction ACTION_MOVE_DOWN = new DynamicAction("Down");
	public static final String ATT_CAN_MOVE_LEFT = "canMoveLeft";
	public static final String ATT_CAN_MOVE_RIGHT = "canMoveRight";
	public static final String ATT_CAN_MOVE_DOWN = "canMoveDown";
	public static final String ATT_CAN_MOVE_UP = "canMoveUp";

	private final int xDimension;
	private final int yDimension;

	public MazeVacuumEnvironment(int xDim, int yDim) {
		super(createLocations(xDim * yDim));
		xDimension = xDim;
		yDimension = yDim;
		for (String loc : getLocations()) {
			LocationState state = Util.randomBoolean() ? LocationState.Dirty : LocationState.Clean;
			envState.setLocationState(loc, state);
		}
	}

	// Obstacles are marked by with locationState==null
	public MazeVacuumEnvironment(int xDim, int yDim, double obstacleProbability) {
		this(xDim, yDim);
		for (String loc : getLocations())
			if (Util.randomInt(100) < obstacleProbability * 100)
				envState.setLocationState(loc,null);
	}

	public void setObstacle(String location, boolean state) {
		envState.setLocationState(location, state ? null : LocationState.Clean);
	}

	public boolean containsObstacle(String location) {
		return envState.getLocationState(location) == null;
	}

	@Override
	public int getXDimension() {
		return xDimension;
	}

	@Override
	public int getYDimension() {
		return yDimension;
	}

	@Override
	public VacuumPercept getPerceptSeenBy(Agent agent) {
		VacuumPercept result = super.getPerceptSeenBy(agent);
		String loc = getAgentLocation(agent);
		result.setAttribute(ATT_CAN_MOVE_LEFT, canMoveLeft(loc) ? "True" : "False");
		result.setAttribute(ATT_CAN_MOVE_RIGHT, canMoveRight(loc) ? "True" : "False");
		result.setAttribute(ATT_CAN_MOVE_DOWN, canMoveDown(loc) ? "True" : "False");
		result.setAttribute(ATT_CAN_MOVE_UP, canMoveUp(loc) ? "True" : "False");
		return result;
	}

	@Override
	public void execute(Agent agent, Action action) {
		String loc = getAgentLocation(agent);
		if (action == ACTION_MOVE_RIGHT) {
			if (canMoveRight(loc))
				envState.setAgentLocation(agent, getLocation(getX(loc) + 1, getY(loc)));
			updatePerformanceMeasure(agent, -1);
		} else if (action == ACTION_MOVE_LEFT) {
			if (canMoveLeft(loc))
				envState.setAgentLocation(agent, getLocation(getX(loc) - 1, getY(loc)));
			updatePerformanceMeasure(agent, -1);
		} else if (action == ACTION_MOVE_UP) {
			if (canMoveUp(loc))
				envState.setAgentLocation(agent, getLocation(getX(loc), getY(loc) + 1));
			updatePerformanceMeasure(agent, -1);
		} else if (action == ACTION_MOVE_DOWN) {
			if (canMoveDown(loc))
				envState.setAgentLocation(agent, getLocation(getX(loc), getY(loc) - 1));
			updatePerformanceMeasure(agent, -1);
		} else if (action == ACTION_SUCK) {
			if (LocationState.Dirty == envState.getLocationState(envState.getAgentLocation(agent))) {
				envState.setLocationState(envState.getAgentLocation(agent), LocationState.Clean);
				updatePerformanceMeasure(agent, 10);
			}
		}
	}

	private boolean canMoveLeft(String loc) {
		int x = getX(loc);
		int y = getY(loc);
		return x > 1 && !containsObstacle(getLocation(x - 1, y));
	}

	private boolean canMoveRight(String loc) {
		int x = getX(loc);
		int y = getY(loc);
		return x < getXDimension() && !containsObstacle(getLocation(x + 1, y));
	}

	private boolean canMoveDown(String loc) {
		int x = getX(loc);
		int y = getY(loc);
		return y > 1 && !containsObstacle(getLocation(x, y - 1));
	}

	private boolean canMoveUp(String loc) {
		int x = getX(loc);
		int y = getY(loc);
		return y < getYDimension() && !containsObstacle(getLocation(x, y + 1));
	}

	private static List<String> createLocations(int size) {
		List<String> result = new ArrayList<>();
		for (int i = 0; i < size; i++) {
			String loc;
			if (size < 27)
				loc = Character.toString((char) ('A' + i));
			else
				loc = Integer.toString(i + 1);
			result.add(loc);
		}
		return result;
	}
}
