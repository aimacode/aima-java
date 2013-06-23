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
		case WumpusPosition.ORIENTATION_DOWN:
			if (direction == DIRECTION_LEFT)
				toOrientation = WumpusPosition.ORIENTATION_RIGHT;
			else
				toOrientation = WumpusPosition.ORIENTATION_LEFT;
			break;
		case WumpusPosition.ORIENTATION_LEFT:
			if (direction == DIRECTION_LEFT)
				toOrientation = WumpusPosition.ORIENTATION_DOWN;
			else
				toOrientation = WumpusPosition.ORIENTATION_UP;
			break;
		case WumpusPosition.ORIENTATION_RIGHT:
			if (direction == DIRECTION_LEFT)
				toOrientation = WumpusPosition.ORIENTATION_UP;
			else
				toOrientation = WumpusPosition.ORIENTATION_DOWN;
			break;
		case WumpusPosition.ORIENTATION_UP:
			if (direction == DIRECTION_LEFT)
				toOrientation = WumpusPosition.ORIENTATION_LEFT;
			else
				toOrientation = WumpusPosition.ORIENTATION_RIGHT;
			break;
		default:
			break;
		}
		
	}

	public int getToOrientation() {
		return toOrientation;
	}
}
