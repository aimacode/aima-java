package aima.test.core.unit.search.nqueens;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.search.framework.Successor;
import aima.core.search.nqueens.NQueensBoard;
import aima.core.search.nqueens.NQueensSuccessorFunction;

/**
 * @author Ravi Mohan
 * 
 */
public class NQueensSuccessorFunctionTest {
	NQueensSuccessorFunction successorFunction;

	NQueensBoard oneBoard;

	NQueensBoard eightBoard;

	@Before
	public void setUp() {
		successorFunction = new NQueensSuccessorFunction();
		oneBoard = new NQueensBoard(1);
		eightBoard = new NQueensBoard(8);
	}

	@Test
	public void testSimpleBoardSuccessorGenerator() {
		List successors = successorFunction.getSuccessors(oneBoard);
		Assert.assertEquals(1, successors.size());
		Successor successor = (Successor) successors.get(0);
		NQueensBoard next = (NQueensBoard) successor.getState();
		Assert.assertEquals(1, next.getNumberOfQueensOnBoard());
	}

	@Test
	public void testComplexBoardSuccessorGenerator() {
		List successors = successorFunction.getSuccessors(eightBoard);
		Assert.assertEquals(8, successors.size());
		Successor successor = (Successor) successors.get(0);
		NQueensBoard next = (NQueensBoard) successor.getState();
		Assert.assertEquals(1, next.getNumberOfQueensOnBoard());

		successors = successorFunction.getSuccessors(next);
		Assert.assertEquals(6, successors.size());
	}
}
