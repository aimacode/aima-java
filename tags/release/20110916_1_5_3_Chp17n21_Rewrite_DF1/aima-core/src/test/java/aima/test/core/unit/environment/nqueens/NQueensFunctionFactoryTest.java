package aima.test.core.unit.environment.nqueens;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.agent.Action;
import aima.core.environment.nqueens.NQueensBoard;
import aima.core.environment.nqueens.NQueensFunctionFactory;
import aima.core.search.framework.ActionsFunction;
import aima.core.search.framework.ResultFunction;

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public class NQueensFunctionFactoryTest {
	ActionsFunction af;
	ResultFunction rf;
	NQueensBoard oneBoard;
	NQueensBoard eightBoard;

	@Before
	public void setUp() {
		oneBoard = new NQueensBoard(1);
		eightBoard = new NQueensBoard(8);

		af = NQueensFunctionFactory.getIActionsFunction();
		rf = NQueensFunctionFactory.getResultFunction();
	}

	@Test
	public void testSimpleBoardSuccessorGenerator() {
		List<Action> actions = new ArrayList<Action>(af.actions(oneBoard));
		Assert.assertEquals(1, actions.size());
		NQueensBoard next = (NQueensBoard) rf.result(oneBoard, actions.get(0));
		Assert.assertEquals(1, next.getNumberOfQueensOnBoard());
	}

	@Test
	public void testComplexBoardSuccessorGenerator() {
		List<Action> actions = new ArrayList<Action>(af.actions(eightBoard));
		Assert.assertEquals(8, actions.size());
		NQueensBoard next = (NQueensBoard) rf
				.result(eightBoard, actions.get(0));
		Assert.assertEquals(1, next.getNumberOfQueensOnBoard());

		actions = new ArrayList<Action>(af.actions(next));
		Assert.assertEquals(6, actions.size());
	}
}
