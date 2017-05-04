package aima.gui.fx.applications.search.games;

import java.util.Observable;

import aima.core.environment.connectfour.ConnectFourAIPlayer;
import aima.core.environment.connectfour.ConnectFourGame;
import aima.core.environment.connectfour.ConnectFourState;
import aima.core.search.adversarial.IterativeDeepeningAlphaBetaSearch;
import aima.core.search.framework.Metrics;

/**
 * Implements a facade for the game related classes of the Connect Four domain
 * layer. It maintains game, current state, and a list of observers which are
 * informed about every state change.
 * 
 * @author Ruediger Lunde
 */
public class ConnectFourModel extends Observable {
	private ConnectFourGame game;
	private ConnectFourState currState;
	Metrics searchMetrics;
	private boolean enableLog;
	

	public ConnectFourModel() {
		game = new ConnectFourGame();
		currState = (ConnectFourState) game.getInitialState();
	}

	public int getCols() {
		return currState.getCols();
	}

	public int getRows() {
		return currState.getRows();
	}

	public String[] getPlayers() {
		return game.getPlayers();
	}

	public String getPlayerForNextMove() {
		return game.getPlayer(currState);
	}

	public String getWinner() {
		String result = null;
		for (int i = 0; i < 2; i++)
			if (game.getUtility(currState, game.getPlayers()[i]) == 1)
				result = game.getPlayers()[i];
		return result;
	}

	public String getPlayerAt(int row, int col) {
		int playerNum = currState.getPlayerNum(row, col);
		return playerNum > 0 ? game.getPlayer(playerNum) : null;
	}

	public boolean isWinPositionFor(int row, int col, String player) {
		return currState.isWinPositionFor(row, col, game.getPlayerNum(player));
	}

	public void initGame() {
		currState = game.getInitialState();
		searchMetrics = null;
		setChanged();
		notifyObservers();
	}

	public boolean isGameOver() {
		return game.isTerminal(currState);
	}

	public void makeMove(int col) {
		currState = game.getResult(currState, col);
		searchMetrics = null;
		setChanged();
		notifyObservers();
	}

	/** Uses adversarial search for selecting the next action. */
	public void proposeMove(int time, int strategy) {
		IterativeDeepeningAlphaBetaSearch<ConnectFourState, Integer, String> search;
		if (strategy == 0)
			search = IterativeDeepeningAlphaBetaSearch.createFor(game, 0.0, 1.0, time);
		else
			search = new ConnectFourAIPlayer(game, time);
		if (enableLog)
			search.setLogEnabled(true);
		Integer action = search.makeDecision(currState);
		searchMetrics = search.getMetrics();
		currState = game.getResult(currState, action);
		setChanged();
		notifyObservers();
	}
}
