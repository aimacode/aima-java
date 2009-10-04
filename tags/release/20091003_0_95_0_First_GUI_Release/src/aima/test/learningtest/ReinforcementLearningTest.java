package aima.test.learningtest;

import java.util.Hashtable;

import junit.framework.TestCase;
import aima.learning.reinforcement.PassiveADPAgent;
import aima.learning.reinforcement.PassiveTDAgent;
import aima.learning.reinforcement.QLearningAgent;
import aima.learning.reinforcement.QTable;
import aima.probability.Randomizer;
import aima.probability.decision.MDP;
import aima.probability.decision.MDPFactory;
import aima.probability.decision.MDPPerception;
import aima.probability.decision.MDPPolicy;
import aima.probability.decision.MDPUtilityFunction;
import aima.probability.decision.cellworld.CellWorld;
import aima.probability.decision.cellworld.CellWorldPosition;
import aima.test.probabilitytest.MockRandomizer;
import aima.util.Pair;

/**
 * @author Ravi Mohan
 * 
 */
public class ReinforcementLearningTest extends TestCase {
	MDP<CellWorldPosition, String> fourByThree;

	MDPPolicy<CellWorldPosition, String> policy;

	@Override
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

		assertEquals(0.676, uf.getUtility(new CellWorldPosition(1, 1)), 0.001);
		assertEquals(0.626, uf.getUtility(new CellWorldPosition(1, 2)), 0.001);
		assertEquals(0.573, uf.getUtility(new CellWorldPosition(1, 3)), 0.001);
		assertEquals(0.519, uf.getUtility(new CellWorldPosition(1, 4)), 0.001);

		assertEquals(0.746, uf.getUtility(new CellWorldPosition(2, 1)), 0.001);
		assertEquals(0.865, uf.getUtility(new CellWorldPosition(2, 3)), 0.001);
		// assertEquals(-1.0, uf.getUtility(new
		// CellWorldPosition(2,4)),0.001);//the pseudo random genrator never
		// gets to this square

		assertEquals(0.796, uf.getUtility(new CellWorldPosition(3, 1)), 0.001);
		assertEquals(0.906, uf.getUtility(new CellWorldPosition(3, 3)), 0.001);
		assertEquals(1.0, uf.getUtility(new CellWorldPosition(3, 4)), 0.001);
	}

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

		assertEquals(0.662, uf.getUtility(new CellWorldPosition(1, 1)), 0.001);
		assertEquals(0.610, uf.getUtility(new CellWorldPosition(1, 2)), 0.001);
		assertEquals(0.553, uf.getUtility(new CellWorldPosition(1, 3)), 0.001);
		assertEquals(0.496, uf.getUtility(new CellWorldPosition(1, 4)), 0.001);

		assertEquals(0.735, uf.getUtility(new CellWorldPosition(2, 1)), 0.001);
		assertEquals(0.835, uf.getUtility(new CellWorldPosition(2, 3)), 0.001);
		// assertEquals(-1.0, uf.getUtility(new
		// CellWorldPosition(2,4)),0.001);//the pseudo random genrator never
		// gets to this square

		assertEquals(0.789, uf.getUtility(new CellWorldPosition(3, 1)), 0.001);
		assertEquals(0.889, uf.getUtility(new CellWorldPosition(3, 3)), 0.001);
		assertEquals(1.0, uf.getUtility(new CellWorldPosition(3, 4)), 0.001);
	}

	public void xtestQLearningAgent() {
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
		System.out.println(qTable);
		System.out.println(qTable.getPolicy());

		// first step

	}

	public void testFirstStepsOfQLAAgentUnderNormalProbability() {
		QLearningAgent<CellWorldPosition, String> qla = new QLearningAgent<CellWorldPosition, String>(
				fourByThree);

		Randomizer alwaysLessThanEightyPercent = new MockRandomizer(
				new double[] { 0.7 });
		CellWorldPosition startingPosition = new CellWorldPosition(1, 4);
		String action = qla.decideAction(new MDPPerception<CellWorldPosition>(
				startingPosition, -0.04));
		assertEquals(CellWorld.LEFT, action);
		assertEquals(0.0, qla.getQTable().getQValue(startingPosition, action));

		qla.execute(action, alwaysLessThanEightyPercent);
		assertEquals(new CellWorldPosition(1, 3), qla.getCurrentState());
		assertEquals(-0.04, qla.getCurrentReward());
		assertEquals(0.0, qla.getQTable().getQValue(startingPosition, action));
		String action2 = qla.decideAction(new MDPPerception<CellWorldPosition>(
				new CellWorldPosition(1, 3), -0.04));

		assertEquals(-0.04, qla.getQTable().getQValue(startingPosition, action));

	}

	public void testFirstStepsOfQLAAgentWhenFirstStepTerminates() {
		QLearningAgent<CellWorldPosition, String> qla = new QLearningAgent<CellWorldPosition, String>(
				fourByThree);

		CellWorldPosition startingPosition = new CellWorldPosition(1, 4);
		String action = qla.decideAction(new MDPPerception<CellWorldPosition>(
				startingPosition, -0.04));
		assertEquals(CellWorld.LEFT, action);

		Randomizer betweenEightyANdNinetyPercent = new MockRandomizer(
				new double[] { 0.85 }); // to force left to become an "up"
		qla.execute(action, betweenEightyANdNinetyPercent);
		assertEquals(new CellWorldPosition(2, 4), qla.getCurrentState());
		assertEquals(-1.0, qla.getCurrentReward());
		assertEquals(0.0, qla.getQTable().getQValue(startingPosition, action));
		String action2 = qla.decideAction(new MDPPerception<CellWorldPosition>(
				new CellWorldPosition(2, 4), -1));
		assertNull(action2);
		assertEquals(-1.0, qla.getQTable().getQValue(startingPosition, action));
	}

}
