package aima.core.search.nqueens;

import aima.core.agent.impl.DynamicAction;

public class PlaceQueenAction extends DynamicAction {
	public static final String ATTRIBUTE_QUEEN_X = "x";
	public static final String ATTRIBUTE_QUEEN_Y = "y";

	public PlaceQueenAction(int x, int y) {
		super("placeQueenAt");
		setAttribute(ATTRIBUTE_QUEEN_X, x);
		setAttribute(ATTRIBUTE_QUEEN_Y, y);
	}

	public int getX() {
		return (Integer) getAttribute(ATTRIBUTE_QUEEN_X);
	}

	public int getY() {
		return (Integer) getAttribute(ATTRIBUTE_QUEEN_Y);
	}
}
