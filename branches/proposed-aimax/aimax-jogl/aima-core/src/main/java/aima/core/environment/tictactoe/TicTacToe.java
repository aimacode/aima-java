package aima.core.environment.tictactoe;

import java.util.ArrayList;
import java.util.List;

import aima.core.search.adversarial.AlphaBeta;
import aima.core.search.adversarial.Game;
import aima.core.search.adversarial.GameState;
import aima.core.util.datastructure.XYLocation;

/**
 * @author Ravi Mohan
 * 
 */
public class TicTacToe extends Game<XYLocation> {
	public TicTacToe() {
		initialState.put("player", "X");
		initialState.put("utility", new Integer(0));
		initialState.put("board", new TicTacToeBoard());
		List<XYLocation> moves = generateMoves(initialState);
		initialState.put("moves", moves);
		initialState.put("level", new Integer(0));
		presentState = initialState;
	}

	/**
	 * Returns the list of legal moves for the specified state.
	 * 
	 * @param state
	 *            the current state of the game
	 * @return the list of legal moves for the specified state.
	 */
	private List<XYLocation> generateMoves(GameState state) {
		TicTacToeBoard position = getBoard(state);

		List<XYLocation> moves = new ArrayList<XYLocation>();
		List<TicTacToeBoard> newPositions = new ArrayList<TicTacToeBoard>();

		// For each row
		for (int r = 0; r < 3; r++) {
			// For each column
			for (int c = 0; c < 3; c++) {
				// If space[r][c] is blank
				if (position.getValue(r, c) == "-") {
					// Add move if it is unique
					XYLocation move = new XYLocation(r, c);
					TicTacToeBoard newPosition = position.cloneBoard();
					if (getPlayerToMove(state) == "X")
						newPosition.markX(r, c);
					else
						newPosition.markO(r, c);
					if (isUnique(newPosition, newPositions)) {
						moves.add(move);
						newPositions.add(newPosition);
					}
				}
			}
		}

		return moves;
	}

	/**
	 * Returns <code>true</code> if the specified position is unique among the
	 * specified list of positions. In the case of tic-tac-toe, the board has
	 * symmetry, and may be flipped horizontally, vertically, or diagonally, to
	 * determine how many unique positions exist.
	 * 
	 * @param position
	 *            a position
	 * @param positions
	 *            a list of positions
	 * @return <code>true</code> if the specified position is unique among the
	 *         the specified list of positions.
	 */
	private boolean isUnique(TicTacToeBoard position,
			List<TicTacToeBoard> positions) {
		for (int i = 0; i < positions.size(); i++) {
			if (position.flipHorizontal().equals(positions.get(i)))
				return false;
			else if (position.flipVertical().equals(positions.get(i)))
				return false;
			else if (position.flipMainDiagonal().equals(positions.get(i)))
				return false;
			else if (position.flipMinorDiagonal().equals(positions.get(i)))
				return false;
		}
		return true;
	}

	public TicTacToeBoard getBoard(GameState state) {

		return (TicTacToeBoard) state.get("board");
	}

	@Override
	public List<GameState> getSuccessorStates(GameState state) {
		GameState temp = presentState;
		List<GameState> retVal = new ArrayList<GameState>();
		int parentLevel = getLevel(state);
		for (int i = 0; i < getMoves(state).size(); i++) {
			XYLocation loc = (XYLocation) getMoves(state).get(i);

			GameState aState = makeMove(state, loc);
			aState.put("moveMade", loc);
			aState.put("level", new Integer(parentLevel + 1));
			retVal.add(aState);

		}
		presentState = temp;
		return retVal;
	}

	@Override
	public GameState makeMove(GameState state, Object o) {
		XYLocation loc = (XYLocation) o;
		return makeMove(state, loc.getXCoOrdinate(), loc.getYCoOrdinate());
	}

	public GameState makeMove(GameState state, int x, int y) {
		GameState temp = getMove(state, x, y);
		if (temp != null) {
			presentState = temp;
		}
		return presentState;
	}

	public GameState makeMove(int x, int y) {
		GameState state = presentState;
		GameState temp = getMove(state, x, y);
		if (temp != null) {
			presentState = temp;
		}
		return presentState;
	}

	public GameState getMove(GameState state, int x, int y) {
		GameState retVal = null;
		List<XYLocation> locations = new ArrayList<XYLocation>();
		for (int r = 0; r < 3; r++) {
			for (int c = 0; c < 3; c++) {
				if (getBoard(state).isEmpty(r, c))
					locations.add(new XYLocation(r, c));
			}
		}
		List<XYLocation> moves = getMoves(state);
		for (int i = 0; i < locations.size(); i++) {
			XYLocation loc = locations.get(i);
			if (moves.contains(loc)) {
				retVal = new GameState();

				TicTacToeBoard newBoard = getBoard(state).cloneBoard();
				if (getPlayerToMove(state) == "X") {
					newBoard.markX(x, y);
					retVal.put("player", "O");

				} else {
					newBoard.markO(x, y);
					retVal.put("player", "X");

				}
				retVal.put("board", newBoard);

				List<XYLocation> newMoves = generateMoves(retVal);
				retVal.put("moves", newMoves);

				retVal.put("utility", new Integer(computeUtility(newBoard,
						getPlayerToMove(getState()))));
				retVal.put("level", new Integer(getLevel(state) + 1));
				// presentState = retVal;

				break;
			}
		}
		return retVal;
	}

	@Override
	public int computeUtility(GameState state) {
		int utility = computeUtility((TicTacToeBoard) state.get("board"),
				(getPlayerToMove(state)));
		return utility;
	}

	@Override
	public boolean terminalTest(GameState state) {
		TicTacToeBoard board = (TicTacToeBoard) state.get("board");
		boolean line = board.lineThroughBoard();
		boolean filled = board.getNumberOfMarkedPositions() == 9;
		return (line || filled);
	}

	public void printPossibleMoves() {
		System.out.println("Possible moves");

		List<XYLocation> moves = getMoves(presentState);
		for (int i = 0; i < moves.size(); i++) {
			XYLocation moveLoc = (XYLocation) moves.get(i);
			GameState newState = getMove(presentState,
					moveLoc.getXCoOrdinate(), moveLoc.getYCoOrdinate());
			System.out.println("utility = " + computeUtility(newState));
			System.out.println("");
		}

	}

	@Override
	public int getMiniMaxValue(GameState state) {
		// statesSeen = new ArrayList();
		// System.out.println("In get Minimax Value");
		// System.out.println("Received state ");
		// ((TicTacToeBoard)state.get("board")).print();
		if (getPlayerToMove(state).equalsIgnoreCase("X")) {
			return maxValue(state);

		} else {
			return minValue(state);
		}
	}

	@Override
	public int getAlphaBetaValue(GameState state) {

		if (getPlayerToMove(state).equalsIgnoreCase("X")) {
			AlphaBeta initial = new AlphaBeta(Integer.MIN_VALUE,
					Integer.MAX_VALUE);
			int max = maxValue(state, initial);
			return max;

		} else {
			// invert?
			AlphaBeta initial = new AlphaBeta(Integer.MIN_VALUE,
					Integer.MAX_VALUE);
			return minValue(state, initial);
		}
	}

	//
	// PRIVATE METHODS
	//
	private int computeUtility(TicTacToeBoard aBoard, String playerToMove) {
		int retVal = 0;
		if (aBoard.lineThroughBoard()) {
			if (playerToMove.equals("X")) {
				retVal = -1;
			} else {
				retVal = 1;
			}

		}
		return retVal;
	}
}
