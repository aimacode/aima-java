package aima.test.core.unit.probability.mdp;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.environment.cellworld.Cell;
import aima.core.environment.cellworld.CellWorld;
import aima.core.environment.cellworld.CellWorldAction;
import aima.core.environment.cellworld.CellWorldFactory;
import aima.core.probability.example.MDPFactory;
import aima.core.probability.mdp.MarkovDecisionProcess;

/**
 * 
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 *
 */
public class MarkovDecisionProcessTest {
	public static final double DELTA_THRESHOLD = 1e-3;

	private CellWorld<Double> cw = null;
	private MarkovDecisionProcess<Cell<Double>, CellWorldAction> mdp = null;

	@Before
	public void setUp() {
		cw = CellWorldFactory.createCellWorldForFig17_1();
		mdp = MDPFactory.createMDPForFigure17_3(cw);
	}

	@Test
	public void testActions() {
		// Ensure all actions can be performed in each cell
		// except for the terminal states.
		for (Cell<Double> s : cw.getCells()) {
			if (4 == s.getX() && (3 == s.getY() || 2 == s.getY())) {
				Assert.assertEquals(0, mdp.actions(s).size());
			} else {
				Assert.assertEquals(5, mdp.actions(s).size());
			}
		}
	}

	@Test
	public void testMDPTransitionModel() {
		Assert.assertEquals(0.8, mdp.transitionProbability(cw.getCellAt(1, 2),
				cw.getCellAt(1, 1), CellWorldAction.Up), DELTA_THRESHOLD);
		Assert.assertEquals(0.1, mdp.transitionProbability(cw.getCellAt(1, 1),
				cw.getCellAt(1, 1), CellWorldAction.Up), DELTA_THRESHOLD);
		Assert.assertEquals(0.1, mdp.transitionProbability(cw.getCellAt(2, 1),
				cw.getCellAt(1, 1), CellWorldAction.Up), DELTA_THRESHOLD);
		Assert.assertEquals(0.0, mdp.transitionProbability(cw.getCellAt(1, 3),
				cw.getCellAt(1, 1), CellWorldAction.Up), DELTA_THRESHOLD);

		Assert.assertEquals(0.9, mdp.transitionProbability(cw.getCellAt(1, 1),
				cw.getCellAt(1, 1), CellWorldAction.Down), DELTA_THRESHOLD);
		Assert.assertEquals(0.1, mdp.transitionProbability(cw.getCellAt(2, 1),
				cw.getCellAt(1, 1), CellWorldAction.Down), DELTA_THRESHOLD);
		Assert.assertEquals(0.0, mdp.transitionProbability(cw.getCellAt(3, 1),
				cw.getCellAt(1, 1), CellWorldAction.Down), DELTA_THRESHOLD);
		Assert.assertEquals(0.0, mdp.transitionProbability(cw.getCellAt(1, 2),
				cw.getCellAt(1, 1), CellWorldAction.Down), DELTA_THRESHOLD);

		Assert.assertEquals(0.9, mdp.transitionProbability(cw.getCellAt(1, 1),
				cw.getCellAt(1, 1), CellWorldAction.Left), DELTA_THRESHOLD);
		Assert.assertEquals(0.0, mdp.transitionProbability(cw.getCellAt(2, 1),
				cw.getCellAt(1, 1), CellWorldAction.Left), DELTA_THRESHOLD);
		Assert.assertEquals(0.0, mdp.transitionProbability(cw.getCellAt(3, 1),
				cw.getCellAt(1, 1), CellWorldAction.Left), DELTA_THRESHOLD);
		Assert.assertEquals(0.1, mdp.transitionProbability(cw.getCellAt(1, 2),
				cw.getCellAt(1, 1), CellWorldAction.Left), DELTA_THRESHOLD);

		Assert.assertEquals(0.8, mdp.transitionProbability(cw.getCellAt(2, 1),
				cw.getCellAt(1, 1), CellWorldAction.Right), DELTA_THRESHOLD);
		Assert.assertEquals(0.1, mdp.transitionProbability(cw.getCellAt(1, 1),
				cw.getCellAt(1, 1), CellWorldAction.Right), DELTA_THRESHOLD);
		Assert.assertEquals(0.1, mdp.transitionProbability(cw.getCellAt(1, 2),
				cw.getCellAt(1, 1), CellWorldAction.Right), DELTA_THRESHOLD);
		Assert.assertEquals(0.0, mdp.transitionProbability(cw.getCellAt(1, 3),
				cw.getCellAt(1, 1), CellWorldAction.Right), DELTA_THRESHOLD);
	}

	@Test
	public void testRewardFunction() {
		// Ensure all actions can be performed in each cell.
		for (Cell<Double> s : cw.getCells()) {
			if (4 == s.getX() && 3 == s.getY()) {
				Assert.assertEquals(1.0, mdp.reward(s), DELTA_THRESHOLD);
			} else if (4 == s.getX() && 2 == s.getY()) {
				Assert.assertEquals(-1.0, mdp.reward(s), DELTA_THRESHOLD);
			} else {
				Assert.assertEquals(-0.04, mdp.reward(s), DELTA_THRESHOLD);
			}
		}
	}
}
