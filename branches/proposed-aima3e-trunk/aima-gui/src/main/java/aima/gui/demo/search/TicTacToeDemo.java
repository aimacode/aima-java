package aima.gui.demo.search;

import aima.core.environment.tictactoe.TicTacToe;
import aima.core.environment.tictactoe.TicTacToeBoard;
import aima.core.search.adversarial.GameState;

/**
 * @author Ravi Mohan
 * 
 */
public class TicTacToeDemo {
	public static void main(String[] args) {
		System.out.println("TicTacToe Demo");
		System.out.println("");
		minimaxDemo();

		alphaBetaDemo();

	}

	private static void alphaBetaDemo() {
		System.out.println("ALPHA BETA ");
		System.out.println("");
		TicTacToe t4 = new TicTacToe();
		while (!(t4.hasEnded())) {
			System.out.println("\n" + t4.getPlayerToMove(t4.getState())
					+ "  playing ... ");

			t4.makeAlphaBetaMove();
			GameState presentState = t4.getState();
			TicTacToeBoard board = t4.getBoard(presentState);
			board.print();
		}
		System.out.println("ALPHA BETA DEMO done");
	}

	private static void minimaxDemo() {
		System.out.println("MINI MAX ");
		System.out.println("");
		TicTacToe t3 = new TicTacToe();
		while (!(t3.hasEnded())) {
			System.out.println("\n" + t3.getPlayerToMove(t3.getState())
					+ " playing");
			System.out.println("");
			t3.makeMiniMaxMove();
			GameState presentState = t3.getState();
			TicTacToeBoard board = t3.getBoard(presentState);
			System.out.println("");
			board.print();

		}
		System.out.println("Mini MAX DEMO done");
	}
}
