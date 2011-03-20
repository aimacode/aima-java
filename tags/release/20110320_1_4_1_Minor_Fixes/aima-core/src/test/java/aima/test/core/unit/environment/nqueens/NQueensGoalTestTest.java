package aima.test.core.unit.environment.nqueens;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.environment.nqueens.NQueensBoard;
import aima.core.environment.nqueens.NQueensGoalTest;
import aima.core.util.datastructure.XYLocation;

/**
 * @author Ravi Mohan
 * 
 */
public class NQueensGoalTestTest {
	NQueensGoalTest goalTest;

	NQueensBoard board;

	@Before
	public void setUp() {
		goalTest = new NQueensGoalTest();
		board = new NQueensBoard(8);
	}

	@Test
	public void testEmptyBoard() {
		Assert.assertFalse(goalTest.isGoalState(board));
	}

	@Test
	public void testSingleSquareBoard() {
		board = new NQueensBoard(1);
		Assert.assertFalse(goalTest.isGoalState(board));
		board.addQueenAt(new XYLocation(0, 0));
		Assert.assertTrue(goalTest.isGoalState(board));
	}

	@Test
	public void testInCorrectPlacement() {
		Assert.assertFalse(goalTest.isGoalState(board));
		// This is the configuration of Figure 3.5 (b) in AIMA 2nd Edition
		board.addQueenAt(new XYLocation(0, 0));
		board.addQueenAt(new XYLocation(1, 2));
		board.addQueenAt(new XYLocation(2, 4));
		board.addQueenAt(new XYLocation(3, 6));
		board.addQueenAt(new XYLocation(4, 1));
		board.addQueenAt(new XYLocation(5, 3));
		board.addQueenAt(new XYLocation(6, 5));
		board.addQueenAt(new XYLocation(7, 7));
		Assert.assertFalse(goalTest.isGoalState(board));
	}

	@Test
	public void testCorrectPlacement() {

		Assert.assertFalse(goalTest.isGoalState(board));
		// This is the configuration of Figure 5.9 (c) in AIMA 2nd Edition
		board.addQueenAt(new XYLocation(0, 1));
		board.addQueenAt(new XYLocation(1, 4));
		board.addQueenAt(new XYLocation(2, 6));
		board.addQueenAt(new XYLocation(3, 3));
		board.addQueenAt(new XYLocation(4, 0));
		board.addQueenAt(new XYLocation(5, 7));
		board.addQueenAt(new XYLocation(6, 5));
		board.addQueenAt(new XYLocation(7, 2));

		Assert.assertTrue(goalTest.isGoalState(board));
	}
}
