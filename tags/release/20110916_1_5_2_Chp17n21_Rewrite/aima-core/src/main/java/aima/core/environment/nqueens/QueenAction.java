package aima.core.environment.nqueens;

import aima.core.agent.impl.DynamicAction;
import aima.core.util.datastructure.XYLocation;

/**
 * Queens can be placed, removed, and moved. For movements, a vertical direction
 * is assumed. Therefore, only the end point needs to be specified.
 * 
 * @author Ravi Mohan
 * @author R. Lunde
 */
public class QueenAction extends DynamicAction {
	public static final String PLACE_QUEEN = "placeQueenAt";
	public static final String REMOVE_QUEEN = "removeQueenAt";
	public static final String MOVE_QUEEN = "moveQueenTo";

	public static final String ATTRIBUTE_QUEEN_LOC = "location";

	/**
	 * Creates a queen action. Supported values of type are {@link #PLACE_QUEEN}
	 * , {@link #REMOVE_QUEEN}, or {@link #MOVE_QUEEN}.
	 */
	public QueenAction(String type, XYLocation loc) {
		super(type);
		setAttribute(ATTRIBUTE_QUEEN_LOC, loc);
	}

	public XYLocation getLocation() {
		return (XYLocation) getAttribute(ATTRIBUTE_QUEEN_LOC);
	}

	public int getX() {
		return getLocation().getXCoOrdinate();
	}

	public int getY() {
		return getLocation().getYCoOrdinate();
	}
}
