package aima.core.environment.wumpusworld;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import aima.core.search.framework.HeuristicFunction;

/**
 * Heuristic for calculating the Manhattan distance between two rooms within a Wumpus World cave.
 * 
 * @author Federico Baron
 * @author Alessandro Daniele
 * @author Ciaran O'Reilly
 */
public class ManhattanHeuristicFunction implements HeuristicFunction {
	
	List<Room> goals = new ArrayList<Room>();
	
	public ManhattanHeuristicFunction(Set<Room> goals) {
		this.goals.addAll(goals);
	}
	
	@Override
	public double h(Object state) {
		AgentPosition pos = (AgentPosition) state;
		int nearestGoalDist = Integer.MAX_VALUE;
		for (Room g : goals) {
			int tmp = evaluateManhattanDistanceOf(pos.getX(), pos.getY(), g.getX(), g.getY());
			
			if (tmp < nearestGoalDist) {
				nearestGoalDist = tmp;
			}
		}
		
		return nearestGoalDist;
	}

	//
	// PRIVATE
	//
	private int evaluateManhattanDistanceOf(int x1, int y1, int x2, int y2) {		
		return Math.abs(x1-x2) + Math.abs(y1-y2); 
	}
}
