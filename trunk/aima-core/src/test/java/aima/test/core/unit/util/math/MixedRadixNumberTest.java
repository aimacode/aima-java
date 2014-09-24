package aima.test.core.unit.util.math;

import org.junit.Assert;
import org.junit.Test;

import aima.core.util.math.MixedRadixNumber;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class MixedRadixNumberTest {

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidRadices() {
		new MixedRadixNumber(100, new int[] { 1, 0, -1 });
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidMaxValue() {
		new MixedRadixNumber(100, new int[] { 3, 3, 3 });
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidInitialValuesValue1() {
		new MixedRadixNumber(new int[] { 0, 0, 4 }, new int[] { 3, 3, 3 });
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidInitialValuesValue2() {
		new MixedRadixNumber(new int[] { 1, 2, -1 }, new int[] { 3, 3, 3 });
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidInitialValuesValue3() {
		new MixedRadixNumber(new int[] { 1, 2, 3, 1 }, new int[] { 3, 3, 3 });
	}

	@Test
	public void testAllowedMaxValue() {
		Assert.assertEquals(15, (new MixedRadixNumber(0,
				new int[] { 2, 2, 2, 2 }).getMaxAllowedValue()));
		Assert.assertEquals(80, (new MixedRadixNumber(0,
				new int[] { 3, 3, 3, 3 }).getMaxAllowedValue()));
		Assert.assertEquals(5, (new MixedRadixNumber(0, new int[] { 3, 2 })
				.getMaxAllowedValue()));
		Assert.assertEquals(35, (new MixedRadixNumber(0,
				new int[] { 3, 3, 2, 2 }).getMaxAllowedValue()));
		Assert.assertEquals(359, (new MixedRadixNumber(0, new int[] { 3, 4, 5,
				6 }).getMaxAllowedValue()));
		Assert.assertEquals(359, (new MixedRadixNumber(0, new int[] { 6, 5, 4,
				3 }).getMaxAllowedValue()));
		Assert.assertEquals(359,
				(new MixedRadixNumber(new int[] { 5, 4, 3, 2 }, new int[] { 6,
						5, 4, 3 }).getMaxAllowedValue()));
	}

	@Test
	public void testIncrement() {
		MixedRadixNumber mrn = new MixedRadixNumber(0, new int[] { 3, 2 });
		int i = 0;
		while (mrn.increment()) {
			i++;
		}
		Assert.assertEquals(i, mrn.getMaxAllowedValue());
	}

	@Test
	public void testDecrement() {
		MixedRadixNumber mrn = new MixedRadixNumber(5, new int[] { 3, 2 });
		int i = 0;
		while (mrn.decrement()) {
			i++;
		}
		Assert.assertEquals(i, mrn.getMaxAllowedValue());
		i = 0;
		while (mrn.increment()) {
			i++;
		}
		while (mrn.decrement()) {
			i--;
		}
		Assert.assertEquals(i, mrn.intValue());
	}

	@Test
	public void testCurrentNumberalValue() {
		MixedRadixNumber mrn;
		//
		mrn = new MixedRadixNumber(0, new int[] { 3, 3, 2, 2 });
		Assert.assertEquals(0, mrn.getCurrentNumeralValue(0));
		Assert.assertEquals(0, mrn.getCurrentNumeralValue(1));
		Assert.assertEquals(0, mrn.getCurrentNumeralValue(2));
		Assert.assertEquals(0, mrn.getCurrentNumeralValue(3));
		//
		mrn = new MixedRadixNumber(35, new int[] { 3, 3, 2, 2 });
		Assert.assertEquals(2, mrn.getCurrentNumeralValue(0));
		Assert.assertEquals(2, mrn.getCurrentNumeralValue(1));
		Assert.assertEquals(1, mrn.getCurrentNumeralValue(2));
		Assert.assertEquals(1, mrn.getCurrentNumeralValue(3));
		//
		mrn = new MixedRadixNumber(25, new int[] { 3, 3, 2, 2 });
		Assert.assertEquals(1, mrn.getCurrentNumeralValue(0));
		Assert.assertEquals(2, mrn.getCurrentNumeralValue(1));
		Assert.assertEquals(0, mrn.getCurrentNumeralValue(2));
		Assert.assertEquals(1, mrn.getCurrentNumeralValue(3));
		//
		mrn = new MixedRadixNumber(17, new int[] { 3, 3, 2, 2 });
		Assert.assertEquals(2, mrn.getCurrentNumeralValue(0));
		Assert.assertEquals(2, mrn.getCurrentNumeralValue(1));
		Assert.assertEquals(1, mrn.getCurrentNumeralValue(2));
		Assert.assertEquals(0, mrn.getCurrentNumeralValue(3));
		//
		mrn = new MixedRadixNumber(8, new int[] { 3, 3, 2, 2 });
		Assert.assertEquals(2, mrn.getCurrentNumeralValue(0));
		Assert.assertEquals(2, mrn.getCurrentNumeralValue(1));
		Assert.assertEquals(0, mrn.getCurrentNumeralValue(2));
		Assert.assertEquals(0, mrn.getCurrentNumeralValue(3));
		//
		mrn = new MixedRadixNumber(359, new int[] { 3, 4, 5, 6 });
		Assert.assertEquals(2, mrn.getCurrentNumeralValue(0));
		Assert.assertEquals(3, mrn.getCurrentNumeralValue(1));
		Assert.assertEquals(4, mrn.getCurrentNumeralValue(2));
		Assert.assertEquals(5, mrn.getCurrentNumeralValue(3));
		//
		mrn = new MixedRadixNumber(359, new int[] { 6, 5, 4, 3 });
		Assert.assertEquals(5, mrn.getCurrentNumeralValue(0));
		Assert.assertEquals(4, mrn.getCurrentNumeralValue(1));
		Assert.assertEquals(3, mrn.getCurrentNumeralValue(2));
		Assert.assertEquals(2, mrn.getCurrentNumeralValue(3));
	}

	@Test
	public void testCurrentValueFor() {
		MixedRadixNumber mrn;
		//
		mrn = new MixedRadixNumber(0, new int[] { 3, 3, 2, 2 });
		Assert.assertEquals(0, mrn.getCurrentValueFor(new int[] { 0, 0, 0, 0 }));
		//
		mrn = new MixedRadixNumber(35, new int[] { 3, 3, 2, 2 });
		Assert.assertEquals(35,
				mrn.getCurrentValueFor(new int[] { 2, 2, 1, 1 }));
		//
		mrn = new MixedRadixNumber(25, new int[] { 3, 3, 2, 2 });
		Assert.assertEquals(25,
				mrn.getCurrentValueFor(new int[] { 1, 2, 0, 1 }));
		//
		mrn = new MixedRadixNumber(17, new int[] { 3, 3, 2, 2 });
		Assert.assertEquals(17,
				mrn.getCurrentValueFor(new int[] { 2, 2, 1, 0 }));
		//
		mrn = new MixedRadixNumber(8, new int[] { 3, 3, 2, 2 });
		Assert.assertEquals(8, mrn.getCurrentValueFor(new int[] { 2, 2, 0, 0 }));
		//
		mrn = new MixedRadixNumber(359, new int[] { 3, 4, 5, 6 });
		Assert.assertEquals(359,
				mrn.getCurrentValueFor(new int[] { 2, 3, 4, 5 }));
		//
		mrn = new MixedRadixNumber(359, new int[] { 6, 5, 4, 3 });
		Assert.assertEquals(359,
				mrn.getCurrentValueFor(new int[] { 5, 4, 3, 2 }));
	}

	@Test
	public void testSetCurrentValueFor() {
		MixedRadixNumber mrn;
		//
		mrn = new MixedRadixNumber(0, new int[] { 3, 3, 2, 2 });
		mrn.setCurrentValueFor(new int[] { 0, 0, 0, 0 });
		Assert.assertEquals(0, mrn.intValue());
		Assert.assertEquals(0, mrn.getCurrentNumeralValue(0));
		Assert.assertEquals(0, mrn.getCurrentNumeralValue(1));
		Assert.assertEquals(0, mrn.getCurrentNumeralValue(2));
		Assert.assertEquals(0, mrn.getCurrentNumeralValue(3));
		//
		mrn = new MixedRadixNumber(0, new int[] { 3, 3, 2, 2 });
		mrn.setCurrentValueFor(new int[] { 2, 2, 1, 1 });
		Assert.assertEquals(35, mrn.intValue());
		Assert.assertEquals(2, mrn.getCurrentNumeralValue(0));
		Assert.assertEquals(2, mrn.getCurrentNumeralValue(1));
		Assert.assertEquals(1, mrn.getCurrentNumeralValue(2));
		Assert.assertEquals(1, mrn.getCurrentNumeralValue(3));
		//
		mrn = new MixedRadixNumber(0, new int[] { 3, 3, 2, 2 });
		mrn.setCurrentValueFor(new int[] { 1, 2, 0, 1 });
		Assert.assertEquals(25, mrn.intValue());
		Assert.assertEquals(1, mrn.getCurrentNumeralValue(0));
		Assert.assertEquals(2, mrn.getCurrentNumeralValue(1));
		Assert.assertEquals(0, mrn.getCurrentNumeralValue(2));
		Assert.assertEquals(1, mrn.getCurrentNumeralValue(3));
		//
		mrn = new MixedRadixNumber(0, new int[] { 3, 3, 2, 2 });
		mrn.setCurrentValueFor(new int[] { 2, 2, 1, 0 });
		Assert.assertEquals(17, mrn.intValue());
		Assert.assertEquals(2, mrn.getCurrentNumeralValue(0));
		Assert.assertEquals(2, mrn.getCurrentNumeralValue(1));
		Assert.assertEquals(1, mrn.getCurrentNumeralValue(2));
		Assert.assertEquals(0, mrn.getCurrentNumeralValue(3));
		//
		mrn = new MixedRadixNumber(0, new int[] { 3, 3, 2, 2 });
		mrn.setCurrentValueFor(new int[] { 2, 2, 0, 0 });
		Assert.assertEquals(8, mrn.intValue());
		Assert.assertEquals(2, mrn.getCurrentNumeralValue(0));
		Assert.assertEquals(2, mrn.getCurrentNumeralValue(1));
		Assert.assertEquals(0, mrn.getCurrentNumeralValue(2));
		Assert.assertEquals(0, mrn.getCurrentNumeralValue(3));
		//
		mrn = new MixedRadixNumber(0, new int[] { 3, 4, 5, 6 });
		mrn.setCurrentValueFor(new int[] { 2, 3, 4, 5 });
		Assert.assertEquals(359, mrn.intValue());
		Assert.assertEquals(2, mrn.getCurrentNumeralValue(0));
		Assert.assertEquals(3, mrn.getCurrentNumeralValue(1));
		Assert.assertEquals(4, mrn.getCurrentNumeralValue(2));
		Assert.assertEquals(5, mrn.getCurrentNumeralValue(3));
		//
		mrn = new MixedRadixNumber(0, new int[] { 6, 5, 4, 3 });
		mrn.setCurrentValueFor(new int[] { 5, 4, 3, 2 });
		Assert.assertEquals(359, mrn.intValue());
		Assert.assertEquals(5, mrn.getCurrentNumeralValue(0));
		Assert.assertEquals(4, mrn.getCurrentNumeralValue(1));
		Assert.assertEquals(3, mrn.getCurrentNumeralValue(2));
		Assert.assertEquals(2, mrn.getCurrentNumeralValue(3));
	}
}
