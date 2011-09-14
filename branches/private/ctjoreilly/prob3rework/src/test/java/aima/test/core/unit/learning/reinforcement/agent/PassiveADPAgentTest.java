package aima.test.core.unit.learning.reinforcement.agent;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import aima.core.environment.cellworld.Cell;
import aima.core.environment.cellworld.CellWorld;
import aima.core.environment.cellworld.CellWorldAction;
import aima.core.environment.cellworld.CellWorldFactory;
import aima.core.learning.reinforcement.agent.PassiveADPAgent;
import aima.core.learning.reinforcement.example.CellWorldEnvironment;
import aima.core.probability.example.MDPFactory;
import aima.core.probability.mdp.impl.ModifiedPolicyEvaluation;
import aima.core.util.JavaRandomizer;

public class PassiveADPAgentTest extends ReinforcementLearningAgentTest {
	//
	private CellWorld<Double> cw = null;
	private CellWorldEnvironment cwe = null;
	private PassiveADPAgent<Cell<Double>, CellWorldAction> padpa = null;

	@Before
	public void setUp() {
		cw = CellWorldFactory.createCellWorldForFig17_1();
		cwe = new CellWorldEnvironment(
				cw.getCellAt(1, 1),
				MDPFactory
						.createCellWorldPossibleOutcomesFunctionForFig17_1(cw),
				MDPFactory.createTransitionProbabilityFunctionForFigure17_1(cw),
				new JavaRandomizer());

		Map<Cell<Double>, CellWorldAction> fixedPolicy = new HashMap<Cell<Double>, CellWorldAction>();
		fixedPolicy.put(cw.getCellAt(1, 1), CellWorldAction.Up);
		fixedPolicy.put(cw.getCellAt(1, 2), CellWorldAction.Up);
		fixedPolicy.put(cw.getCellAt(1, 3), CellWorldAction.Right);
		fixedPolicy.put(cw.getCellAt(2, 1), CellWorldAction.Left);
		fixedPolicy.put(cw.getCellAt(2, 3), CellWorldAction.Right);
		fixedPolicy.put(cw.getCellAt(3, 1), CellWorldAction.Left);
		fixedPolicy.put(cw.getCellAt(3, 2), CellWorldAction.Up);
		fixedPolicy.put(cw.getCellAt(3, 3), CellWorldAction.Right);
		fixedPolicy.put(cw.getCellAt(4, 1), CellWorldAction.Left);

		padpa = new PassiveADPAgent<Cell<Double>, CellWorldAction>(fixedPolicy,
				cw.getCells(), cw.getCellAt(1, 1),
				MDPFactory.createActionsFunctionForFigure17_1(cw),
				new ModifiedPolicyEvaluation<Cell<Double>, CellWorldAction>(10,
						1.0));
		
		cwe.addAgent(padpa);
	}

	@Test
	public void test_ADP_learning_fig21_1() {
		
		cwe.executeTrials(2000);
		
		Map<Cell<Double>, Double> U = padpa.getUtility();

		Assert.assertNotNull(U.get(cw.getCellAt(1, 1)));
		// Note:
		// These are not reachable when starting at 1,1 using
		// the policy and default transition model
		// (i.e. 80% intended, 10% each right angle from intended).
		Assert.assertNull(U.get(cw.getCellAt(3, 1)));
		Assert.assertNull(U.get(cw.getCellAt(4, 1)));
		Assert.assertEquals(9, U.size());

		double DELTA_THRESHOLD = 1e-1;

		Assert.assertEquals(0.705, U.get(cw.getCellAt(1, 1)), DELTA_THRESHOLD);
		Assert.assertEquals(0.762, U.get(cw.getCellAt(1, 2)), DELTA_THRESHOLD);
		Assert.assertEquals(0.812, U.get(cw.getCellAt(1, 3)), DELTA_THRESHOLD);

		Assert.assertEquals(0.655, U.get(cw.getCellAt(2, 1)), DELTA_THRESHOLD);
		Assert.assertEquals(0.868, U.get(cw.getCellAt(2, 3)), DELTA_THRESHOLD);

		// Note: Not reachable based on transition model
		// Assert.assertEquals(0.611, U.get(cw.getCellAt(3, 1)),
		// DELTA_THRESHOLD);
		Assert.assertEquals(0.660, U.get(cw.getCellAt(3, 2)), DELTA_THRESHOLD);
		Assert.assertEquals(0.918, U.get(cw.getCellAt(3, 3)), DELTA_THRESHOLD);

		// Note: Not reachable based on transition model
		// Assert.assertEquals(0.388, U.get(cw.getCellAt(4, 1)),
		// DELTA_THRESHOLD);
		Assert.assertEquals(-1.0, U.get(cw.getCellAt(4, 2)), DELTA_THRESHOLD);
		Assert.assertEquals(1.0, U.get(cw.getCellAt(4, 3)), DELTA_THRESHOLD);
	}

	@Ignore
	@Test
	public void test_ADP_learning_rate_fig21_3() {
		test_utility_learning_rates(padpa, 20, 100, 100, 1);
	}
}
