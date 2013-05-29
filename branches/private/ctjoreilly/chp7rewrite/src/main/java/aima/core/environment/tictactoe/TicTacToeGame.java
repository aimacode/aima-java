package aima.core.environment.tictactoe;

import java.util.List;

import aima.core.search.adversarial.Game;
import aima.core.util.datastructure.XYLocation;

/**
 * Provides an implementation of the Tic-tac-toe game which can be used for
 * experiments with the Minimax algorithm.
 * 
 * @author Ruediger Lunde
 * 
 */
public class TicTacToeGame implements Game<TicTacToeState, XYLocation, String> {

	TicTacToeState initialState = new TicTacToeState();

	@Override
	public TicTacToeState getInitialState() {
		return initialState;
	}

	@Override
	public String[] getPlayers() {
		return new String[] { TicTacToeState.X, TicTacToeState.O };
	}

	@Override
	public String getPlayer(TicTacToeState state) {
		return state.getPlayerToMove();
	}

	@Override
	public List<XYLocation> getActions(TicTacToeState state) {
		return state.getUnMarkedPositions();
	}

	@Override
	public TicTacToeState getResult(TicTacToeState state, XYLocation action) {
		TicTacToeState result = state.clone();
		result.mark(action);
		return result;
	}

	@Override
	public boolean isTerminal(TicTacToeState state) {
		return state.getUtility() != -1;
	}

	@Override
	public double getUtility(TicTacToeState state, String player) {
		double result = state.getUtility();
		if (result != -1) {
			if (player == TicTacToeState.O)
				result = 1 - result;
		} else {
			throw new IllegalArgumentException("State is not terminal.");
		}
		return result;
	}
}
