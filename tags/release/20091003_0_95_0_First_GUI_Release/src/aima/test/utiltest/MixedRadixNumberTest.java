package aima.test.utiltest;

import aima.util.MixedRadixNumber;
import junit.framework.TestCase;

/**
 * @author Ciaran O'Reilly
 * 
 */
public class MixedRadixNumberTest extends TestCase {
	
	public void testInvalidRadixs() {
		try {
			new MixedRadixNumber(100, new int[] {1, 0, -1});
			
			fail("Should have thrown an Illegal Argument Exception");
		} catch (IllegalArgumentException iae) {
			// Expected
		}
	}
	
	public void testInvalidMaxValue() {
		try {
			new MixedRadixNumber(100, new int[] {3, 3, 3});
			
			fail("Should have thrown an Illegal Argument Exception");
		} catch (IllegalArgumentException iae) {
			// Expected
		}
	}
	
	public void testAllowedMaxValue() {
		assertEquals(15,  (new MixedRadixNumber(0, new int[] {2, 2, 2, 2}).getMaxAllowedValue()));		
		assertEquals(80,  (new MixedRadixNumber(0, new int[] {3, 3, 3, 3}).getMaxAllowedValue()));
		assertEquals(5,   (new MixedRadixNumber(0, new int[] {3, 2}).getMaxAllowedValue()));
		assertEquals(35,  (new MixedRadixNumber(0, new int[] {3, 3, 2, 2}).getMaxAllowedValue()));
		assertEquals(359, (new MixedRadixNumber(0, new int[] {3, 4, 5, 6}).getMaxAllowedValue()));
		assertEquals(359, (new MixedRadixNumber(0, new int[] {6, 5, 4, 3}).getMaxAllowedValue()));
	}
	
	public void testIncrement() {
		MixedRadixNumber mrn = new MixedRadixNumber(0, new int[] {3, 2});
		int i = 0;
		while (mrn.increment()) {
			i++;
		}
		assertEquals(i, mrn.getMaxAllowedValue());
	}
	
	public void testDecrement() {
		MixedRadixNumber mrn = new MixedRadixNumber(5, new int[] {3, 2});
		int i = 0;
		while (mrn.decrement()) {
			i++;
		}
		assertEquals(i, mrn.getMaxAllowedValue());
	}
	
	public void testCurrentNumberalValue() {
		MixedRadixNumber mrn; 
		//
		mrn = new MixedRadixNumber(0, new int[] {3, 3, 2, 2});
		assertEquals(0, mrn.getCurrentNumeralValue(0));
		assertEquals(0, mrn.getCurrentNumeralValue(1));
		assertEquals(0, mrn.getCurrentNumeralValue(2));
		assertEquals(0, mrn.getCurrentNumeralValue(3));
		//
		mrn = new MixedRadixNumber(35, new int[] {3, 3, 2, 2});
		assertEquals(2, mrn.getCurrentNumeralValue(0));
		assertEquals(2, mrn.getCurrentNumeralValue(1));
		assertEquals(1, mrn.getCurrentNumeralValue(2));
		assertEquals(1, mrn.getCurrentNumeralValue(3));
		//
		mrn = new MixedRadixNumber(25, new int[] {3, 3, 2, 2});
		assertEquals(1, mrn.getCurrentNumeralValue(0));
		assertEquals(2, mrn.getCurrentNumeralValue(1));
		assertEquals(0, mrn.getCurrentNumeralValue(2));
		assertEquals(1, mrn.getCurrentNumeralValue(3));
		//
		mrn = new MixedRadixNumber(17, new int[] {3, 3, 2, 2});
		assertEquals(2, mrn.getCurrentNumeralValue(0));
		assertEquals(2, mrn.getCurrentNumeralValue(1));
		assertEquals(1, mrn.getCurrentNumeralValue(2));
		assertEquals(0, mrn.getCurrentNumeralValue(3));
		//
		mrn = new MixedRadixNumber(8, new int[] {3, 3, 2, 2});
		assertEquals(2, mrn.getCurrentNumeralValue(0));
		assertEquals(2, mrn.getCurrentNumeralValue(1));
		assertEquals(0, mrn.getCurrentNumeralValue(2));
		assertEquals(0, mrn.getCurrentNumeralValue(3));
		//
		mrn = new MixedRadixNumber(359, new int[] {3, 4, 5, 6});
		assertEquals(2, mrn.getCurrentNumeralValue(0));
		assertEquals(3, mrn.getCurrentNumeralValue(1));
		assertEquals(4, mrn.getCurrentNumeralValue(2));
		assertEquals(5, mrn.getCurrentNumeralValue(3));
		//
		mrn = new MixedRadixNumber(359, new int[] {6, 5, 4, 3});
		assertEquals(5, mrn.getCurrentNumeralValue(0));
		assertEquals(4, mrn.getCurrentNumeralValue(1));
		assertEquals(3, mrn.getCurrentNumeralValue(2));
		assertEquals(2, mrn.getCurrentNumeralValue(3));
	}	
}
