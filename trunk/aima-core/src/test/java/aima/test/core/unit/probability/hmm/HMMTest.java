package aima.test.core.unit.probability.hmm;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.probability.hmm.FixedLagSmoothing;
import aima.core.probability.hmm.HMMFactory;
import aima.core.probability.hmm.HiddenMarkovModel;
import aima.core.probability.hmm.HmmConstants;
import aima.core.probability.hmm.VarDistribution;

/**
 * @author Ravi Mohan
 * 
 */
public class HMMTest {
	private HiddenMarkovModel robotHmm, rainmanHmm;

	private static final double TOLERANCE = 0.001;

	@Before
	public void setUp() {
		robotHmm = HMMFactory.createRobotHMM();
		rainmanHmm = HMMFactory.createRainmanHMM();
	}

	@Test
	public void testRobotHMMInitialization() {

		Assert.assertEquals(0.5, robotHmm.prior().getProbabilityOf(
				HmmConstants.DOOR_OPEN), 0.001);
		Assert.assertEquals(0.5, robotHmm.prior().getProbabilityOf(
				HmmConstants.DOOR_CLOSED), 0.001);
	}

	@Test
	public void testRainmanHmmInitialization() {

		Assert.assertEquals(0.5, rainmanHmm.prior().getProbabilityOf(
				HmmConstants.RAINING), 0.001);
		Assert.assertEquals(0.5, rainmanHmm.prior().getProbabilityOf(
				HmmConstants.NOT_RAINING), 0.001);
	}

	@Test
	public void testForwardMessagingWorksForFiltering() {
		VarDistribution afterOneStep = robotHmm.forward(robotHmm.prior(),
				HmmConstants.DO_NOTHING, HmmConstants.SEE_DOOR_OPEN);
		Assert.assertEquals(0.75, afterOneStep
				.getProbabilityOf(HmmConstants.DOOR_OPEN), TOLERANCE);
		Assert.assertEquals(0.25, afterOneStep
				.getProbabilityOf(HmmConstants.DOOR_CLOSED), TOLERANCE);

		VarDistribution afterTwoSteps = robotHmm.forward(afterOneStep,
				HmmConstants.PUSH_DOOR, HmmConstants.SEE_DOOR_OPEN);
		Assert.assertEquals(0.983, afterTwoSteps
				.getProbabilityOf(HmmConstants.DOOR_OPEN), TOLERANCE);
		Assert.assertEquals(0.017, afterTwoSteps
				.getProbabilityOf(HmmConstants.DOOR_CLOSED), TOLERANCE);
	}

	@Test
	public void testRecursiveBackwardMessageCalculationIsCorrect() {
		VarDistribution afterOneStep = rainmanHmm.forward(rainmanHmm.prior(),
				HmmConstants.DO_NOTHING, HmmConstants.SEE_UMBRELLA);
		VarDistribution afterTwoSteps = rainmanHmm.forward(afterOneStep,
				HmmConstants.DO_NOTHING, HmmConstants.SEE_UMBRELLA);

		VarDistribution postSequence = afterTwoSteps.duplicate()
				.createUnitBelief();

		VarDistribution smoothed = rainmanHmm.calculate_next_backward_message(
				postSequence, HmmConstants.SEE_UMBRELLA);
		Assert.assertEquals(0.627, smoothed
				.getProbabilityOf(HmmConstants.RAINING), TOLERANCE);
		Assert.assertEquals(0.373, smoothed
				.getProbabilityOf(HmmConstants.NOT_RAINING), TOLERANCE);
	}

	@Test
	public void testForwardBackwardOnRainmanHmm() {
		List<String> perceptions = new ArrayList<String>();
		perceptions.add(HmmConstants.SEE_UMBRELLA);
		perceptions.add(HmmConstants.SEE_UMBRELLA);

		List<VarDistribution> results = rainmanHmm.forward_backward(perceptions);
		Assert.assertEquals(3, results.size());

		Assert.assertNull(results.get(0));
		VarDistribution smoothedDayOne = results.get(1);
		Assert.assertEquals(0.883, smoothedDayOne
				.getProbabilityOf(HmmConstants.RAINING), TOLERANCE);
		Assert.assertEquals(0.117, smoothedDayOne
				.getProbabilityOf(HmmConstants.NOT_RAINING), TOLERANCE);

		VarDistribution smoothedDayTwo = results.get(2);
		Assert.assertEquals(0.883, smoothedDayTwo
				.getProbabilityOf(HmmConstants.RAINING), TOLERANCE);
		Assert.assertEquals(0.117, smoothedDayTwo
				.getProbabilityOf(HmmConstants.NOT_RAINING), TOLERANCE);
	}

	@Test
	public void testForwardBackwardOnRainmanHmmFor3daysData() {
		List<String> perceptions = new ArrayList<String>();
		perceptions.add(HmmConstants.SEE_UMBRELLA);
		perceptions.add(HmmConstants.SEE_UMBRELLA);
		perceptions.add(HmmConstants.SEE_NO_UMBRELLA);

		List<VarDistribution> results = rainmanHmm.forward_backward(perceptions);
		Assert.assertEquals(4, results.size());
		Assert.assertNull(results.get(0));

		VarDistribution smoothedDayOne = results.get(1);
		Assert.assertEquals(0.861, smoothedDayOne
				.getProbabilityOf(HmmConstants.RAINING), TOLERANCE);
		Assert.assertEquals(0.138, smoothedDayOne
				.getProbabilityOf(HmmConstants.NOT_RAINING), TOLERANCE);

		VarDistribution smoothedDayTwo = results.get(2);
		Assert.assertEquals(0.799, smoothedDayTwo
				.getProbabilityOf(HmmConstants.RAINING), TOLERANCE);
		Assert.assertEquals(0.201, smoothedDayTwo
				.getProbabilityOf(HmmConstants.NOT_RAINING), TOLERANCE);

		VarDistribution smoothedDayThree = results.get(3);
		Assert.assertEquals(0.190, smoothedDayThree
				.getProbabilityOf(HmmConstants.RAINING), TOLERANCE);
		Assert.assertEquals(0.810, smoothedDayThree
				.getProbabilityOf(HmmConstants.NOT_RAINING), TOLERANCE);
	}

	@Test
	public void testForwardBackwardAndFixedLagSmoothingGiveSameResults() {
		List<String> perceptions = new ArrayList<String>();

		String dayOnePerception = HmmConstants.SEE_UMBRELLA;
		String dayTwoPerception = HmmConstants.SEE_UMBRELLA;
		String dayThreePerception = HmmConstants.SEE_NO_UMBRELLA;

		perceptions.add(dayOnePerception);
		perceptions.add(dayTwoPerception);
		perceptions.add(dayThreePerception);

		List<VarDistribution> fbResults = rainmanHmm
				.forward_backward(perceptions);
		Assert.assertEquals(4, fbResults.size());

		// RandomVariable fbDayOneResult = fbResults.get(1);
		// System.out.println(fbDayOneResult);

		FixedLagSmoothing fls = new FixedLagSmoothing(rainmanHmm, 2);

		Assert.assertNull(fls.smooth(dayOnePerception));
		// System.out.println(fls.smooth(dayTwoPerception));
		// RandomVariable flsDayoneResult = fls.smooth(dayThreePerception);
		// System.out.println(flsDayoneResult);
	}

	@Test
	public void testOneStepFixedLagSmoothingOnRainManHmm() {
		FixedLagSmoothing fls = new FixedLagSmoothing(rainmanHmm, 1);

		VarDistribution smoothedDayZero = fls.smooth(HmmConstants.SEE_UMBRELLA); // see
		// umbrella on day one
		Assert.assertEquals(0.627, smoothedDayZero
				.getProbabilityOf(HmmConstants.RAINING), TOLERANCE);

		VarDistribution smoothedDayOne = fls.smooth(HmmConstants.SEE_UMBRELLA); // see
		// umbrella on day two
		Assert.assertEquals(0.883, smoothedDayOne
				.getProbabilityOf(HmmConstants.RAINING), TOLERANCE);
		Assert.assertEquals(0.117, smoothedDayOne
				.getProbabilityOf(HmmConstants.NOT_RAINING), TOLERANCE);

		VarDistribution smoothedDayTwo = fls
				.smooth(HmmConstants.SEE_NO_UMBRELLA); // see no umbrella on
		// day three
		Assert.assertEquals(0.799, smoothedDayTwo
				.getProbabilityOf(HmmConstants.RAINING), TOLERANCE);
		Assert.assertEquals(0.201, smoothedDayTwo
				.getProbabilityOf(HmmConstants.NOT_RAINING), TOLERANCE);
	}

	@Test
	public void testOneStepFixedLagSmoothingOnRainManHmmWithDifferingEvidence() {
		FixedLagSmoothing fls = new FixedLagSmoothing(rainmanHmm, 1);

		VarDistribution smoothedDayZero = fls.smooth(HmmConstants.SEE_UMBRELLA);// see
		// umbrella on day one
		Assert.assertEquals(0.627, smoothedDayZero
				.getProbabilityOf(HmmConstants.RAINING), TOLERANCE);

		VarDistribution smoothedDayOne = fls
				.smooth(HmmConstants.SEE_NO_UMBRELLA);// no umbrella on day
		// two
		Assert.assertEquals(0.702, smoothedDayOne
				.getProbabilityOf(HmmConstants.RAINING), TOLERANCE);
		Assert.assertEquals(0.297, smoothedDayOne
				.getProbabilityOf(HmmConstants.NOT_RAINING), TOLERANCE);
	}

	@Test
	public void testTwoStepFixedLagSmoothingOnRainManHmm() {
		FixedLagSmoothing fls = new FixedLagSmoothing(rainmanHmm, 2);

		VarDistribution smoothedOne = fls.smooth(HmmConstants.SEE_UMBRELLA); // see
		// umbrella on day one
		Assert.assertNull(smoothedOne);

		smoothedOne = fls.smooth(HmmConstants.SEE_UMBRELLA); // see
		// umbrella on day two
		Assert.assertEquals(0.653, smoothedOne
				.getProbabilityOf(HmmConstants.RAINING), TOLERANCE);
		Assert.assertEquals(0.346, smoothedOne
				.getProbabilityOf(HmmConstants.NOT_RAINING), TOLERANCE);

		VarDistribution smoothedTwo = fls.smooth(HmmConstants.SEE_UMBRELLA);// see
		// umbrella on day 3
		Assert.assertEquals(0.894, smoothedTwo
				.getProbabilityOf(HmmConstants.RAINING), TOLERANCE);
		Assert.assertEquals(0.105, smoothedTwo
				.getProbabilityOf(HmmConstants.NOT_RAINING), TOLERANCE);
	}
}
