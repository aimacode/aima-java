package aima.core.environment.wumpusworld;

import java.util.ArrayList;

import aima.core.agent.impl.DynamicAction;
import aima.core.util.datastructure.Point2D;

/**
 * @author Federico Baron
 * @author Alessandro Daniele
 * 
 */
public class ShotAction extends DynamicAction {
	
	private WumpusPosition current;

	public ShotAction(WumpusPosition current) {
		super("Shot");
		
		this.current = current;
	}

	/**
	 * Return, in order, all the squares that will be affected by the shot.
	 */
	public ArrayList<Point2D> getArrowRoute(int dimRow) {
		int x = (int)current.getLocation().getX();
		int y = (int)current.getLocation().getY();
		ArrayList<Point2D> result = new ArrayList<Point2D>();
		
		switch (current.getOrientation()) {
		case WumpusPosition.ORIENTATION_DOWN:
			for (int i=y-1; i>=1; i--)
				result.add(new Point2D(x,i));
			break;
		case WumpusPosition.ORIENTATION_UP:
			for (int i=y+1; i<=dimRow; i++)
				result.add(new Point2D(x,i));
			break;
		case WumpusPosition.ORIENTATION_RIGHT:
			for (int i=x+1; i<=dimRow; i++)
				result.add(new Point2D(i,y));
			break;
		case WumpusPosition.ORIENTATION_LEFT:
			for (int i=x-1; i>=1; i--)
				result.add(new Point2D(i,y));
			break;
		}
		
		return result;
	}
	
}
