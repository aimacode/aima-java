package aima.test.core.unit.robotics.impl.simple;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import aima.core.robotics.impl.map.IRangeReadingFactory;
import aima.core.robotics.impl.simple.SimpleRangeReading;
import aima.core.robotics.impl.simple.SimpleRangeReadingFactory;

/**
 * Test case for the {@code aima.core.robotics.impl.simple} package.
 * Tests valid implementation of the {@link IRangeReadingFactory} interface by {@link SimpleRangeReadingFactory}.
 * 
 * @author Arno v. Borries
 * @author Jan Phillip Kretzschmar
 * @author Andreas Walscheid
 *
 */
@SuppressWarnings("javadoc")
public class SimpleRangeReadingFactoryTest {
	
	private SimpleRangeReadingFactory testFactory;
	
	@Before
	public void setUp() {
		testFactory = new SimpleRangeReadingFactory();
	}
	
	@Test
	public void testGetRangeReading() {
		assertTrue("A valid Reading.", testFactory.getRangeReading(1345.452123d) instanceof SimpleRangeReading);
		
	}

}
