package aima.test.core.unit.environment.eightpuzzle;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.environment.eightpuzzle.EightPuzzleBoard;

/**
 * @author Ravi Mohan
 * 
 */
public class EightPuzzleBoardMoveTest {
	EightPuzzleBoard board;

	@Before
	public void setUp() {
		board = new EightPuzzleBoard(new int[] { 0, 5, 4, 6, 1, 8, 7, 3, 2 });
	}

	// Position 1
	@Test
	public void testPosition1Movabilty() {
		Assert.assertEquals(false, board.canMoveGap(EightPuzzleBoard.UP));
		Assert.assertEquals(true, board.canMoveGap(EightPuzzleBoard.DOWN));
		Assert.assertEquals(false, board.canMoveGap(EightPuzzleBoard.LEFT));
		Assert.assertEquals(true, board.canMoveGap(EightPuzzleBoard.RIGHT));
	}

	@Test
	public void testPosition1MoveUp() {
		board.moveGapUp();
		Assert.assertEquals(new EightPuzzleBoard(new int[] { 0, 5, 4, 6, 1, 8,
				7, 3, 2 }), board);
	}

	@Test
	public void testPosition1MoveDown() {
		board.moveGapDown();
		Assert.assertEquals(new EightPuzzleBoard(new int[] { 6, 5, 4, 0, 1, 8,
				7, 3, 2 }), board);
	}

	@Test
	public void testPosition1MoveLeft() {
		board.moveGapLeft();
		Assert.assertEquals(new EightPuzzleBoard(new int[] { 0, 5, 4, 6, 1, 8,
				7, 3, 2 }), board);
	}

	@Test
	public void testPosition1MoveRight() {
		board.moveGapRight();
		Assert.assertEquals(new EightPuzzleBoard(new int[] { 5, 0, 4, 6, 1, 8,
				7, 3, 2 }), board);
	}

	// Position 2
	@Test
	public void testPosition2Movabilty() {
		setGapToPosition2();
		Assert.assertEquals(false, board.canMoveGap(EightPuzzleBoard.UP));
		Assert.assertEquals(true, board.canMoveGap(EightPuzzleBoard.DOWN));
		Assert.assertEquals(true, board.canMoveGap(EightPuzzleBoard.LEFT));
		Assert.assertEquals(true, board.canMoveGap(EightPuzzleBoard.RIGHT));
	}

	@Test
	public void testPosition2MoveUp() {
		// { 5, 0, 4, 6, 1, 8, 7, 3, 2 }
		setGapToPosition2();
		board.moveGapUp();
		Assert.assertEquals(new EightPuzzleBoard(new int[] { 5, 0, 4, 6, 1, 8,
				7, 3, 2 }), board);
	}

	@Test
	public void testPosition2MoveDown() {
		// { 5, 0, 4, 6, 1, 8, 7, 3, 2 }
		setGapToPosition2();
		board.moveGapDown();
		Assert.assertEquals(new EightPuzzleBoard(new int[] { 5, 1, 4, 6, 0, 8,
				7, 3, 2 }), board);
	}

	@Test
	public void testPosition2MoveLeft() {
		// { 5, 0, 4, 6, 1, 8, 7, 3, 2 }
		setGapToPosition2();
		board.moveGapLeft();
		Assert.assertEquals(new EightPuzzleBoard(new int[] { 0, 5, 4, 6, 1, 8,
				7, 3, 2 }), board);
	}

	@Test
	public void testPosition2MoveRight() {
		// { 5, 0, 4, 6, 1, 8, 7, 3, 2 }
		setGapToPosition2();
		board.moveGapRight();
		Assert.assertEquals(new EightPuzzleBoard(new int[] { 5, 4, 0, 6, 1, 8,
				7, 3, 2 }), board);
	}

	// Position 3
	@Test
	public void testPosition3Movabilty() {
		setGapToPosition3();
		Assert.assertEquals(false, board.canMoveGap(EightPuzzleBoard.UP));
		Assert.assertEquals(true, board.canMoveGap(EightPuzzleBoard.DOWN));
		Assert.assertEquals(true, board.canMoveGap(EightPuzzleBoard.LEFT));
		Assert.assertEquals(false, board.canMoveGap(EightPuzzleBoard.RIGHT));
	}

	@Test
	public void testPosition3MoveUp() {
		// { 5, 4, 0, 6, 1, 8, 7, 3, 2 }
		setGapToPosition3();
		board.moveGapUp();
		Assert.assertEquals(new EightPuzzleBoard(new int[] { 5, 4, 0, 6, 1, 8,
				7, 3, 2 }), board);
	}

	@Test
	public void testPosition3MoveDown() {
		// { 5, 4, 0, 6, 1, 8, 7, 3, 2 }
		setGapToPosition3();
		board.moveGapDown();
		Assert.assertEquals(new EightPuzzleBoard(new int[] { 5, 4, 8, 6, 1, 0,
				7, 3, 2 }), board);
	}

	@Test
	public void testPosition3MoveLeft() {
		// { 5, 4, 0, 6, 1, 8, 7, 3, 2 }
		setGapToPosition3();
		board.moveGapLeft();
		Assert.assertEquals(new EightPuzzleBoard(new int[] { 5, 0, 4, 6, 1, 8,
				7, 3, 2 }), board);
	}

	@Test
	public void testPosition3MoveRight() {
		// { 5, 4, 0, 6, 1, 8, 7, 3, 2 }
		setGapToPosition3();
		board.moveGapRight();
		Assert.assertEquals(new EightPuzzleBoard(new int[] { 5, 4, 0, 6, 1, 8,
				7, 3, 2 }), board);
	}

	// Position 4
	@Test
	public void testPosition4Movabilty() {
		setGapToPosition4();
		Assert.assertEquals(true, board.canMoveGap(EightPuzzleBoard.UP));
		Assert.assertEquals(true, board.canMoveGap(EightPuzzleBoard.DOWN));
		Assert.assertEquals(false, board.canMoveGap(EightPuzzleBoard.LEFT));
		Assert.assertEquals(true, board.canMoveGap(EightPuzzleBoard.RIGHT));
	}

	@Test
	public void testPosition4MoveUp() {
		// { 6, 5, 4, 0, 1, 8, 7, 3, 2 }
		setGapToPosition4();
		board.moveGapUp();
		Assert.assertEquals(new EightPuzzleBoard(new int[] { 0, 5, 4, 6, 1, 8,
				7, 3, 2 }), board);
	}

	@Test
	public void testPosition4MoveDown() {
		// { 6, 5, 4, 0, 1, 8, 7, 3, 2 }
		setGapToPosition4();
		board.moveGapDown();
		Assert.assertEquals(new EightPuzzleBoard(new int[] { 6, 5, 4, 7, 1, 8,
				0, 3, 2 }), board);
	}

	@Test
	public void testPosition4MoveLeft() {
		// { 6, 5, 4, 0, 1, 8, 7, 3, 2 }
		setGapToPosition4();
		board.moveGapLeft();
		Assert.assertEquals(new EightPuzzleBoard(new int[] { 6, 5, 4, 0, 1, 8,
				7, 3, 2 }), board);
	}

	@Test
	public void testPosition4MoveRight() {
		// { 6, 5, 4, 0, 1, 8, 7, 3, 2 }
		setGapToPosition4();
		board.moveGapRight();
		Assert.assertEquals(new EightPuzzleBoard(new int[] { 6, 5, 4, 1, 0, 8,
				7, 3, 2 }), board);
	}

	// Position 5
	@Test
	public void testPosition5Movabilty() {
		setGapToPosition5();
		Assert.assertEquals(true, board.canMoveGap(EightPuzzleBoard.UP));
		Assert.assertEquals(true, board.canMoveGap(EightPuzzleBoard.DOWN));
		Assert.assertEquals(true, board.canMoveGap(EightPuzzleBoard.LEFT));
		Assert.assertEquals(true, board.canMoveGap(EightPuzzleBoard.RIGHT));
	}

	@Test
	public void testPosition5MoveUp() {
		// { 6, 5, 4, 1, 0, 8, 7, 3, 2 }
		setGapToPosition5();
		board.moveGapUp();
		Assert.assertEquals(new EightPuzzleBoard(new int[] { 6, 0, 4, 1, 5, 8,
				7, 3, 2 }), board);
	}

	@Test
	public void testPosition5MoveDown() {
		// { 6, 5, 4, 1, 0, 8, 7, 3, 2 }
		setGapToPosition5();
		board.moveGapDown();
		Assert.assertEquals(new EightPuzzleBoard(new int[] { 6, 5, 4, 1, 3, 8,
				7, 0, 2 }), board);
	}

	@Test
	public void testPosition5MoveLeft() {
		// { 6, 5, 4, 1, 0, 8, 7, 3, 2 }
		setGapToPosition5();
		board.moveGapLeft();
		Assert.assertEquals(new EightPuzzleBoard(new int[] { 6, 5, 4, 0, 1, 8,
				7, 3, 2 }), board);
	}

	@Test
	public void testPosition5MoveRight() {
		// { 6, 5, 4, 1, 0, 8, 7, 3, 2 }
		setGapToPosition5();
		board.moveGapRight();
		Assert.assertEquals(new EightPuzzleBoard(new int[] { 6, 5, 4, 1, 8, 0,
				7, 3, 2 }), board);
	}

	// Position 6
	@Test
	public void testPosition6Movabilty() {
		setGapToPosition6();
		Assert.assertEquals(true, board.canMoveGap(EightPuzzleBoard.UP));
		Assert.assertEquals(true, board.canMoveGap(EightPuzzleBoard.DOWN));
		Assert.assertEquals(true, board.canMoveGap(EightPuzzleBoard.LEFT));
		Assert.assertEquals(false, board.canMoveGap(EightPuzzleBoard.RIGHT));
	}

	@Test
	public void testPosition6MoveUp() {
		// { 6, 5, 4, 1, 8, 0, 7, 3, 2 }
		setGapToPosition6();
		board.moveGapUp();
		Assert.assertEquals(new EightPuzzleBoard(new int[] { 6, 5, 0, 1, 8, 4,
				7, 3, 2 }), board);
	}

	@Test
	public void testPosition6MoveDown() {
		// { 6, 5, 4, 1, 8, 0, 7, 3, 2 }
		setGapToPosition6();
		board.moveGapDown();
		Assert.assertEquals(new EightPuzzleBoard(new int[] { 6, 5, 4, 1, 8, 2,
				7, 3, 0 }), board);
	}

	@Test
	public void testPosition6MoveLeft() {
		// { 6, 5, 4, 1, 8, 0, 7, 3, 2 }
		setGapToPosition6();
		board.moveGapLeft();
		Assert.assertEquals(new EightPuzzleBoard(new int[] { 6, 5, 4, 1, 0, 8,
				7, 3, 2 }), board);
	}

	@Test
	public void testPosition6MoveRight() {
		// { 6, 5, 4, 1, 8, 0, 7, 3, 2 }
		setGapToPosition6();
		board.moveGapRight();
		Assert.assertEquals(new EightPuzzleBoard(new int[] { 6, 5, 4, 1, 8, 0,
				7, 3, 2 }), board);
	}

	// Position 7
	@Test
	public void testPosition7Movabilty() {
		setGapToPosition7();
		Assert.assertEquals(true, board.canMoveGap(EightPuzzleBoard.UP));
		Assert.assertEquals(false, board.canMoveGap(EightPuzzleBoard.DOWN));
		Assert.assertEquals(false, board.canMoveGap(EightPuzzleBoard.LEFT));
		Assert.assertEquals(true, board.canMoveGap(EightPuzzleBoard.RIGHT));
	}

	@Test
	public void testPosition7MoveUp() {
		// { 6, 5, 4, 7, 1, 8, 0, 3, 2 }
		setGapToPosition7();
		board.moveGapUp();
		Assert.assertEquals(new EightPuzzleBoard(new int[] { 6, 5, 4, 0, 1, 8,
				7, 3, 2 }), board);
	}

	@Test
	public void testPosition7MoveDown() {
		// { 6, 5, 4, 7, 1, 8, 0, 3, 2 }
		setGapToPosition7();
		board.moveGapDown();
		Assert.assertEquals(new EightPuzzleBoard(new int[] { 6, 5, 4, 7, 1, 8,
				0, 3, 2 }), board);
	}

	@Test
	public void testPosition7MoveLeft() {
		// { 6, 5, 4, 7, 1, 8, 0, 3, 2 }
		setGapToPosition7();
		board.moveGapLeft();
		Assert.assertEquals(new EightPuzzleBoard(new int[] { 6, 5, 4, 7, 1, 8,
				0, 3, 2 }), board);
	}

	@Test
	public void testPosition7MoveRight() {
		// { 6, 5, 4, 7, 1, 8, 0, 3, 2 }
		setGapToPosition7();
		board.moveGapRight();
		Assert.assertEquals(new EightPuzzleBoard(new int[] { 6, 5, 4, 7, 1, 8,
				3, 0, 2 }), board);
	}

	// Position 8
	@Test
	public void testPosition8Movabilty() {
		setGapToPosition8();
		Assert.assertEquals(true, board.canMoveGap(EightPuzzleBoard.UP));
		Assert.assertEquals(false, board.canMoveGap(EightPuzzleBoard.DOWN));
		Assert.assertEquals(true, board.canMoveGap(EightPuzzleBoard.LEFT));
		Assert.assertEquals(true, board.canMoveGap(EightPuzzleBoard.RIGHT));
	}

	@Test
	public void testPosition8MoveUp() {
		// { 6, 5, 4, 7, 1, 8, 3, 0, 2 }
		setGapToPosition8();
		board.moveGapUp();
		Assert.assertEquals(new EightPuzzleBoard(new int[] { 6, 5, 4, 7, 0, 8,
				3, 1, 2 }), board);
	}

	@Test
	public void testPosition8MoveDown() {
		// { 6, 5, 4, 7, 1, 8, 3, 0, 2 }
		setGapToPosition8();
		board.moveGapDown();
		Assert.assertEquals(new EightPuzzleBoard(new int[] { 6, 5, 4, 7, 1, 8,
				3, 0, 2 }), board);
	}

	@Test
	public void testPosition8MoveLeft() {
		// { 6, 5, 4, 7, 1, 8, 3, 0, 2 }
		setGapToPosition8();
		board.moveGapLeft();
		Assert.assertEquals(new EightPuzzleBoard(new int[] { 6, 5, 4, 7, 1, 8,
				0, 3, 2 }), board);
	}

	@Test
	public void testPosition8MoveRight() {
		// { 6, 5, 4, 7, 1, 8, 3, 0, 2 }
		setGapToPosition8();
		board.moveGapRight();
		Assert.assertEquals(new EightPuzzleBoard(new int[] { 6, 5, 4, 7, 1, 8,
				3, 2, 0 }), board);
	}

	// Position 9
	@Test
	public void testPosition9Movabilty() {
		setGapToPosition9();
		Assert.assertEquals(true, board.canMoveGap(EightPuzzleBoard.UP));
		Assert.assertEquals(false, board.canMoveGap(EightPuzzleBoard.DOWN));
		Assert.assertEquals(true, board.canMoveGap(EightPuzzleBoard.LEFT));
		Assert.assertEquals(false, board.canMoveGap(EightPuzzleBoard.RIGHT));
	}

	@Test
	public void testPosition9MoveUp() {
		// { 6, 5, 4, 7, 1, 8, 3, 2, 0 }
		setGapToPosition9();
		board.moveGapUp();
		Assert.assertEquals(new EightPuzzleBoard(new int[] { 6, 5, 4, 7, 1, 0,
				3, 2, 8 }), board);
	}

	@Test
	public void testPosition9MoveDown() {
		// { 6, 5, 4, 7, 1, 8, 3, 2, 0 }
		setGapToPosition9();
		board.moveGapDown();
		Assert.assertEquals(new EightPuzzleBoard(new int[] { 6, 5, 4, 7, 1, 8,
				3, 2, 0 }), board);
	}

	@Test
	public void testPosition9MoveLeft() {
		// { 6, 5, 4, 7, 1, 8, 3, 2, 0 }
		setGapToPosition9();
		board.moveGapLeft();
		Assert.assertEquals(new EightPuzzleBoard(new int[] { 6, 5, 4, 7, 1, 8,
				3, 0, 2 }), board);
	}

	@Test
	public void testPosition9MoveRight() {
		// { 6, 5, 4, 7, 1, 8, 3, 2, 0 }
		setGapToPosition9();
		board.moveGapRight();
		Assert.assertEquals(new EightPuzzleBoard(new int[] { 6, 5, 4, 7, 1, 8,
				3, 2, 0 }), board);
	}

	//
	// PRIVATE METHODS
	//
	private void setGapToPosition2() {
		board.moveGapRight();
	}

	private void setGapToPosition3() {
		board.moveGapRight();
		board.moveGapRight();
	}

	private void setGapToPosition4() {
		board.moveGapDown();
	}

	private void setGapToPosition5() {
		// { 6, 5, 4, 1, 0, 8, 7, 3, 2 }
		board.moveGapDown();
		board.moveGapRight();
	}

	private void setGapToPosition6() {
		// { 6, 5, 4, 1, 8, 0, 7, 3, 2 }
		board.moveGapDown();
		board.moveGapRight();
		board.moveGapRight();
	}

	private void setGapToPosition7() {
		// { 6, 5, 4, 7, 1, 8, 0, 3, 2 }
		board.moveGapDown();
		board.moveGapDown();
	}

	private void setGapToPosition8() {
		// { 6, 5, 4, 7, 1, 8, 3, 0, 2 }
		board.moveGapDown();
		board.moveGapDown();
		board.moveGapRight();
	}

	private void setGapToPosition9() {
		// { 6, 5, 4, 7, 1, 8, 3, 2, 0 }
		board.moveGapDown();
		board.moveGapDown();
		board.moveGapRight();
		board.moveGapRight();
	}
}
