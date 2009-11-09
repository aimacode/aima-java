package aima.core.search.nqueens;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import aima.core.agent.Action;
import aima.core.search.framework.ActionsFunction;
import aima.core.search.framework.ResultFunction;
import aima.core.util.datastructure.XYLocation;

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public class NQueensFunctionFactory {
	private static ActionsFunction _actionsFunction = null;
	private static ResultFunction _resultFunction = null;

	public static ActionsFunction getActionsFunction() {
		if (null == _actionsFunction) {
			_actionsFunction = new NQActionsFunction();
		}
		return _actionsFunction;
	}

	public static ResultFunction getResultFunction() {
		if (null == _resultFunction) {
			_resultFunction = new NQResultFunction();
		}
		return _resultFunction;
	}

	private static class NQActionsFunction implements ActionsFunction {
		public Set<Action> actions(Object state) {
			NQueensBoard board = (NQueensBoard) state;

			Set<Action> actions = new LinkedHashSet<Action>();

			int numQueens = board.getNumberOfQueensOnBoard();
			int boardSize = board.getSize();
			for (int i = 0; i < boardSize; i++) {
				XYLocation newLocation = new XYLocation(numQueens, i);
				if (!(board.isSquareUnderAttack(newLocation))) {
					actions.add(new PlaceQueenAction(newLocation
							.getXCoOrdinate(), newLocation.getYCoOrdinate()));
				}
			}

			return actions;
		}
	}

	private static class NQResultFunction implements ResultFunction {
		public Object result(Object s, Action a) {

			if (a instanceof PlaceQueenAction) {
				PlaceQueenAction pqa = (PlaceQueenAction) a;

				return placeQueenAt(pqa.getX(), pqa.getY(), (NQueensBoard) s);
			}
			// The Action is not understood or is a NoOp
			// the result will be the current state.
			return s;
		}
	}

	private static NQueensBoard placeQueenAt(int x, int y,
			NQueensBoard parentBoard) {

		NQueensBoard newBoard = new NQueensBoard(parentBoard.getSize());
		List<XYLocation> queenPositionsOnParentBoard = parentBoard
				.getQueenPositions();
		queenPositionsOnParentBoard.add(new XYLocation(x, y));
		newBoard.setBoard(queenPositionsOnParentBoard);
		return newBoard;
	}
}