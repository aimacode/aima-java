package aima.test.probdecisiontest;

import aima.probability.Randomizer;
import aima.probability.decision.MDP;
import aima.probability.decision.MDPFactory;
import aima.probability.decision.MDPPolicy;
import aima.probability.decision.MDPUtilityFunction;
import aima.probability.decision.cellworld.CellWorldPosition;
import junit.framework.TestCase;

/**
 * @author Ravi Mohan
 * 
 */
public class PolicyIterationTest extends TestCase {
	private MDP<CellWorldPosition, String> fourByThreeMDP;

	private Randomizer alwaysLessThanEightyPercent;

	@Override
	public void setUp() {
		fourByThreeMDP = MDPFactory.createFourByThreeMDP();

	}

	public void testPolicyEvaluation() {
		MDPPolicy<CellWorldPosition, String> policy = fourByThreeMDP
				.randomPolicy();
		MDPUtilityFunction<CellWorldPosition> uf1 = fourByThreeMDP
				.initialUtilityFunction();
		// System.out.println("before " +uf1);
		MDPUtilityFunction<CellWorldPosition> uf2 = fourByThreeMDP
				.policyEvaluation(policy, uf1, 1, 3);
		// System.out.println("after " +uf2);

		assertFalse(uf1.equals(uf2));

	}

	public void testPolicyIteration() {

		MDPPolicy<CellWorldPosition, String> policy = fourByThreeMDP
				.policyIteration(1);
		// check With Figure 17.2 (a)
		// System.out.println("policy after = " + policy);

		assertEquals("up", policy.getAction(new CellWorldPosition(1, 1)));
		assertEquals("up", policy.getAction(new CellWorldPosition(2, 1)));
		assertEquals("right", policy.getAction(new CellWorldPosition(3, 1)));

		assertEquals("left", policy.getAction(new CellWorldPosition(1, 2)));
		assertEquals("right", policy.getAction(new CellWorldPosition(3, 2)));

		assertEquals("left", policy.getAction(new CellWorldPosition(1, 3)));
		assertEquals("up", policy.getAction(new CellWorldPosition(2, 3)));
		assertEquals("right", policy.getAction(new CellWorldPosition(3, 3)));

		assertEquals("left", policy.getAction(new CellWorldPosition(1, 4)));

	}

}
