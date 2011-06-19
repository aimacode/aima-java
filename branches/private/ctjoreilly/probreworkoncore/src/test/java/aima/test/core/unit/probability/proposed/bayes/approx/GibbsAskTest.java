package aima.test.core.unit.probability.proposed.bayes.approx;

import org.junit.Assert;
import org.junit.Test;

import aima.core.probability.proposed.ProbabilityModel;
import aima.core.probability.proposed.RandomVariable;
import aima.core.probability.proposed.bayes.BayesianNetwork;
import aima.core.probability.proposed.bayes.approx.GibbsAsk;
import aima.core.probability.proposed.example.BayesNetExampleFactory;
import aima.core.probability.proposed.example.ExampleRV;
import aima.core.probability.proposed.proposition.AssignmentProposition;
import aima.core.util.MockRandomizer;

/**
 * 
 * @author Ciaran O'Reilly
 * @author Ravi Mohan
 */
public class GibbsAskTest {
	public static final double DELTA_THRESHOLD = ProbabilityModel.DEFAULT_ROUNDING_THRESHOLD;

	@Test
	public void testGibbsAsk_basic() {
		BayesianNetwork bn = BayesNetExampleFactory
				.constructCloudySprinklerRainWetGrassNetwork();
		AssignmentProposition[] e = new AssignmentProposition[] { new AssignmentProposition(
				ExampleRV.SPRINKLER_RV, Boolean.TRUE) };
		MockRandomizer r = new MockRandomizer(new double[] { 0.5, 0.5, 0.5,
				0.6 });

		GibbsAsk ga = new GibbsAsk(r);

		double[] estimate = ga.gibbsAsk(
				new RandomVariable[] {ExampleRV.RAIN_RV}, e, bn, 1)
				.getValues();

		Assert.assertArrayEquals(new double[] { 0.3333333333333333,
				0.6666666666666666 }, estimate, DELTA_THRESHOLD);
	}
}
