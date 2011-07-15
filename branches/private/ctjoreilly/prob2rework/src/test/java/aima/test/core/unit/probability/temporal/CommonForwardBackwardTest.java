package aima.test.core.unit.probability.temporal;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;

import aima.core.probability.CategoricalDistribution;
import aima.core.probability.example.ExampleRV;
import aima.core.probability.proposition.AssignmentProposition;
import aima.core.probability.temporal.BackwardStepInference;
import aima.core.probability.temporal.ForwardBackwardInference;
import aima.core.probability.temporal.ForwardStepInference;
import aima.core.probability.util.ProbabilityTable;

/**
 * 
 * @author Ciaran O'Reilly
 */
public abstract class CommonForwardBackwardTest {
	public static final double DELTA_THRESHOLD = 1e-3;

	//
	// PROTECTED METHODS
	//
	protected void testForwardStep_UmbrellaWorld(ForwardStepInference uw) {
		// AIMA3e pg. 572
		// Day 0, no observations only the security guards prior beliefs
		// P(R<sub>0</sub>) = <0.5, 0.5>
		CategoricalDistribution prior = new ProbabilityTable(new double[] {
				0.5, 0.5 }, ExampleRV.RAIN_t_RV);

		// Day 1, the umbrella appears, so U<sub>1</sub> = true.
		// &asymp; <0.818, 0.182>
		List<AssignmentProposition> e1 = new ArrayList<AssignmentProposition>();
		e1
				.add(new AssignmentProposition(ExampleRV.UMBREALLA_t_RV,
						Boolean.TRUE));
		CategoricalDistribution f1 = uw.forward(prior, e1);
		Assert.assertArrayEquals(new double[] { 0.818, 0.182 }, f1.getValues(),
				DELTA_THRESHOLD);

		// Day 2, the umbrella appears, so U<sub>2</sub> = true.
		// &asymp; <0.883, 0.117>
		List<AssignmentProposition> e2 = new ArrayList<AssignmentProposition>();
		e2
				.add(new AssignmentProposition(ExampleRV.UMBREALLA_t_RV,
						Boolean.TRUE));
		CategoricalDistribution f2 = uw.forward(f1, e2);
		Assert.assertArrayEquals(new double[] { 0.883, 0.117 }, f2.getValues(),
				DELTA_THRESHOLD);
	}

	protected void testBackwardStep_UmbrellaWorld(BackwardStepInference uw) {
		// AIMA3e pg. 575
		CategoricalDistribution b_kp2t = new ProbabilityTable(new double[] {
				1.0, 1.0 }, ExampleRV.RAIN_t_RV);
		List<AssignmentProposition> e2 = new ArrayList<AssignmentProposition>();
		e2
				.add(new AssignmentProposition(ExampleRV.UMBREALLA_t_RV,
						Boolean.TRUE));
		CategoricalDistribution b1 = uw.backward(b_kp2t, e2);
		Assert.assertArrayEquals(new double[] { 0.69, 0.41 }, b1.getValues(),
				DELTA_THRESHOLD);
	}

	protected void testForwardBackward_UmbrellaWorld(ForwardBackwardInference uw) {
		// AIMA3e pg. 572
		// Day 0, no observations only the security guards prior beliefs
		// P(R<sub>0</sub>) = <0.5, 0.5>
		CategoricalDistribution prior = new ProbabilityTable(new double[] {
				0.5, 0.5 }, ExampleRV.RAIN_t_RV);

		// Day 1
		List<List<AssignmentProposition>> evidence = new ArrayList<List<AssignmentProposition>>();
		List<AssignmentProposition> e1 = new ArrayList<AssignmentProposition>();
		e1
				.add(new AssignmentProposition(ExampleRV.UMBREALLA_t_RV,
						Boolean.TRUE));
		evidence.add(e1);

		List<CategoricalDistribution> smoothed = uw.forwardBackward(evidence,
				prior);

		Assert.assertEquals(1, smoothed.size());
		Assert.assertArrayEquals(new double[] { 0.818, 0.182 }, smoothed.get(0)
				.getValues(), DELTA_THRESHOLD);

		// Day 2
		List<AssignmentProposition> e2 = new ArrayList<AssignmentProposition>();
		e2
				.add(new AssignmentProposition(ExampleRV.UMBREALLA_t_RV,
						Boolean.TRUE));
		evidence.add(e2);

		smoothed = uw.forwardBackward(evidence, prior);

		Assert.assertEquals(2, smoothed.size());
		Assert.assertArrayEquals(new double[] { 0.883, 0.117 }, smoothed.get(0)
				.getValues(), DELTA_THRESHOLD);
		Assert.assertArrayEquals(new double[] { 0.883, 0.117 }, smoothed.get(1)
				.getValues(), DELTA_THRESHOLD);

		// Day 3
		List<AssignmentProposition> e3 = new ArrayList<AssignmentProposition>();
		e3.add(new AssignmentProposition(ExampleRV.UMBREALLA_t_RV,
				Boolean.FALSE));
		evidence.add(e3);

		smoothed = uw.forwardBackward(evidence, prior);

		Assert.assertEquals(3, smoothed.size());
		Assert.assertArrayEquals(new double[] { 0.861, 0.138 }, smoothed.get(0)
				.getValues(), DELTA_THRESHOLD);
		Assert.assertArrayEquals(new double[] { 0.799, 0.201 }, smoothed.get(1)
				.getValues(), DELTA_THRESHOLD);
		Assert.assertArrayEquals(new double[] { 0.190, 0.810 }, smoothed.get(2)
				.getValues(), DELTA_THRESHOLD);
	}
}
