package aima.test.probreasoningtest;

import junit.framework.TestCase;
import aima.probability.reasoning.HMMFactory;
import aima.probability.reasoning.HiddenMarkovModel;
import aima.probability.reasoning.HmmConstants;

public class HMMTest extends TestCase {
	private static final double TOLERANCE = 0.001;
	private HiddenMarkovModel robot;

	public void setUp(){

		robot = HMMFactory.createRobotHMM();
	}
	
	public void testInitialization(){
		assertEquals(0.5, robot.prior().getProbabilityOf(HmmConstants.OPEN));
		assertEquals(0.5, robot.prior().getProbabilityOf(HmmConstants.CLOSED));
		
		
		assertEquals(0.5, robot.belief().getProbabilityOf(HmmConstants.OPEN));
		assertEquals(0.5, robot.belief().getProbabilityOf(HmmConstants.CLOSED));
	}
	
	public void testPredictionAndMeasurementUpdateStepsModifyBeliefCorrectly(){
		
		assertEquals(0.5, robot.belief().getProbabilityOf(HmmConstants.OPEN));
		assertEquals(0.5, robot.belief().getProbabilityOf(HmmConstants.CLOSED));
		
		robot.act("do_nothing");
		assertEquals(0.5, robot.belief().getProbabilityOf(HmmConstants.OPEN));
		assertEquals(0.5, robot.belief().getProbabilityOf(HmmConstants.CLOSED));
		
		robot.perceptionUpDate("see_open");
		assertEquals(0.75, robot.belief().getProbabilityOf(HmmConstants.OPEN),TOLERANCE);
		assertEquals(0.25, robot.belief().getProbabilityOf(HmmConstants.CLOSED),TOLERANCE);
		
		robot.act("push");
		assertEquals(0.95, robot.belief().getProbabilityOf(HmmConstants.OPEN));
		assertEquals(0.05, robot.belief().getProbabilityOf(HmmConstants.CLOSED));
		
		robot.perceptionUpDate("see_open");
		assertEquals(0.983, robot.belief().getProbabilityOf(HmmConstants.OPEN),TOLERANCE);
		assertEquals(0.017, robot.belief().getProbabilityOf(HmmConstants.CLOSED),TOLERANCE);
		
		
	}
}
