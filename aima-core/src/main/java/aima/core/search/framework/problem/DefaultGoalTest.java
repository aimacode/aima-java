package aima.core.search.framework.problem;

/**
 * Checks whether a given state equals an explicitly specified goal state.
 * 
 * @author Ruediger Lunde
 */
public class DefaultGoalTest implements GoalTest {
	private Object goalState;

	public DefaultGoalTest(Object goalState) {
		this.goalState = goalState;
	}

	public boolean isGoalState(Object state) {
		return goalState.equals(state);
	}
}
