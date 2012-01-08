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
import aima.core.learning.reinforcement.agent.PassiveTDAgent;
import aima.core.learning.reinforcement.example.CellWorldEnvironment;
import aima.core.probability.example.MDPFactory;
import aima.core.util.JavaRandomizer;

public class PassiveTDAgentTest extends ReinforcementLearningAgentTest {
	//
	private CellWorld<Double> cw = null;
	private CellWorldEnvironment cwe = null;
	private PassiveTDAgent<Cell<Double>, CellWorldAction> ptda = null;

	@Before
	public void setUp() {
		cw = CellWorldFactory.createCellWorldForFig17_1();
		cwe = new CellWorldEnvironment(
				cw.getCellAt(1, 1),
				cw.getCells(),
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

		ptda = new PassiveTDAgent<Cell<Double>, CellWorldAction>(fixedPolicy,
				0.2, 1.0);

		cwe.addAgent(ptda);
	}

	@Test
	public void test_TD_learning_fig21_1() {

		ptda.reset();
		cwe.executeTrials(10000);

		Map<Cell<Double>, Double> U = ptda.getUtility();

		Assert.assertNotNull(U.get(cw.getCellAt(1, 1)));

		// Note:
		// These are not reachable when starting at 1,1 using
		// the policy and default transition model
		// (i.e. 80% intended, 10% each right angle from intended).
		Assert.assertNull(U.get(cw.getCellAt(3, 1)));
		Assert.assertNull(U.get(cw.getCellAt(4, 1)));
		Assert.assertEquals(9, U.size());

		// Note: Due to stochastic nature of environment,
		// will not test the individual utilities calculated
		// as this will take a fair amount of time.
		// Instead we will check if the RMS error in utility
		// for 1,1 is below a reasonable threshold.
		test_RMSeiu_for_1_1(ptda, 20, 1000, 0.07);
	}

	// Note: Enable this test if you wish to generate tables for
	// creating figures, in a spreadsheet, of the learning
	// rate of the agent.
	@Ignore
	@Test
	public void test_TD_learning_rate_fig21_5() {
		test_utility_learning_rates(ptda, 20, 500, 100, 1);
	}
}
