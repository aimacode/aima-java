/*
 * Created on Sep 28, 2005
 *
 */
package aima.test.search;

import junit.framework.TestCase;
import aima.search.eightpuzzle.EightPuzzleBoard;
import aima.search.eightpuzzle.MisplacedTilleHeuristicFunction;

/**
 * @author Ravi Mohan
 * 
 */

public class MisplacedTileHeuristicFunctionTest extends TestCase {
	public void testHeuristicCalculation() {
		MisplacedTilleHeuristicFunction fn = new MisplacedTilleHeuristicFunction();
		EightPuzzleBoard board = new EightPuzzleBoard(new int[] { 2, 0, 5, 6,
				4, 8, 3, 7, 1 });
		assertEquals(7.0, fn.getHeuristicValue(board));

		board = new EightPuzzleBoard(new int[] { 6, 2, 5, 3, 4, 8, 0, 7, 1 });
		assertEquals(6.0, fn.getHeuristicValue(board));

		board = new EightPuzzleBoard(new int[] { 6, 2, 5, 3, 4, 8, 7, 0, 1 });
		assertEquals(7.0, fn.getHeuristicValue(board));
	}
}
