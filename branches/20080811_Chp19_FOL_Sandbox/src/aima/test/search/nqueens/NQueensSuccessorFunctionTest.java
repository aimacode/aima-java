package aima.test.search.nqueens;

import java.util.List;

import junit.framework.TestCase;
import aima.search.framework.Successor;
import aima.search.nqueens.NQueensBoard;
import aima.search.nqueens.NQueensSuccessorFunction;

/**
 * @author Ravi Mohan
 * 
 */

public class NQueensSuccessorFunctionTest extends TestCase {
	NQueensSuccessorFunction successorFunction;

	NQueensBoard oneBoard;

	NQueensBoard eightBoard;

	@Override
	public void setUp() {
		successorFunction = new NQueensSuccessorFunction();
		oneBoard = new NQueensBoard(1);
		eightBoard = new NQueensBoard(8);
	}

	public void testSimpleBoardSuccessorGenerator() {
		List successors = successorFunction.getSuccessors(oneBoard);
		assertEquals(1, successors.size());
		Successor successor = (Successor) successors.get(0);
		NQueensBoard next = (NQueensBoard) successor.getState();
		assertEquals(1, next.getNumberOfQueensOnBoard());
	}

	public void testComplexBoardSuccessorGenerator() {
		List successors = successorFunction.getSuccessors(eightBoard);
		assertEquals(8, successors.size());
		Successor successor = (Successor) successors.get(0);
		NQueensBoard next = (NQueensBoard) successor.getState();
		assertEquals(1, next.getNumberOfQueensOnBoard());

		successors = successorFunction.getSuccessors(next);
		assertEquals(6, successors.size());
	}

}
