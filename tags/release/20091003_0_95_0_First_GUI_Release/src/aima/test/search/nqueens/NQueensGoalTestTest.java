package aima.test.search.nqueens;

import junit.framework.TestCase;
import aima.basic.XYLocation;
import aima.search.nqueens.NQueensBoard;
import aima.search.nqueens.NQueensGoalTest;

/**
 * @author Ravi Mohan
 * 
 */
public class NQueensGoalTestTest extends TestCase {
	NQueensGoalTest goalTest;

	NQueensBoard board;

	@Override
	public void setUp() {
		goalTest = new NQueensGoalTest();
		board = new NQueensBoard(8);
	}

	public void testEmptyBoard() {
		assertFalse(goalTest.isGoalState(board));
	}

	public void testSingleSquareBoard() {
		board = new NQueensBoard(1);
		assertFalse(goalTest.isGoalState(board));
		board.addQueenAt(new XYLocation(0, 0));
		assertTrue(goalTest.isGoalState(board));
	}

	public void testInCorrectPlacement() {
		assertFalse(goalTest.isGoalState(board));
		// This is the configuration of Figure 3.5 (b) in AIMA 2nd Edition
		board.addQueenAt(new XYLocation(0, 0));
		board.addQueenAt(new XYLocation(1, 2));
		board.addQueenAt(new XYLocation(2, 4));
		board.addQueenAt(new XYLocation(3, 6));
		board.addQueenAt(new XYLocation(4, 1));
		board.addQueenAt(new XYLocation(5, 3));
		board.addQueenAt(new XYLocation(6, 5));
		board.addQueenAt(new XYLocation(7, 7));
		assertFalse(goalTest.isGoalState(board));
	}

	public void testCorrectPlacement() {

		assertFalse(goalTest.isGoalState(board));
		// This is the configuration of Figure 5.9 (c) in AIMA 2nd Edition
		board.addQueenAt(new XYLocation(0, 1));
		board.addQueenAt(new XYLocation(1, 4));
		board.addQueenAt(new XYLocation(2, 6));
		board.addQueenAt(new XYLocation(3, 3));
		board.addQueenAt(new XYLocation(4, 0));
		board.addQueenAt(new XYLocation(5, 7));
		board.addQueenAt(new XYLocation(6, 5));
		board.addQueenAt(new XYLocation(7, 2));

		assertTrue(goalTest.isGoalState(board));
	}
}
