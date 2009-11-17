package aima.test.core.unit.environment.cellworld;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.environment.cellworld.CellWorld;
import aima.core.environment.cellworld.CellWorldPosition;
import aima.core.probability.Randomizer;
import aima.core.probability.decision.MDPTransitionModel;
import aima.core.probability.decision.MDPUtilityFunction;
import aima.core.util.datastructure.Pair;
import aima.test.core.unit.probability.MockRandomizer;

/**
 * @author Ravi Mohan
 * 
 */
public class CellWorldTest {

	private CellWorld cw;

	private Randomizer alwaysLessThanEightyPercent,
			betweenEightyAndNinetyPercent, greaterThanNinetyPercent;

	@Before
	public void setUp() {
		cw = new CellWorld(3, 4, -0.04);

		cw.markBlocked(2, 2);

		cw.setTerminalState(2, 4);
		cw.setReward(2, 4, -1);

		cw.setTerminalState(3, 4);
		cw.setReward(3, 4, 1);

		alwaysLessThanEightyPercent = new MockRandomizer(new double[] { 0.7 });
		betweenEightyAndNinetyPercent = new MockRandomizer(
				new double[] { 0.85 });
		greaterThanNinetyPercent = new MockRandomizer(new double[] { 0.95 });
	}

	@Test
	public void testMoveLeftIntoWallLeavesPositionUnchanged() {
		CellWorldPosition pos = cw.moveProbabilisticallyFrom(1, 1,
				CellWorld.LEFT, alwaysLessThanEightyPercent);
		Assert.assertEquals(1, pos.getX());
		Assert.assertEquals(1, pos.getY());
	}

	@Test
	public void testMoveLeftIntoUnblockedCellChangesPositionCorrectly() {
		CellWorldPosition pos = cw.moveProbabilisticallyFrom(1, 2,
				CellWorld.LEFT, alwaysLessThanEightyPercent);
		Assert.assertEquals(1, pos.getX());
		Assert.assertEquals(1, pos.getY());
	}

	@Test
	public void testMoveLeftIntoUnblockedCellActuallyMovesUpWhenProbabilityBetween80And90Percent() {
		CellWorldPosition pos = cw.moveProbabilisticallyFrom(1, 4,
				CellWorld.LEFT, betweenEightyAndNinetyPercent);
		Assert.assertEquals(2, pos.getX());
		Assert.assertEquals(4, pos.getY());
	}

	@Test
	public void testMoveLeftIntoUnblockedCellActuallyMovesDownWhenProbabilityGreaterThan90Percent() {
		CellWorldPosition pos = cw.moveProbabilisticallyFrom(3, 3,
				CellWorld.LEFT, greaterThanNinetyPercent);
		Assert.assertEquals(2, pos.getX());
		Assert.assertEquals(3, pos.getY());
	}

	@Test
	public void testMoveLeftIntoBlockedCellLeavesPositionUnchanged() {
		CellWorldPosition pos = cw.moveProbabilisticallyFrom(2, 3,
				CellWorld.LEFT, alwaysLessThanEightyPercent);
		Assert.assertEquals(2, pos.getX());
		Assert.assertEquals(3, pos.getY());
	}

	@Test
	public void testMoveRightIntoWallLeavesPositionUnchanged() {
		CellWorldPosition pos = cw.moveProbabilisticallyFrom(1, 4,
				CellWorld.RIGHT, alwaysLessThanEightyPercent);
		Assert.assertEquals(1, pos.getX());
		Assert.assertEquals(4, pos.getY());
	}

	@Test
	public void testMoveRightIntoUnblockedCellChangesPositionCorrectly() {
		CellWorldPosition pos = cw.moveProbabilisticallyFrom(1, 2,
				CellWorld.RIGHT, alwaysLessThanEightyPercent);
		Assert.assertEquals(1, pos.getX());
		Assert.assertEquals(3, pos.getY());
	}

	@Test
	public void testMoveRightIntoBlockedCellLeavesPositionUnchanged() {
		CellWorldPosition pos = cw.moveProbabilisticallyFrom(2, 1,
				CellWorld.RIGHT, alwaysLessThanEightyPercent);
		Assert.assertEquals(2, pos.getX());
		Assert.assertEquals(1, pos.getY());
	}

	@Test
	public void testMoveRightIntoUnblockedCellActuallyMovesUpWhenProbabilityBetween80And90Percent() {
		CellWorldPosition pos = cw.moveProbabilisticallyFrom(1, 1,
				CellWorld.RIGHT, betweenEightyAndNinetyPercent);
		Assert.assertEquals(2, pos.getX());
		Assert.assertEquals(1, pos.getY());
	}

	@Test
	public void testMoveRightIntoUnblockedCellActuallyMovesDownWhenProbabilityGreaterThan90Percent() {
		CellWorldPosition pos = cw.moveProbabilisticallyFrom(3, 3,
				CellWorld.RIGHT, greaterThanNinetyPercent);
		Assert.assertEquals(2, pos.getX());
		Assert.assertEquals(3, pos.getY());
	}

	@Test
	public void testMoveUpIntoWallLeavesPositionUnchanged() {
		CellWorldPosition pos = cw.moveProbabilisticallyFrom(3, 1,
				CellWorld.UP, alwaysLessThanEightyPercent);
		Assert.assertEquals(3, pos.getX());
		Assert.assertEquals(1, pos.getY());
	}

	@Test
	public void testMoveUpIntoUnblockedCellChangesPositionCorrectly() {
		CellWorldPosition pos = cw.moveProbabilisticallyFrom(1, 1,
				CellWorld.UP, alwaysLessThanEightyPercent);
		Assert.assertEquals(2, pos.getX());
		Assert.assertEquals(1, pos.getY());
	}

	@Test
	public void testMoveUpIntoBlockedCellLeavesPositionUnchanged() {
		CellWorldPosition pos = cw.moveProbabilisticallyFrom(1, 2,
				CellWorld.UP, alwaysLessThanEightyPercent);
		Assert.assertEquals(1, pos.getX());
		Assert.assertEquals(2, pos.getY());
	}

	@Test
	public void testMoveUpActuallyMovesLeftWhenProbabilityBetween80And90Percent() {
		CellWorldPosition pos = cw.moveProbabilisticallyFrom(1, 4,
				CellWorld.UP, betweenEightyAndNinetyPercent);
		Assert.assertEquals(1, pos.getX());
		Assert.assertEquals(3, pos.getY());
	}

	@Test
	public void testMoveUpActuallyMovesRightWhenProbabilityGreaterThan90Percent() {
		CellWorldPosition pos = cw.moveProbabilisticallyFrom(1, 3,
				CellWorld.UP, greaterThanNinetyPercent);
		Assert.assertEquals(1, pos.getX());
		Assert.assertEquals(4, pos.getY());
	}

	@Test
	public void testMoveDownIntoWallLeavesPositionUnchanged() {
		CellWorldPosition pos = cw.moveProbabilisticallyFrom(1, 1,
				CellWorld.DOWN, alwaysLessThanEightyPercent);
		Assert.assertEquals(1, pos.getX());
		Assert.assertEquals(1, pos.getY());
	}

	@Test
	public void testMoveDownIntoUnblockedCellChangesPositionCorrectly() {
		CellWorldPosition pos = cw.moveProbabilisticallyFrom(2, 1,
				CellWorld.DOWN, alwaysLessThanEightyPercent);
		Assert.assertEquals(1, pos.getX());
		Assert.assertEquals(1, pos.getY());
	}

	@Test
	public void testMoveDownIntoBlockedCellLeavesPositionUnchanged() {
		CellWorldPosition pos = cw.moveProbabilisticallyFrom(3, 2,
				CellWorld.UP, alwaysLessThanEightyPercent);
		Assert.assertEquals(3, pos.getX());
		Assert.assertEquals(2, pos.getY());
	}

	@Test
	public void testMoveDownActuallyMovesLeftWhenProbabilityBetween80And90Percent() {
		CellWorldPosition pos = cw.moveProbabilisticallyFrom(3, 3,
				CellWorld.DOWN, betweenEightyAndNinetyPercent);
		Assert.assertEquals(3, pos.getX());
		Assert.assertEquals(2, pos.getY());
	}

	@Test
	public void testMoveDownActuallyMovesRightWhenProbabilityGreaterThan90Percent() {
		CellWorldPosition pos = cw.moveProbabilisticallyFrom(2, 3,
				CellWorld.UP, greaterThanNinetyPercent);
		Assert.assertEquals(2, pos.getX());
		Assert.assertEquals(4, pos.getY());
	}

	@Test
	public void testNumberOfUnBlockedCells() {
		Assert.assertEquals(11, cw.unblockedCells().size());
	}

	@Test
	public void testMoveFromATerminalStateFailsForAllProbabilityValues() {
		CellWorldPosition pos = cw.moveProbabilisticallyFrom(2, 4,
				CellWorld.UP, alwaysLessThanEightyPercent);
		Assert.assertEquals(2, pos.getX());
		Assert.assertEquals(4, pos.getY());
	}

	@Test
	public void testTransitionProbabilityCalculationWhenEndingPositionIsNextToStartingPositionButIsBlocked() {
		CellWorldPosition startingPosition = new CellWorldPosition(2, 1);
		// to the left of blocked cell
		CellWorldPosition endingPosition = new CellWorldPosition(2, 2); // blocked
		// cell
		double transitionProb = cw.getTransitionProbability(startingPosition,
				CellWorld.RIGHT, endingPosition);
		Assert.assertEquals(0.0, transitionProb, 0.001);
	}

	@Test
	public void testTransitionProbabilityCalculationWhenEndingPositionCannotBeReachedUsingDesiredActionOrRightAngledSteps() {
		CellWorldPosition startingPosition = new CellWorldPosition(1, 3);
		CellWorldPosition endingPosition = new CellWorldPosition(3, 3);
		double transitionProb = cw.getTransitionProbability(startingPosition,
				CellWorld.UP, endingPosition);
		Assert.assertEquals(0.0, transitionProb, 0.001);
	}

	@Test
	public void testTransitionProbabilityCalculationWhenEndingPositionReachebleByExecutingSuggestedAction() {
		CellWorldPosition startingPosition = new CellWorldPosition(1, 1);
		CellWorldPosition endingPosition = new CellWorldPosition(2, 1);
		double transitionProb = cw.getTransitionProbability(startingPosition,
				CellWorld.UP, endingPosition);
		Assert.assertEquals(0.8, transitionProb, 0.001);
	}

	@Test
	public void testTransitionProbabilityCalculationWhenBothRightAngledActiosnLeadToStartingPosition() {
		CellWorldPosition startingPosition = new CellWorldPosition(2, 1);
		CellWorldPosition endingPosition = new CellWorldPosition(2, 1);
		double transitionProb = cw.getTransitionProbability(startingPosition,
				CellWorld.UP, endingPosition);
		Assert.assertEquals(0.2, transitionProb, 0.001);
	}

	@Test
	public void testTransitionModelCreation() {
		MDPTransitionModel<CellWorldPosition, String> mtm = cw
				.getTransitionModel();
		CellWorldPosition startingPosition = new CellWorldPosition(1, 1);
		CellWorldPosition endingPosition = new CellWorldPosition(2, 1);
		Assert.assertEquals(0.8, mtm.getTransitionProbability(startingPosition,
				CellWorld.UP, endingPosition), 0.001);

		CellWorldPosition endingPosition2 = new CellWorldPosition(1, 1);
		Assert.assertEquals(0.1, mtm.getTransitionProbability(startingPosition,
				CellWorld.UP, endingPosition2), 0.001);
		CellWorldPosition endingPosition3 = new CellWorldPosition(1, 2);
		Assert.assertEquals(0.1, mtm.getTransitionProbability(startingPosition,
				CellWorld.UP, endingPosition3), 0.001);
	}

	@Test
	public void testCannotTransitionFromFinalState() {
		MDPTransitionModel<CellWorldPosition, String> mtm = cw
				.getTransitionModel();
		CellWorldPosition terminalOne = new CellWorldPosition(2, 4);
		CellWorldPosition terminalTwo = new CellWorldPosition(3, 4);
		Assert.assertEquals(0.0, mtm.getTransitionProbability(terminalOne,
				CellWorld.UP, terminalTwo), 0.001);
		Assert.assertEquals(0.0, mtm.getTransitionProbability(terminalTwo,
				CellWorld.DOWN, terminalOne), 0.001);
	}

	@Test
	public void testMaximumTransitionDetection() {
		// aka policy extraction
		// given a utility function

		// create the Utility Function depicted in Fig 17.3
		MDPUtilityFunction<CellWorldPosition> uf = new MDPUtilityFunction<CellWorldPosition>();
		uf.setUtility(new CellWorldPosition(1, 1), 0.705);
		uf.setUtility(new CellWorldPosition(1, 2), 0.655);
		uf.setUtility(new CellWorldPosition(1, 3), 0.611);
		uf.setUtility(new CellWorldPosition(1, 4), 0.388);

		uf.setUtility(new CellWorldPosition(2, 1), 0.762);
		uf.setUtility(new CellWorldPosition(2, 3), 0.660);
		uf.setUtility(new CellWorldPosition(2, 4), -1.0);

		uf.setUtility(new CellWorldPosition(3, 1), 0.812);
		uf.setUtility(new CellWorldPosition(3, 2), 0.868);
		uf.setUtility(new CellWorldPosition(3, 3), 0.918);
		uf.setUtility(new CellWorldPosition(3, 4), 1.0);

		assertPolicyReccomends(cw, uf, 1, 1, CellWorld.UP);
		assertPolicyReccomends(cw, uf, 1, 2, CellWorld.LEFT);
		assertPolicyReccomends(cw, uf, 1, 3, CellWorld.LEFT);
		assertPolicyReccomends(cw, uf, 1, 4, CellWorld.LEFT);

		assertPolicyReccomends(cw, uf, 2, 1, CellWorld.UP);
		assertPolicyReccomends(cw, uf, 2, 3, CellWorld.UP);
		assertPolicyReccomends(cw, uf, 2, 4, null);

		assertPolicyReccomends(cw, uf, 3, 1, CellWorld.RIGHT);
		assertPolicyReccomends(cw, uf, 3, 2, CellWorld.RIGHT);
		assertPolicyReccomends(cw, uf, 3, 3, CellWorld.RIGHT);
		assertPolicyReccomends(cw, uf, 3, 4, null);
	}

	//
	// PRIVATE METHODS
	//
	private void assertPolicyReccomends(CellWorld cw,
			MDPUtilityFunction<CellWorldPosition> uf, int x, int y,
			String actionExpected) {
		Pair<String, Double> p = cw.getTransitionModel()
				.getTransitionWithMaximumExpectedUtility(
						new CellWorldPosition(x, y), uf);

		Assert.assertEquals(actionExpected, p.getFirst());
	}
}
