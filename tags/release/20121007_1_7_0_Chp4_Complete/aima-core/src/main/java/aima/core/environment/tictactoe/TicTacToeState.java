package aima.core.environment.tictactoe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import aima.core.util.datastructure.XYLocation;

/**
 * A state of the Tic-tac-toe game is characterized by a board containing
 * symbols X and O, the next player to move, and an utility information.
 * 
 * @author Ruediger Lunde
 * 
 */
public class TicTacToeState implements Cloneable {
	public static final String O = "O";
	public static final String X = "X";
	public static final String EMPTY = "-";
	//
	private String[] board = new String[] { EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
			EMPTY, EMPTY, EMPTY, EMPTY };

	private String playerToMove = X;
	private double utility = -1; // 1: win for X, 0: win for O, 0.5: draw

	public String getPlayerToMove() {
		return playerToMove;
	}
	
	public boolean isEmpty(int col, int row) {
		return board[getAbsPosition(col, row)] == EMPTY;
	}

	public String getValue(int col, int row) {
		return board[getAbsPosition(col, row)];
	}

	public double getUtility() {
		return utility;
	}

	public void mark(XYLocation action) {
		mark(action.getXCoOrdinate(), action.getYCoOrdinate());
	}

	public void mark(int col, int row) {
		if (utility == -1 && getValue(col, row) == EMPTY) {
			board[getAbsPosition(col, row)] = playerToMove;
			analyzeUtility();
			playerToMove = (playerToMove == X ? O : X);
		}
	}

	private void analyzeUtility() {
		if (lineThroughBoard()) {
			utility = (playerToMove == X ? 1 : 0);
		} else if (getNumberOfMarkedPositions() == 9) {
			utility = 0.5;
		}
	}

	public boolean lineThroughBoard() {
		return (isAnyRowComplete() || isAnyColumnComplete() || isAnyDiagonalComplete());
	}
	
	private boolean isAnyRowComplete() {
		for (int row = 0; row < 3; row++) {
			String val = getValue(0, row);
			if (val != EMPTY && val == getValue(1, row) && val == getValue(2, row)) {
				return true;
			}
		}
		return false;
	}

	private boolean isAnyColumnComplete() {
		for (int col = 0; col < 3; col++) {
			String val = getValue(col, 0);
			if (val != EMPTY && val == getValue(col, 1) && val == getValue(col, 2)) {
				return true;
			}
		}
		return false;
	}

	private boolean isAnyDiagonalComplete() {
		boolean retVal = false;
		String val = getValue(0, 0);
		if (val != EMPTY && val == getValue(1, 1) && val == getValue(2, 2)) {
			return true;
		}
		val = getValue(0, 2);
		if (val != EMPTY && val == getValue(1, 1) && val == getValue(2, 0)) {
			return true;
		}
		return retVal;
	}

	public int getNumberOfMarkedPositions() {
		int retVal = 0;
		for (int col = 0; col < 3; col++) {
			for (int row = 0; row < 3; row++) {
				if (!(isEmpty(col, row))) {
					retVal++;
				}
			}
		}
		return retVal;
	}

	public List<XYLocation> getUnMarkedPositions() {
		List<XYLocation> result = new ArrayList<XYLocation>();
		for (int col = 0; col < 3; col++) {
			for (int row = 0; row < 3; row++) {
				if (isEmpty(col, row)) {
					result.add(new XYLocation(col, row));
				}
			}
		}
		return result;
	}

	@Override
	public TicTacToeState clone() {
		TicTacToeState copy = null;
		try {
			copy = (TicTacToeState) super.clone();
			copy.board = Arrays.copyOf(board, board.length);
		} catch (CloneNotSupportedException e) {
			e.printStackTrace(); // should never happen...
		}
		return copy;
	}

	@Override
	public boolean equals(Object anObj) {
		if (anObj != null && anObj.getClass() == getClass()) {
			TicTacToeState anotherState = (TicTacToeState) anObj;
			for (int i = 0; i < 9; i++) {
				if (board[i] != anotherState.board[i]) {
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		// Need to ensure equal objects have equivalent hashcodes (Issue 77).
		return toString().hashCode();
	}

	@Override
	public String toString() {
		StringBuilder strBuilder = new StringBuilder();
		for (int row = 0; row < 3; row++) {
			for (int col = 0; col < 3; col++) {
				strBuilder.append(getValue(col, row) + " ");
			}
			strBuilder.append("\n");
		}
		return strBuilder.toString();
	}

	//
	// PRIVATE METHODS
	//

	private int getAbsPosition(int col, int row) {
		return row * 3 + col;
	}
}
