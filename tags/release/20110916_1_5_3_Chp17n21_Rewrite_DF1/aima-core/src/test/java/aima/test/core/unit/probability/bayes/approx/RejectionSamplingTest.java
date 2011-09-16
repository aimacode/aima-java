package aima.test.core.unit.probability.bayes.approx;

import org.junit.Assert;
import org.junit.Test;

import aima.core.probability.ProbabilityModel;
import aima.core.probability.RandomVariable;
import aima.core.probability.bayes.BayesianNetwork;
import aima.core.probability.bayes.approx.PriorSample;
import aima.core.probability.bayes.approx.RejectionSampling;
import aima.core.probability.example.BayesNetExampleFactory;
import aima.core.probability.example.ExampleRV;
import aima.core.probability.proposition.AssignmentProposition;
import aima.core.util.MockRandomizer;

/**
 * 
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 */
public class RejectionSamplingTest {

	public static final double DELTA_THRESHOLD = ProbabilityModel.DEFAULT_ROUNDING_THRESHOLD;

	@Test
	public void testPriorSample_basic() {

		BayesianNetwork bn = BayesNetExampleFactory
				.constructCloudySprinklerRainWetGrassNetwork();
		AssignmentProposition[] e = new AssignmentProposition[] { new AssignmentProposition(
				ExampleRV.SPRINKLER_RV, Boolean.TRUE) };
		MockRandomizer r = new MockRandomizer(new double[] { 0.1 });
		RejectionSampling rs = new RejectionSampling(new PriorSample(r));

		double[] estimate = rs.rejectionSampling(
				new RandomVariable[] { ExampleRV.RAIN_RV }, e, bn, 100)
				.getValues();

		Assert.assertArrayEquals(new double[] { 1.0, 0.0 }, estimate,
				DELTA_THRESHOLD);
	}

	@Test
	public void testRejectionSampling_AIMA3e_pg532() {
		// AIMA3e pg. 532

		BayesianNetwork bn = BayesNetExampleFactory
				.constructCloudySprinklerRainWetGrassNetwork();
		AssignmentProposition[] e = new AssignmentProposition[] { new AssignmentProposition(
				ExampleRV.SPRINKLER_RV, Boolean.TRUE) };

		// 400 required as 4 variables and 100 samples planned
		double[] ma = new double[400];
		for (int i = 0; i < ma.length; i += 4) {
			// Of the 100 that we generate, suppose
			// that 73 have Sprinkler = false and are rejected,
			if (i < (73 * 4)) {
				ma[i] = 0.5; // i.e Cloudy=true
				ma[i + 1] = 0.2; // i.e. Sprinkler=false
				ma[i + 2] = 0.5; // i.e. Rain=true
				ma[i + 3] = 0.1; // i.e. WetGrass=true
			} else {
				ma[i] = 0.5; // i.e Cloudy=true
				ma[i + 1] = 0.09; // i.e. Sprinkler=true
				// while 27 have Sprinkler = true; of the 27,
				// 8 have Rain = true
				if (i < ((73 + 8) * 4)) {
					ma[i + 2] = 0.5; // i.e. Rain=true
				} else {
					// and 19 have Rain = false.
					ma[i + 2] = 0.9; // i.e. Rain=false
				}

				ma[i + 3] = 0.1; // i.e. WetGrass=true
			}
		}
		MockRandomizer r = new MockRandomizer(ma);
		RejectionSampling rs = new RejectionSampling(new PriorSample(r));

		double[] estimate = rs.rejectionSampling(
				new RandomVariable[] { ExampleRV.RAIN_RV }, e, bn, 100)
				.getValues();

		Assert.assertArrayEquals(new double[] { 0.2962962962962963,
				0.7037037037037037 }, estimate, DELTA_THRESHOLD);
	}
}
