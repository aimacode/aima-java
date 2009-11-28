package aima.core.search.framework;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 67.
 * 
 * The goal test, which determines whether a given state is a goal state.
 */

/**
 * @author Ravi Mohan
 * 
 */
public interface GoalTest {
	boolean isGoalState(Object state);
}