package aima.test.core.unit.probability.bayes.approx;

import org.junit.Assert;
import org.junit.Test;

import aima.core.probability.ProbabilityModel;
import aima.core.probability.RandomVariable;
import aima.core.probability.bayes.BayesianNetwork;
import aima.core.probability.bayes.approx.LikelihoodWeighting;
import aima.core.probability.example.BayesNetExampleFactory;
import aima.core.probability.example.ExampleRV;
import aima.core.probability.proposition.AssignmentProposition;
import aima.core.util.MockRandomizer;

/**
 * 
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 */
public class LikelihoodWeightingTest {

	public static final double DELTA_THRESHOLD = ProbabilityModel.DEFAULT_ROUNDING_THRESHOLD;

	@Test
	public void testLikelihoodWeighting_basic() {
		BayesianNetwork bn = BayesNetExampleFactory
				.constructCloudySprinklerRainWetGrassNetwork();
		AssignmentProposition[] e = new AssignmentProposition[] { new AssignmentProposition(
				ExampleRV.SPRINKLER_RV, Boolean.TRUE) };
		MockRandomizer r = new MockRandomizer(
				new double[] { 0.5, 0.5, 0.5, 0.5 });

		LikelihoodWeighting lw = new LikelihoodWeighting(r);

		double[] estimate = lw.likelihoodWeighting(
				new RandomVariable[] { ExampleRV.RAIN_RV }, e, bn, 1000)
				.getValues();

		Assert.assertArrayEquals(new double[] { 1.0, 0.0 }, estimate,
				DELTA_THRESHOLD);
	}

	@Test
	public void testLikelihoodWeighting_AIMA3e_pg533() {
		// AIMA3e pg. 533
		// <b>P</b>(Rain | Cloudy = true, WetGrass = true)
		BayesianNetwork bn = BayesNetExampleFactory
				.constructCloudySprinklerRainWetGrassNetwork();
		AssignmentProposition[] e = new AssignmentProposition[] {
				new AssignmentProposition(ExampleRV.CLOUDY_RV, Boolean.TRUE),
				new AssignmentProposition(ExampleRV.WET_GRASS_RV, Boolean.TRUE) };
		// sample P(Sprinkler | Cloudy = true) = <0.1, 0.9>; suppose
		// Sprinkler=false
		// sample P(Rain | Cloudy = true) = <0.8, 0.2>; suppose Rain=true
		MockRandomizer r = new MockRandomizer(new double[] { 0.5, 0.5 });

		LikelihoodWeighting lw = new LikelihoodWeighting(r);
		double[] estimate = lw.likelihoodWeighting(
				new RandomVariable[] { ExampleRV.RAIN_RV }, e, bn, 1)
				.getValues();

		// Here the event [true,false,true,true] should have weight 0.45,
		// and this is tallied under Rain = true, which when normalized
		// should be <1.0, 0.0>;
		Assert.assertArrayEquals(new double[] { 1.0, 0.0 }, estimate,
				DELTA_THRESHOLD);
	}
}
