package aima.core.environment.checkers;

import java.util.ArrayList;
import java.util.List;
import aima.core.environment.checkers.Checkerboard;
import aima.core.environment.tictactoe.TicTacToeBoard;

import aima.core.search.adversarial.AlphaBeta;
import aima.core.search.adversarial.Game;
import aima.core.search.adversarial.GameState;
import aima.core.util.datastructure.Queue;

public class Checkers extends Game<Queue<Integer>> {
	
	public Checkers()
	{
		initialState.put("player", "red");
		initialState.put("utility", new Integer(0));
		initialState.put("board", new Checkerboard());
		//List<Queue<Integer>> moves = generateMoves(initialState);
		//initialState.put("moves", moves);
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
	private List<Queue<Integer>> generateMoves(GameState state) {
		/*TicTacToeBoard position = getBoard(state);

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
		*/
		return null;
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
			Queue<Integer> loc = (Queue<Integer>) getMoves(state).get(i);

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
		//XYLocation loc = (XYLocation) o;
		// TODO
		return null;
	}	
	
	@Override
	public int computeUtility(GameState state) {
		int utility = computeUtility((Checkerboard) state.get("board"),
				(getPlayerToMove(state)));
		return utility;
	}	
	
	@Override
	public boolean terminalTest(GameState state) {
		Checkerboard board = (Checkerboard) state.get("board");
		return true;
	}
	
	@Override
	public int getMiniMaxValue(GameState state) {
		// statesSeen = new ArrayList();
		// System.out.println("In get Minimax Value");
		// System.out.println("Received state ");
		// ((TicTacToeBoard)state.get("board")).print();
		if (getPlayerToMove(state).equalsIgnoreCase("red")) {
			return maxValue(state);

		} else {
			return minValue(state);
		}
	}

	@Override
	public int getAlphaBetaValue(GameState state) {

		if (getPlayerToMove(state).equalsIgnoreCase("red")) {
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
	private int computeUtility(Checkerboard aBoard, String playerToMove) {
		int retVal = 0;
		/*if (aBoard.lineThroughBoard()) {
			if (playerToMove.equals("red")) {
				retVal = -1;
			} else {
				retVal = 1;
			}

		}*/
		return retVal;
	}
}
