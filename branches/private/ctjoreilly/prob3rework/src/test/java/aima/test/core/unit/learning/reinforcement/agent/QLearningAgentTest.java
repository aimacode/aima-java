package aima.test.core.unit.learning.reinforcement.agent;

import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.environment.cellworld.next.Cell;
import aima.core.environment.cellworld.next.CellWorld;
import aima.core.environment.cellworld.next.CellWorldAction;
import aima.core.environment.cellworld.next.CellWorldFactory;
import aima.core.learning.reinforcement.next.agent.QLearningAgent;
import aima.core.learning.reinforcement.next.example.CellWorldEnvironment;
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
				MDPFactory
						.createCellWorldPossibleOutcomesFunctionForFig17_1(cw),
				MDPFactory.createTransitionProbabilityFunctionForFigure17_1(cw),
				new JavaRandomizer());

		qla = new QLearningAgent<Cell<Double>, CellWorldAction>(
				MDPFactory.createActionsFunctionForFigure17_1(cw),
				CellWorldAction.actions(),
				CellWorldAction.None,
				0.4, 1.0,
				5, 2.0);
	
		cwe.addAgent(qla);
	}

	@Test
	public void test_Q_learning() {
		
		cwe.executeTrials(1000000);
		
		Map<Cell<Double>, Double> U = qla.getUtility();

		Assert.assertEquals(11, U.size());

		double DELTA_THRESHOLD = 1e-1;

		Assert.assertEquals(0.705, U.get(cw.getCellAt(1, 1)), DELTA_THRESHOLD);
		Assert.assertEquals(0.762, U.get(cw.getCellAt(1, 2)), DELTA_THRESHOLD);
		Assert.assertEquals(0.812, U.get(cw.getCellAt(1, 3)), DELTA_THRESHOLD);

		Assert.assertEquals(0.655, U.get(cw.getCellAt(2, 1)), DELTA_THRESHOLD);
		Assert.assertEquals(0.868, U.get(cw.getCellAt(2, 3)), DELTA_THRESHOLD);

		Assert.assertEquals(0.611, U.get(cw.getCellAt(3, 1)), DELTA_THRESHOLD);
		Assert.assertEquals(0.660, U.get(cw.getCellAt(3, 2)), DELTA_THRESHOLD);
		Assert.assertEquals(0.918, U.get(cw.getCellAt(3, 3)), DELTA_THRESHOLD);

		Assert.assertEquals(0.388, U.get(cw.getCellAt(4, 1)), DELTA_THRESHOLD);
		Assert.assertEquals(-1.0, U.get(cw.getCellAt(4, 2)), DELTA_THRESHOLD);
		Assert.assertEquals(1.0, U.get(cw.getCellAt(4, 3)), DELTA_THRESHOLD);
	}

	@Test
	public void test_Q_learning_rate() {
		test_utility_learning_rates(qla, 20, 10000, 10000);
	}
}
