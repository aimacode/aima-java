package aima.core.environment.checkers;

public class Checkerboard {
	// -1 opponent's piece
	// -2 opponent's king
	// 0 empty square
	// 1 player's piece
	// 2 player's king
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
				newBoard.state[2 - r][2 - c] = state[2 - r][2 - c];
			}
		}

		return newBoard;
	}

	public Checkerboard cloneBoard() {
		return (Checkerboard) clone();
	}

	public Object clone() {
		Checkerboard newBoard = new Checkerboard();
		for (int r = 0; r < 8; r++) {
			for (int c = 0; c < 4; c++) {
				newBoard.state[r][c] = state[2 - r][2 - c];
			}
		}
		return newBoard;
	}
}
