package aima.test.core.unit.probability.reasoning;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.probability.reasoning.HMMAgent;
import aima.core.probability.reasoning.HMMFactory;
import aima.core.probability.reasoning.HmmConstants;

/**
 * @author Ravi Mohan
 * 
 */
public class HMMAgentTest {
	private static final double TOLERANCE = 0.001;

	private HMMAgent robot, rainman;

	@Before
	public void setUp() {

		robot = new HMMAgent(HMMFactory.createRobotHMM());
		rainman = new HMMAgent(HMMFactory.createRainmanHMM());
	}

	@Test
	public void testRobotInitialization() {
		Assert.assertEquals(0.5, robot.belief().getProbabilityOf(
				HmmConstants.DOOR_OPEN), 0.001);
		Assert.assertEquals(0.5, robot.belief().getProbabilityOf(
				HmmConstants.DOOR_CLOSED), 0.001);
	}

	@Test
	public void testRobotHMMPredictionAndMeasurementUpdateStepsModifyBeliefCorrectly() {

		Assert.assertEquals(0.5, robot.belief().getProbabilityOf(
				HmmConstants.DOOR_OPEN), 0.001);
		Assert.assertEquals(0.5, robot.belief().getProbabilityOf(
				HmmConstants.DOOR_CLOSED), 0.001);

		robot.act(HmmConstants.DO_NOTHING);
		Assert.assertEquals(0.5, robot.belief().getProbabilityOf(
				HmmConstants.DOOR_OPEN), 0.001);
		Assert.assertEquals(0.5, robot.belief().getProbabilityOf(
				HmmConstants.DOOR_CLOSED), 0.001);

		robot.perceive(HmmConstants.SEE_DOOR_OPEN);
		Assert.assertEquals(0.75, robot.belief().getProbabilityOf(
				HmmConstants.DOOR_OPEN), TOLERANCE);
		Assert.assertEquals(0.25, robot.belief().getProbabilityOf(
				HmmConstants.DOOR_CLOSED), TOLERANCE);

		robot.act(HmmConstants.PUSH_DOOR);
		Assert.assertEquals(0.95, robot.belief().getProbabilityOf(
				HmmConstants.DOOR_OPEN), 0.001);
		Assert.assertEquals(0.05, robot.belief().getProbabilityOf(
				HmmConstants.DOOR_CLOSED), 0.001);

		robot.perceive(HmmConstants.SEE_DOOR_OPEN);
		Assert.assertEquals(0.983, robot.belief().getProbabilityOf(
				HmmConstants.DOOR_OPEN), TOLERANCE);
		Assert.assertEquals(0.017, robot.belief().getProbabilityOf(
				HmmConstants.DOOR_CLOSED), TOLERANCE);
	}

	@Test
	public void testRainmanInitialization() {
		Assert.assertEquals(0.5, rainman.belief().getProbabilityOf(
				HmmConstants.RAINING), 0.001);
		Assert.assertEquals(0.5, rainman.belief().getProbabilityOf(
				HmmConstants.NOT_RAINING), 0.001);
	}

	@Test
	public void testRainmanHMMPredictionAndMeasurementUpdateStepsModifyBeliefCorrectly() {
		Assert.assertEquals(0.5, rainman.belief().getProbabilityOf(
				HmmConstants.RAINING), 0.001);
		Assert.assertEquals(0.5, rainman.belief().getProbabilityOf(
				HmmConstants.NOT_RAINING), 0.001);

		rainman.waitWithoutActing();
		Assert.assertEquals(0.5, rainman.belief().getProbabilityOf(
				HmmConstants.RAINING), 0.001);
		Assert.assertEquals(0.5, rainman.belief().getProbabilityOf(
				HmmConstants.NOT_RAINING), 0.001);

		rainman.perceive(HmmConstants.SEE_UMBRELLA);
		Assert.assertEquals(0.818, rainman.belief().getProbabilityOf(
				HmmConstants.RAINING), TOLERANCE);
		Assert.assertEquals(0.182, rainman.belief().getProbabilityOf(
				HmmConstants.NOT_RAINING), TOLERANCE);

		rainman.waitWithoutActing();
		Assert.assertEquals(0.627, rainman.belief().getProbabilityOf(
				HmmConstants.RAINING), TOLERANCE);
		Assert.assertEquals(0.373, rainman.belief().getProbabilityOf(
				HmmConstants.NOT_RAINING), TOLERANCE);

		rainman.perceive(HmmConstants.SEE_UMBRELLA);
		Assert.assertEquals(0.883, rainman.belief().getProbabilityOf(
				HmmConstants.RAINING), TOLERANCE);
		Assert.assertEquals(0.117, rainman.belief().getProbabilityOf(
				HmmConstants.NOT_RAINING), TOLERANCE);
	}
}
