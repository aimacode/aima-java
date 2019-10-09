package aima.test.core.unit.learning.reinforcement.agent;

import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import aima.core.environment.cellworld.Cell;
import aima.core.environment.cellworld.CellWorld;
import aima.core.environment.cellworld.CellWorldAction;
import aima.core.environment.cellworld.CellWorldFactory;
import aima.core.learning.reinforcement.agent.QLearningAgent;
import aima.core.learning.reinforcement.example.CellWorldEnvironment;
import aima.core.probability.example.MDPFactory;
import aima.core.util.JavaRandomizer;

public class QLearningAgentTest extends ReinforcementLearningAgentTest {
	//
	private CellWorld<Double> cw = null;
	private CellWorldEnvironment cwe = null;
	private QLearningAgent<Cell<Double>, CellWorldAction> qla = null;

	@Before
	public void setUp() {
		cw = CellWorldFactory.createCellWorldForFig17_1();
		cwe = new CellWorldEnvironment(
				cw.getCellAt(1, 1),
				cw.getCells(),
				MDPFactory.createTransitionProbabilityFunctionForFigure17_1(cw),
				new JavaRandomizer());

		qla = new QLearningAgent<>(MDPFactory
				.createActionsFunctionForFigure17_1(cw), 0.2, 1.0, 5, 2.0);

		cwe.addAgent(qla);
	}

	@Test
	public void test_Q_learning() {

		qla.reset();
		cwe.executeTrials(100000);

		Map<Cell<Double>, Double> U = qla.getUtility();

		Assert.assertNotNull(U.get(cw.getCellAt(1, 1)));

		// Note:
		// As the Q-Learning Agent is not using a fixed
		// policy it should with a reasonable number
		// of iterations observe and calculate an
		// approximate utility for all of the states.
		Assert.assertEquals(11, U.size());

		// Note: Due to stochastic nature of environment,
		// will not test the individual utilities calculated
		// as this will take a fair amount of time.
		// Instead we will check if the RMS error in utility
		// for 1,1 is below a reasonable threshold.
		test_RMSeiu_for_1_1(qla, 20, 10000, 0.2);
	}

	// Note: Enable this test if you wish to generate tables for
	// creating figures, in a spreadsheet, of the learning
	// rate of the agent.
	@Ignore
	@Test
	public void test_Q_learning_rate() {
		test_utility_learning_rates(qla, 20, 10000, 500, 20);
	}
}
