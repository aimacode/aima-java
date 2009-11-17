package aima.test.core.unit.probability.decision;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.environment.cellworld.CellWorldPosition;
import aima.core.probability.decision.MDP;
import aima.core.probability.decision.MDPFactory;
import aima.core.probability.decision.MDPPolicy;
import aima.core.probability.decision.MDPUtilityFunction;

/**
 * @author Ravi Mohan
 * 
 */
public class PolicyIterationTest {
	private MDP<CellWorldPosition, String> fourByThreeMDP;

	@Before
	public void setUp() {
		fourByThreeMDP = MDPFactory.createFourByThreeMDP();
	}

	@Test
	public void testPolicyEvaluation() {
		MDPPolicy<CellWorldPosition, String> policy = fourByThreeMDP
				.randomPolicy();
		MDPUtilityFunction<CellWorldPosition> uf1 = fourByThreeMDP
				.initialUtilityFunction();

		MDPUtilityFunction<CellWorldPosition> uf2 = fourByThreeMDP
				.policyEvaluation(policy, uf1, 1, 3);

		Assert.assertFalse(uf1.equals(uf2));
	}

	@Test
	public void testPolicyIteration() {

		MDPPolicy<CellWorldPosition, String> policy = fourByThreeMDP
				.policyIteration(1);
		// AIMA2e check With Figure 17.2 (a)

		Assert
				.assertEquals("up", policy
						.getAction(new CellWorldPosition(1, 1)));
		Assert
				.assertEquals("up", policy
						.getAction(new CellWorldPosition(2, 1)));
		Assert.assertEquals("right", policy.getAction(new CellWorldPosition(3,
				1)));

		Assert.assertEquals("left", policy
				.getAction(new CellWorldPosition(1, 2)));
		Assert.assertEquals("right", policy.getAction(new CellWorldPosition(3,
				2)));

		Assert.assertEquals("left", policy
				.getAction(new CellWorldPosition(1, 3)));
		Assert
				.assertEquals("up", policy
						.getAction(new CellWorldPosition(2, 3)));
		Assert.assertEquals("right", policy.getAction(new CellWorldPosition(3,
				3)));

		Assert.assertEquals("left", policy
				.getAction(new CellWorldPosition(1, 4)));
	}
}
