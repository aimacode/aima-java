package aimax.osm.routing;

import aima.core.search.framework.GoalTest;

/**
 * Implementation of the GoalTest interface for testing
 * whether a state is equal to an explicitly given goal state.
 * @author R. Lunde
 */
public class SimpleGoalTest implements GoalTest {
	private Object goalState = null;

	public SimpleGoalTest(Object goalState) {
		this.goalState = goalState;
	}

	public boolean isGoalState(Object state) {
		return goalState.equals(state);
	}
}