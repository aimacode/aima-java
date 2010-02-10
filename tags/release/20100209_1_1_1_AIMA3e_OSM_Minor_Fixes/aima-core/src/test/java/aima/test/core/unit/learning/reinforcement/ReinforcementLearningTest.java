package aima.test.core.unit.learning.reinforcement;

import java.util.Hashtable;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.environment.cellworld.CellWorld;
import aima.core.environment.cellworld.CellWorldPosition;
import aima.core.learning.reinforcement.PassiveADPAgent;
import aima.core.learning.reinforcement.PassiveTDAgent;
import aima.core.learning.reinforcement.QLearningAgent;
import aima.core.learning.reinforcement.QTable;
import aima.core.probability.Randomizer;
import aima.core.probability.decision.MDP;
import aima.core.probability.decision.MDPFactory;
import aima.core.probability.decision.MDPPerception;
import aima.core.probability.decision.MDPPolicy;
import aima.core.probability.decision.MDPUtilityFunction;
import aima.core.util.datastructure.Pair;
import aima.test.core.unit.probability.MockRandomizer;

/**
 * @author Ravi Mohan
 * 
 */
public class ReinforcementLearningTest {
	MDP<CellWorldPosition, String> fourByThree;

	MDPPolicy<CellWorldPosition, String> policy;

	@Before
	public void setUp() {
		fourByThree = MDPFactory.createFourByThreeMDP();

		policy = new MDPPolicy<CellWorldPosition, String>();

		policy.setAction(new CellWorldPosition(1, 1), CellWorld.UP);
		policy.setAction(new CellWorldPosition(1, 2), CellWorld.LEFT);
		policy.setAction(new CellWorldPosition(1, 3), CellWorld.LEFT);
		policy.setAction(new CellWorldPosition(1, 4), CellWorld.LEFT);

		policy.setAction(new CellWorldPosition(2, 1), CellWorld.UP);
		policy.setAction(new CellWorldPosition(2, 3), CellWorld.UP);

		policy.setAction(new CellWorldPosition(3, 1), CellWorld.RIGHT);
		policy.setAction(new CellWorldPosition(3, 2), CellWorld.RIGHT);
		policy.setAction(new CellWorldPosition(3, 3), CellWorld.RIGHT);
	}

	@Test
	public void testPassiveADPAgent() {

		PassiveADPAgent<CellWorldPosition, String> agent = new PassiveADPAgent<CellWorldPosition, String>(
				fourByThree, policy);

		// Randomizer r = new JavaRandomizer();
		Randomizer r = new MockRandomizer(new double[] { 0.1, 0.9, 0.2, 0.8,
				0.3, 0.7, 0.4, 0.6, 0.5 });
		MDPUtilityFunction<CellWorldPosition> uf = null;
		for (int i = 0; i < 100; i++) {
			agent.executeTrial(r);
			uf = agent.getUtilityFunction();

		}

		Assert.assertEquals(0.676, uf.getUtility(new CellWorldPosition(1, 1)),
				0.001);
		Assert.assertEquals(0.626, uf.getUtility(new CellWorldPosition(1, 2)),
				0.001);
		Assert.assertEquals(0.573, uf.getUtility(new CellWorldPosition(1, 3)),
				0.001);
		Assert.assertEquals(0.519, uf.getUtility(new CellWorldPosition(1, 4)),
				0.001);

		Assert.assertEquals(0.746, uf.getUtility(new CellWorldPosition(2, 1)),
				0.001);
		Assert.assertEquals(0.865, uf.getUtility(new CellWorldPosition(2, 3)),
				0.001);
		// assertEquals(-1.0, uf.getUtility(new
		// CellWorldPosition(2,4)),0.001);//the pseudo random genrator never
		// gets to this square

		Assert.assertEquals(0.796, uf.getUtility(new CellWorldPosition(3, 1)),
				0.001);
		Assert.assertEquals(0.906, uf.getUtility(new CellWorldPosition(3, 3)),
				0.001);
		Assert.assertEquals(1.0, uf.getUtility(new CellWorldPosition(3, 4)),
				0.001);
	}

	@Test
	public void testPassiveTDAgent() {
		PassiveTDAgent<CellWorldPosition, String> agent = new PassiveTDAgent<CellWorldPosition, String>(
				fourByThree, policy);
		// Randomizer r = new JavaRandomizer();
		Randomizer r = new MockRandomizer(new double[] { 0.1, 0.9, 0.2, 0.8,
				0.3, 0.7, 0.4, 0.6, 0.5 });
		MDPUtilityFunction<CellWorldPosition> uf = null;
		for (int i = 0; i < 200; i++) {
			agent.executeTrial(r);
			uf = agent.getUtilityFunction();
			// System.out.println(uf);

		}

		Assert.assertEquals(0.662, uf.getUtility(new CellWorldPosition(1, 1)),
				0.001);
		Assert.assertEquals(0.610, uf.getUtility(new CellWorldPosition(1, 2)),
				0.001);
		Assert.assertEquals(0.553, uf.getUtility(new CellWorldPosition(1, 3)),
				0.001);
		Assert.assertEquals(0.496, uf.getUtility(new CellWorldPosition(1, 4)),
				0.001);

		Assert.assertEquals(0.735, uf.getUtility(new CellWorldPosition(2, 1)),
				0.001);
		Assert.assertEquals(0.835, uf.getUtility(new CellWorldPosition(2, 3)),
				0.001);
		// assertEquals(-1.0, uf.getUtility(new
		// CellWorldPosition(2,4)),0.001);//the pseudo random genrator never
		// gets to this square

		Assert.assertEquals(0.789, uf.getUtility(new CellWorldPosition(3, 1)),
				0.001);
		Assert.assertEquals(0.889, uf.getUtility(new CellWorldPosition(3, 3)),
				0.001);
		Assert.assertEquals(1.0, uf.getUtility(new CellWorldPosition(3, 4)),
				0.001);
	}

	@Test
	public void testQLearningAgent() {
		QLearningAgent<CellWorldPosition, String> qla = new QLearningAgent(
				fourByThree);
		Randomizer r = new MockRandomizer(new double[] { 0.1, 0.9, 0.2, 0.8,
				0.3, 0.7, 0.4, 0.6, 0.5 });

		// Randomizer r = new JavaRandomizer();
		Hashtable<Pair<CellWorldPosition, String>, Double> q = null;
		QTable<CellWorldPosition, String> qTable = null;
		for (int i = 0; i < 100; i++) {
			qla.executeTrial(r);
			q = qla.getQ();
			qTable = qla.getQTable();

		}
		// qTable.normalize();
		// System.out.println(qTable);
		// System.out.println(qTable.getPolicy());
	}

	@Test
	public void testFirstStepsOfQLAAgentUnderNormalProbability() {
		QLearningAgent<CellWorldPosition, String> qla = new QLearningAgent<CellWorldPosition, String>(
				fourByThree);

		Randomizer alwaysLessThanEightyPercent = new MockRandomizer(
				new double[] { 0.7 });
		CellWorldPosition startingPosition = new CellWorldPosition(1, 4);
		String action = qla.decideAction(new MDPPerception<CellWorldPosition>(
				startingPosition, -0.04));
		Assert.assertEquals(CellWorld.LEFT, action);
		Assert.assertEquals(0.0, qla.getQTable().getQValue(startingPosition,
				action), 0.001);

		qla.execute(action, alwaysLessThanEightyPercent);
		Assert.assertEquals(new CellWorldPosition(1, 3), qla.getCurrentState());
		Assert.assertEquals(-0.04, qla.getCurrentReward(), 0.001);
		Assert.assertEquals(0.0, qla.getQTable().getQValue(startingPosition,
				action), 0.001);
		String action2 = qla.decideAction(new MDPPerception<CellWorldPosition>(
				new CellWorldPosition(1, 3), -0.04));

		Assert.assertEquals(-0.04, qla.getQTable().getQValue(startingPosition,
				action), 0.001);
	}

	@Test
	public void testFirstStepsOfQLAAgentWhenFirstStepTerminates() {
		QLearningAgent<CellWorldPosition, String> qla = new QLearningAgent<CellWorldPosition, String>(
				fourByThree);

		CellWorldPosition startingPosition = new CellWorldPosition(1, 4);
		String action = qla.decideAction(new MDPPerception<CellWorldPosition>(
				startingPosition, -0.04));
		Assert.assertEquals(CellWorld.LEFT, action);

		Randomizer betweenEightyANdNinetyPercent = new MockRandomizer(
				new double[] { 0.85 }); // to force left to become an "up"
		qla.execute(action, betweenEightyANdNinetyPercent);
		Assert.assertEquals(new CellWorldPosition(2, 4), qla.getCurrentState());
		Assert.assertEquals(-1.0, qla.getCurrentReward(), 0.001);
		Assert.assertEquals(0.0, qla.getQTable().getQValue(startingPosition,
				action), 0.001);
		String action2 = qla.decideAction(new MDPPerception<CellWorldPosition>(
				new CellWorldPosition(2, 4), -1));
		Assert.assertNull(action2);
		Assert.assertEquals(-1.0, qla.getQTable().getQValue(startingPosition,
				action), 0.001);
	}
}
