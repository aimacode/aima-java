package aima.test.core.unit.probability.bayes.impl;

import org.junit.Assert;
import org.junit.Test;

import aima.core.probability.ProbabilityModel;
import aima.core.probability.bayes.impl.CPT;
import aima.core.probability.domain.BooleanDomain;
import aima.core.probability.util.RandVar;

public class CPTTest {
	public static final double DELTA_THRESHOLD = ProbabilityModel.DEFAULT_ROUNDING_THRESHOLD;

	@Test
	public void test_getConditioningCase() {
		RandVar aRV = new RandVar("A", new BooleanDomain());
		RandVar bRV = new RandVar("B", new BooleanDomain());
		RandVar cRV = new RandVar("C", new BooleanDomain());

		CPT cpt = new CPT(cRV, new double[] {
				// A = true, B = true, C = true
				0.1,
				// A = true, B = true, C = false
				0.9,
				// A = true, B = false, C = true
				0.2,
				// A = true, B = false, C = false
				0.8,
				// A = false, B = true, C = true
				0.3,
				// A = false, B = true, C = false
				0.7,
				// A = false, B = false, C = true
				0.4,
				// A = false, B = false, C = false
				0.6 }, aRV, bRV);

		Assert.assertArrayEquals(new double[] { 0.1, 0.9 }, cpt
				.getConditioningCase(true, true).getValues(), DELTA_THRESHOLD);

		Assert.assertArrayEquals(new double[] { 0.2, 0.8 }, cpt
				.getConditioningCase(true, false).getValues(), DELTA_THRESHOLD);

		Assert.assertArrayEquals(new double[] { 0.3, 0.7 }, cpt
				.getConditioningCase(false, true).getValues(), DELTA_THRESHOLD);

		Assert.assertArrayEquals(new double[] { 0.4, 0.6 }, cpt
				.getConditioningCase(false, false).getValues(), DELTA_THRESHOLD);

	}
}
