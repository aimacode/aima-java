package aima.test.probdecisiontest;

import aima.probability.decision.MDP;
import aima.probability.decision.MDPFactory;
import aima.probability.decision.cellworld.CellWorldPosition;
import junit.framework.TestCase;

public class PolicyIterationTest extends TestCase {
	private MDP<CellWorldPosition,String> fourByThreeMDP;
	public void setUp(){
		fourByThreeMDP = MDPFactory.createFourByThreeMDP();
	}
	
}
