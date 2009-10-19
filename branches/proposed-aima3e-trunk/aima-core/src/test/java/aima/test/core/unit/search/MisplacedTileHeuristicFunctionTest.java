package aima.test.core.unit.search;

import org.junit.Assert;
import org.junit.Test;

import aima.core.search.eightpuzzle.EightPuzzleBoard;
import aima.core.search.eightpuzzle.MisplacedTilleHeuristicFunction;

/**
 * @author Ravi Mohan
 * 
 */
public class MisplacedTileHeuristicFunctionTest {

	@Test
	public void testHeuristicCalculation() {
		MisplacedTilleHeuristicFunction fn = new MisplacedTilleHeuristicFunction();
		EightPuzzleBoard board = new EightPuzzleBoard(new int[] { 2, 0, 5, 6,
				4, 8, 3, 7, 1 });
		Assert.assertEquals(7.0, fn.getHeuristicValue(board));

		board = new EightPuzzleBoard(new int[] { 6, 2, 5, 3, 4, 8, 0, 7, 1 });
		Assert.assertEquals(6.0, fn.getHeuristicValue(board));

		board = new EightPuzzleBoard(new int[] { 6, 2, 5, 3, 4, 8, 7, 0, 1 });
		Assert.assertEquals(7.0, fn.getHeuristicValue(board));
	}
}
