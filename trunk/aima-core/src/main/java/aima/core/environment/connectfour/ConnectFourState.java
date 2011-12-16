package aima.core.environment.connectfour;

/**
 * A state of the Connect Four game is characterized by a board containing a
 * grid of spaces for disks, the next player to move, and some utility
 * informations. A win position for a player x is an empty space which turns a
 * situation into a win situation for x if he is able to place a disk there.
 * 
 * @author Ruediger Lunde
 * 
 */
public class ConnectFourState implements Cloneable {
	private int cols;
	/**
	 * Uses special bit coding. First bit: disk of player 1, second bit: disk of
	 * player 2, third bit: win position for player 1, fourth bit: win position
	 * for player 2.
	 */
	private byte[] board;

	private int moveCount;
	/**
	 * Indicates the utility of the state. 1: win for player 1, 0: win for
	 * player 2, 0.5: draw, -1 for all non-terminal states.
	 */
	private double utility;
	public int winPositions1;
	public int winPositions2;

	public ConnectFourState(int rows, int cols) {
		utility = -1;
		this.cols = cols;
		board = new byte[rows * cols];
	}

	public int getRows() {
		return board.length / cols;
	}

	public int getCols() {
		return cols;
	}

	public double getUtility() {
		return utility;
	}

	public int getPlayerNum(int row, int col) {
		return board[row * cols + col] & 3;
	}

	public int getPlayerToMove() {
		return moveCount % 2 + 1;
	}
	
	public int getMoves() {
		return moveCount;
	}

	public void dropDisk(int col) {
		int playerNum = getPlayerToMove();
		int row = getFreeRow(col);
		if (row != -1) {
			moveCount++;
			if (moveCount == board.length)
				utility = 0.5;
			if (isWinPositionFor(row, col, 1)) {
				winPositions1--;
				if (playerNum == 1)
					utility = 1.0;
			}
			if (isWinPositionFor(row, col, 2)) {
				winPositions2--;
				if (playerNum == 2)
					utility = 0.0;
			}
			board[row * cols + col] = (byte) playerNum;
			if (utility == -1)
				analyzeWinPositions(row, col);
		}
	}

	/**
	 * Returns the row of the first empty space in the specified column and -1
	 * if the column is full.
	 */
	private int getFreeRow(int col) {
		for (int row = getRows() - 1; row >= 0; row--)
			if (getPlayerNum(row, col) == 0)
				return row;
		return -1;
	}

	public boolean isWinMoveFor(int col, int playerNum) {
		return isWinPositionFor(getFreeRow(col), col, playerNum);
	}
	
	public boolean isWinPositionFor(int row, int col, int playerNum) {
		return (board[row * cols + col] & playerNum * 4) > 0;
	}

	private void setWinPositionFor(int row, int col, int playerNum) {
		if (playerNum == 1) {
			if (!isWinPositionFor(row, col, 1))
				winPositions1++;
		} else if (playerNum == 2) {
			if (!isWinPositionFor(row, col, 2))
				winPositions2++;
		} else {
			throw new IllegalArgumentException("Wrong player number.");
		}
		board[row * cols + col] |= playerNum * 4;
	}

	/**
	 * Assumes a disk at position <code>moveRow</code> and <code>moveCol</code>
	 * and analyzes the vicinity with respect to win positions.
	 */
	private void analyzeWinPositions(int moveRow, int moveCol) {
		final int[] rowIncr = new int[] { 1, 0, 1, 1 };
		final int[] colIncr = new int[] { 0, 1, -1, 1 };
		int playerNum = getPlayerNum(moveRow, moveCol);
		WinPositionInfo[] wInfo = new WinPositionInfo[] {
				new WinPositionInfo(), new WinPositionInfo() };
		for (int i = 0; i < 4; i++) {
			int rIncr = rowIncr[i];
			int cIncr = colIncr[i];
			int diskCount = 1;

			for (int j = 0; j < 2; j++) {
				WinPositionInfo wInf = wInfo[j];
				wInf.clear();
				int rBound = rIncr > 0 ? getRows() : -1;
				int cBound = cIncr > 0 ? getCols() : -1;

				int row = moveRow + rIncr;
				int col = moveCol + cIncr;
				while (row != rBound && col != cBound) {
					int plNum = getPlayerNum(row, col);
					if (plNum == playerNum) {
						if (wInf.hasData())
							wInf.diskCount++;
						else
							diskCount++;
					} else if (plNum == 0) {
						if (!wInf.hasData()) {
							wInf.row = row;
							wInf.col = col;
						} else {
							break;
						}
					} else {
						break;
					}
					row += rIncr;
					col += cIncr;
				}
				rIncr = -rIncr;
				cIncr = -cIncr;
			}
			for (int j = 0; j < 2; j++) {
				WinPositionInfo wInf = wInfo[j];
				if (wInf.hasData() && diskCount + wInf.diskCount >= 3) {
					setWinPositionFor(wInf.row, wInf.col, playerNum);
				}
			}
		}
	}

	public int analyzePotentialWinPositions(Integer action) {
		final int[] rowIncr = new int[] { 1, 0, 1, 1 };
		final int[] colIncr = new int[] { 0, 1, -1, 1 };
		int moveCol = action;
		int moveRow = getFreeRow(moveCol);

		int playerNum = getPlayerToMove();
		int result = 0;
		for (int i = 0; i < 4; i++) {
			int rIncr = rowIncr[i];
			int cIncr = colIncr[i];
			int posCountSum = 0;

			for (int j = 0; j < 2; j++) {
				int rBound = rIncr > 0 ? getRows() : -1;
				int cBound = cIncr > 0 ? getCols() : -1;
				int posCount = 0;

				int row = moveRow + rIncr;
				int col = moveCol + cIncr;
				while (row != rBound && col != cBound && posCount < 3) {
					int plNum = getPlayerNum(row, col);
					if (plNum == 3 - playerNum)
						break;
					posCount++;
					row += rIncr;
					col += cIncr;
				}
				posCountSum += posCount;
				rIncr = -rIncr;
				cIncr = -cIncr;
			}
			if (posCountSum >= 3)
				result += posCountSum;
		}
		return result;
	}

	public ConnectFourState clone() {
		ConnectFourState result = null;
		try {
			result = (ConnectFourState) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		result.board = board.clone();
		return result;
	}

	@Override
	public int hashCode() {
		int result = 0;
		for (int i = 0; i < board.length; i++)
			result = result * 7 + board[i] + 1;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ConnectFourState) {
			ConnectFourState s = (ConnectFourState) obj;
			for (int i = 0; i < board.length; i++)
				if (board[i] != s.board[i])
					return false;
			return true;
		}
		return false;
	}

	// ////////////////////////////////////////////////////////////////////
	// nested classes

	static class WinPositionInfo {
		int row = -1;
		int col = -1;
		int diskCount;

		void clear() {
			row = -1;
			col = -1;
			diskCount = 0;
		}

		boolean hasData() {
			return row != -1;
		}
	}
}
