package aima.core.search.map;

import aima.core.search.framework.GoalTest;

/**
 * Implementation of the GoalTest interface for testing if at a desired location.
 */

/**
 * @author Ciaran O'Reilly
 * 
 */
public class MapGoalTest implements GoalTest {
	private String goalState = null;

	public MapGoalTest(String goalState) {
		this.goalState = goalState;
	}

	public boolean isGoalState(Object state) {
		return goalState.equals(state);
	}
}
