package aima.test.learningtest;

import junit.framework.TestCase;
import aima.learning.reinforcement.PassiveADPAgent;
import aima.probability.JavaRandomizer;
import aima.probability.Randomizer;
import aima.probability.decision.MDP;
import aima.probability.decision.MDPFactory;
import aima.probability.decision.MDPPolicy;
import aima.probability.decision.MDPUtilityFunction;
import aima.probability.decision.cellworld.CellWorld;
import aima.probability.decision.cellworld.CellWorldPosition;
import aima.test.probabilitytest.MockRandomizer;

public class ReinforcementLearningTest extends TestCase {
	MDP<CellWorldPosition, String> fourByThree;

	MDPPolicy<CellWorldPosition, String> policy;



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

		//Randomizer r =  new JavaRandomizer();
		Randomizer r =  new MockRandomizer(new double[]{0.1,0.9,0.2,0.8,0.3,0.7,0.4,0.6,0.5});
		MDPUtilityFunction<CellWorldPosition> uf = null;
		for (int i = 0; i < 100; i++) {
			agent.executeTrial(r);
			uf = agent.getUtilityFunction();
			
			
		}
		
		assertEquals(0.676, uf.getUtility(new CellWorldPosition(1,1)),0.001);
		assertEquals(0.626, uf.getUtility(new CellWorldPosition(1,2)),0.001);
		assertEquals(0.573, uf.getUtility(new CellWorldPosition(1,3)),0.001);
		assertEquals(0.519, uf.getUtility(new CellWorldPosition(1,4)),0.001);
		
		assertEquals(0.746, uf.getUtility(new CellWorldPosition(2,1)),0.001);
		assertEquals(0.865, uf.getUtility(new CellWorldPosition(2,3)),0.001);
		//assertEquals(-1.0, uf.getUtility(new CellWorldPosition(2,4)),0.001);//the pseudo random genrator never gets to this square

		assertEquals(0.796, uf.getUtility(new CellWorldPosition(3,1)),0.001);
		assertEquals(0.906, uf.getUtility(new CellWorldPosition(3,3)),0.001);
		assertEquals(1.0, uf.getUtility(new CellWorldPosition(3,4)),0.001);
	}

}
