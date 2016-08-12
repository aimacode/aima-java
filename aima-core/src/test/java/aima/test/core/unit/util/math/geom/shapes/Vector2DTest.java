package aima.test.core.unit.util.math.geom.shapes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import aima.core.util.math.geom.shapes.Point2D;
import aima.core.util.math.geom.shapes.Vector2D;

/**
 * Test case for the {@code aima.core.util.math.geom} package.
 * Tests valid implementation of {@link Vector2D}.
 * 
 * @author Arno v. Borries
 * @author Jan Phillip Kretzschmar
 * @author Andreas Walscheid
 *
 */
@SuppressWarnings("javadoc")
public class Vector2DTest {
	private Vector2D testVector;
	
	@Before
	public void setUp() throws Exception {
		testVector = new Vector2D(3.0d,4.0d);
	}

	@Test
	public final void testAdd() {
		assertEquals("Correct vector addition X-Value.",testVector.add(new Vector2D(8.0d,12.0d)).getX(), 11.0d,0.000005d);
		assertEquals("Correct vector addition Y-Value.",testVector.add(new Vector2D(8.0d,12.0d)).getY(), 16.0d,0.000005d);
		
	}

	@Test
	public final void testSub() {
		assertEquals("Correct vector subtraction X-Value.",testVector.sub(new Vector2D(8.0d,12.0d)).getX(), -5.0d,0.000005d);
		assertEquals("Correct vector subtraction Y-Value.",testVector.sub(new Vector2D(8.0d,12.0d)).getY(), -8.0d,0.000005d);
	}

	@Test
	public final void testInvert() {
		assertEquals("Correct vector inversion X-Value.",testVector.invert().getX(), -3.0d,0.000005d);
		assertEquals("Correct vector inversion Y-Value.",testVector.invert().getY(), -4.0d,0.000005d);
	}

	@Test
	public final void testIsParallel() {
		assertFalse("Two non-parallel vectors.",testVector.isAbsoluteParallel(new Vector2D(4.0d, 7.0d)));
		assertTrue("Two parallel vectors.", testVector.isAbsoluteParallel(new Vector2D(9.0d, 12.0d)));
		assertTrue("Every vector is parallel to the zero vector.", testVector.isAbsoluteParallel(Vector2D.ZERO_VECTOR));
		
	}
	
	@Test
	public final void testIsAbsoluteParallel() {
		assertFalse("Two non-absolute-parallel vectors.", testVector.isParallel(new Vector2D(6.5d, 5.8d)));
		assertFalse("No vector is absolute parallel to the zero vector.", testVector.isParallel(Vector2D.ZERO_VECTOR));
		assertTrue("Two absolute-parallel vectors.", testVector.isParallel(new Vector2D(9,12)));
		assertTrue("Two absolute-parallel vectors.", testVector.isParallel(new Vector2D(-9,-12)));		
	}

	@Test
	public final void testAngleTo() {
		assertEquals("Rotating vector by 1/2 Pi radians.", testVector.angleTo(new Vector2D(-4.0d,3.0d)), 1.0d/2.0d * Math.PI, 0.000005d);
		assertEquals("Rotating vector by Pi radians.", testVector.angleTo(new Vector2D(-3.0d,-4.0d)), Math.PI, 0.000005d);
		assertEquals("Rotating vector by 3/2 Pi radians.", testVector.angleTo(new Vector2D(4.0d,-3.0d)), 3.0d/2.0d * Math.PI, 0.000005d);
		assertEquals("Rotating vector by 2 Pi radians.", testVector.angleTo(new Vector2D(6.0d,8.0d)), 0.0d, 0.000005d);
	}

	@Test
	public final void testLength() {
		assertEquals("The correct length.", testVector.length(), 5.0d, 0.000005d);
	}

	@Test
	public final void testEqualsVector2D() {
		assertFalse("Not the same vector.", testVector.equals(new Vector2D(6.0d,5.0d)));
		assertTrue("The exact same vector.", testVector.equals(new Vector2D(3.0d,4.0d)));
	}

	@Test
	public final void testEqualsObject() {
		assertFalse("Not the same object, since no cast defined.", testVector.equals(new Point2D(3.0d,4.0d)));
		assertTrue("The exact same object.", testVector.equals(new Vector2D(3.0d,4.0d)));
	}

}
