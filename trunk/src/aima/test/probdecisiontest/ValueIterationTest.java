package aima.test.probdecisiontest;

import junit.framework.TestCase;
import aima.probability.decision.MDP;
import aima.probability.decision.MDPFactory;
import aima.probability.decision.MDPUtilityFunction;
import aima.probability.decision.cellworld.CellWorldPosition;

/**
 * @author Ravi Mohan
 * 
 */
public class ValueIterationTest extends TestCase {
	private MDP<CellWorldPosition, String> fourByThreeMDP;

	@Override
	public void setUp() {
		fourByThreeMDP = MDPFactory.createFourByThreeMDP();
	}

	public void testValueIterationInCellWorld() {
		MDPUtilityFunction<CellWorldPosition> uf = fourByThreeMDP
				.valueIterationTillMAximumUtilityGrowthFallsBelowErrorMargin(1,
						0.00001);

		// check against Fig 17.3
		assertEquals(0.705, uf.getUtility(new CellWorldPosition(1, 1)), 0.001);
		assertEquals(0.655, uf.getUtility(new CellWorldPosition(1, 2)), 0.001);
		assertEquals(0.611, uf.getUtility(new CellWorldPosition(1, 3)), 0.001);
		assertEquals(0.388, uf.getUtility(new CellWorldPosition(1, 4)), 0.001);

		assertEquals(0.762, uf.getUtility(new CellWorldPosition(2, 1)), 0.001);
		assertEquals(0.660, uf.getUtility(new CellWorldPosition(2, 3)), 0.001);
		assertEquals(-1.0, uf.getUtility(new CellWorldPosition(2, 4)), 0.001);

		assertEquals(0.812, uf.getUtility(new CellWorldPosition(3, 1)), 0.001);
		assertEquals(0.868, uf.getUtility(new CellWorldPosition(3, 2)), 0.001);
		assertEquals(0.918, uf.getUtility(new CellWorldPosition(3, 3)), 0.001);
		assertEquals(1.0, uf.getUtility(new CellWorldPosition(3, 4)), 0.001);

		assertEquals(0.868, uf.getUtility(new CellWorldPosition(3, 2)), 0.001);
	}
}
