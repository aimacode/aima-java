package aima.test.probdecisiontest;

import junit.framework.TestCase;
import aima.probability.Randomizer;
import aima.probability.decision.CellWorld;
import aima.test.probabilitytest.MockRandomizer;
import aima.util.Pair;

public class CellWorldTest extends TestCase {
	
	private CellWorld cw;
	private Randomizer alwaysLessThanEightyPercent,betweenEightyAndNinetyPercent,greaterThanNinetyPercent;
	
	public void setUp(){
		cw = new CellWorld(3,4,0.01,-0.4);
		cw.markBlocked(2, 2);
		
		alwaysLessThanEightyPercent = new MockRandomizer(new double[]{0.7});
		betweenEightyAndNinetyPercent = new MockRandomizer(new double[]{0.85});
		greaterThanNinetyPercent= new MockRandomizer(new double[]{0.95});
	}
	
	public void testMoveLeftIntoWallLeavesPositionUnchanged(){
		Pair<Integer,Integer> pos = cw.moveProbabilisticallyFrom(1,1,cw.LEFT,alwaysLessThanEightyPercent);
		assertEquals(new Integer(1), pos.getFirst());
		assertEquals(new Integer(1), pos.getSecond());
	}
	
	public void testMoveLeftIntoUnblockedCellChangesPositionCorrectly(){
		Pair<Integer,Integer> pos = cw.moveProbabilisticallyFrom(1,2,cw.LEFT,alwaysLessThanEightyPercent);
		assertEquals(new Integer(1), pos.getFirst());
		assertEquals(new Integer(1), pos.getSecond());
	}
	
	public void testMoveLeftIntoUnblockedCellActuallyMovesUpWhenProbabilityBetween80And90Percent(){
		Pair<Integer,Integer> pos = cw.moveProbabilisticallyFrom(1,4,cw.LEFT,betweenEightyAndNinetyPercent);
		assertEquals(new Integer(2), pos.getFirst());
		assertEquals(new Integer(4), pos.getSecond());
	}
	
	public void testMoveLeftIntoUnblockedCellActuallyMovesDownWhenProbabilityGreaterThan90Percent(){
		Pair<Integer,Integer> pos = cw.moveProbabilisticallyFrom(2,4,cw.LEFT,greaterThanNinetyPercent);
		assertEquals(new Integer(1), pos.getFirst());
		assertEquals(new Integer(4), pos.getSecond());
	}
	
	public void testMoveLeftIntoBlockedCellLeavesPositionUnchanged(){
		Pair<Integer,Integer> pos = cw.moveProbabilisticallyFrom(2,3,cw.LEFT,alwaysLessThanEightyPercent);
		assertEquals(new Integer(2), pos.getFirst());
		assertEquals(new Integer(3), pos.getSecond());
	}
	
	public void testMoveRightIntoWallLeavesPositionUnchanged(){
		Pair<Integer,Integer> pos = cw.moveProbabilisticallyFrom(1,4,cw.RIGHT,alwaysLessThanEightyPercent);
		assertEquals(new Integer(1), pos.getFirst());
		assertEquals(new Integer(4), pos.getSecond());
	}
	
	public void testMoveRightIntoUnblockedCellChangesPositionCorrectly(){
		Pair<Integer,Integer> pos = cw.moveProbabilisticallyFrom(1,2,cw.RIGHT,alwaysLessThanEightyPercent);
		assertEquals(new Integer(1), pos.getFirst());
		assertEquals(new Integer(3), pos.getSecond());
	}
	
	public void testMoveRightIntoBlockedCellLeavesPositionUnchanged(){
		Pair<Integer,Integer> pos = cw.moveProbabilisticallyFrom(2,1,cw.RIGHT,alwaysLessThanEightyPercent);
		assertEquals(new Integer(2), pos.getFirst());
		assertEquals(new Integer(1), pos.getSecond());
	}
	public void testMoveRightIntoUnblockedCellActuallyMovesUpWhenProbabilityBetween80And90Percent(){
		Pair<Integer,Integer> pos = cw.moveProbabilisticallyFrom(1,1,cw.RIGHT,betweenEightyAndNinetyPercent);
		assertEquals(new Integer(2), pos.getFirst());
		assertEquals(new Integer(1), pos.getSecond());
	}
	
	public void testMoveRightIntoUnblockedCellActuallyMovesDownWhenProbabilityGreaterThan90Percent(){
		Pair<Integer,Integer> pos = cw.moveProbabilisticallyFrom(2,4,cw.RIGHT,greaterThanNinetyPercent);
		assertEquals(new Integer(1), pos.getFirst());
		assertEquals(new Integer(4), pos.getSecond());
	}
	
	public void testMoveUpIntoWallLeavesPositionUnchanged(){
		Pair<Integer,Integer> pos = cw.moveProbabilisticallyFrom(3,1,cw.UP,alwaysLessThanEightyPercent);
		assertEquals(new Integer(3), pos.getFirst());
		assertEquals(new Integer(1), pos.getSecond());
	}
	
	public void testMoveUpIntoUnblockedCellChangesPositionCorrectly(){
		Pair<Integer,Integer> pos = cw.moveProbabilisticallyFrom(1,1,cw.UP,alwaysLessThanEightyPercent);
		assertEquals(new Integer(2), pos.getFirst());
		assertEquals(new Integer(1), pos.getSecond());
	}
	
	public void testMoveUpIntoBlockedCellLeavesPositionUnchanged(){
		Pair<Integer,Integer> pos = cw.moveProbabilisticallyFrom(1,2,cw.UP,alwaysLessThanEightyPercent);
		assertEquals(new Integer(1), pos.getFirst());
		assertEquals(new Integer(2), pos.getSecond());
	}
	
	public void testMoveUpActuallyMovesLeftWhenProbabilityBetween80And90Percent(){
		Pair<Integer,Integer> pos = cw.moveProbabilisticallyFrom(1,4,cw.UP,betweenEightyAndNinetyPercent);
		assertEquals(new Integer(1), pos.getFirst());
		assertEquals(new Integer(3), pos.getSecond());
	}
	
	public void testMoveUpActuallyMovesRightWhenProbabilityGreaterThan90Percent(){
		Pair<Integer,Integer> pos = cw.moveProbabilisticallyFrom(1,3,cw.UP,greaterThanNinetyPercent);
		assertEquals(new Integer(1), pos.getFirst());
		assertEquals(new Integer(4), pos.getSecond());
	}
	
	public void testMoveDownIntoWallLeavesPositionUnchanged(){
		Pair<Integer,Integer> pos = cw.moveProbabilisticallyFrom(1,1,cw.DOWN,alwaysLessThanEightyPercent);
		assertEquals(new Integer(1), pos.getFirst());
		assertEquals(new Integer(1), pos.getSecond());
	}
	
	public void testMoveDownIntoUnblockedCellChangesPositionCorrectly(){
		Pair<Integer,Integer> pos = cw.moveProbabilisticallyFrom(2,1,cw.DOWN,alwaysLessThanEightyPercent);
		assertEquals(new Integer(1), pos.getFirst());
		assertEquals(new Integer(1), pos.getSecond());
	}
	
	public void testMoveDownIntoBlockedCellLeavesPositionUnchanged(){
		Pair<Integer,Integer> pos = cw.moveProbabilisticallyFrom(3,2,cw.UP,alwaysLessThanEightyPercent);
		assertEquals(new Integer(3), pos.getFirst());
		assertEquals(new Integer(2), pos.getSecond());
	}
	
	public void testMoveDownActuallyMovesLeftWhenProbabilityBetween80And90Percent(){
		Pair<Integer,Integer> pos = cw.moveProbabilisticallyFrom(2,4,cw.DOWN,betweenEightyAndNinetyPercent);
		assertEquals(new Integer(2), pos.getFirst());
		assertEquals(new Integer(3), pos.getSecond());
	}
	
	public void testMoveDownActuallyMovesRightWhenProbabilityGreaterThan90Percent(){
		Pair<Integer,Integer> pos = cw.moveProbabilisticallyFrom(2,3,cw.UP,greaterThanNinetyPercent);
		assertEquals(new Integer(2), pos.getFirst());
		assertEquals(new Integer(4), pos.getSecond());
	}

}
