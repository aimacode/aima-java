package aima.test.probdecisiontest;

import junit.framework.TestCase;
import aima.probability.decision.MDP;
import aima.probability.decision.MDPFactory;

public class ValueIterationTest extends TestCase {
	private MDP fourByThreeMDP;
	public void setUp(){
		fourByThreeMDP = MDPFactory.createFourByThreeMDP();
	}
}
