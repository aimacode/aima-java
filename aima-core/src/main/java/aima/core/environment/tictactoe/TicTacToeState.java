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

	private String[] board = new String[] { EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
			EMPTY, EMPTY, EMPTY, EMPTY };

	private String playerToMove = X;
	private double utility = -1; // 1: win for X, 0: win for O, 0.5: draw

	public boolean isEmpty(int row, int col) {
		return board[getAbsPosition(row, col)] == EMPTY;
	}

	public String getPlayerToMove() {
		return playerToMove;
	}

	public String getValue(int row, int col) {
		return board[getAbsPosition(row, col)];
	}

	public double getUtility() {
		return utility;
	}

	public void mark(XYLocation action) {
		mark(action.getYCoOrdinate(), action.getXCoOrdinate());
	}

	public void mark(int row, int col) {
		if (utility == -1 && getValue(row, col) == EMPTY) {
			board[getAbsPosition(row, col)] = playerToMove;
			analyzeUtility();
			playerToMove = (playerToMove == X ? O : X);
		}
	}

	private void analyzeUtility() {
		if (lineThroughBoard())
			utility = (playerToMove == X ? 1 : 0);
		else if (getNumberOfMarkedPositions() == 9)
			utility = 0.5;
	}

	public boolean lineThroughBoard() {
		return (isAnyRowComplete() || isAnyColumnComplete() || isAnyDiagonalComplete());
	}
	
	private boolean isAnyRowComplete() {
		for (int i = 0; i < 3; i++) {
			String val = getValue(i, 0);
			if (val != EMPTY && val == getValue(i, 1) && val == getValue(i, 2))
				return true;
		}
		return false;
	}

	private boolean isAnyColumnComplete() {
		for (int j = 0; j < 3; j++) {
			String val = getValue(0, j);
			if (val != EMPTY && val == getValue(1, j) && val == getValue(2, j))
				return true;
		}
		return false;
	}

	private boolean isAnyDiagonalComplete() {
		boolean retVal = false;
		String val = getValue(0, 0);
		if (val != EMPTY && val == getValue(1, 1) && val == getValue(2, 2))
			return true;
		val = getValue(0, 2);
		if (val != EMPTY && val == getValue(1, 1) && val == getValue(2, 0))
			return true;
		return retVal;
	}

	public int getNumberOfMarkedPositions() {
		int retVal = 0;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (!(isEmpty(i, j))) {
					retVal++;
				}
			}
		}
		return retVal;
	}

	public List<XYLocation> getUnMarkedPositions() {
		List<XYLocation> retVal = new ArrayList<XYLocation>();
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (isEmpty(i, j)) {
					retVal.add(new XYLocation(j, i));
				}
			}

		}
		return retVal;
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
		TicTacToeState anotherState = (TicTacToeState) anObj;
		for (int i = 0; i < 9; i++)
			if (board[i] != anotherState.board[i])
				return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++)
				buf.append(getValue(i, j) + " ");
			buf.append("\n");
		}
		return buf.toString();
	}

	//
	// PRIVATE METHODS
	//

	private int getAbsPosition(int row, int col) {
		return row * 3 + col;
	}
}
