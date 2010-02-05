package aima.core.environment.eightpuzzle;

import java.util.LinkedHashSet;
import java.util.Set;

import aima.core.agent.Action;
import aima.core.search.framework.ActionsFunction;
import aima.core.search.framework.ResultFunction;

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public class EightPuzzleFunctionFactory {
	private static ActionsFunction _actionsFunction = null;
	private static ResultFunction _resultFunction = null;

	public static ActionsFunction getActionsFunction() {
		if (null == _actionsFunction) {
			_actionsFunction = new EPActionsFunction();
		}
		return _actionsFunction;
	}

	public static ResultFunction getResultFunction() {
		if (null == _resultFunction) {
			_resultFunction = new EPResultFunction();
		}
		return _resultFunction;
	}

	private static class EPActionsFunction implements ActionsFunction {
		public Set<Action> actions(Object state) {
			EightPuzzleBoard board = (EightPuzzleBoard) state;

			Set<Action> actions = new LinkedHashSet<Action>();

			if (board.canMoveGap(EightPuzzleBoard.UP)) {
				actions.add(EightPuzzleBoard.UP);
			}
			if (board.canMoveGap(EightPuzzleBoard.DOWN)) {
				actions.add(EightPuzzleBoard.DOWN);
			}
			if (board.canMoveGap(EightPuzzleBoard.LEFT)) {
				actions.add(EightPuzzleBoard.LEFT);
			}
			if (board.canMoveGap(EightPuzzleBoard.RIGHT)) {
				actions.add(EightPuzzleBoard.RIGHT);
			}

			return actions;
		}
	}

	private static class EPResultFunction implements ResultFunction {
		public Object result(Object s, Action a) {
			EightPuzzleBoard board = (EightPuzzleBoard) s;

			if (EightPuzzleBoard.UP.equals(a)
					&& board.canMoveGap(EightPuzzleBoard.UP)) {
				EightPuzzleBoard newBoard = new EightPuzzleBoard(board);
				newBoard.moveGapUp();
				return newBoard;
			} else if (EightPuzzleBoard.DOWN.equals(a)
					&& board.canMoveGap(EightPuzzleBoard.DOWN)) {
				EightPuzzleBoard newBoard = new EightPuzzleBoard(board);
				newBoard.moveGapDown();
				return newBoard;
			} else if (EightPuzzleBoard.LEFT.equals(a)
					&& board.canMoveGap(EightPuzzleBoard.LEFT)) {
				EightPuzzleBoard newBoard = new EightPuzzleBoard(board);
				newBoard.moveGapLeft();
				return newBoard;
			} else if (EightPuzzleBoard.RIGHT.equals(a)
					&& board.canMoveGap(EightPuzzleBoard.RIGHT)) {
				EightPuzzleBoard newBoard = new EightPuzzleBoard(board);
				newBoard.moveGapRight();
				return newBoard;
			}

			// The Action is not understood or is a NoOp
			// the result will be the current state.
			return s;
		}
	}
}