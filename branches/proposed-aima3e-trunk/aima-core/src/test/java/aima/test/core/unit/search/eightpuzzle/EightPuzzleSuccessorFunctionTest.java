package aima.test.core.unit.search.eightpuzzle;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.search.eightpuzzle.EightPuzzleBoard;
import aima.core.search.eightpuzzle.EightPuzzleSuccessorFunction;
import aima.core.search.framework.Successor;

/**
 * @author Ravi Mohan
 * 
 */
public class EightPuzzleSuccessorFunctionTest {
	EightPuzzleBoard board;

	EightPuzzleSuccessorFunction func = new EightPuzzleSuccessorFunction();

	@Before
	public void setUp() {
		board = new EightPuzzleBoard(new int[] { 1, 2, 5, 3, 4, 0, 6, 7, 8 });
	}

	@Test
	public void testGenerateCorrect3successors() {
		List successors = func.getSuccessors(board);
		Assert.assertEquals(3, successors.size());

		// test first successor
		EightPuzzleBoard expectedFirst = new EightPuzzleBoard(new int[] { 1, 2,
				0, 3, 4, 5, 6, 7, 8 });
		EightPuzzleBoard actualFirst = (EightPuzzleBoard) ((Successor) successors
				.get(0)).getState();
		Assert.assertEquals(expectedFirst, actualFirst);
		Assert.assertEquals(EightPuzzleBoard.UP,
				((Successor) successors.get(0)).getAction());

		// test second successor
		EightPuzzleBoard expectedSecond = new EightPuzzleBoard(new int[] { 1,
				2, 5, 3, 4, 8, 6, 7, 0 });
		EightPuzzleBoard actualSecond = (EightPuzzleBoard) ((Successor) successors
				.get(1)).getState();
		Assert.assertEquals(expectedSecond, actualSecond);
		Assert.assertEquals(EightPuzzleBoard.DOWN, ((Successor) successors
				.get(1)).getAction());

		// test third successor
		EightPuzzleBoard expectedThird = new EightPuzzleBoard(new int[] { 1, 2,
				5, 3, 0, 4, 6, 7, 8 });
		EightPuzzleBoard actualThird = (EightPuzzleBoard) ((Successor) successors
				.get(2)).getState();
		Assert.assertEquals(expectedThird, actualThird);
		Assert.assertEquals(EightPuzzleBoard.LEFT, ((Successor) successors
				.get(2)).getAction());
	}

	@Test
	public void testGenerateCorrectWhenGapMovedRightward() {
		board.moveGapLeft();// gives { 1, 2, 5, 3, 0, 4, 6, 7, 8 }
		Assert.assertEquals(new EightPuzzleBoard(new int[] { 1, 2, 5, 3, 0, 4,
				6, 7, 8 }), board);
		List successors = func.getSuccessors(board);
		Assert.assertEquals(4, successors.size());
		EightPuzzleBoard expectedFourth = new EightPuzzleBoard(new int[] { 1,
				2, 5, 3, 4, 0, 6, 7, 8 });
		EightPuzzleBoard actualFourth = (EightPuzzleBoard) ((Successor) successors
				.get(3)).getState();
		Assert.assertEquals(expectedFourth, actualFourth);
		Assert.assertEquals(EightPuzzleBoard.RIGHT, ((Successor) successors
				.get(3)).getAction());
	}
}
