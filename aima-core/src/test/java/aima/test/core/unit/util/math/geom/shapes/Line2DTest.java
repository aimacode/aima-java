package aima.test.core.unit.util.math.geom.shapes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import aima.core.util.Util;
import aima.core.util.math.geom.shapes.IGeometric2D;
import aima.core.util.math.geom.shapes.Line2D;
import aima.core.util.math.geom.shapes.Point2D;
import aima.core.util.math.geom.shapes.Ray2D;
import aima.core.util.math.geom.shapes.TransformMatrix2D;
import aima.core.util.math.geom.shapes.Vector2D;

/**
 * Test case for the {@code aima.core.util.math.geom} package.
 * Tests valid implementation of the {@link IGeometric2D} interface by {@link Line2D}.
 * 
 * @author Arno v. Borries
 * @author Jan Phillip Kretzschmar
 * @author Andreas Walscheid
 *
 */
@SuppressWarnings("javadoc")
public class Line2DTest {
	private Line2D testLine; 
	private Line2D testLine2;
	private Line2D testLine3;
	private Line2D testLine4;
	private Point2D zeroPoint;
	
	@Before
	public void setUp() throws Exception {
		testLine = new Line2D(2.0d, 3.0d, 4.0d, 5.0d);
		testLine2 = new Line2D(6.0d, 4.0d, 6.0d, -3.0d);
		testLine3 = new Line2D(6.0d, -3.0d, 2.0d, 2.0d);
		testLine4 = new Line2D(3.0d, 4.0d, 6.0d, 4.0d);
		zeroPoint = new Point2D(0.0d,0.0d);
	}

	@Test
	public final void testRandomPoint() {
		assertTrue("Random point on horizontal line", testLine4.isInsideBorder(testLine4.randomPoint()));
		
		for (int i = 0; i < 1000; i++){
			assertTrue("Random point on line.", testLine.isInsideBorder(testLine.randomPoint()));
		}
	}

	@Test
	public final void testIsInside() {
		assertFalse("Point not on line.", testLine.isInside(zeroPoint));
		assertFalse("Point on line.", testLine.isInside(new Point2D(3.0d,4.0d)));
	}

	@Test
	public final void testIsInsideBorder() {
		assertFalse("Point not on line.", testLine.isInsideBorder(zeroPoint));
		assertTrue("Point on line.", testLine.isInsideBorder(new Point2D(3.0d,4.0d)));
		assertTrue("Point on line.", testLine2.isInsideBorder(new Point2D(6.0d,2.0d)));
	}

	@Test
	public final void testRayCast() {
		
		// Static rayCast tests
		assertEquals("Ray doesn't intersect.", Double.POSITIVE_INFINITY, testLine.rayCast(new Ray2D(1.0d,1.0d,-7.0d,-8.0d)), 0.000005d);
		assertEquals("Ray intersects.", Math.sqrt(2), testLine.rayCast(new Ray2D(2.0d,5.0d,4.0d,3.0d)), 0.000005d);
		assertEquals("Ray intersects.", 6, testLine2.rayCast(new Ray2D(zeroPoint,Vector2D.X_VECTOR)), 0.000005d);
		assertEquals("Ray intersects.", 3.0, testLine2.rayCast(new Ray2D(new Point2D(3.0d,3.0d),Vector2D.X_VECTOR)), 0.000005d);
		assertEquals("Ray intersects.", Double.POSITIVE_INFINITY, testLine2.rayCast(new Ray2D(new Point2D(6.0d,2.0d),Vector2D.X_VECTOR)), 0.000005d);
		assertEquals("Ray intersects.", 3.6, testLine3.rayCast(new Ray2D(zeroPoint,Vector2D.X_VECTOR)), 0.000005d);
		
		// Serial RayCast tests
		Point2D randomPoint;
		Line2D randomLine;
		Point2D randomPointOnLine;
		int counter = 5000;
				
		// generate a random point and another random point on a random Line and compare the distance between the two with a rayCast from the former towards the latter.
		do {
			randomLine = new Line2D(Util.generateRandomDoubleBetween(-1000.0d, 1000.0d), Util.generateRandomDoubleBetween(-1000.0d, 1000.0d), Util.generateRandomDoubleBetween(-1000.0d, 1000.0d), Util.generateRandomDoubleBetween(-1000.0d, 1000.0d));
			randomPointOnLine = randomLine.randomPoint();
			randomPoint = new Point2D(Util.generateRandomDoubleBetween(-1000.0d, 1000.0d), Util.generateRandomDoubleBetween(-1000.0d, 1000.0d));
			// System.out.printf("Line2D rayCast test no. %d: Line (%.2f,%.2f,%.2f,%.2f), point (%.2f,%.2f), point on line (%.2f,%.2f), distance: %.2f.\n", counter, randomLine.getStart().getX(), randomLine.getStart().getY(), randomLine.getEnd().getX(), randomLine.getEnd().getY(), randomPoint.getX(), randomPoint.getY(), randomPointOnLine.getX(), randomPointOnLine.getY(), randomPoint.distance(randomPointOnLine));
			assertEquals("Serial rayCast test for Line2D.", randomPoint.distance(randomPointOnLine), randomLine.rayCast(new Ray2D(randomPoint,randomPoint.vec(randomPointOnLine))), 0.000005d);
			counter -= 1;
		} while (counter > 0);
	}

	@Test
	public final void testGetBounds() {
		assertNotEquals("Not the bounding rectangle.", 1.0d, testLine.getBounds().getUpperLeft().getX(), 0.000005d);
		assertNotEquals("Not the bounding rectangle.", 6.0d, testLine.getBounds().getUpperLeft().getY(), 0.000005d);
		assertNotEquals("Not the bounding rectangle.", 6.0d, testLine.getBounds().getLowerRight().getX(), 0.000005d);
		assertNotEquals("Not the bounding rectangle.", 1.0d, testLine.getBounds().getLowerRight().getY(), 0.000005d);
		assertEquals("The bounding rectangle.", 2.0d, testLine.getBounds().getUpperLeft().getX(), 0.000005d);
		assertEquals("The bounding rectangle.", 5.0d, testLine.getBounds().getUpperLeft().getY(), 0.000005d);
		assertEquals("The bounding rectangle.", 4.0d, testLine.getBounds().getLowerRight().getX(), 0.000005d);
		assertEquals("The bounding rectangle.", 3.0d, testLine.getBounds().getLowerRight().getY(), 0.000005d);
	}

	@Test
	public final void testTransform() {
		assertEquals("Transformation by identity matrix.", testLine.transform(TransformMatrix2D.UNITY_MATRIX).getStart().getX(), testLine.getStart().getX(), 0.000005d);
		assertEquals("Transformation by identity matrix.", testLine.transform(TransformMatrix2D.UNITY_MATRIX).getStart().getY(), testLine.getStart().getY(), 0.000005d);
		assertEquals("Transformation by identity matrix.", testLine.transform(TransformMatrix2D.UNITY_MATRIX).getEnd().getX(), testLine.getEnd().getX(), 0.000005d);
		assertEquals("Transformation by identity matrix.", testLine.transform(TransformMatrix2D.UNITY_MATRIX).getEnd().getY(), testLine.getEnd().getY(), 0.000005d);
	}
}
