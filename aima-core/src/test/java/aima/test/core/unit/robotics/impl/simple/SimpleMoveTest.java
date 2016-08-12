package aima.test.core.unit.robotics.impl.simple;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import aima.core.robotics.datatypes.IMclMove;
import aima.core.robotics.impl.simple.SimpleMove;

/**
 * Test case for the {@code aima.core.robotics.impl.simple} package.
 * Tests valid implementation of the {@link IMclMove} interface by {@link SimpleMove}.
 * 
 * @author Arno v. Borries
 * @author Jan Phillip Kretzschmar
 * @author Andreas Walscheid
 *
 */
@SuppressWarnings("javadoc")
public class SimpleMoveTest {
	private SimpleMove testMove;
	
	@Before
	public void setUp(){
		testMove = new SimpleMove(1.0d,20.0d,1.0d);
		SimpleMove.setMovementNoise(3.0d);
		SimpleMove.setRotationNoise(0.3d);
	}
	
	@Test
	public void testGenerateNoise() {
		testMove.generateNoise();
		assertFalse("First rotation outside minimum noise range", 0.7d > testMove.getFirstRotation());
		assertFalse("First rotation outside maximum noise range", 1.3d < testMove.getFirstRotation());
		assertTrue("First rotation within minimum noise range", 0.7d <= testMove.getFirstRotation());
		assertTrue("First rotation within maximum noise range", 1.3d >= testMove.getFirstRotation());
		assertFalse("Forward movement outside minimum noise range", 17.0d > testMove.getForward());
		assertFalse("Forward movement outside maximum noise range", 23.0d < testMove.getForward());
		assertTrue("Forward movement within minimum noise range", 17.0d <= testMove.getForward());
		assertTrue("Forward movement within maximum noise range", 23.0d >= testMove.getForward());
		assertFalse("Last rotation outside minimum noise range", 0.7d > testMove.getLastRotation());
		assertFalse("Last rotation outside maximum noise range", 1.3d < testMove.getLastRotation());
		assertTrue("Last rotation within minimum noise range", 0.7d <= testMove.getLastRotation());
		assertTrue("Last rotation within maximum noise range", 1.3d >= testMove.getLastRotation());
	}

}
