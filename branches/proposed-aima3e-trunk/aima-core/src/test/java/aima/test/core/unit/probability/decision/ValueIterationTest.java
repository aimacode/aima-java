package aima.test.core.unit.probability.decision;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.environment.cellworld.CellWorldPosition;
import aima.core.probability.decision.MDP;
import aima.core.probability.decision.MDPFactory;
import aima.core.probability.decision.MDPUtilityFunction;

/**
 * @author Ravi Mohan
 * 
 */
public class ValueIterationTest {
	private MDP<CellWorldPosition, String> fourByThreeMDP;

	@Before
	public void setUp() {
		fourByThreeMDP = MDPFactory.createFourByThreeMDP();
	}

	@Test
	public void testValueIterationInCellWorld() {
		MDPUtilityFunction<CellWorldPosition> uf = fourByThreeMDP
				.valueIterationTillMAximumUtilityGrowthFallsBelowErrorMargin(1,
						0.00001);

		// AIMA2e check against Fig 17.3
		Assert.assertEquals(0.705, uf.getUtility(new CellWorldPosition(1, 1)),
				0.001);
		Assert.assertEquals(0.655, uf.getUtility(new CellWorldPosition(1, 2)),
				0.001);
		Assert.assertEquals(0.611, uf.getUtility(new CellWorldPosition(1, 3)),
				0.001);
		Assert.assertEquals(0.388, uf.getUtility(new CellWorldPosition(1, 4)),
				0.001);

		Assert.assertEquals(0.762, uf.getUtility(new CellWorldPosition(2, 1)),
				0.001);
		Assert.assertEquals(0.660, uf.getUtility(new CellWorldPosition(2, 3)),
				0.001);
		Assert.assertEquals(-1.0, uf.getUtility(new CellWorldPosition(2, 4)),
				0.001);

		Assert.assertEquals(0.812, uf.getUtility(new CellWorldPosition(3, 1)),
				0.001);
		Assert.assertEquals(0.868, uf.getUtility(new CellWorldPosition(3, 2)),
				0.001);
		Assert.assertEquals(0.918, uf.getUtility(new CellWorldPosition(3, 3)),
				0.001);
		Assert.assertEquals(1.0, uf.getUtility(new CellWorldPosition(3, 4)),
				0.001);

		Assert.assertEquals(0.868, uf.getUtility(new CellWorldPosition(3, 2)),
				0.001);
	}
}
