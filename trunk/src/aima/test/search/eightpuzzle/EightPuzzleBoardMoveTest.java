package aima.test.search.eightpuzzle;

import junit.framework.TestCase;
import aima.search.eightpuzzle.EightPuzzleBoard;

/**
 * @author Ravi Mohan
 * 
 */

public class EightPuzzleBoardMoveTest extends TestCase {
	EightPuzzleBoard board;

	@Override
	public void setUp() {
		board = new EightPuzzleBoard(new int[] { 0, 5, 4, 6, 1, 8, 7, 3, 2 });
	}

	// Position 1
	public void testPosition1Movabilty() {
		assertEquals(false, board.canMoveGap(EightPuzzleBoard.UP));
		assertEquals(true, board.canMoveGap(EightPuzzleBoard.DOWN));
		assertEquals(false, board.canMoveGap(EightPuzzleBoard.LEFT));
		assertEquals(true, board.canMoveGap(EightPuzzleBoard.RIGHT));
	}

	public void testPosition1MoveUp() {
		board.moveGapUp();
		assertEquals(new EightPuzzleBoard(
				new int[] { 0, 5, 4, 6, 1, 8, 7, 3, 2 }), board);
	}

	public void testPosition1MoveDown() {
		board.moveGapDown();
		assertEquals(new EightPuzzleBoard(
				new int[] { 6, 5, 4, 0, 1, 8, 7, 3, 2 }), board);
	}

	public void testPosition1MoveLeft() {
		board.moveGapLeft();
		assertEquals(new EightPuzzleBoard(
				new int[] { 0, 5, 4, 6, 1, 8, 7, 3, 2 }), board);
	}

	public void testPosition1MoveRight() {
		board.moveGapRight();
		assertEquals(new EightPuzzleBoard(
				new int[] { 5, 0, 4, 6, 1, 8, 7, 3, 2 }), board);
	}

	// Position 2
	public void testPosition2Movabilty() {
		setGapToPosition2();
		assertEquals(false, board.canMoveGap(EightPuzzleBoard.UP));
		assertEquals(true, board.canMoveGap(EightPuzzleBoard.DOWN));
		assertEquals(true, board.canMoveGap(EightPuzzleBoard.LEFT));
		assertEquals(true, board.canMoveGap(EightPuzzleBoard.RIGHT));
	}

	public void testPosition2MoveUp() {
		// { 5, 0, 4, 6, 1, 8, 7, 3, 2 }
		setGapToPosition2();
		board.moveGapUp();
		assertEquals(new EightPuzzleBoard(
				new int[] { 5, 0, 4, 6, 1, 8, 7, 3, 2 }), board);
	}

	public void testPosition2MoveDown() {
		// { 5, 0, 4, 6, 1, 8, 7, 3, 2 }
		setGapToPosition2();
		board.moveGapDown();
		assertEquals(new EightPuzzleBoard(
				new int[] { 5, 1, 4, 6, 0, 8, 7, 3, 2 }), board);
	}

	public void testPosition2MoveLeft() {
		// { 5, 0, 4, 6, 1, 8, 7, 3, 2 }
		setGapToPosition2();
		board.moveGapLeft();
		assertEquals(new EightPuzzleBoard(
				new int[] { 0, 5, 4, 6, 1, 8, 7, 3, 2 }), board);
	}

	public void testPosition2MoveRight() {
		// { 5, 0, 4, 6, 1, 8, 7, 3, 2 }
		setGapToPosition2();
		board.moveGapRight();
		assertEquals(new EightPuzzleBoard(
				new int[] { 5, 4, 0, 6, 1, 8, 7, 3, 2 }), board);
	}

	// Position 3
	public void testPosition3Movabilty() {
		setGapToPosition3();
		assertEquals(false, board.canMoveGap(EightPuzzleBoard.UP));
		assertEquals(true, board.canMoveGap(EightPuzzleBoard.DOWN));
		assertEquals(true, board.canMoveGap(EightPuzzleBoard.LEFT));
		assertEquals(false, board.canMoveGap(EightPuzzleBoard.RIGHT));
	}

	public void testPosition3MoveUp() {
		// { 5, 4, 0, 6, 1, 8, 7, 3, 2 }
		setGapToPosition3();
		board.moveGapUp();
		assertEquals(new EightPuzzleBoard(
				new int[] { 5, 4, 0, 6, 1, 8, 7, 3, 2 }), board);
	}

	public void testPosition3MoveDown() {
		// { 5, 4, 0, 6, 1, 8, 7, 3, 2 }
		setGapToPosition3();
		board.moveGapDown();
		assertEquals(new EightPuzzleBoard(
				new int[] { 5, 4, 8, 6, 1, 0, 7, 3, 2 }), board);
	}

	public void testPosition3MoveLeft() {
		// { 5, 4, 0, 6, 1, 8, 7, 3, 2 }
		setGapToPosition3();
		board.moveGapLeft();
		assertEquals(new EightPuzzleBoard(
				new int[] { 5, 0, 4, 6, 1, 8, 7, 3, 2 }), board);
	}

	public void testPosition3MoveRight() {
		// { 5, 4, 0, 6, 1, 8, 7, 3, 2 }
		setGapToPosition3();
		board.moveGapRight();
		assertEquals(new EightPuzzleBoard(
				new int[] { 5, 4, 0, 6, 1, 8, 7, 3, 2 }), board);
	}

	// Position 4
	public void testPosition4Movabilty() {
		setGapToPosition4();
		assertEquals(true, board.canMoveGap(EightPuzzleBoard.UP));
		assertEquals(true, board.canMoveGap(EightPuzzleBoard.DOWN));
		assertEquals(false, board.canMoveGap(EightPuzzleBoard.LEFT));
		assertEquals(true, board.canMoveGap(EightPuzzleBoard.RIGHT));
	}

	public void testPosition4MoveUp() {
		// { 6, 5, 4, 0, 1, 8, 7, 3, 2 }
		setGapToPosition4();
		board.moveGapUp();
		assertEquals(new EightPuzzleBoard(
				new int[] { 0, 5, 4, 6, 1, 8, 7, 3, 2 }), board);
	}

	public void testPosition4MoveDown() {
		// { 6, 5, 4, 0, 1, 8, 7, 3, 2 }
		setGapToPosition4();
		board.moveGapDown();
		assertEquals(new EightPuzzleBoard(
				new int[] { 6, 5, 4, 7, 1, 8, 0, 3, 2 }), board);
	}

	public void testPosition4MoveLeft() {
		// { 6, 5, 4, 0, 1, 8, 7, 3, 2 }
		setGapToPosition4();
		board.moveGapLeft();
		assertEquals(new EightPuzzleBoard(
				new int[] { 6, 5, 4, 0, 1, 8, 7, 3, 2 }), board);
	}

	public void testPosition4MoveRight() {
		// { 6, 5, 4, 0, 1, 8, 7, 3, 2 }
		setGapToPosition4();
		board.moveGapRight();
		assertEquals(new EightPuzzleBoard(
				new int[] { 6, 5, 4, 1, 0, 8, 7, 3, 2 }), board);
	}

	// Position 5
	public void testPosition5Movabilty() {
		setGapToPosition5();
		assertEquals(true, board.canMoveGap(EightPuzzleBoard.UP));
		assertEquals(true, board.canMoveGap(EightPuzzleBoard.DOWN));
		assertEquals(true, board.canMoveGap(EightPuzzleBoard.LEFT));
		assertEquals(true, board.canMoveGap(EightPuzzleBoard.RIGHT));
	}

	public void testPosition5MoveUp() {
		// { 6, 5, 4, 1, 0, 8, 7, 3, 2 }
		setGapToPosition5();
		board.moveGapUp();
		assertEquals(new EightPuzzleBoard(
				new int[] { 6, 0, 4, 1, 5, 8, 7, 3, 2 }), board);
	}

	public void testPosition5MoveDown() {
		// { 6, 5, 4, 1, 0, 8, 7, 3, 2 }
		setGapToPosition5();
		board.moveGapDown();
		assertEquals(new EightPuzzleBoard(
				new int[] { 6, 5, 4, 1, 3, 8, 7, 0, 2 }), board);
	}

	public void testPosition5MoveLeft() {
		// { 6, 5, 4, 1, 0, 8, 7, 3, 2 }
		setGapToPosition5();
		board.moveGapLeft();
		assertEquals(new EightPuzzleBoard(
				new int[] { 6, 5, 4, 0, 1, 8, 7, 3, 2 }), board);
	}

	public void testPosition5MoveRight() {
		// { 6, 5, 4, 1, 0, 8, 7, 3, 2 }
		setGapToPosition5();
		board.moveGapRight();
		assertEquals(new EightPuzzleBoard(
				new int[] { 6, 5, 4, 1, 8, 0, 7, 3, 2 }), board);
	}

	// Position 6
	public void testPosition6Movabilty() {
		setGapToPosition6();
		assertEquals(true, board.canMoveGap(EightPuzzleBoard.UP));
		assertEquals(true, board.canMoveGap(EightPuzzleBoard.DOWN));
		assertEquals(true, board.canMoveGap(EightPuzzleBoard.LEFT));
		assertEquals(false, board.canMoveGap(EightPuzzleBoard.RIGHT));
	}

	public void testPosition6MoveUp() {
		// { 6, 5, 4, 1, 8, 0, 7, 3, 2 }
		setGapToPosition6();
		board.moveGapUp();
		assertEquals(new EightPuzzleBoard(
				new int[] { 6, 5, 0, 1, 8, 4, 7, 3, 2 }), board);
	}

	public void testPosition6MoveDown() {
		// { 6, 5, 4, 1, 8, 0, 7, 3, 2 }
		setGapToPosition6();
		board.moveGapDown();
		assertEquals(new EightPuzzleBoard(
				new int[] { 6, 5, 4, 1, 8, 2, 7, 3, 0 }), board);
	}

	public void testPosition6MoveLeft() {
		// { 6, 5, 4, 1, 8, 0, 7, 3, 2 }
		setGapToPosition6();
		board.moveGapLeft();
		assertEquals(new EightPuzzleBoard(
				new int[] { 6, 5, 4, 1, 0, 8, 7, 3, 2 }), board);
	}

	public void testPosition6MoveRight() {
		// { 6, 5, 4, 1, 8, 0, 7, 3, 2 }
		setGapToPosition6();
		board.moveGapRight();
		assertEquals(new EightPuzzleBoard(
				new int[] { 6, 5, 4, 1, 8, 0, 7, 3, 2 }), board);
	}

	// Position 7
	public void testPosition7Movabilty() {
		setGapToPosition7();
		assertEquals(true, board.canMoveGap(EightPuzzleBoard.UP));
		assertEquals(false, board.canMoveGap(EightPuzzleBoard.DOWN));
		assertEquals(false, board.canMoveGap(EightPuzzleBoard.LEFT));
		assertEquals(true, board.canMoveGap(EightPuzzleBoard.RIGHT));
	}

	public void testPosition7MoveUp() {
		// { 6, 5, 4, 7, 1, 8, 0, 3, 2 }
		setGapToPosition7();
		board.moveGapUp();
		assertEquals(new EightPuzzleBoard(
				new int[] { 6, 5, 4, 0, 1, 8, 7, 3, 2 }), board);
	}

	public void testPosition7MoveDown() {
		// { 6, 5, 4, 7, 1, 8, 0, 3, 2 }
		setGapToPosition7();
		board.moveGapDown();
		assertEquals(new EightPuzzleBoard(
				new int[] { 6, 5, 4, 7, 1, 8, 0, 3, 2 }), board);
	}

	public void testPosition7MoveLeft() {
		// { 6, 5, 4, 7, 1, 8, 0, 3, 2 }
		setGapToPosition7();
		board.moveGapLeft();
		assertEquals(new EightPuzzleBoard(
				new int[] { 6, 5, 4, 7, 1, 8, 0, 3, 2 }), board);
	}

	public void testPosition7MoveRight() {
		// { 6, 5, 4, 7, 1, 8, 0, 3, 2 }
		setGapToPosition7();
		board.moveGapRight();
		assertEquals(new EightPuzzleBoard(
				new int[] { 6, 5, 4, 7, 1, 8, 3, 0, 2 }), board);
	}

	// Position 8
	public void testPosition8Movabilty() {
		setGapToPosition8();
		assertEquals(true, board.canMoveGap(EightPuzzleBoard.UP));
		assertEquals(false, board.canMoveGap(EightPuzzleBoard.DOWN));
		assertEquals(true, board.canMoveGap(EightPuzzleBoard.LEFT));
		assertEquals(true, board.canMoveGap(EightPuzzleBoard.RIGHT));
	}

	public void testPosition8MoveUp() {
		// { 6, 5, 4, 7, 1, 8, 3, 0, 2 }
		setGapToPosition8();
		board.moveGapUp();
		assertEquals(new EightPuzzleBoard(
				new int[] { 6, 5, 4, 7, 0, 8, 3, 1, 2 }), board);
	}

	public void testPosition8MoveDown() {
		// { 6, 5, 4, 7, 1, 8, 3, 0, 2 }
		setGapToPosition8();
		board.moveGapDown();
		assertEquals(new EightPuzzleBoard(
				new int[] { 6, 5, 4, 7, 1, 8, 3, 0, 2 }), board);
	}

	public void testPosition8MoveLeft() {
		// { 6, 5, 4, 7, 1, 8, 3, 0, 2 }
		setGapToPosition8();
		board.moveGapLeft();
		assertEquals(new EightPuzzleBoard(
				new int[] { 6, 5, 4, 7, 1, 8, 0, 3, 2 }), board);
	}

	public void testPosition8MoveRight() {
		// { 6, 5, 4, 7, 1, 8, 3, 0, 2 }
		setGapToPosition8();
		board.moveGapRight();
		assertEquals(new EightPuzzleBoard(
				new int[] { 6, 5, 4, 7, 1, 8, 3, 2, 0 }), board);
	}

	// Position 9
	public void testPosition9Movabilty() {
		setGapToPosition9();
		assertEquals(true, board.canMoveGap(EightPuzzleBoard.UP));
		assertEquals(false, board.canMoveGap(EightPuzzleBoard.DOWN));
		assertEquals(true, board.canMoveGap(EightPuzzleBoard.LEFT));
		assertEquals(false, board.canMoveGap(EightPuzzleBoard.RIGHT));
	}

	public void testPosition9MoveUp() {
		// { 6, 5, 4, 7, 1, 8, 3, 2, 0 }
		setGapToPosition9();
		board.moveGapUp();
		assertEquals(new EightPuzzleBoard(
				new int[] { 6, 5, 4, 7, 1, 0, 3, 2, 8 }), board);
	}

	public void testPosition9MoveDown() {
		// { 6, 5, 4, 7, 1, 8, 3, 2, 0 }
		setGapToPosition9();
		board.moveGapDown();
		assertEquals(new EightPuzzleBoard(
				new int[] { 6, 5, 4, 7, 1, 8, 3, 2, 0 }), board);
	}

	public void testPosition9MoveLeft() {
		// { 6, 5, 4, 7, 1, 8, 3, 2, 0 }
		setGapToPosition9();
		board.moveGapLeft();
		assertEquals(new EightPuzzleBoard(
				new int[] { 6, 5, 4, 7, 1, 8, 3, 0, 2 }), board);
	}

	public void testPosition9MoveRight() {
		// { 6, 5, 4, 7, 1, 8, 3, 2, 0 }
		setGapToPosition9();
		board.moveGapRight();
		assertEquals(new EightPuzzleBoard(
				new int[] { 6, 5, 4, 7, 1, 8, 3, 2, 0 }), board);
	}

	public void setGapToPosition2() {
		board.moveGapRight();
	}

	public void setGapToPosition3() {
		board.moveGapRight();
		board.moveGapRight();
	}

	public void setGapToPosition4() {
		board.moveGapDown();
	}

	public void setGapToPosition5() {
		// { 6, 5, 4, 1, 0, 8, 7, 3, 2 }
		board.moveGapDown();
		board.moveGapRight();
	}

	public void setGapToPosition6() {
		// { 6, 5, 4, 1, 8, 0, 7, 3, 2 }
		board.moveGapDown();
		board.moveGapRight();
		board.moveGapRight();
	}

	public void setGapToPosition7() {
		// { 6, 5, 4, 7, 1, 8, 0, 3, 2 }
		board.moveGapDown();
		board.moveGapDown();
	}

	public void setGapToPosition8() {
		// { 6, 5, 4, 7, 1, 8, 3, 0, 2 }
		board.moveGapDown();
		board.moveGapDown();
		board.moveGapRight();
	}

	public void setGapToPosition9() {
		// { 6, 5, 4, 7, 1, 8, 3, 2, 0 }
		board.moveGapDown();
		board.moveGapDown();
		board.moveGapRight();
		board.moveGapRight();
	}

}
