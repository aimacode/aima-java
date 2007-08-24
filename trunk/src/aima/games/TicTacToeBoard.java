/*
 * Created on Feb 15, 2005
 *
 */
package aima.games;

import java.util.ArrayList;
import java.util.List;

import aima.basic.XYLocation;

/**
 * @author Ravi Mohan
 * 
 */
public class TicTacToeBoard {
	private static final String O = "O";

	private static final String X = "X";

	String[] topRow = { " ", " ", " " };

	String[] midRow = { " ", " ", " " };

	String[] bottomRow = { " ", " ", " " };

	private String[] whichRow(int rowNumber) {
		String[] whichRow = null;
		if (rowNumber == 0) {
			whichRow = topRow;
		} else if (rowNumber == 1) {
			whichRow = midRow;
		} else if (rowNumber == 2) {
			whichRow = bottomRow;
		}
		return whichRow;

	}

	public boolean isEmpty(int row, int col) {
		String[] whichRow = whichRow(row);

		return (whichRow[col] == " ");
	}

	public void markX(int row, int col) {
		mark(row, col, X);
	}

	public void markO(int row, int col) {
		mark(row, col, O);
	}

	public void mark(int row, int col, String symbol) {
		String[] whichRow = null;
		whichRow = whichRow(row);
		whichRow[col] = symbol;
	}

	public boolean isAnyRowComplete() {
		boolean retVal = false;
		for (int i = 0; i < 3; i++) {
			String[] whichRow = whichRow(i);
			if ((whichRow[0] != " ") && (whichRow[0] == whichRow[1])
					&& (whichRow[1] == whichRow[2])) {
				retVal = true;
				break;
			}
		}
		return retVal;
	}

	public boolean isAnyColumnComplete() {
		boolean retVal = false;
		for (int i = 0; i < 3; i++) {

			if ((topRow[i] != " ") && (topRow[i] == midRow[i])
					&& (midRow[i] == bottomRow[i])) {
				retVal = true;
				break;
			}
		}
		return retVal;
	}

	public boolean isAnyDiagonalComplete() {
		boolean retVal = false;
		// check diagonal 1
		if ((topRow[0] != " ") && (topRow[0] == midRow[1])
				&& (midRow[1] == bottomRow[2])) {
			retVal = true;
		} else if ((topRow[2] != " ") && (topRow[2] == midRow[1])
				&& (midRow[1] == bottomRow[0])) {
			retVal = true;
		}

		return retVal;
	}

	public boolean lineThroughBoard() {
		return ((isAnyRowComplete()) || (isAnyColumnComplete()) || (isAnyDiagonalComplete()));
	}

	public String getValue(int row, int col) {
		String[] whichRow = whichRow(row);
		return whichRow[col];
	}

	private void setValue(int row, int col, String val) {
		String[] whichRow = whichRow(row);
		whichRow[col] = val;
	}

	public void print() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				String value = getValue(i, j);
				String printValue;
				if (value == " ") {
					printValue = "-";
				} else {
					printValue = value;
				}
				System.out.print(printValue + " ");
			}
			System.out.println();
		}
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				String value = getValue(i, j);
				String printValue;
				if (value == " ") {
					printValue = "-";
				} else {
					printValue = value;
				}
				buf.append(printValue + " ");
			}
			buf.append("\n");
		}
		return buf.toString();
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

	public List getUnMarkedPositions() {
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

	@Override
	public boolean equals(Object anObj) {
		boolean retVal = true;
		TicTacToeBoard anotherBoard = (TicTacToeBoard) anObj;
		boolean secondBreak = false;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (anotherBoard.getValue(i, j) != getValue(i, j)) {
					retVal = false;
					secondBreak = true;
					break;
				}

			}
			if (secondBreak == false) {
				break;
			}

		}
		return retVal;
	}

	public boolean isMarked(String string, int i, int j) {
		return getValue(i, j).equals(string);
	}
}
