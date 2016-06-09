package aima.core.search.framework.problem;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 67.<br>
 * <br>
 * The goal test, which determines whether a given state is a goal state.
 * 
 * @author Ravi Mohan
 * @author Mike Stampone
 */
public interface GoalTest {
	/**
	 * Returns <code>true</code> if the given state is a goal state.
	 * 
	 * @return <code>true</code> if the given state is a goal state.
	 */
	boolean isGoalState(Object state);
}