package aima.test.core.unit.environment.cellworld;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.environment.cellworld.Cell;
import aima.core.environment.cellworld.CellWorld;
import aima.core.environment.cellworld.CellWorldAction;
import aima.core.environment.cellworld.CellWorldFactory;

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public class CellWorldTest {

	private CellWorld<Double> cw;

	@Before
	public void setUp() {
		cw = CellWorldFactory.createCellWorldForFig17_1();
	}

	@Test
	public void testNumberOfCells() {
		Assert.assertEquals(11, cw.getCells().size());
	}
	
	@Test
	public void testMoveUpIntoAdjacentCellChangesPositionCorrectly() {
		Cell<Double> sDelta = cw.result(cw.getCellAt(1, 1),
				CellWorldAction.Up);
		Assert.assertEquals(1, sDelta.getX());
		Assert.assertEquals(2, sDelta.getY());
	}

	@Test
	public void testMoveUpIntoWallLeavesPositionUnchanged() {
		Cell<Double> sDelta = cw.result(cw.getCellAt(1, 3),
				CellWorldAction.Up);
		Assert.assertEquals(1, sDelta.getX());
		Assert.assertEquals(3, sDelta.getY());
	}

	@Test
	public void testMoveUpIntoRemovedCellLeavesPositionUnchanged() {
		Cell<Double> sDelta = cw.result(cw.getCellAt(2, 1),
				CellWorldAction.Up);
		Assert.assertEquals(2, sDelta.getX());
		Assert.assertEquals(1, sDelta.getY());
	}
	
	@Test
	public void testMoveDownIntoAdjacentCellChangesPositionCorrectly() {
		Cell<Double> sDelta = cw.result(cw.getCellAt(1, 2),
				CellWorldAction.Down);
		Assert.assertEquals(1, sDelta.getX());
		Assert.assertEquals(1, sDelta.getY());
	}

	@Test
	public void testMoveDownIntoWallLeavesPositionUnchanged() {
		Cell<Double> sDelta = cw.result(cw.getCellAt(1, 1),
				CellWorldAction.Down);
		Assert.assertEquals(1, sDelta.getX());
		Assert.assertEquals(1, sDelta.getY());
	}

	@Test
	public void testMoveDownIntoRemovedCellLeavesPositionUnchanged() {
		Cell<Double> sDelta = cw.result(cw.getCellAt(2, 3),
				CellWorldAction.Down);
		Assert.assertEquals(2, sDelta.getX());
		Assert.assertEquals(3, sDelta.getY());
	}

	@Test
	public void testMoveLeftIntoAdjacentCellChangesPositionCorrectly() {
		Cell<Double> sDelta = cw.result(cw.getCellAt(2, 1),
				CellWorldAction.Left);
		Assert.assertEquals(1, sDelta.getX());
		Assert.assertEquals(1, sDelta.getY());
	}

	@Test
	public void testMoveLeftIntoWallLeavesPositionUnchanged() {
		Cell<Double> sDelta = cw.result(cw.getCellAt(1, 1),
				CellWorldAction.Left);
		Assert.assertEquals(1, sDelta.getX());
		Assert.assertEquals(1, sDelta.getY());
	}

	@Test
	public void testMoveLeftIntoRemovedCellLeavesPositionUnchanged() {
		Cell<Double> sDelta = cw.result(cw.getCellAt(3, 2),
				CellWorldAction.Left);
		Assert.assertEquals(3, sDelta.getX());
		Assert.assertEquals(2, sDelta.getY());
	}

	@Test
	public void testMoveRightIntoAdjacentCellChangesPositionCorrectly() {
		Cell<Double> sDelta = cw.result(cw.getCellAt(1, 1),
				CellWorldAction.Right);
		Assert.assertEquals(2, sDelta.getX());
		Assert.assertEquals(1, sDelta.getY());
	}

	@Test
	public void testMoveRightIntoWallLeavesPositionUnchanged() {
		Cell<Double> sDelta = cw.result(cw.getCellAt(4, 1),
				CellWorldAction.Right);
		Assert.assertEquals(4, sDelta.getX());
		Assert.assertEquals(1, sDelta.getY());
	}

	@Test
	public void testMoveRightIntoRemovedCellLeavesPositionUnchanged() {
		Cell<Double> sDelta = cw.result(cw.getCellAt(1, 2),
				CellWorldAction.Right);
		Assert.assertEquals(1, sDelta.getX());
		Assert.assertEquals(2, sDelta.getY());
	}
}
