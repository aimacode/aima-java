package aima.core.environment.nqueens;

import java.util.List;

import aima.core.search.framework.GoalTest;
import aima.core.util.datastructure.XYLocation;

/**
 * @author Ravi Mohan
 * 
 */

public class NQueensGoalTest implements GoalTest {
	NQueensBoard board;

	public boolean isGoalState(Object state) {

		board = (NQueensBoard) state;
		return (allQueensPlaced() && allQueenPositionsHaveZeroAttacks(board
				.getQueenPositions()));
	}

	private boolean allQueensPlaced() {
		return board.getNumberOfQueensOnBoard() == board.getSize();
	}

	private boolean allQueenPositionsHaveZeroAttacks(List<XYLocation> positions) {

		for (int i = 0; i < positions.size(); i++) {
			XYLocation location = (XYLocation) positions.get(i);
			if (board.getNumberOfAttacksOn(location) != 0) {
				return false;
			}
		}
		return true;
	}
}