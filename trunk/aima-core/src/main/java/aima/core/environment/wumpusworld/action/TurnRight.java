package aima.core.environment.wumpusworld.action;

import aima.core.agent.impl.DynamicAction;
import aima.core.environment.wumpusworld.AgentPosition;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 237.<br>
 * <br>
 * The agent can TurnRight by 90 degrees.
 * 
 * @author Federico Baron
 * @author Alessandro Daniele
 * @author Ciaran O'Reilly
 */
public class TurnRight extends DynamicAction {
	public static final String TURN_RIGHT_ACTION_NAME = "TurnRight";
	public static final String ATTRIBUTE_TO_ORIENTATION = "toOrientation";
	//
	private AgentPosition.Orientation toOrientation;

	/**
	 * Constructor.
	 * 
	 * @param currentOrientation
	 */
	public TurnRight(AgentPosition.Orientation currentOrientation) {
		super(TURN_RIGHT_ACTION_NAME);
		
		switch(currentOrientation) {
		case FACING_UP:
			toOrientation = AgentPosition.Orientation.FACING_RIGHT;
			break;
		case FACING_DOWN:
			toOrientation = AgentPosition.Orientation.FACING_LEFT;
			break;
		case FACING_RIGHT:
			toOrientation = AgentPosition.Orientation.FACING_DOWN;
			break;
		case FACING_LEFT:
			toOrientation = AgentPosition.Orientation.FACING_UP;
			break;
		}
		setAttribute(ATTRIBUTE_TO_ORIENTATION, toOrientation);
	}
	
	/**
	 * 
	 * @return the orientation the agent should be after the action occurred.
	 *         <b>Note:<b> this may not be a legal orientation within the
	 *         environment in which the action was performed and this should be
	 *         checked for.
	 */
	public AgentPosition.Orientation getToOrientation() {
		return toOrientation;
	}
}
