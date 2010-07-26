package aima.test.core.unit.environment.eightpuzzle;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.environment.eightpuzzle.EightPuzzleBoard;
import aima.core.util.datastructure.XYLocation;

/**
 * @author Ravi Mohan
 * 
 */
public class EightPuzzleBoardTest {
	EightPuzzleBoard board;

	@Before
	public void setUp() {
		board = new EightPuzzleBoard();
	}

	@Test
	public void testGetBoard() {
		int[] expected = new int[] { 5, 4, 0, 6, 1, 8, 7, 3, 2 };
		int[] boardRepr = board.getState();
		Assert.assertEquals(expected[0], boardRepr[0]);
		Assert.assertEquals(expected[1], boardRepr[1]);
		Assert.assertEquals(expected[2], boardRepr[2]);
		Assert.assertEquals(expected[3], boardRepr[3]);
		Assert.assertEquals(expected[4], boardRepr[4]);
		Assert.assertEquals(expected[5], boardRepr[5]);
		Assert.assertEquals(expected[6], boardRepr[6]);
		Assert.assertEquals(expected[7], boardRepr[7]);
		Assert.assertEquals(expected[8], boardRepr[8]);
	}

	@Test
	public void testGetLocation() {
		Assert.assertEquals(new XYLocation(0, 2), board.getLocationOf(0));
		Assert.assertEquals(new XYLocation(1, 1), board.getLocationOf(1));
		Assert.assertEquals(new XYLocation(2, 2), board.getLocationOf(2));
		Assert.assertEquals(new XYLocation(2, 1), board.getLocationOf(3));
		Assert.assertEquals(new XYLocation(0, 1), board.getLocationOf(4));
		Assert.assertEquals(new XYLocation(0, 0), board.getLocationOf(5));
		Assert.assertEquals(new XYLocation(1, 0), board.getLocationOf(6));
		Assert.assertEquals(new XYLocation(2, 0), board.getLocationOf(7));
		Assert.assertEquals(new XYLocation(1, 2), board.getLocationOf(8));
	}

	@Test
	public void testGetValueAt() {
		Assert.assertEquals(5, board.getValueAt(new XYLocation(0, 0)));
		Assert.assertEquals(4, board.getValueAt(new XYLocation(0, 1)));
		Assert.assertEquals(0, board.getValueAt(new XYLocation(0, 2)));
		Assert.assertEquals(6, board.getValueAt(new XYLocation(1, 0)));
		Assert.assertEquals(1, board.getValueAt(new XYLocation(1, 1)));
		Assert.assertEquals(8, board.getValueAt(new XYLocation(1, 2)));
		Assert.assertEquals(7, board.getValueAt(new XYLocation(2, 0)));
		Assert.assertEquals(3, board.getValueAt(new XYLocation(2, 1)));
		Assert.assertEquals(2, board.getValueAt(new XYLocation(2, 2)));
	}

	@Test
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

		List<XYLocation> actual = board.getPositions();
		Assert.assertEquals(expected, actual);
	}

	@Test
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
		Assert.assertEquals(new XYLocation(1, 1), board.getLocationOf(0));
		Assert.assertEquals(new XYLocation(0, 2), board.getLocationOf(1));
	}
}
