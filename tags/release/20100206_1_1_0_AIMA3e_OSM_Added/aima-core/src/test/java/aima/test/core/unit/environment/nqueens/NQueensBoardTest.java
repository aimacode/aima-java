package aima.test.core.unit.environment.nqueens;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.environment.nqueens.NQueensBoard;
import aima.core.util.datastructure.XYLocation;

/**
 * @author Ravi Mohan
 * 
 */
public class NQueensBoardTest {

	NQueensBoard board;

	@Before
	public void setUp() {

		board = new NQueensBoard(8);
	}

	@Test
	public void testBasics() {
		Assert.assertEquals(0, board.getNumberOfQueensOnBoard());
		board.addQueenAt(new XYLocation(0, 0));
		Assert.assertEquals(1, board.getNumberOfQueensOnBoard());
		board.addQueenAt(new XYLocation(0, 0));
		Assert.assertEquals(1, board.getNumberOfQueensOnBoard());
		board.addQueenAt(new XYLocation(1, 1));
		Assert.assertEquals(2, board.getNumberOfQueensOnBoard());
		Assert.assertTrue(board.queenExistsAt(new XYLocation(1, 1)));
		Assert.assertTrue(board.queenExistsAt(new XYLocation(0, 0)));
		board.moveQueen(new XYLocation(1, 1), new XYLocation(3, 3));
		Assert.assertTrue(board.queenExistsAt(new XYLocation(3, 3)));
		Assert.assertTrue(!(board.queenExistsAt(new XYLocation(1, 1))));
		Assert.assertEquals(2, board.getNumberOfQueensOnBoard());
	}

	@Test
	public void testCornerQueenAttack1() {

		board.addQueenAt(new XYLocation(0, 0));
		Assert.assertEquals(false, board.isSquareUnderAttack(new XYLocation(0,
				0)));
		// queen on square not included
		Assert.assertEquals(true, board
				.isSquareUnderAttack(new XYLocation(1, 0)));
		Assert.assertEquals(true, board
				.isSquareUnderAttack(new XYLocation(7, 0)));
		Assert.assertEquals(true, board
				.isSquareUnderAttack(new XYLocation(0, 7)));
		Assert.assertEquals(true, board
				.isSquareUnderAttack(new XYLocation(1, 1)));
		Assert.assertEquals(true, board
				.isSquareUnderAttack(new XYLocation(2, 2)));
		Assert.assertEquals(false, board.isSquareUnderAttack(new XYLocation(2,
				1)));
		Assert.assertEquals(false, board.isSquareUnderAttack(new XYLocation(1,
				2)));
	}

	@Test
	public void testCornerQueenAttack2() {

		board.addQueenAt(new XYLocation(7, 7));
		Assert.assertEquals(true, board
				.isSquareUnderAttack(new XYLocation(0, 0)));
		Assert.assertEquals(true, board
				.isSquareUnderAttack(new XYLocation(7, 0)));
		Assert.assertEquals(true, board
				.isSquareUnderAttack(new XYLocation(0, 7)));
		Assert.assertEquals(true, board
				.isSquareUnderAttack(new XYLocation(7, 0)));
		Assert.assertEquals(true, board
				.isSquareUnderAttack(new XYLocation(6, 6)));
		Assert.assertEquals(true, board
				.isSquareUnderAttack(new XYLocation(5, 5)));
		Assert.assertEquals(false, board.isSquareUnderAttack(new XYLocation(6,
				5)));
		Assert.assertEquals(false, board.isSquareUnderAttack(new XYLocation(5,
				6)));
	}

	@Test
	public void testEdgeQueenAttack() {

		board.addQueenAt(new XYLocation(0, 3));
		Assert.assertEquals(true, board
				.isSquareUnderAttack(new XYLocation(0, 0)));
		Assert.assertEquals(true, board
				.isSquareUnderAttack(new XYLocation(0, 7)));
		Assert.assertEquals(true, board
				.isSquareUnderAttack(new XYLocation(7, 3)));
		Assert.assertEquals(true, board
				.isSquareUnderAttack(new XYLocation(3, 0)));
		Assert.assertEquals(true, board
				.isSquareUnderAttack(new XYLocation(4, 7)));
	}

	@Test
	public void testAttack2() {

		board.addQueenAt(new XYLocation(7, 0));
		Assert.assertEquals(true, board
				.isSquareUnderAttack(new XYLocation(6, 1)));
	}

	@Test
	public void testAttack3() {

		board.addQueenAt(new XYLocation(0, 0));
		Assert.assertEquals(true, board
				.isSquareUnderAttack(new XYLocation(0, 1)));
	}

	@Test
	public void testAttack4() {

		board.addQueenAt(new XYLocation(0, 2));
		Assert.assertTrue(board.isSquareUnderAttack(new XYLocation(1, 1)));
	}

	@Test
	public void testMidBoardDiagonalAttack() {

		board.addQueenAt(new XYLocation(3, 3));
		// forwardDiagonal from the queen
		Assert.assertTrue(board.isSquareUnderAttack(new XYLocation(4, 2)));
		Assert.assertTrue(board.isSquareUnderAttack(new XYLocation(4, 4)));
		// backwardDiagonal from the queen
		Assert.assertTrue(board.isSquareUnderAttack(new XYLocation(2, 2)));
		Assert.assertTrue(board.isSquareUnderAttack(new XYLocation(2, 4)));
	}

	@Test
	public void testCornerDiagonalAttack() {

		board.addQueenAt(new XYLocation(0, 0));
		// forwardDiagonal from the queen
		Assert.assertTrue(board.isSquareUnderAttack(new XYLocation(1, 1)));
		board.clear();

		board.addQueenAt(new XYLocation(7, 7));
		// backwardDiagonal from the queen
		Assert.assertTrue(board.isSquareUnderAttack(new XYLocation(6, 6)));

		// assertTrue(board.isSquareUnderAttack(new XYLocation(2, 2)));
		// assertTrue(board.isSquareUnderAttack(new XYLocation(2, 4)));
	}

	@Test
	public void testAttack6() {

		board.addQueenAt(new XYLocation(1, 6));
		Assert.assertTrue(board.isSquareUnderAttack(new XYLocation(0, 7)));
	}

	@Test
	public void testRemoveQueen() {

		board.addQueenAt(new XYLocation(0, 0));
		Assert.assertEquals(1, board.getNumberOfQueensOnBoard());
		board.removeQueenFrom(new XYLocation(0, 0));
		Assert.assertEquals(0, board.getNumberOfQueensOnBoard());
	}

	@Test
	public void testMoveQueen() {

		XYLocation from = new XYLocation(0, 0);
		XYLocation to = new XYLocation(1, 1);

		board.addQueenAt(from);
		Assert.assertEquals(1, board.getNumberOfQueensOnBoard());
		Assert.assertTrue(board.queenExistsAt(from));
		Assert.assertFalse(board.queenExistsAt(to));

		board.moveQueen(from, to);
		Assert.assertEquals(1, board.getNumberOfQueensOnBoard());
		Assert.assertFalse(board.queenExistsAt(from));
		Assert.assertTrue(board.queenExistsAt(to));
	}

	@Test
	public void testMoveNonExistentQueen() {

		XYLocation from = new XYLocation(0, 0);
		XYLocation to = new XYLocation(1, 1);
		board.moveQueen(from, to);

		Assert.assertEquals(0, board.getNumberOfQueensOnBoard());
	}

	@Test
	public void testRemoveNonExistentQueen() {
		board.removeQueenFrom(new XYLocation(0, 0));
		Assert.assertEquals(0, board.getNumberOfQueensOnBoard());
	}

	@Test
	public void testEquality() {

		board.addQueenAt(new XYLocation(0, 0));
		NQueensBoard board2 = new NQueensBoard(8);
		board2.addQueenAt(new XYLocation(0, 0));
		Assert.assertEquals(board, board2);
		NQueensBoard board3 = new NQueensBoard(8);
		board3.addQueenAt(new XYLocation(0, 1));
		Assert.assertFalse(board.equals(board3));
	}

	@Test
	public void testPrint() {

		NQueensBoard board2 = new NQueensBoard(2);
		board2.addQueenAt(new XYLocation(0, 0));
		String expected = " Q  - \n -  - \n";
		Assert.assertEquals(expected, board2.getBoardPic());
	}

	@Test
	public void testDontPlaceTwoQueensOnOneSquare() {

		board.addQueenAt(new XYLocation(0, 0));
		board.addQueenAt(new XYLocation(0, 0));
		Assert.assertEquals(1, board.getNumberOfQueensOnBoard());
	}

	@Test
	public void testSimpleHorizontalAttack() {
		XYLocation loc = new XYLocation(0, 0);
		board.addQueenAt(loc);
		Assert.assertEquals(0, board.getNumberOfAttacksOn(loc));
		Assert
				.assertEquals(1, board
						.getNumberOfAttacksOn(new XYLocation(1, 0)));
		Assert.assertEquals(1, board.getNumberOfAttacksOn(loc.right()));
		Assert
				.assertEquals(1, board
						.getNumberOfAttacksOn(new XYLocation(7, 0)));
	}

	@Test
	public void testSimpleVerticalAttack() {
		XYLocation loc = new XYLocation(0, 0);
		board.addQueenAt(loc);
		Assert.assertEquals(0, board.getNumberOfAttacksOn(loc));
		Assert.assertEquals(1, board.getNumberOfAttacksOn(loc.down()));
		Assert
				.assertEquals(1, board
						.getNumberOfAttacksOn(new XYLocation(0, 7)));
	}

	@Test
	public void testSimpleDiagonalAttack() {
		XYLocation loc = new XYLocation(3, 3);
		board.addQueenAt(loc);
		Assert.assertEquals(0, board.getNumberOfAttacksOn(loc));
		Assert.assertEquals(1, board.getNumberOfAttacksOn(loc.down().right()));
		Assert.assertEquals(1, board.getNumberOfAttacksOn(loc.down().left()));
		Assert.assertEquals(1, board.getNumberOfAttacksOn(loc.up().left()));
		Assert.assertEquals(1, board.getNumberOfAttacksOn(loc.up().right()));
		Assert
				.assertEquals(1, board
						.getNumberOfAttacksOn(new XYLocation(7, 7)));
		Assert
				.assertEquals(1, board
						.getNumberOfAttacksOn(new XYLocation(0, 0)));
		Assert
				.assertEquals(1, board
						.getNumberOfAttacksOn(new XYLocation(6, 0)));
		Assert
				.assertEquals(1, board
						.getNumberOfAttacksOn(new XYLocation(0, 6)));
	}

	@Test
	public void testMultipleQueens() {
		XYLocation loc1 = new XYLocation(3, 3);
		board.addQueenAt(loc1);
		Assert.assertEquals(1, board.getNumberOfAttacksOn(loc1.right()));

		board.addQueenAt(loc1.right().right());
		Assert.assertEquals(1, board.getNumberOfAttacksOn(loc1));
		Assert.assertEquals(2, board.getNumberOfAttacksOn(loc1.right()));

		board.addQueenAt(loc1.right().down());
		Assert.assertEquals(2, board.getNumberOfAttacksOn(loc1));
		Assert.assertEquals(3, board.getNumberOfAttacksOn(loc1.right()));
		Assert
				.assertEquals(2, board.getNumberOfAttacksOn(loc1.right()
						.right()));
	}

	@Test
	public void testBoardDisplay() {
		board.addQueenAt(new XYLocation(0, 5));
		board.addQueenAt(new XYLocation(1, 6));
		board.addQueenAt(new XYLocation(2, 1));
		board.addQueenAt(new XYLocation(3, 3));
		board.addQueenAt(new XYLocation(4, 6));
		board.addQueenAt(new XYLocation(5, 4));
		board.addQueenAt(new XYLocation(6, 7));
		board.addQueenAt(new XYLocation(7, 7));
		Assert.assertEquals(" -  -  -  -  -  -  -  - \n"
				+ " -  -  Q  -  -  -  -  - \n" + " -  -  -  -  -  -  -  - \n"
				+ " -  -  -  Q  -  -  -  - \n" + " -  -  -  -  -  Q  -  - \n"
				+ " Q  -  -  -  -  -  -  - \n" + " -  Q  -  -  Q  -  -  - \n"
				+ " -  -  -  -  -  -  Q  Q \n", board.getBoardPic());

		Assert.assertEquals("--------\n" + "--Q-----\n" + "--------\n"
				+ "---Q----\n" + "-----Q--\n" + "Q-------\n" + "-Q--Q---\n"
				+ "------QQ\n", board.toString());
	}
}
