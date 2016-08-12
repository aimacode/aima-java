package aima.test.core.unit.robotics.impl.simple;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import aima.core.robotics.impl.map.IPoseFactory;
import aima.core.robotics.impl.simple.SimplePose;
import aima.core.robotics.impl.simple.SimplePoseFactory;
import aima.core.util.math.geom.shapes.Point2D;

/**
 * Test case for the {@code aima.core.robotics.impl.simple} package.
 * Tests valid implementation of the {@link IPoseFactory} interface by {@link SimplePoseFactory}.
 * 
 * @author Arno v. Borries
 * @author Jan Phillip Kretzschmar
 * @author Andreas Walscheid
 *
 */
@SuppressWarnings("javadoc")
public class SimplePoseFactoryTest {

	private SimplePoseFactory testFactory;
	
	@Before
	public void setUp() {
		testFactory = new SimplePoseFactory();
	}
	
	@Test
	public void testGetPosePoint2D() {
		assertTrue("A valid Pose", testFactory.getPose(new Point2D(23.0d,13.0d)) instanceof SimplePose);
	}

	@Test
	public void testGetPosePoint2DDouble() {
		assertTrue("A valid Pose", testFactory.getPose(new Point2D(23.0d,13.0d), 0.78d) instanceof SimplePose);
	}

	@Test
	public void testIsHeadingValid() {
		assertTrue("A valid heading.",testFactory.isHeadingValid(new SimplePose(24.0d,13.0d,0.56d)));
	}

}
