package aima.core.environment.wumpusworld;

import aima.core.util.datastructure.Point2D;

/**
 * 
 * @author Federico Baron
 * @author Alessandro Daniele
 */
public class WumpusPosition {

	public static final int ORIENTATION_LEFT = 0;
	public static final int ORIENTATION_RIGHT = 1;
	public static final int ORIENTATION_UP = 2;
	public static final int ORIENTATION_DOWN = 3;
	
	private Point2D location;
	private int orientation;
	
	public WumpusPosition(int x, int y, int orientation) {
		location = new Point2D(x, y);
		this.orientation = orientation;
	}

	public Point2D getLocation() {
		return location;
	}

	public int getOrientation() {
		return orientation;
	}

	@Override
	public String toString() {
		return String.valueOf((int)location.getX())+String.valueOf((int)location.getY())+String.valueOf(orientation);
	}

	@Override
	public boolean equals(Object obj) {
		WumpusPosition location2 = (WumpusPosition) obj;
		if (((int)location.getX() == (int)location2.getLocation().getX()) && ((int)location.getY() == (int)location2.getLocation().getY()) && (orientation == location2.getOrientation()) )
			return true;
		else
			return false;
	}
	
}
