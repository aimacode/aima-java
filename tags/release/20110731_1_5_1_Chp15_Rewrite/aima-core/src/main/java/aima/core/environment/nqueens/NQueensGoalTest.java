package aima.core.environment.nqueens;

import aima.core.search.framework.GoalTest;

/**
 * @author R. Lunde
 */
public class NQueensGoalTest implements GoalTest {

	public boolean isGoalState(Object state) {
		NQueensBoard board = (NQueensBoard) state;
		return board.getNumberOfQueensOnBoard() == board.getSize()
				&& board.getNumberOfAttackingPairs() == 0;
	}
}