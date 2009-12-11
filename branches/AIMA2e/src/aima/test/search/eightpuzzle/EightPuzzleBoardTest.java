package aima.test.search.eightpuzzle;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import aima.basic.XYLocation;
import aima.search.eightpuzzle.EightPuzzleBoard;

/**
 * @author Ravi Mohan
 * 
 */

public class EightPuzzleBoardTest extends TestCase {
	EightPuzzleBoard board;

	@Override
	public void setUp() {
		board = new EightPuzzleBoard();
	}

	public void testGetBoard() {
		int[] expected = new int[] { 5, 4, 0, 6, 1, 8, 7, 3, 2 };
		int[] boardRepr = board.getBoard();
		assertEquals(expected[0], boardRepr[0]);
		assertEquals(expected[1], boardRepr[1]);
		assertEquals(expected[2], boardRepr[2]);
		assertEquals(expected[3], boardRepr[3]);
		assertEquals(expected[4], boardRepr[4]);
		assertEquals(expected[5], boardRepr[5]);
		assertEquals(expected[6], boardRepr[6]);
		assertEquals(expected[7], boardRepr[7]);
		assertEquals(expected[8], boardRepr[8]);
	}

	public void testGetLocation() {
		assertEquals(new XYLocation(0, 2), board.getLocationOf(0));
		assertEquals(new XYLocation(1, 1), board.getLocationOf(1));
		assertEquals(new XYLocation(2, 2), board.getLocationOf(2));
		assertEquals(new XYLocation(2, 1), board.getLocationOf(3));
		assertEquals(new XYLocation(0, 1), board.getLocationOf(4));
		assertEquals(new XYLocation(0, 0), board.getLocationOf(5));
		assertEquals(new XYLocation(1, 0), board.getLocationOf(6));
		assertEquals(new XYLocation(2, 0), board.getLocationOf(7));
		assertEquals(new XYLocation(1, 2), board.getLocationOf(8));
	}

	public void testGetValueAt() {
		assertEquals(5, board.getValueAt(new XYLocation(0, 0)));
		assertEquals(4, board.getValueAt(new XYLocation(0, 1)));
		assertEquals(0, board.getValueAt(new XYLocation(0, 2)));
		assertEquals(6, board.getValueAt(new XYLocation(1, 0)));
		assertEquals(1, board.getValueAt(new XYLocation(1, 1)));
		assertEquals(8, board.getValueAt(new XYLocation(1, 2)));
		assertEquals(7, board.getValueAt(new XYLocation(2, 0)));
		assertEquals(3, board.getValueAt(new XYLocation(2, 1)));
		assertEquals(2, board.getValueAt(new XYLocation(2, 2)));
	}

	public void testGetPositions() {
		List<XYLocation> expected = new ArrayList<XYLocation>();
		expected.add(new XYLocation(0, 2));
		expected.add(new XYLocation(1, 1));
		expected.add(new XYLocation(2, 2));
		expected.add(new XYLocation(2, 1));
		expected.add(new XYLocation(0, 1));
		expected.add(new XYLocation(0, 0));
		expected.add(new XYLocation(1, 0));
		expected.add(new XYLocation(2, 0));
		expected.add(new XYLocation(1, 2));

		List actual = board.getPositions();
		assertEquals(expected, actual);
	}

	public void testSetBoard() {
		List<XYLocation> passedIn = new ArrayList<XYLocation>();
		passedIn.add(new XYLocation(1, 1));
		passedIn.add(new XYLocation(0, 2));
		passedIn.add(new XYLocation(2, 2));
		passedIn.add(new XYLocation(2, 1));
		passedIn.add(new XYLocation(0, 1));
		passedIn.add(new XYLocation(0, 0));
		passedIn.add(new XYLocation(1, 0));
		passedIn.add(new XYLocation(2, 0));
		passedIn.add(new XYLocation(1, 2));
		board.setBoard(passedIn);
		assertEquals(new XYLocation(1, 1), board.getLocationOf(0));
		assertEquals(new XYLocation(0, 2), board.getLocationOf(1));
	}

}
