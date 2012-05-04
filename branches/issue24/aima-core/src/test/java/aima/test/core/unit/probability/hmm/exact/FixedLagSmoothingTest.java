package aima.test.core.unit.probability.hmm.exact;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import aima.core.probability.CategoricalDistribution;
import aima.core.probability.example.ExampleRV;
import aima.core.probability.example.HMMExampleFactory;
import aima.core.probability.hmm.exact.FixedLagSmoothing;
import aima.core.probability.proposition.AssignmentProposition;

/**
 * 
 * @author Ciaran O'Reilly
 *
 */
public class FixedLagSmoothingTest {
	public static final double DELTA_THRESHOLD = 1e-3;

	@Test
	public void testFixedLagSmoothing_lag_1_UmbrellaWorld() {
		FixedLagSmoothing uw = new FixedLagSmoothing(HMMExampleFactory
				.getUmbrellaWorldModel(), 1);

		// Day 1 - Lag 1
		List<AssignmentProposition> e1 = new ArrayList<AssignmentProposition>();
		e1
				.add(new AssignmentProposition(ExampleRV.UMBREALLA_t_RV,
						Boolean.TRUE));

		CategoricalDistribution smoothed = uw.fixedLagSmoothing(e1);
		Assert.assertNull(smoothed);
		
		// Day 2 - Lag 1
		List<AssignmentProposition> e2 = new ArrayList<AssignmentProposition>();
		e2
				.add(new AssignmentProposition(ExampleRV.UMBREALLA_t_RV,
						Boolean.TRUE));

		smoothed = uw.fixedLagSmoothing(e2);

		// Day 1 smoothed probabilities based on 2 days of evidence
		Assert.assertNotNull(smoothed);
		Assert.assertArrayEquals(new double[] { 0.883, 0.117 }, smoothed
				.getValues(), DELTA_THRESHOLD);

		// Day 3 - Lag 1
		List<AssignmentProposition> e3 = new ArrayList<AssignmentProposition>();
		e3.add(new AssignmentProposition(ExampleRV.UMBREALLA_t_RV,
				Boolean.FALSE));

		smoothed = uw.fixedLagSmoothing(e3);

		// Day 2 smoothed probabilities based on 3 days of evidence
		Assert.assertNotNull(smoothed);
		Assert.assertArrayEquals(new double[] { 0.799, 0.201 }, smoothed
				.getValues(), DELTA_THRESHOLD);
	}

	@Test
	public void testFixedLagSmoothing_lag_2_UmbrellaWorld() {
		FixedLagSmoothing uw = new FixedLagSmoothing(HMMExampleFactory
				.getUmbrellaWorldModel(), 2);

		// Day 1 - Lag 2
		List<AssignmentProposition> e1 = new ArrayList<AssignmentProposition>();
		e1
				.add(new AssignmentProposition(ExampleRV.UMBREALLA_t_RV,
						Boolean.TRUE));

		CategoricalDistribution smoothed = uw.fixedLagSmoothing(e1);
		Assert.assertNull(smoothed);

		// Day 2 - Lag 2
		List<AssignmentProposition> e2 = new ArrayList<AssignmentProposition>();
		e2
				.add(new AssignmentProposition(ExampleRV.UMBREALLA_t_RV,
						Boolean.TRUE));

		smoothed = uw.fixedLagSmoothing(e2);
		Assert.assertNull(smoothed);

		// Day 3 - Lag 2
		List<AssignmentProposition> e3 = new ArrayList<AssignmentProposition>();
		e3.add(new AssignmentProposition(ExampleRV.UMBREALLA_t_RV,
				Boolean.FALSE));

		smoothed = uw.fixedLagSmoothing(e3);

		Assert.assertNotNull(smoothed);
		Assert.assertArrayEquals(new double[] { 0.861, 0.138 }, smoothed
				.getValues(), DELTA_THRESHOLD);
	}
}
