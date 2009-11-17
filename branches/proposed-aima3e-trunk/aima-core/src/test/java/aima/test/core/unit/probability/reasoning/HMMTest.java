package aima.test.core.unit.probability.reasoning;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import aima.core.probability.RandomVariable;
import aima.core.probability.reasoning.FixedLagSmoothing;
import aima.core.probability.reasoning.HMMFactory;
import aima.core.probability.reasoning.HiddenMarkovModel;
import aima.core.probability.reasoning.HmmConstants;

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
		RandomVariable afterOneStep = robotHmm.forward(robotHmm.prior(),
				HmmConstants.DO_NOTHING, HmmConstants.SEE_DOOR_OPEN);
		Assert.assertEquals(0.75, afterOneStep
				.getProbabilityOf(HmmConstants.DOOR_OPEN), TOLERANCE);
		Assert.assertEquals(0.25, afterOneStep
				.getProbabilityOf(HmmConstants.DOOR_CLOSED), TOLERANCE);

		RandomVariable afterTwoSteps = robotHmm.forward(afterOneStep,
				HmmConstants.PUSH_DOOR, HmmConstants.SEE_DOOR_OPEN);
		Assert.assertEquals(0.983, afterTwoSteps
				.getProbabilityOf(HmmConstants.DOOR_OPEN), TOLERANCE);
		Assert.assertEquals(0.017, afterTwoSteps
				.getProbabilityOf(HmmConstants.DOOR_CLOSED), TOLERANCE);
	}

	@Test
	public void testRecursiveBackwardMessageCalculationIsCorrect() {
		RandomVariable afterOneStep = rainmanHmm.forward(rainmanHmm.prior(),
				HmmConstants.DO_NOTHING, HmmConstants.SEE_UMBRELLA);
		RandomVariable afterTwoSteps = rainmanHmm.forward(afterOneStep,
				HmmConstants.DO_NOTHING, HmmConstants.SEE_UMBRELLA);

		RandomVariable postSequence = afterTwoSteps.duplicate()
				.createUnitBelief();

		RandomVariable smoothed = rainmanHmm.calculate_next_backward_message(
				afterOneStep, postSequence, HmmConstants.SEE_UMBRELLA);
		Assert.assertEquals(0.883, smoothed
				.getProbabilityOf(HmmConstants.RAINING), TOLERANCE);
		Assert.assertEquals(0.117, smoothed
				.getProbabilityOf(HmmConstants.NOT_RAINING), TOLERANCE);
	}

	@Test
	public void testForwardBackwardOnRainmanHmm() {
		List<String> perceptions = new ArrayList<String>();
		perceptions.add(HmmConstants.SEE_UMBRELLA);
		perceptions.add(HmmConstants.SEE_UMBRELLA);

		List<RandomVariable> results = rainmanHmm.forward_backward(perceptions);
		Assert.assertEquals(3, results.size());

		Assert.assertNull(results.get(0));
		RandomVariable smoothedDayOne = results.get(1);
		Assert.assertEquals(0.982, smoothedDayOne
				.getProbabilityOf(HmmConstants.RAINING), TOLERANCE);
		Assert.assertEquals(0.018, smoothedDayOne
				.getProbabilityOf(HmmConstants.NOT_RAINING), TOLERANCE);

		RandomVariable smoothedDayTwo = results.get(2);
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

		List<RandomVariable> results = rainmanHmm.forward_backward(perceptions);
		Assert.assertEquals(4, results.size());
		Assert.assertNull(results.get(0));

		RandomVariable smoothedDayOne = results.get(1);
		Assert.assertEquals(0.964, smoothedDayOne
				.getProbabilityOf(HmmConstants.RAINING), TOLERANCE);
		Assert.assertEquals(0.036, smoothedDayOne
				.getProbabilityOf(HmmConstants.NOT_RAINING), TOLERANCE);

		RandomVariable smoothedDayTwo = results.get(2);
		Assert.assertEquals(0.484, smoothedDayTwo
				.getProbabilityOf(HmmConstants.RAINING), TOLERANCE);
		Assert.assertEquals(0.516, smoothedDayTwo
				.getProbabilityOf(HmmConstants.NOT_RAINING), TOLERANCE);

		RandomVariable smoothedDayThree = results.get(3);
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

		List<RandomVariable> fbResults = rainmanHmm
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

		RandomVariable smoothedDayZero = fls.smooth(HmmConstants.SEE_UMBRELLA); // see
		// umbrella on day one
		Assert.assertEquals(0.627, smoothedDayZero
				.getProbabilityOf(HmmConstants.RAINING), TOLERANCE);

		RandomVariable smoothedDayOne = fls.smooth(HmmConstants.SEE_UMBRELLA); // see
		// umbrella on day two
		Assert.assertEquals(0.883, smoothedDayOne
				.getProbabilityOf(HmmConstants.RAINING), TOLERANCE);
		Assert.assertEquals(0.117, smoothedDayOne
				.getProbabilityOf(HmmConstants.NOT_RAINING), TOLERANCE);

		RandomVariable smoothedDayTwo = fls
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

		RandomVariable smoothedDayZero = fls.smooth(HmmConstants.SEE_UMBRELLA);// see
		// umbrella on day one
		Assert.assertEquals(0.627, smoothedDayZero
				.getProbabilityOf(HmmConstants.RAINING), TOLERANCE);

		RandomVariable smoothedDayOne = fls
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

		RandomVariable smoothedOne = fls.smooth(HmmConstants.SEE_UMBRELLA); // see
		// umbrella on day one
		Assert.assertNull(smoothedOne);

		smoothedOne = fls.smooth(HmmConstants.SEE_UMBRELLA); // see
		// umbrella on day two
		Assert.assertEquals(0.653, smoothedOne
				.getProbabilityOf(HmmConstants.RAINING), TOLERANCE);
		Assert.assertEquals(0.346, smoothedOne
				.getProbabilityOf(HmmConstants.NOT_RAINING), TOLERANCE);

		RandomVariable smoothedTwo = fls.smooth(HmmConstants.SEE_UMBRELLA);// see
		// umbrella on day 3
		Assert.assertEquals(0.894, smoothedTwo
				.getProbabilityOf(HmmConstants.RAINING), TOLERANCE);
		Assert.assertEquals(0.105, smoothedTwo
				.getProbabilityOf(HmmConstants.NOT_RAINING), TOLERANCE);
	}
}
