package aima.test.probdecisiontest;

import junit.framework.TestCase;
import aima.probability.Randomizer;
import aima.probability.decision.MDPTransitionModel;
import aima.probability.decision.cellworld.CellWorld;
import aima.probability.decision.cellworld.CellWorldPosition;
import aima.test.probabilitytest.MockRandomizer;
import aima.util.Pair;

public class CellWorldTest extends TestCase {
	
	private CellWorld cw;
	private Randomizer alwaysLessThanEightyPercent,betweenEightyAndNinetyPercent,greaterThanNinetyPercent;
	
	public void setUp(){
		cw = new CellWorld(3,4,0.01,-0.4);
		cw.markBlocked(2, 2);
		cw.setReward(2,4,-1);
		
		alwaysLessThanEightyPercent = new MockRandomizer(new double[]{0.7});
		betweenEightyAndNinetyPercent = new MockRandomizer(new double[]{0.85});
		greaterThanNinetyPercent= new MockRandomizer(new double[]{0.95});
	}
	
	public void testMoveLeftIntoWallLeavesPositionUnchanged(){
		CellWorldPosition pos = cw.moveProbabilisticallyFrom(1,1,cw.LEFT,alwaysLessThanEightyPercent);
		assertEquals(1, pos.getX());
		assertEquals(1, pos.getY());
	}
	
	public void testMoveLeftIntoUnblockedCellChangesPositionCorrectly(){
		CellWorldPosition  pos = cw.moveProbabilisticallyFrom(1,2,cw.LEFT,alwaysLessThanEightyPercent);
		assertEquals(1, pos.getX());
		assertEquals(1, pos.getY());
	}
	
	public void testMoveLeftIntoUnblockedCellActuallyMovesUpWhenProbabilityBetween80And90Percent(){
		CellWorldPosition pos = cw.moveProbabilisticallyFrom(1,4,cw.LEFT,betweenEightyAndNinetyPercent);
		assertEquals(2, pos.getX());
		assertEquals(4, pos.getY());
	}
	
	public void testMoveLeftIntoUnblockedCellActuallyMovesDownWhenProbabilityGreaterThan90Percent(){
		CellWorldPosition pos = cw.moveProbabilisticallyFrom(2,4,cw.LEFT,greaterThanNinetyPercent);
		assertEquals(1, pos.getX());
		assertEquals(4, pos.getY());
	}
	
	public void testMoveLeftIntoBlockedCellLeavesPositionUnchanged(){
		CellWorldPosition pos = cw.moveProbabilisticallyFrom(2,3,cw.LEFT,alwaysLessThanEightyPercent);
		assertEquals(2, pos.getX());
		assertEquals(3, pos.getY());
	}
	
	public void testMoveRightIntoWallLeavesPositionUnchanged(){
		CellWorldPosition pos = cw.moveProbabilisticallyFrom(1,4,cw.RIGHT,alwaysLessThanEightyPercent);
		assertEquals(1, pos.getX());
		assertEquals(4, pos.getY());
	}
	
	public void testMoveRightIntoUnblockedCellChangesPositionCorrectly(){
		CellWorldPosition pos = cw.moveProbabilisticallyFrom(1,2,cw.RIGHT,alwaysLessThanEightyPercent);
		assertEquals(1, pos.getX());
		assertEquals(3, pos.getY());
	}
	
	public void testMoveRightIntoBlockedCellLeavesPositionUnchanged(){
		CellWorldPosition pos = cw.moveProbabilisticallyFrom(2,1,cw.RIGHT,alwaysLessThanEightyPercent);
		assertEquals(2, pos.getX());
		assertEquals(1, pos.getY());
	}
	public void testMoveRightIntoUnblockedCellActuallyMovesUpWhenProbabilityBetween80And90Percent(){
		CellWorldPosition pos = cw.moveProbabilisticallyFrom(1,1,cw.RIGHT,betweenEightyAndNinetyPercent);
		assertEquals(2, pos.getX());
		assertEquals(1, pos.getY());
	}
	
	public void testMoveRightIntoUnblockedCellActuallyMovesDownWhenProbabilityGreaterThan90Percent(){
		CellWorldPosition pos = cw.moveProbabilisticallyFrom(2,4,cw.RIGHT,greaterThanNinetyPercent);
		assertEquals(1, pos.getX());
		assertEquals(4, pos.getY());
	}
	
	public void testMoveUpIntoWallLeavesPositionUnchanged(){
		CellWorldPosition pos = cw.moveProbabilisticallyFrom(3,1,cw.UP,alwaysLessThanEightyPercent);
		assertEquals(3, pos.getX());
		assertEquals(1, pos.getY());
	}
	
	public void testMoveUpIntoUnblockedCellChangesPositionCorrectly(){
		CellWorldPosition pos = cw.moveProbabilisticallyFrom(1,1,cw.UP,alwaysLessThanEightyPercent);
		assertEquals(2, pos.getX());
		assertEquals(1, pos.getY());
	}
	
	public void testMoveUpIntoBlockedCellLeavesPositionUnchanged(){
		CellWorldPosition pos = cw.moveProbabilisticallyFrom(1,2,cw.UP,alwaysLessThanEightyPercent);
		assertEquals(1, pos.getX());
		assertEquals(2, pos.getY());
	}
	
	public void testMoveUpActuallyMovesLeftWhenProbabilityBetween80And90Percent(){
		CellWorldPosition pos = cw.moveProbabilisticallyFrom(1,4,cw.UP,betweenEightyAndNinetyPercent);
		assertEquals(1, pos.getX());
		assertEquals(3, pos.getY());
	}
	
	public void testMoveUpActuallyMovesRightWhenProbabilityGreaterThan90Percent(){
		CellWorldPosition pos = cw.moveProbabilisticallyFrom(1,3,cw.UP,greaterThanNinetyPercent);
		assertEquals(1, pos.getX());
		assertEquals(4, pos.getY());
	}
	
	public void testMoveDownIntoWallLeavesPositionUnchanged(){
		CellWorldPosition pos = cw.moveProbabilisticallyFrom(1,1,cw.DOWN,alwaysLessThanEightyPercent);
		assertEquals(1, pos.getX());
		assertEquals(1, pos.getY());
	}
	
	public void testMoveDownIntoUnblockedCellChangesPositionCorrectly(){
		CellWorldPosition pos = cw.moveProbabilisticallyFrom(2,1,cw.DOWN,alwaysLessThanEightyPercent);
		assertEquals(1, pos.getX());
		assertEquals(1, pos.getY());
	}
	
	public void testMoveDownIntoBlockedCellLeavesPositionUnchanged(){
		CellWorldPosition pos = cw.moveProbabilisticallyFrom(3,2,cw.UP,alwaysLessThanEightyPercent);
		assertEquals(3, pos.getX());
		assertEquals(2, pos.getY());
	}
	
	public void testMoveDownActuallyMovesLeftWhenProbabilityBetween80And90Percent(){
		CellWorldPosition pos = cw.moveProbabilisticallyFrom(2,4,cw.DOWN,betweenEightyAndNinetyPercent);
		assertEquals(2, pos.getX());
		assertEquals(3, pos.getY());
	}
	
	public void testMoveDownActuallyMovesRightWhenProbabilityGreaterThan90Percent(){
		CellWorldPosition pos = cw.moveProbabilisticallyFrom(2,3,cw.UP,greaterThanNinetyPercent);
		assertEquals(2, pos.getX());
		assertEquals(4, pos.getY());
	}
	
	public void testNumberOfUnBlockedCells(){
		assertEquals(11,cw.unblockedCells().size());
	}
	
	public void testTransitionProbabilityCalculationWhenEndingPositionIsNextToStartingPositionButIsBlocked(){
		CellWorldPosition startingPosition = new CellWorldPosition(2,1); //to the left of blocked cell
		CellWorldPosition endingPosition = new CellWorldPosition(2,2); //blocked cell
		double transitionProb = cw.getTransitionProbability(startingPosition, cw.RIGHT, endingPosition);
		assertEquals(0.0, transitionProb);
	}
	
	public void testTransitionProbabilityCalculationWhenEndingPositionCannotBeReachedUsingDesiredActionOrRightAngledSteps(){
		CellWorldPosition startingPosition = new CellWorldPosition(1,3); 
		CellWorldPosition endingPosition = new CellWorldPosition(3,3);
		double transitionProb = cw.getTransitionProbability(startingPosition, cw.UP, endingPosition);
		assertEquals(0.0, transitionProb);
	}
	
	public void testTransitionProbabilityCalculationWhenEndingPositionReachebleByExecutingSuggestedAction(){
		CellWorldPosition startingPosition = new CellWorldPosition(1,1); 
		CellWorldPosition endingPosition = new CellWorldPosition(2,1);
		double transitionProb = cw.getTransitionProbability(startingPosition, cw.UP, endingPosition);
		assertEquals(0.8, transitionProb);

	}
	
	public void testTransitionProbabilityCalculationWhenBothRightAngledActiosnLeadToStartingPosition(){
		CellWorldPosition startingPosition = new CellWorldPosition(2,1); 
		CellWorldPosition endingPosition = new CellWorldPosition(2,1);
		double transitionProb = cw.getTransitionProbability(startingPosition, cw.UP, endingPosition);
		assertEquals(0.2, transitionProb);
		
	}
	
	public void testTransitionModelCreation(){
		MDPTransitionModel <CellWorldPosition, String> mtm = cw.getTransitionModel();
		CellWorldPosition startingPosition = new CellWorldPosition(1,1); 
		CellWorldPosition endingPosition = new CellWorldPosition(2,1);
		assertEquals(0.8, mtm.getTransitionProbability(startingPosition, cw.UP, endingPosition));
		
		CellWorldPosition endingPosition2 = new CellWorldPosition(1,1);
		assertEquals(0.1, mtm.getTransitionProbability(startingPosition, cw.UP, endingPosition2));
		CellWorldPosition endingPosition3 = new CellWorldPosition(1,2);
		assertEquals(0.1, mtm.getTransitionProbability(startingPosition, cw.UP, endingPosition3));

	}

}
