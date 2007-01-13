package aima.test.probreasoningtest;

import junit.framework.TestCase;
import aima.probability.reasoning.HMMFactory;
import aima.probability.reasoning.HiddenMarkovModel;

public class HMMTest extends TestCase {
	private HiddenMarkovModel hmm;

	public void setUp(){

		hmm = HMMFactory.createRobotHMM();
	}
	
	public void testInitialization(){
		assertEquals(0.5, hmm.prior().get("open","probability"));
		assertEquals(0.5, hmm.prior().get("closed","probability"));
	}
}
