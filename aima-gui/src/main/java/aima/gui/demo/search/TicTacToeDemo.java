package aima.gui.demo.search;

import aima.core.environment.tictactoe.TicTacToeGame;
import aima.core.environment.tictactoe.TicTacToeState;
import aima.core.search.adversarial.AdversarialSearch;
import aima.core.search.adversarial.AlphaBetaSearch;
import aima.core.search.adversarial.MinimaxSearch;
import aima.core.util.datastructure.XYLocation;

/**
 * Applies Minimax search and alpha-beta pruning to find optimal moves for the
 * Tic-tac-toe game.
 * 
 * @author Ruediger Lunde
 */
public class TicTacToeDemo {
	public static void main(String[] args) {
		System.out.println("TIC-TAC-TOE DEMO");
		System.out.println("");
		startMinimaxDemo();
		startAlphaBetaDemo();
	}

	private static void startMinimaxDemo() {
		System.out.println("MINI MAX DEMO\n");
		TicTacToeGame game = new TicTacToeGame();
		TicTacToeState currState = game.getInitialState();
		AdversarialSearch<TicTacToeState, XYLocation> search = MinimaxSearch
				.createFor(game);
		while (!(game.isTerminal(currState))) {
			System.out.println(game.getPlayer(currState) + "  playing ... ");
			XYLocation action = search.makeDecision(currState);
			currState = game.getResult(currState, action);
			System.out.println(currState);
		}
		System.out.println("MINI MAX DEMO done");
	}

	private static void startAlphaBetaDemo() {
		System.out.println("ALPHA BETA DEMO\n");
		TicTacToeGame game = new TicTacToeGame();
		TicTacToeState currState = game.getInitialState();
		AdversarialSearch<TicTacToeState, XYLocation> search = AlphaBetaSearch
				.createFor(game);
		while (!(game.isTerminal(currState))) {
			System.out.println(game.getPlayer(currState) + "  playing ... ");
			XYLocation action = search.makeDecision(currState);
			currState = game.getResult(currState, action);
			System.out.println(currState);
		}
		System.out.println("ALPHA BETA DEMO done");
	}
}
