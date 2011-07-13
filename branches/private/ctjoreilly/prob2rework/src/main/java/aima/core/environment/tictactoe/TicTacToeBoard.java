package aima.core.environment.tictactoe;

import java.util.ArrayList;
import java.util.List;

import aima.core.util.datastructure.XYLocation;

/**
 * @author Ravi Mohan
 * @author R. Lunde
 * 
 */
public class TicTacToeBoard {
	public static final String O = "O";
	public static final String X = "X";
	public static final String EMPTY = "-";

	String[] state = new String[] { EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY,
			EMPTY, EMPTY, EMPTY };

	public boolean isEmpty(int row, int col) {
		return state[getAbsPosition(row, col)] == EMPTY;
	}

	public boolean isMarked(String string, int i, int j) {
		return getValue(i, j).equals(string);
	}

	public void markX(int row, int col) {
		mark(row, col, X);
	}

	public void markO(int row, int col) {
		mark(row, col, O);
	}

	private void mark(int row, int col, String symbol) {
		state[getAbsPosition(row, col)] = symbol;
	}

	public boolean isAnyRowComplete() {
		for (int i = 0; i < 3; i++) {
			String val = getValue(i, 0);
			if (val != EMPTY && val == getValue(i, 1) && val == getValue(i, 2))
				return true;
		}
		return false;
	}

	public boolean isAnyColumnComplete() {
		for (int j = 0; j < 3; j++) {
			String val = getValue(0, j);
			if (val != EMPTY && val == getValue(1, j) && val == getValue(2, j))
				return true;
		}
		return false;
	}

	public boolean isAnyDiagonalComplete() {
		boolean retVal = false;
		String val = getValue(0, 0);
		if (val != EMPTY && val == getValue(1, 1) && val == getValue(2, 2))
			return true;
		val = getValue(0, 2);
		if (val != EMPTY && val == getValue(1, 1) && val == getValue(2, 0))
			return true;
		return retVal;
	}

	public boolean lineThroughBoard() {
		return (isAnyRowComplete() || isAnyColumnComplete() || isAnyDiagonalComplete());
	}

	public String getValue(int row, int col) {
		return state[getAbsPosition(row, col)];
	}

	private void setValue(int row, int col, String val) {
		state[getAbsPosition(row, col)] = val;
	}

	public TicTacToeBoard cloneBoard() {
		return (TicTacToeBoard) clone();
	}

	@Override
	public Object clone() {
		TicTacToeBoard newBoard = new TicTacToeBoard();
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				String s = getValue(i, j);
				newBoard.setValue(i, j, s);
			}
		}
		return newBoard;
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
					retVal.add(new XYLocation(i, j));
				}
			}

		}
		return retVal;
	}

	public String[] getState() {
		return state;
	}

	public void setState(String[] state) {
		this.state = state;
	}

	@Override
	public boolean equals(Object anObj) {
		TicTacToeBoard anotherBoard = (TicTacToeBoard) anObj;
		for (int i = 0; i < 9; i++)
			if (state[i] != anotherBoard.state[i])
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

	public void print() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++)
				System.out.print(getValue(i, j) + " ");
			System.out.println();
		}
	}

	//
	// PRIVATE METHODS
	//

	private int getAbsPosition(int row, int col) {
		return row * 3 + col;
	}
}
