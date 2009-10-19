package aima.core.search.nqueens;

import aima.core.agent.impl.DynamicAction;

public class PlaceQueenAction extends DynamicAction {
	public static final String ATTRIBUTE_QUEEN_ROW = "row";
	public static final String ATTRIBUTE_QUEEN_COL = "col";
	
	private int row = 0;
	private int col = 0;
	
	public PlaceQueenAction(int row, int col) {
		super("placeQueenAt");
		this.row = row;
		this.col = col;
		setAttribute(ATTRIBUTE_QUEEN_ROW, row);
		setAttribute(ATTRIBUTE_QUEEN_COL, col);
	}
	
	public int getRow() {
		return row;
	}
	
	public int getCol() {
		return col;
	}
}
