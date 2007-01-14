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
		assertEquals(0.5, hmm.prior().getProbabilityOf("open"));
		assertEquals(0.5, hmm.prior().getProbabilityOf("closed"));
		assertEquals(0.5, hmm.belief().getProbabilityOf("open"));
		assertEquals(0.5, hmm.belief().getProbabilityOf("closed"));
	}
	
	public void xtestPredictionAndMeasurementUpdateStepsModifyBeliefCorrectly(){
		
	}
}
