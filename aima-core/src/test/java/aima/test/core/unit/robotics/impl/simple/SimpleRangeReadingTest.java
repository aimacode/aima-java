package aima.test.core.unit.robotics.impl.simple;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import aima.core.robotics.datatypes.IMclRangeReading;
import aima.core.robotics.impl.datatypes.Angle;
import aima.core.robotics.impl.simple.SimpleRangeReading;

/**
 * Test case for the {@code aima.core.robotics.impl.simple} package.
 * Tests valid implementation of the {@link IMclRangeReading} interface by {@link SimpleRangeReading}.
 * 
 * @author Arno v. Borries
 * @author Jan Phillip Kretzschmar
 * @author Andreas Walscheid
 *
 */
@SuppressWarnings("javadoc")
public class SimpleRangeReadingTest {
	private static final int TEST_COUNT = 1000;
	private SimpleRangeReading testReading;
	
	@Before
	public void setUp() {
		testReading = new SimpleRangeReading(130.0d, Angle.degreeAngle(45));
		SimpleRangeReading.setRangeNoise(10.0d);
	}
	
	@Test
	public void testGetAngle() {
		assertNotEquals("Not the same angle.", Angle.degreeAngle(15).getValue(), testReading.getAngle().getValue(), 0.000005d);
		assertEquals("The same angle.", Angle.degreeAngle(45).getValue(), testReading.getAngle().getValue(), 0.000005d);
	}
	
	@Test
	public void testRangeNoise() {
		for (int i = 0; i < TEST_COUNT; i++){
			testReading.addRangeNoise();
			assertFalse("Reading outside maximum noise range.", 140.0d < testReading.getValue());
			assertFalse("Reading outside minimum noise range.", 120.0d > testReading.getValue());
			assertTrue("Reading within maximum noise range.", 140.0d >= testReading.getValue());
			assertTrue("Reading within minimum noise range.", 120.0d <= testReading.getValue());	
		}
	}
}
