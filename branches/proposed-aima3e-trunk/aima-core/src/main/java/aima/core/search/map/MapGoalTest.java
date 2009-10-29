package aima.core.search.map;

import aima.core.agent.impl.DynamicPercept;
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

	public boolean isGoalState(Object currentState) {

		String location = currentState.toString();

		// TODO-Why are we doing this?
		if (currentState instanceof DynamicPercept) {
			location = (String) ((DynamicPercept) currentState)
					.getAttribute(DynAttributeNames.PERCEPT_IN);
		}

		return goalState.equals(location);
	}
}
