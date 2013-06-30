package aima.core.environment.wumpusworld;

import aima.core.agent.impl.DynamicAction;

/**
 * @author Federico Baron
 * @author Alessandro Daniele
 * 
 */
public class ForwardAction extends DynamicAction {
	
	public static final String ATTRIBUTE_LOCATION = "toPosition";

	private WumpusPosition toPosition = null;
	
	public ForwardAction(WumpusPosition pos) {
		super("Forward");
		
		int orientation = pos.getOrientation();
		switch(orientation) {
		case WumpusPosition.ORIENTATION_SOUTH:
			toPosition = new WumpusPosition((int)pos.getLocation().getX(), (int)pos.getLocation().getY()-1, orientation);
			setAttribute(ATTRIBUTE_LOCATION, toPosition);
			break;
		case WumpusPosition.ORIENTATION_WEST:
			toPosition = new WumpusPosition((int)pos.getLocation().getX()-1, (int)pos.getLocation().getY(), orientation);
			setAttribute(ATTRIBUTE_LOCATION, toPosition);
			break;
		case WumpusPosition.ORIENTATION_EAST:
			toPosition = new WumpusPosition((int)pos.getLocation().getX()+1, (int)pos.getLocation().getY(), orientation);
			setAttribute(ATTRIBUTE_LOCATION, toPosition);
			break;
		case WumpusPosition.ORIENTATION_NORTH:
			toPosition = new WumpusPosition((int)pos.getLocation().getX(), (int)pos.getLocation().getY()+1, orientation);
			setAttribute(ATTRIBUTE_LOCATION, toPosition);
			break;
		default:
			break;
		}
		
	}

	public WumpusPosition getToPosition() {
		return toPosition;
	}
}
