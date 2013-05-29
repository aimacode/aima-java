package aima.core.environment.connectfour;

import java.util.ArrayList;
import java.util.List;

import aima.core.search.adversarial.Game;

/**
 * Provides an implementation of the ConnectFour game which can be used for
 * experiments with the Minimax algorithm.
 * 
 * @author Ruediger Lunde
 * 
 */
public class ConnectFourGame implements Game<ConnectFourState, Integer, String> {

	String[] players = new String[] { "red", "yellow" };
	ConnectFourState initialState = new ConnectFourState(6, 7);

	@Override
	public ConnectFourState getInitialState() {
		return initialState;
	}

	@Override
	public String[] getPlayers() {
		return players;
	}

	@Override
	public String getPlayer(ConnectFourState state) {
		return getPlayer(state.getPlayerToMove());
	}

	/**
	 * Returns the player corresponding to the specified player number. For
	 * efficiency reasons, <code>ConnectFourState</code>s use numbers
	 * instead of strings to identify players.
	 */
	public String getPlayer(int playerNum) {
		switch (playerNum) {
		case 1:
			return players[0];
		case 2:
			return players[1];
		}
		return null;
	}

	@Override
	public List<Integer> getActions(ConnectFourState state) {
		List<Integer> result = new ArrayList<Integer>();
		for (int i = 0; i < state.getCols(); i++)
			if (state.getPlayerNum(0, i) == 0)
				result.add(i);
		return result;
	}

	@Override
	public ConnectFourState getResult(ConnectFourState state, Integer action) {
		ConnectFourState result = state.clone();
		result.dropDisk(action);
		return result;
	}

	@Override
	public boolean isTerminal(ConnectFourState state) {
		return state.getUtility() != -1;
	}

	@Override
	public double getUtility(ConnectFourState state, String player) {
		double result = state.getUtility();
		if (result != -1) {
			if (player == players[1])
				result = 1 - result;
		} else {
			throw new IllegalArgumentException("State is not terminal.");
		}
		return result;
	}
}
