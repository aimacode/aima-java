package aima.search.map;

import aima.basic.Percept;
import aima.search.framework.GoalTest;

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

	public boolean isGoalState(Object currentState) {

		String location = currentState.toString();
		if (currentState instanceof Percept) {
			location = (String) ((Percept) currentState)
					.getAttribute(MapEnvironment.STATE_IN);
		}

		return goalState.equals(location);
	}
}
