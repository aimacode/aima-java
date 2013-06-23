package aima.core.environment.wumpusworld;

import java.util.ArrayList;

import aima.core.search.framework.HeuristicFunction;
import aima.core.util.datastructure.Point2D;

/**
 * @author Federico Baron
 * @author Alessandro Daniele
 * 
 */
public class ManhattanHeuristicFunction implements HeuristicFunction {
	
	ArrayList<Point2D> goals;
	
	public ManhattanHeuristicFunction(ArrayList<Point2D> goals) {
		this.goals = goals;
	}
	
	@Override
	public double h(Object state) {
		WumpusPosition pos = (WumpusPosition) state;
		int nearestGoalDist = Integer.MAX_VALUE;
		for (Point2D g : goals) {
			int tmp = evaluateManhattanDistanceOf(pos.getLocation(), g);
			
			if (tmp < nearestGoalDist)
				nearestGoalDist = tmp;
		}
		
		return nearestGoalDist;
	}

	public int evaluateManhattanDistanceOf(Point2D p1, Point2D p2) {
		int x1 = (int)p1.getX(); 
		int y1 = (int)p1.getY();
		int x2 = (int)p2.getX();
		int y2 = (int)p2.getY();
		
		return Math.abs(x1-x2) + Math.abs(y1-y2); 
	}
}
