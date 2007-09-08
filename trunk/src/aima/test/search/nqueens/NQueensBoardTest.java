package aima.test.search.nqueens;

import junit.framework.TestCase;
import aima.basic.XYLocation;
import aima.search.nqueens.NQueensBoard;

/**
 * @author Ravi Mohan
 * 
 */

public class NQueensBoardTest extends TestCase {

	NQueensBoard board;

	@Override
	public void setUp() {

		board = new NQueensBoard(8);
	}

	public void testBasics() {

		assertEquals(0, board.getNumberOfQueensOnBoard());
		board.addQueenAt(new XYLocation(0, 0));
		assertEquals(1, board.getNumberOfQueensOnBoard());
		board.addQueenAt(new XYLocation(0, 0));
		assertEquals(1, board.getNumberOfQueensOnBoard());
		board.addQueenAt(new XYLocation(1, 1));
		assertEquals(2, board.getNumberOfQueensOnBoard());
		assertTrue(board.queenExistsAt(new XYLocation(1, 1)));
		assertTrue(board.queenExistsAt(new XYLocation(0, 0)));
		board.moveQueen(new XYLocation(1, 1), new XYLocation(3, 3));
		assertTrue(board.queenExistsAt(new XYLocation(3, 3)));
		assertTrue(!(board.queenExistsAt(new XYLocation(1, 1))));
		assertEquals(2, board.getNumberOfQueensOnBoard());

	}

	public void testCornerQueenAttack1() {

		board.addQueenAt(new XYLocation(0, 0));
		assertEquals(false, board.isSquareUnderAttack(new XYLocation(0, 0))); // queen
		// on
		// square
		// not
		// included
		assertEquals(true, board.isSquareUnderAttack(new XYLocation(1, 0)));
		assertEquals(true, board.isSquareUnderAttack(new XYLocation(7, 0)));
		assertEquals(true, board.isSquareUnderAttack(new XYLocation(0, 7)));
		assertEquals(true, board.isSquareUnderAttack(new XYLocation(1, 1)));
		assertEquals(true, board.isSquareUnderAttack(new XYLocation(2, 2)));
		assertEquals(false, board.isSquareUnderAttack(new XYLocation(2, 1)));
		assertEquals(false, board.isSquareUnderAttack(new XYLocation(1, 2)));

	}

	public void testCornerQueenAttack2() {

		board.addQueenAt(new XYLocation(7, 7));
		assertEquals(true, board.isSquareUnderAttack(new XYLocation(0, 0)));
		assertEquals(true, board.isSquareUnderAttack(new XYLocation(7, 0)));
		assertEquals(true, board.isSquareUnderAttack(new XYLocation(0, 7)));
		assertEquals(true, board.isSquareUnderAttack(new XYLocation(7, 0)));
		assertEquals(true, board.isSquareUnderAttack(new XYLocation(6, 6)));
		assertEquals(true, board.isSquareUnderAttack(new XYLocation(5, 5)));
		assertEquals(false, board.isSquareUnderAttack(new XYLocation(6, 5)));
		assertEquals(false, board.isSquareUnderAttack(new XYLocation(5, 6)));

	}

	public void testEdgeQueenAttack() {

		board.addQueenAt(new XYLocation(0, 3));
		assertEquals(true, board.isSquareUnderAttack(new XYLocation(0, 0)));
		assertEquals(true, board.isSquareUnderAttack(new XYLocation(0, 7)));
		assertEquals(true, board.isSquareUnderAttack(new XYLocation(7, 3)));
		assertEquals(true, board.isSquareUnderAttack(new XYLocation(3, 0)));
		assertEquals(true, board.isSquareUnderAttack(new XYLocation(4, 7)));

	}

	public void testAttack2() {

		board.addQueenAt(new XYLocation(7, 0));
		assertEquals(true, board.isSquareUnderAttack(new XYLocation(6, 1)));
	}

	public void testAttack3() {

		board.addQueenAt(new XYLocation(0, 0));
		assertEquals(true, board.isSquareUnderAttack(new XYLocation(0, 1)));
	}

	public void testAttack4() {

		board.addQueenAt(new XYLocation(0, 2));
		assertTrue(board.isSquareUnderAttack(new XYLocation(1, 1)));
	}

	public void testMidBoardDiagonalAttack() {

		board.addQueenAt(new XYLocation(3, 3));
		// forwardDiagonal from the queen
		assertTrue(board.isSquareUnderAttack(new XYLocation(4, 2)));
		assertTrue(board.isSquareUnderAttack(new XYLocation(4, 4)));
		// backwardDiagonal from the queen
		assertTrue(board.isSquareUnderAttack(new XYLocation(2, 2)));
		assertTrue(board.isSquareUnderAttack(new XYLocation(2, 4)));
	}

	public void testCornerDiagonalAttack() {

		board.addQueenAt(new XYLocation(0, 0));
		// forwardDiagonal from the queen
		assertTrue(board.isSquareUnderAttack(new XYLocation(1, 1)));
		board.clear();

		board.addQueenAt(new XYLocation(7, 7));
		// backwardDiagonal from the queen
		assertTrue(board.isSquareUnderAttack(new XYLocation(6, 6)));

		// assertTrue(board.isSquareUnderAttack(new XYLocation(2, 2)));
		// assertTrue(board.isSquareUnderAttack(new XYLocation(2, 4)));
	}

	public void testAttack6() {

		board.addQueenAt(new XYLocation(1, 6));
		assertTrue(board.isSquareUnderAttack(new XYLocation(0, 7)));
	}

	public void testRemoveQueen() {

		board.addQueenAt(new XYLocation(0, 0));
		assertEquals(1, board.getNumberOfQueensOnBoard());
		board.removeQueenFrom(new XYLocation(0, 0));
		assertEquals(0, board.getNumberOfQueensOnBoard());
	}

	public void testMoveQueen() {

		XYLocation from = new XYLocation(0, 0);
		XYLocation to = new XYLocation(1, 1);

		board.addQueenAt(from);
		assertEquals(1, board.getNumberOfQueensOnBoard());
		assertTrue(board.queenExistsAt(from));
		assertFalse(board.queenExistsAt(to));

		board.moveQueen(from, to);
		assertEquals(1, board.getNumberOfQueensOnBoard());
		assertFalse(board.queenExistsAt(from));
		assertTrue(board.queenExistsAt(to));
	}

	public void testMoveNonExistentQueen() {

		XYLocation from = new XYLocation(0, 0);
		XYLocation to = new XYLocation(1, 1);
		board.moveQueen(from, to);

		assertEquals(0, board.getNumberOfQueensOnBoard());

	}

	public void testRemoveNonExistentQueen() {
		board.removeQueenFrom(new XYLocation(0, 0));
		assertEquals(0, board.getNumberOfQueensOnBoard());

	}

	public void testEquality() {

		board.addQueenAt(new XYLocation(0, 0));
		NQueensBoard board2 = new NQueensBoard(8);
		board2.addQueenAt(new XYLocation(0, 0));
		assertEquals(board, board2);
		NQueensBoard board3 = new NQueensBoard(8);
		board3.addQueenAt(new XYLocation(0, 1));
		assertFalse(board.equals(board3));

	}

	public void testPrint() {

		NQueensBoard board2 = new NQueensBoard(2);
		board2.addQueenAt(new XYLocation(0, 0));
		String expected = " Q  - \n -  - \n";
		assertEquals(expected, board2.getBoardPic());

	}

	public void testDontPlaceTwoQueensOnOneSquare() {

		board.addQueenAt(new XYLocation(0, 0));
		board.addQueenAt(new XYLocation(0, 0));
		assertEquals(1, board.getNumberOfQueensOnBoard());
	}

	public void testSimpleHorizontalAttack() {
		XYLocation loc = new XYLocation(0, 0);
		board.addQueenAt(loc);
		assertEquals(0, board.getNumberOfAttacksOn(loc));
		assertEquals(1, board.getNumberOfAttacksOn(new XYLocation(1, 0)));
		assertEquals(1, board.getNumberOfAttacksOn(loc.right()));
		assertEquals(1, board.getNumberOfAttacksOn(new XYLocation(7, 0)));
	}

	public void testSimpleVerticalAttack() {
		XYLocation loc = new XYLocation(0, 0);
		board.addQueenAt(loc);
		assertEquals(0, board.getNumberOfAttacksOn(loc));
		assertEquals(1, board.getNumberOfAttacksOn(loc.down()));
		assertEquals(1, board.getNumberOfAttacksOn(new XYLocation(0, 7)));
	}

	public void testSimpleDiagonalAttack() {
		XYLocation loc = new XYLocation(3, 3);
		board.addQueenAt(loc);
		assertEquals(0, board.getNumberOfAttacksOn(loc));
		assertEquals(1, board.getNumberOfAttacksOn(loc.down().right()));
		assertEquals(1, board.getNumberOfAttacksOn(loc.down().left()));
		assertEquals(1, board.getNumberOfAttacksOn(loc.up().left()));
		assertEquals(1, board.getNumberOfAttacksOn(loc.up().right()));
		assertEquals(1, board.getNumberOfAttacksOn(new XYLocation(7, 7)));
		assertEquals(1, board.getNumberOfAttacksOn(new XYLocation(0, 0)));
		assertEquals(1, board.getNumberOfAttacksOn(new XYLocation(6, 0)));
		assertEquals(1, board.getNumberOfAttacksOn(new XYLocation(0, 6)));
	}

	public void testMultipleQueens() {
		XYLocation loc1 = new XYLocation(3, 3);
		board.addQueenAt(loc1);
		assertEquals(1, board.getNumberOfAttacksOn(loc1.right()));

		board.addQueenAt(loc1.right().right());
		assertEquals(1, board.getNumberOfAttacksOn(loc1));
		assertEquals(2, board.getNumberOfAttacksOn(loc1.right()));

		board.addQueenAt(loc1.right().down());
		assertEquals(2, board.getNumberOfAttacksOn(loc1));
		assertEquals(3, board.getNumberOfAttacksOn(loc1.right()));
		assertEquals(2, board.getNumberOfAttacksOn(loc1.right().right()));

	}

	public void testBoardDisplay() {
		board.addQueenAt(new XYLocation(0, 5));
		board.addQueenAt(new XYLocation(1, 6));
		board.addQueenAt(new XYLocation(2, 1));
		board.addQueenAt(new XYLocation(3, 3));
		board.addQueenAt(new XYLocation(4, 6));
		board.addQueenAt(new XYLocation(5, 4));
		board.addQueenAt(new XYLocation(6, 7));
		board.addQueenAt(new XYLocation(7, 7));
		assertEquals(" -  -  -  -  -  -  -  - \n"
				+ " -  -  Q  -  -  -  -  - \n" + " -  -  -  -  -  -  -  - \n"
				+ " -  -  -  Q  -  -  -  - \n" + " -  -  -  -  -  Q  -  - \n"
				+ " Q  -  -  -  -  -  -  - \n" + " -  Q  -  -  Q  -  -  - \n"
				+ " -  -  -  -  -  -  Q  Q \n", board.getBoardPic());

		assertEquals("--------\n" + "--Q-----\n" + "--------\n" + "---Q----\n"
				+ "-----Q--\n" + "Q-------\n" + "-Q--Q---\n" + "------QQ\n",
				board.toString());
	}

}
