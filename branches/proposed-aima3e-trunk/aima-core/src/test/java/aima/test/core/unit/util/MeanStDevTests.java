package aima.test.core.unit.util;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;

import aima.core.util.Util;

public class MeanStDevTests {
	private List<Double> values;

	@Before
	public void setUp() {
		values = new ArrayList<Double>();
		values.add(1.0);
		values.add(2.0);
		values.add(3.0);
		values.add(4.0);
		values.add(5.0);
	}

	@Test
	public void testMeanCalculation() {
		Assert.assertEquals(3.0, Util.calculateMean(values));
	}

	@Test
	public void testStDevCalculation() {
		Assert.assertEquals(1.5811, Util.calculateStDev(values, 3.0), 0.001);
	}

	@Test
	public void testNormalization() {
		List<Double> nrm = Util.normalizeFromMeanAndStdev(values, 3.0, 1.5811);
		Assert.assertEquals(-1.264, nrm.get(0), 0.001);
		Assert.assertEquals(-0.632, nrm.get(1), 0.001);
		Assert.assertEquals(0.0, nrm.get(2), 0.001);
		Assert.assertEquals(0.632, nrm.get(3), 0.001);
		Assert.assertEquals(1.264, nrm.get(4), 0.001);
	}

	@Test
	public void testRandomNumberGenrationWhenStartAndEndNumbersAreSame() {
		int i = Util.randomNumberBetween(0, 0);
		int j = Util.randomNumberBetween(23, 23);
		Assert.assertEquals(0, i);
		Assert.assertEquals(23, j);
	}
}
