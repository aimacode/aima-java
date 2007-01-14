package aima.test.probreasoningtest;

import junit.framework.TestCase;
import aima.probability.reasoning.HMMFactory;
import aima.probability.reasoning.HiddenMarkovModel;

public class HMMTest extends TestCase {
	private static final double TOLERANCE = 0.001;
	private HiddenMarkovModel robot;

	public void setUp(){

		robot = HMMFactory.createRobotHMM();
	}
	
	public void testInitialization(){
		assertEquals(0.5, robot.prior().getProbabilityOf("open"));
		assertEquals(0.5, robot.prior().getProbabilityOf("closed"));
		
		
		assertEquals(0.5, robot.belief().getProbabilityOf("open"));
		assertEquals(0.5, robot.belief().getProbabilityOf("closed"));
	}
	
	public void testPredictionAndMeasurementUpdateStepsModifyBeliefCorrectly(){
		
		assertEquals(0.5, robot.belief().getProbabilityOf("open"));
		assertEquals(0.5, robot.belief().getProbabilityOf("closed"));
		
		robot.act("do_nothing");
		assertEquals(0.5, robot.belief().getProbabilityOf("open"));
		assertEquals(0.5, robot.belief().getProbabilityOf("closed"));
		
		robot.perceptionUpDate("see_open");
		assertEquals(0.75, robot.belief().getProbabilityOf("open"),TOLERANCE);
		assertEquals(0.25, robot.belief().getProbabilityOf("closed"),TOLERANCE);
		
		robot.act("push");
		assertEquals(0.95, robot.belief().getProbabilityOf("open"));
		assertEquals(0.05, robot.belief().getProbabilityOf("closed"));
		
		robot.perceptionUpDate("see_open");
		assertEquals(0.983, robot.belief().getProbabilityOf("open"),TOLERANCE);
		assertEquals(0.017, robot.belief().getProbabilityOf("closed"),TOLERANCE);
		
		
	}
}
