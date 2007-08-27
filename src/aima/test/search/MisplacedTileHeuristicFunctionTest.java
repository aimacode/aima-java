/*
 * Created on Sep 28, 2005
 *
 */
package aima.test.search;

import junit.framework.TestCase;

import aima.search.eightpuzzle.EightPuzzleBoard;
import aima.search.eightpuzzle.MisplacedTilleHeuristicFunction;
import aima.search.framework.Node;

/**
 * @author Ravi Mohan
 * 
 */

public class MisplacedTileHeuristicFunctionTest extends TestCase {
	public void testHeuristicCalculation() {
		MisplacedTilleHeuristicFunction fn = new MisplacedTilleHeuristicFunction();
		EightPuzzleBoard board = new EightPuzzleBoard(new int[] { 2, 0, 5, 6,
				4, 8, 3, 7, 1 });
		Node n = new Node(board);
		assertEquals(7, fn.getHeuristicValue(n));

		board = new EightPuzzleBoard(new int[] { 6, 2, 5, 3, 4, 8, 0, 7, 1 });
		n = new Node(board);
		assertEquals(6, fn.getHeuristicValue(n));

		board = new EightPuzzleBoard(new int[] { 6, 2, 5, 3, 4, 8, 7, 0, 1 });
		n = new Node(board);
		assertEquals(7, fn.getHeuristicValue(n));
	}
}
