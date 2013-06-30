package aima.core.environment.wumpusworld;

import aima.core.agent.impl.DynamicAction;

/**
 * @author Federico Baron
 * @author Alessandro Daniele
 * 
 */
public class TurnAction extends DynamicAction {
	
	public static final int DIRECTION_LEFT = 0;
	public static final int DIRECTION_RIGHT = 1;
	public static final String ATTRIBUTE_DIRECTION = "Direction";
	private int toOrientation;

	public TurnAction(int orientation, int direction) {
		super("Turn");
		
		if (direction == DIRECTION_LEFT)
			setAttribute(ATTRIBUTE_DIRECTION, "Left");
		else
			setAttribute(ATTRIBUTE_DIRECTION, "Right");
		
		switch(orientation) {
		case WumpusPosition.ORIENTATION_SOUTH:
			if (direction == DIRECTION_LEFT)
				toOrientation = WumpusPosition.ORIENTATION_EAST;
			else
				toOrientation = WumpusPosition.ORIENTATION_WEST;
			break;
		case WumpusPosition.ORIENTATION_WEST:
			if (direction == DIRECTION_LEFT)
				toOrientation = WumpusPosition.ORIENTATION_SOUTH;
			else
				toOrientation = WumpusPosition.ORIENTATION_NORTH;
			break;
		case WumpusPosition.ORIENTATION_EAST:
			if (direction == DIRECTION_LEFT)
				toOrientation = WumpusPosition.ORIENTATION_NORTH;
			else
				toOrientation = WumpusPosition.ORIENTATION_SOUTH;
			break;
		case WumpusPosition.ORIENTATION_NORTH:
			if (direction == DIRECTION_LEFT)
				toOrientation = WumpusPosition.ORIENTATION_WEST;
			else
				toOrientation = WumpusPosition.ORIENTATION_EAST;
			break;
		default:
			break;
		}
		
	}

	public int getToOrientation() {
		return toOrientation;
	}
}
