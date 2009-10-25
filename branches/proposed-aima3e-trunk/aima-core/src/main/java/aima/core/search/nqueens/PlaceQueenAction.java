package aima.core.search.nqueens;

import aima.core.agent.impl.DynamicAction;

public class PlaceQueenAction extends DynamicAction {
	public static final String ATTRIBUTE_QUEEN_X = "x";
	public static final String ATTRIBUTE_QUEEN_Y = "y";
	
	private int x = 0;
    private int y = 0;
	
	public PlaceQueenAction(int x, int y) {
		super("placeQueenAt");
		this.x = x;
		this.y = y;
		setAttribute(ATTRIBUTE_QUEEN_X, x);
		setAttribute(ATTRIBUTE_QUEEN_Y, y);
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
}
