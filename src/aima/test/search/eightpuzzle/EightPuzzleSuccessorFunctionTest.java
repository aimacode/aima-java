/*
 * Created on Sep 12, 2004
 *
 */
package aima.test.search.eightpuzzle;

import java.util.List;

import junit.framework.TestCase;
import aima.search.eightpuzzle.EightPuzzleBoard;
import aima.search.eightpuzzle.EightPuzzleSuccessorFunction;
import aima.search.framework.Successor;

/**
 * @author Ravi Mohan
 * 
 */

public class EightPuzzleSuccessorFunctionTest extends TestCase {
	EightPuzzleBoard board;

	EightPuzzleSuccessorFunction func = new EightPuzzleSuccessorFunction();

	@Override
	public void setUp() {
		board = new EightPuzzleBoard(new int[] { 1, 2, 5, 3, 4, 0, 6, 7, 8 });
	}

	public void testGenerateCorrect3successors() {
		List successors = func.getSuccessors(board);
		assertEquals(3, successors.size());

		// test first successor
		EightPuzzleBoard expectedFirst = new EightPuzzleBoard(new int[] { 1, 2,
				0, 3, 4, 5, 6, 7, 8 });
		EightPuzzleBoard actualFirst = (EightPuzzleBoard) ((Successor) successors
				.get(0)).getState();
		assertEquals(expectedFirst, actualFirst);
		assertEquals(EightPuzzleBoard.UP, ((Successor) successors.get(0))
				.getAction());

		// test second successor
		EightPuzzleBoard expectedSecond = new EightPuzzleBoard(new int[] { 1,
				2, 5, 3, 4, 8, 6, 7, 0 });
		EightPuzzleBoard actualSecond = (EightPuzzleBoard) ((Successor) successors
				.get(1)).getState();
		assertEquals(expectedSecond, actualSecond);
		assertEquals(EightPuzzleBoard.DOWN, ((Successor) successors.get(1))
				.getAction());

		// test third successor
		EightPuzzleBoard expectedThird = new EightPuzzleBoard(new int[] { 1, 2,
				5, 3, 0, 4, 6, 7, 8 });
		EightPuzzleBoard actualThird = (EightPuzzleBoard) ((Successor) successors
				.get(2)).getState();
		assertEquals(expectedThird, actualThird);
		assertEquals(EightPuzzleBoard.LEFT, ((Successor) successors.get(2))
				.getAction());
	}

	public void testGenerateCorrectWhenGapMovedRightward() {
		board.moveGapLeft();// gives { 1, 2, 5, 3, 0, 4, 6, 7, 8 }
		assertEquals(new EightPuzzleBoard(
				new int[] { 1, 2, 5, 3, 0, 4, 6, 7, 8 }), board);
		List successors = func.getSuccessors(board);
		assertEquals(4, successors.size());
		EightPuzzleBoard expectedFourth = new EightPuzzleBoard(new int[] { 1,
				2, 5, 3, 4, 0, 6, 7, 8 });
		EightPuzzleBoard actualFourth = (EightPuzzleBoard) ((Successor) successors
				.get(3)).getState();
		assertEquals(expectedFourth, actualFourth);
		assertEquals(EightPuzzleBoard.RIGHT, ((Successor) successors.get(3))
				.getAction());

	}

}
