package aima.test.probdecisiontest;

import junit.framework.TestCase;
import aima.probability.Randomizer;
import aima.probability.decision.MDPTransitionModel;
import aima.probability.decision.MDPUtilityFunction;
import aima.probability.decision.cellworld.CellWorld;
import aima.probability.decision.cellworld.CellWorldPosition;
import aima.test.probabilitytest.MockRandomizer;
import aima.util.Pair;

/**
 * @author Ravi Mohan
 * 
 */

public class CellWorldTest extends TestCase {

	private CellWorld cw;

	private Randomizer alwaysLessThanEightyPercent,
			betweenEightyAndNinetyPercent, greaterThanNinetyPercent;

	@Override
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

	public void testMoveLeftIntoWallLeavesPositionUnchanged() {
		CellWorldPosition pos = cw.moveProbabilisticallyFrom(1, 1,
				CellWorld.LEFT, alwaysLessThanEightyPercent);
		assertEquals(1, pos.getX());
		assertEquals(1, pos.getY());
	}

	public void testMoveLeftIntoUnblockedCellChangesPositionCorrectly() {
		CellWorldPosition pos = cw.moveProbabilisticallyFrom(1, 2,
				CellWorld.LEFT, alwaysLessThanEightyPercent);
		assertEquals(1, pos.getX());
		assertEquals(1, pos.getY());
	}

	public void testMoveLeftIntoUnblockedCellActuallyMovesUpWhenProbabilityBetween80And90Percent() {
		CellWorldPosition pos = cw.moveProbabilisticallyFrom(1, 4,
				CellWorld.LEFT, betweenEightyAndNinetyPercent);
		assertEquals(2, pos.getX());
		assertEquals(4, pos.getY());
	}

	public void testMoveLeftIntoUnblockedCellActuallyMovesDownWhenProbabilityGreaterThan90Percent() {
		CellWorldPosition pos = cw.moveProbabilisticallyFrom(3, 3,
				CellWorld.LEFT, greaterThanNinetyPercent);
		assertEquals(2, pos.getX());
		assertEquals(3, pos.getY());
	}

	public void testMoveLeftIntoBlockedCellLeavesPositionUnchanged() {
		CellWorldPosition pos = cw.moveProbabilisticallyFrom(2, 3,
				CellWorld.LEFT, alwaysLessThanEightyPercent);
		assertEquals(2, pos.getX());
		assertEquals(3, pos.getY());
	}

	public void testMoveRightIntoWallLeavesPositionUnchanged() {
		CellWorldPosition pos = cw.moveProbabilisticallyFrom(1, 4,
				CellWorld.RIGHT, alwaysLessThanEightyPercent);
		assertEquals(1, pos.getX());
		assertEquals(4, pos.getY());
	}

	public void testMoveRightIntoUnblockedCellChangesPositionCorrectly() {
		CellWorldPosition pos = cw.moveProbabilisticallyFrom(1, 2,
				CellWorld.RIGHT, alwaysLessThanEightyPercent);
		assertEquals(1, pos.getX());
		assertEquals(3, pos.getY());
	}

	public void testMoveRightIntoBlockedCellLeavesPositionUnchanged() {
		CellWorldPosition pos = cw.moveProbabilisticallyFrom(2, 1,
				CellWorld.RIGHT, alwaysLessThanEightyPercent);
		assertEquals(2, pos.getX());
		assertEquals(1, pos.getY());
	}

	public void testMoveRightIntoUnblockedCellActuallyMovesUpWhenProbabilityBetween80And90Percent() {
		CellWorldPosition pos = cw.moveProbabilisticallyFrom(1, 1,
				CellWorld.RIGHT, betweenEightyAndNinetyPercent);
		assertEquals(2, pos.getX());
		assertEquals(1, pos.getY());
	}

	public void testMoveRightIntoUnblockedCellActuallyMovesDownWhenProbabilityGreaterThan90Percent() {
		CellWorldPosition pos = cw.moveProbabilisticallyFrom(3, 3,
				CellWorld.RIGHT, greaterThanNinetyPercent);
		assertEquals(2, pos.getX());
		assertEquals(3, pos.getY());
	}

	public void testMoveUpIntoWallLeavesPositionUnchanged() {
		CellWorldPosition pos = cw.moveProbabilisticallyFrom(3, 1,
				CellWorld.UP, alwaysLessThanEightyPercent);
		assertEquals(3, pos.getX());
		assertEquals(1, pos.getY());
	}

	public void testMoveUpIntoUnblockedCellChangesPositionCorrectly() {
		CellWorldPosition pos = cw.moveProbabilisticallyFrom(1, 1,
				CellWorld.UP, alwaysLessThanEightyPercent);
		assertEquals(2, pos.getX());
		assertEquals(1, pos.getY());
	}

	public void testMoveUpIntoBlockedCellLeavesPositionUnchanged() {
		CellWorldPosition pos = cw.moveProbabilisticallyFrom(1, 2,
				CellWorld.UP, alwaysLessThanEightyPercent);
		assertEquals(1, pos.getX());
		assertEquals(2, pos.getY());
	}

	public void testMoveUpActuallyMovesLeftWhenProbabilityBetween80And90Percent() {
		CellWorldPosition pos = cw.moveProbabilisticallyFrom(1, 4,
				CellWorld.UP, betweenEightyAndNinetyPercent);
		assertEquals(1, pos.getX());
		assertEquals(3, pos.getY());
	}

	public void testMoveUpActuallyMovesRightWhenProbabilityGreaterThan90Percent() {
		CellWorldPosition pos = cw.moveProbabilisticallyFrom(1, 3,
				CellWorld.UP, greaterThanNinetyPercent);
		assertEquals(1, pos.getX());
		assertEquals(4, pos.getY());
	}

	public void testMoveDownIntoWallLeavesPositionUnchanged() {
		CellWorldPosition pos = cw.moveProbabilisticallyFrom(1, 1,
				CellWorld.DOWN, alwaysLessThanEightyPercent);
		assertEquals(1, pos.getX());
		assertEquals(1, pos.getY());
	}

	public void testMoveDownIntoUnblockedCellChangesPositionCorrectly() {
		CellWorldPosition pos = cw.moveProbabilisticallyFrom(2, 1,
				CellWorld.DOWN, alwaysLessThanEightyPercent);
		assertEquals(1, pos.getX());
		assertEquals(1, pos.getY());
	}

	public void testMoveDownIntoBlockedCellLeavesPositionUnchanged() {
		CellWorldPosition pos = cw.moveProbabilisticallyFrom(3, 2,
				CellWorld.UP, alwaysLessThanEightyPercent);
		assertEquals(3, pos.getX());
		assertEquals(2, pos.getY());
	}

	public void testMoveDownActuallyMovesLeftWhenProbabilityBetween80And90Percent() {
		CellWorldPosition pos = cw.moveProbabilisticallyFrom(3, 3,
				CellWorld.DOWN, betweenEightyAndNinetyPercent);
		assertEquals(3, pos.getX());
		assertEquals(2, pos.getY());
	}

	public void testMoveDownActuallyMovesRightWhenProbabilityGreaterThan90Percent() {
		CellWorldPosition pos = cw.moveProbabilisticallyFrom(2, 3,
				CellWorld.UP, greaterThanNinetyPercent);
		assertEquals(2, pos.getX());
		assertEquals(4, pos.getY());
	}

	public void testNumberOfUnBlockedCells() {
		assertEquals(11, cw.unblockedCells().size());
	}

	public void testMoveFromATerminalStateFailsForAllProbabilityValues() {
		CellWorldPosition pos = cw.moveProbabilisticallyFrom(2, 4,
				CellWorld.UP, alwaysLessThanEightyPercent);
		assertEquals(2, pos.getX());
		assertEquals(4, pos.getY());
	}

	public void testTransitionProbabilityCalculationWhenEndingPositionIsNextToStartingPositionButIsBlocked() {
		CellWorldPosition startingPosition = new CellWorldPosition(2, 1); // to
		// the
		// left
		// of
		// blocked
		// cell
		CellWorldPosition endingPosition = new CellWorldPosition(2, 2); // blocked
		// cell
		double transitionProb = cw.getTransitionProbability(startingPosition,
				CellWorld.RIGHT, endingPosition);
		assertEquals(0.0, transitionProb);
	}

	public void testTransitionProbabilityCalculationWhenEndingPositionCannotBeReachedUsingDesiredActionOrRightAngledSteps() {
		CellWorldPosition startingPosition = new CellWorldPosition(1, 3);
		CellWorldPosition endingPosition = new CellWorldPosition(3, 3);
		double transitionProb = cw.getTransitionProbability(startingPosition,
				CellWorld.UP, endingPosition);
		assertEquals(0.0, transitionProb);
	}

	public void testTransitionProbabilityCalculationWhenEndingPositionReachebleByExecutingSuggestedAction() {
		CellWorldPosition startingPosition = new CellWorldPosition(1, 1);
		CellWorldPosition endingPosition = new CellWorldPosition(2, 1);
		double transitionProb = cw.getTransitionProbability(startingPosition,
				CellWorld.UP, endingPosition);
		assertEquals(0.8, transitionProb);

	}

	public void testTransitionProbabilityCalculationWhenBothRightAngledActiosnLeadToStartingPosition() {
		CellWorldPosition startingPosition = new CellWorldPosition(2, 1);
		CellWorldPosition endingPosition = new CellWorldPosition(2, 1);
		double transitionProb = cw.getTransitionProbability(startingPosition,
				CellWorld.UP, endingPosition);
		assertEquals(0.2, transitionProb);

	}

	public void testTransitionModelCreation() {
		MDPTransitionModel<CellWorldPosition, String> mtm = cw
				.getTransitionModel();
		CellWorldPosition startingPosition = new CellWorldPosition(1, 1);
		CellWorldPosition endingPosition = new CellWorldPosition(2, 1);
		assertEquals(0.8, mtm.getTransitionProbability(startingPosition,
				CellWorld.UP, endingPosition));

		CellWorldPosition endingPosition2 = new CellWorldPosition(1, 1);
		assertEquals(0.1, mtm.getTransitionProbability(startingPosition,
				CellWorld.UP, endingPosition2));
		CellWorldPosition endingPosition3 = new CellWorldPosition(1, 2);
		assertEquals(0.1, mtm.getTransitionProbability(startingPosition,
				CellWorld.UP, endingPosition3));

		// Reward
		// for (CellWorldPosition cp : cw.unblockedPositions()){
		//			
		// }
		//		
		// MDPTransition<CellWorldPosition, String> transition =
		// mtm.maxTransition(eew CellWorldPosition(1,1), uf);

	}

	public void testCannotTransitionFromFinalState() {
		MDPTransitionModel<CellWorldPosition, String> mtm = cw
				.getTransitionModel();
		CellWorldPosition terminalOne = new CellWorldPosition(2, 4);
		CellWorldPosition terminalTwo = new CellWorldPosition(3, 4);
		assertEquals(0.0, mtm.getTransitionProbability(terminalOne,
				CellWorld.UP, terminalTwo));
		assertEquals(0.0, mtm.getTransitionProbability(terminalTwo,
				CellWorld.DOWN, terminalOne));

	}

	public void testMaximumTransitionDetection() { // aka policy extraction
		// given a utility function
		MDPTransitionModel<CellWorldPosition, String> mtm = cw
				.getTransitionModel();

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

	private void assertPolicyReccomends(CellWorld cw,
			MDPUtilityFunction<CellWorldPosition> uf, int x, int y,
			String actionExpected) {
		Pair<String, Double> p = cw.getTransitionModel()
				.getTransitionWithMaximumExpectedUtility(
						new CellWorldPosition(x, y), uf);
		// System.out.println(p);
		assertEquals(actionExpected, p.getFirst());
	}

}
