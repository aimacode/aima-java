package aima.test.core.unit.environment.tictactoe;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.environment.tictactoe.TicTacToe;
import aima.core.environment.tictactoe.TicTacToeBoard;
import aima.core.search.adversarial.AlphaBeta;
import aima.core.search.adversarial.GameState;

/**
 * @author Ravi Mohan
 * 
 */
public class TicTacToeTest {

	private TicTacToeBoard tb;

	@Before
	public void setUp() {
		tb = new TicTacToeBoard();
	}

	@Test
	public void testCreation() {
		TicTacToe t3 = new TicTacToe();
		Assert.assertEquals(9, t3.getMoves(t3.getState()).size());
		Assert.assertEquals("X", t3.getPlayerToMove(t3.getState()));
	}

	@Test
	public void testOnCreationBoardIsEmpty() {
		Assert.assertEquals(true, tb.isEmpty(0, 0));
		Assert.assertEquals(true, tb.isEmpty(0, 2));
		Assert.assertEquals(true, tb.isEmpty(2, 0));
		Assert.assertEquals(true, tb.isEmpty(2, 2));
	}

	@Test
	public void testMovingBumpsLevelByOne() {
		TicTacToe t1 = new TicTacToe();
		int level = t1.getLevel(t1.getState());
		Assert.assertEquals(0, level);
		t1.makeMove(0, 0);
		level = t1.getLevel(t1.getState());
		Assert.assertEquals(1, level);
	}

	@Test
	public void testMarkingAsSquareMakesItNonEmpty() {
		tb.markX(0, 0);
		Assert.assertEquals(false, tb.isEmpty(0, 0));
	}

	@Test
	public void testAllCombinationsOfSuccessLines() {
		tb.markX(0, 0);
		tb.markX(0, 1);
		tb.markX(0, 2);
		Assert.assertEquals(true, tb.isAnyRowComplete());
		tb.markO(0, 2);
		Assert.assertEquals(false, tb.isAnyRowComplete());
		tb.markO(1, 2);
		tb.markO(2, 2);
		Assert.assertEquals(true, tb.isAnyColumnComplete());
		tb.markX(2, 2);
		tb.markX(1, 1);
		Assert.assertEquals(true, tb.isAnyDiagonalComplete());
		tb.markO(1, 1);
		tb.markO(2, 0);
		Assert.assertEquals(true, tb.isAnyDiagonalComplete());
	}

	@Test
	public void testGivenABoardNUmberOfPossibleMovesCalculatedCorrectly() {
		TicTacToe t3 = new TicTacToe();
		Assert.assertEquals(9, t3.getMoves(t3.getState()).size());

		Assert.assertEquals("X", t3.getPlayerToMove(t3.getState()));
		t3.makeMove(t3.getState(), 0, 0);

		Assert.assertEquals(8, t3.getMoves(t3.getState()).size());
		Assert.assertEquals("O", t3.getPlayerToMove(t3.getState()));

		// try illegal move
		t3.makeMove(t3.getState(), 0, 0);
		Assert.assertEquals(8, t3.getMoves(t3.getState()).size());
		Assert.assertEquals("O", t3.getPlayerToMove(t3.getState()));

		t3.makeMove(t3.getState(), 1, 1);
		Assert.assertEquals(7, t3.getMoves(t3.getState()).size());
		Assert.assertEquals("X", t3.getPlayerToMove(t3.getState()));
	}

	@Test
	public void testCalculateUtilityOfABoard() {
		// game 1
		TicTacToe t3 = new TicTacToe();
		t3.makeMove(t3.getState(), 0, 0);
		Assert.assertEquals(0, t3.getUtility(t3.getState()));
		t3.makeMove(t3.getState(), 2, 2);
		Assert.assertEquals(0, t3.getUtility(t3.getState()));
		t3.makeMove(t3.getState(), 2, 0);
		Assert.assertEquals(0, t3.getUtility(t3.getState()));
		t3.makeMove(t3.getState(), 1, 0);
		Assert.assertEquals(0, t3.getUtility(t3.getState()));
		t3.makeMove(t3.getState(), 0, 2);
		Assert.assertEquals(0, t3.getUtility(t3.getState()));
		t3.makeMove(t3.getState(), 0, 1);
		Assert.assertEquals(0, t3.getUtility(t3.getState()));
		t3.makeMove(t3.getState(), 1, 1);
		Assert.assertEquals(1, t3.computeUtility((t3.getState())));

		// game 2
		TicTacToe t4 = new TicTacToe();
		t4.makeMove(t4.getState(), 0, 0);
		Assert.assertEquals(0, t4.getUtility(t4.getState()));
		t4.makeMove(t4.getState(), 0, 1);
		Assert.assertEquals(0, t4.getUtility(t4.getState()));
		t4.makeMove(t4.getState(), 0, 2);
		Assert.assertEquals(0, t4.getUtility(t4.getState()));
		t4.makeMove(t4.getState(), 1, 0);
		Assert.assertEquals(0, t4.getUtility(t4.getState()));
		t4.makeMove(t4.getState(), 1, 1);
		Assert.assertEquals(0, t4.getUtility(t4.getState()));
		t4.makeMove(t4.getState(), 1, 2);
		Assert.assertEquals(0, t4.getUtility(t4.getState()));
		t4.makeMove(t4.getState(), 2, 0);

		Assert.assertEquals(1, t4.computeUtility(t4.getState()));
	}

	/*
	 * public void testMinimumValue(){ TicTacToe t3 = new TicTacToe();
	 * t3.makeMove(2,2); int i = t3.minValue(t3.getState());
	 * System.out.println("i = " + i);
	 * 
	 * TicTacToe t4 = new TicTacToe(); t4.makeMove(2,1); int j =
	 * t4.minValue(t4.getState()); System.out.println("j = " + j);
	 * assertEquals(-1,i); assertEquals(-1,j); }
	 */
	@Test
	public void testGenerateSuccessors() {
		TicTacToe t3 = new TicTacToe();
		ArrayList successors = t3.getSuccessorStates(t3.getState());
		Assert.assertEquals(9, successors.size());
		checkSuccessorList(successors, "O", 8);
		ArrayList successors2 = t3.getSuccessorStates((GameState) successors
				.get(0));
		checkSuccessorList(successors2, "X", 7);
		ArrayList successors3 = t3.getSuccessorStates((GameState) successors2
				.get(0));
		checkSuccessorList(successors3, "O", 6);
	}

	@Test
	public void testGameStateEquality() {
		TicTacToeBoard tb1 = new TicTacToeBoard();
		TicTacToeBoard tb2 = new TicTacToeBoard();
		GameState gs1 = new GameState();
		GameState gs2 = new GameState();
		gs1.put("board", tb1);
		gs2.put("board", tb2);
		Assert.assertEquals(gs1, gs2);
		gs1.put("minimaxValue", new Integer(3));
		Assert.assertTrue(!(gs1.equals(gs2)));
	}

	@Test
	public void testMiniMax() {
		TicTacToe t3 = new TicTacToe();
		t3.makeMove(0, 0);
		t3.makeMove(2, 2);
		t3.makeMove(2, 0);
		t3.makeMove(1, 1);
		Assert.assertEquals(1, t3.getMiniMaxValue(t3.getState()));
	}

	@Test
	public void testMiniMax2() {
		TicTacToe t3 = new TicTacToe();
		t3.makeMove(0, 0);
		t3.makeMove(2, 2);
		t3.makeMove(2, 0);

		Assert.assertEquals(1, t3.getMiniMaxValue(t3.getState()));
	}

	@Test
	public void testMiniMax3() {
		TicTacToe t3 = new TicTacToe();
		t3.makeMove(0, 0);
		t3.makeMove(1, 1);

		Assert.assertEquals(0, t3.getMiniMaxValue(t3.getState()));
	}

	@Test
	public void testMiniMax7() {
		TicTacToe t3 = new TicTacToe();
		t3.makeMove(0, 0);
		t3.makeMove(0, 1);

		Assert.assertEquals(1, t3.getMiniMaxValue(t3.getState()));
	}

	@Test
	public void tesMiniMax4() {
		TicTacToe t3 = new TicTacToe();

		Assert.assertEquals(0, t3.getMiniMaxValue(t3.getState()));
	}

	@Test
	public void testTerminalStateDetection() {
		TicTacToe t3 = new TicTacToe();
		t3.makeMove(0, 0);
		t3.makeMove(0, 1);
		t3.makeMove(0, 2);
		t3.makeMove(1, 0);
		t3.makeMove(1, 1);
		t3.makeMove(1, 2);

		Assert.assertEquals(1, t3.getMiniMaxValue(t3.getState()));
	}

	@Test
	public void testMiniMax15() {

		TicTacToe t1 = new TicTacToe();
		t1.makeMove(0, 0);

		int minimax1 = t1.getMiniMaxValue(t1.getState());
		TicTacToe t2 = new TicTacToe();
		t2.makeMove(0, 2);
		int minimax2 = t2.getMiniMaxValue(t2.getState());
		TicTacToe t3 = new TicTacToe();
		t3.makeMove(2, 0);
		int minimax3 = t3.getMiniMaxValue(t3.getState());
		TicTacToe t4 = new TicTacToe();
		int minimax4 = t4.getMiniMaxValue(t4.getState());
		t4.makeMove(2, 2);

		Assert.assertEquals(minimax1, minimax2);
	}

	@Test
	public void testMiniMax6() {
		TicTacToe t3 = new TicTacToe();
		t3.makeMove(0, 0);
		t3.makeMove(0, 1);
		t3.makeMove(0, 2);

		t3.makeMove(2, 2);
		t3.makeMove(1, 2);

		t3.makeMove(2, 1);
		t3.makeMove(1, 1);
		Assert.assertEquals(-1, t3.getMiniMaxValue(t3.getState()));
	}

	@Test
	public void testAlphaBeta() {
		TicTacToe t1 = new TicTacToe();
		int alphabeta0 = t1.getAlphaBetaValue(t1.getState());
		Assert.assertEquals(0, alphabeta0);
		t1.makeMove(0, 0);

		int alphabeta1 = t1.getAlphaBetaValue(t1.getState());
		Assert.assertEquals(0, alphabeta1);
		TicTacToe t2 = new TicTacToe();
		t2.makeMove(0, 2);
		int alphabeta2 = t2.getAlphaBetaValue(t2.getState());
		Assert.assertEquals(0, alphabeta2);

		TicTacToe t3 = new TicTacToe();
		t3.makeMove(2, 0);
		int alphabeta3 = t3.getAlphaBetaValue(t3.getState());
		Assert.assertEquals(0, alphabeta3);

		TicTacToe t4 = new TicTacToe();
		int alphabeta4 = t4.getAlphaBetaValue(t4.getState());
		t4.makeMove(2, 2);

		Assert.assertEquals(0, alphabeta4);
	}

	@Test
	public void testAlphaBetaMinValueCalculation() {
		// board x o x , o o x,- x -
		TicTacToe t = new TicTacToe();

		t.makeMove(0, 0); // x
		t.makeMove(0, 1); // o
		t.makeMove(0, 2); // x

		t.makeMove(1, 0); // o
		t.makeMove(1, 2); // x
		t.makeMove(1, 1); // o

		t.makeMove(2, 1); // x

		int minValue = t.minValue(t.getState(), new AlphaBeta(
				Integer.MIN_VALUE, Integer.MAX_VALUE));
		Assert.assertEquals(0, minValue);
	}

	//
	// PRIVATE METHODS
	//
	private void checkSuccessorList(ArrayList successorList,
			String playerToMove, int sizeOfSuccessors) {
		for (int i = 0; i < successorList.size(); i++) {
			GameState h = (GameState) successorList.get(i);

			ArrayList successors2 = new TicTacToe().getSuccessorStates(h);
			Assert.assertEquals(sizeOfSuccessors, successors2.size());
			Assert.assertEquals(playerToMove, new TicTacToe()
					.getPlayerToMove(h));
		}
	}
}
