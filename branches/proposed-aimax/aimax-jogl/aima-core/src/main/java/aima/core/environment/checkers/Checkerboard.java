package aima.core.environment.checkers;

public class Checkerboard {
	// -1 black piece
	// -2 black king
	// 0 empty square
	// 1 red piece
	// 2 red king
	int state[][] = new int[][] { { -1, -1, -1, -1 }, { -1, -1, -1, -1 },
			{ -1, -1, -1, -1 }, { 0, 0, 0, 0 }, { 0, 0, 0, 0 }, { 1, 1, 1, 1 },
			{ 1, 1, 1, 1 }, { 1, 1, 1, 1 } };

	/**
	 * Returns the board rotated 180 to give the next player perspective of the
	 * boards current state.
	 * 
	 * @return the board rotated 180 to give the next player perspective of the
	 *         boards current state.
	 */
	public Checkerboard rotate() {
		Checkerboard newBoard = new Checkerboard();

		// For each row
		for (int r = 0; r < 8; r++) {
			// For each column
			for (int c = 0; c < 4; c++) {
				newBoard.state[2 - r][2 - c] = state[r][c];
			}
		}

		return newBoard;
	}
	
	/**
	 * Returns <code>true</code> if the specified row and column is occupied by
	 * a red checker.  Note: this does not include a red king.
	 */
	public boolean isRedChecker(int r, int c)
	{
		return state[r][c] == 1;
	}
	
	/**
	 * Returns <code>true</code> if the specified row and column is occupied by
	 * a white checker.  Note: this does not include a white king.
	 */
	public boolean isWhiteChecker(int r, int c)
	{
		return state[r][c] == -1;
	}
	
	/**
	 * Returns <code>true</code> if the specified row and column is occupied by
	 * a red king.
	 */
	public boolean isRedKing(int r, int c)
	{
		return state[r][c] == 2;
	}
	
	/**
	 * Returns <code>true</code> if the specified row and column is occupied by
	 * a white king.
	 */
	public boolean isWhiteKing(int r, int c)
	{
		return state[r][c] == -2;
	}

	public Checkerboard cloneBoard() {
		return (Checkerboard) clone();
	}

	public Object clone() {
		Checkerboard newBoard = new Checkerboard();
		for (int r = 0; r < 8; r++) {
			for (int c = 0; c < 4; c++) {
				newBoard.state[r][c] = state[r][c];
			}
		}
		return newBoard;
	}
}
