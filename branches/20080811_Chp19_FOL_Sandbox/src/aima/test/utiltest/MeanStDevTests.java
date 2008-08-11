package aima.test.utiltest;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import aima.util.Util;

public class MeanStDevTests extends TestCase {
	private List<Double> values;

	@Override
	public void setUp() {
		values = new ArrayList<Double>();
		values.add(1.0);
		values.add(2.0);
		values.add(3.0);
		values.add(4.0);
		values.add(5.0);
	}

	public void testMeanCalculation() {
		assertEquals(3.0, Util.calculateMean(values));
	}

	public void testStDevCalculation() {
		assertEquals(1.5811, Util.calculateStDev(values, 3.0), 0.001);
	}

	public void testNormalization() {
		List<Double> nrm = Util.normalizeFromMeanAndStdev(values, 3.0, 1.5811);
		assertEquals(-1.264, nrm.get(0), 0.001);
		assertEquals(-0.632, nrm.get(1), 0.001);
		assertEquals(0.0, nrm.get(2), 0.001);
		assertEquals(0.632, nrm.get(3), 0.001);
		assertEquals(1.264, nrm.get(4), 0.001);

	}

	public void testRandomNumberGenrationWhenStartAndEndNumbersAreSame() {
		int i = Util.randomNumberBetween(0, 0);
		int j = Util.randomNumberBetween(23, 23);
		assertEquals(0, i);
		assertEquals(23, j);
	}

}
