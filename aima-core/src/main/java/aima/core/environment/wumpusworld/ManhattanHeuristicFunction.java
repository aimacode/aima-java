package aima.core.environment.wumpusworld;

import aima.core.search.framework.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.ToDoubleFunction;

/**
 * Heuristic for calculating the Manhattan distance between two rooms within a Wumpus World cave.
 * 
 * @author Federico Baron
 * @author Alessandro Daniele
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 */
public class ManhattanHeuristicFunction implements ToDoubleFunction<Node<AgentPosition, WumpusAction>> {
	
	private List<AgentPosition> goals = new ArrayList<>();
	
	public ManhattanHeuristicFunction(Set<AgentPosition> goals) {
		this.goals.addAll(goals);
	}
	
	@Override
	public double applyAsDouble(Node<AgentPosition, WumpusAction> node) {
		AgentPosition pos = node.getState();
		int nearestGoalDist = Integer.MAX_VALUE;
		for (AgentPosition g : goals) {
			int tmp = evaluateManhattanDistanceOf(pos.getX(), pos.getY(), g.getX(), g.getY());
			if (tmp < nearestGoalDist)
				nearestGoalDist = tmp;
		}
		return (double) nearestGoalDist;
	}

	//
	// PRIVATE
	//
	private int evaluateManhattanDistanceOf(int x1, int y1, int x2, int y2) {		
		return Math.abs(x1-x2) + Math.abs(y1-y2); 
	}
}
