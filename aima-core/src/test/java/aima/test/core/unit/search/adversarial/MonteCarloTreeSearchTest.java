package aima.test.core.unit.search.adversarial;

import aima.core.environment.tictactoe.TicTacToeGame;
import aima.core.environment.tictactoe.TicTacToeState;
import aima.core.search.adversarial.Game;
import aima.core.search.adversarial.MonteCarloTreeSearch;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MonteCarloTreeSearchTest {
	Game game;
	MonteCarloTreeSearch monteCarloTreeSearch;
	
	@Before
	public void setup() {
		this.game = new TicTacToeGame();
		this.monteCarloTreeSearch = new MonteCarloTreeSearch(game, 100);
	}
	
	@Test
	public void testTicTacToeBoard() {
		String[] board = new String[]{TicTacToeState.X, TicTacToeState.O, TicTacToeState.O, TicTacToeState.O, TicTacToeState.X, TicTacToeState.X, TicTacToeState.X, TicTacToeState.O, TicTacToeState.X};
		TicTacToeState state = new TicTacToeState(board, TicTacToeState.O);
		Assert.assertTrue(game.isTerminal(state));
		Assert.assertEquals(1, game.getUtility(state, TicTacToeState.X), 0);
		
		board = new String[]{TicTacToeState.X, TicTacToeState.O, TicTacToeState.O, TicTacToeState.O, TicTacToeState.X, TicTacToeState.X, TicTacToeState.X, TicTacToeState.X, TicTacToeState.O};
		state = new TicTacToeState(board, TicTacToeState.O);
		Assert.assertTrue(game.isTerminal(state));
		Assert.assertEquals(0.5, game.getUtility(state, TicTacToeState.X), 0);
		
		board = new String[]{TicTacToeState.X, TicTacToeState.O, TicTacToeState.EMPTY, TicTacToeState.X, TicTacToeState.EMPTY, TicTacToeState.O, TicTacToeState.X, TicTacToeState.EMPTY, TicTacToeState.EMPTY};
		state = new TicTacToeState(board, TicTacToeState.O);
		Assert.assertTrue(game.isTerminal(state));
		Assert.assertEquals(1, game.getUtility(state, TicTacToeState.X), 0);
	}
	
	@Test
	public void testMonteCarloTreeDecision() {
		String[] board = new String[]{TicTacToeState.O, TicTacToeState.X, TicTacToeState.O, TicTacToeState.EMPTY, TicTacToeState.X, TicTacToeState.X, TicTacToeState.EMPTY, TicTacToeState.EMPTY, TicTacToeState.EMPTY};
		TicTacToeState state = new TicTacToeState(board, TicTacToeState.O);
		String[] expectedBoard = new String[]{TicTacToeState.O, TicTacToeState.X, TicTacToeState.O, TicTacToeState.O, TicTacToeState.X, TicTacToeState.X, TicTacToeState.EMPTY, TicTacToeState.EMPTY, TicTacToeState.EMPTY};
		TicTacToeState expectedState = new TicTacToeState(expectedBoard, TicTacToeState.X);
		Assert.assertEquals(expectedState, game.getResult(state, monteCarloTreeSearch.makeDecision(state)));
	}
}
