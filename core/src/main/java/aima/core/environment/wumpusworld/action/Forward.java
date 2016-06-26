package aima.core.environment.wumpusworld.action;

import aima.core.environment.wumpusworld.AgentPosition;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): page ???.<br>
 * <br>
 * The agent can move Forward.
 * 
 * @author Federico Baron
 * @author Alessandro Daniele
 * @author Ciaran O'Reilly
 * @author Anurag Rai
 */
public class Forward extends WWAction {

	public static final String FORWARD_ACTION_NAME = "Forward";
	public static final String ATTRIBUTE_TO_POSITION = "toPosition";

	private AgentPosition toPosition = null;

	/**
	 * Constructor.
	 * 
	 * @param currentPosition
	 * 
	 */
	public Forward(AgentPosition currentPosition) {
		super(FORWARD_ACTION_NAME);

		int x = currentPosition.getX();
		int y = currentPosition.getY();

		AgentPosition.Orientation orientation = currentPosition.getOrientation();
		switch (orientation) {
		case FACING_NORTH:
			toPosition = new AgentPosition(x, y + 1, orientation);
			break;
		case FACING_SOUTH:
			toPosition = new AgentPosition(x, y - 1, orientation);
			break;
		case FACING_EAST:
			toPosition = new AgentPosition(x + 1, y, orientation);
			break;
		case FACING_WEST:
			toPosition = new AgentPosition(x - 1, y, orientation);
			break;
		}
		setAttribute(ATTRIBUTE_TO_POSITION, toPosition);
	}

	/**
	 * 
	 * @return the position after the agent move's forward. <b>Note:</b> this
	 *         may not be a legal position within the environment in which the
	 *         action was performed and this should be checked for. For example,
	 *         if an agent tries to move forward and bumps into a wall, then the
	 *         agent does not move.
	 */
	public AgentPosition getToPosition() {
		return toPosition;
	}
}
