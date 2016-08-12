package aima.test.core.unit.util.math.geom.shapes;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import aima.core.util.Util;
import aima.core.util.math.geom.shapes.*;

/**
 * Test case for the {@code aima.core.util.math.geom} package.
 * Tests valid implementation of the {@link IGeometric2D} interface by {@link Rect2D}.
 * 
 * @author Arno v. Borries
 * @author Jan Phillip Kretzschmar
 * @author Andreas Walscheid
 *
 */
@SuppressWarnings("javadoc")
public class Rect2DTest {

	private Rect2D testRect; 
	private Point2D zeroPoint;
	
	@Before
	public void setUp() throws Exception {
		testRect = new Rect2D(3.0d, 4.0d, 5.0d, 8.0d);
		zeroPoint = new Point2D(0.0d,0.0d);
	}

	@Test
	public final void testRandomPoint() {
		for (int i = 0; i < 1000; i++){
			assertTrue("Random point in rectangle.", testRect.isInsideBorder(testRect.randomPoint()));	
		}
	}

	@Test
	public final void testIsInside() {
		assertFalse("Point not inside rectangle.", testRect.isInside(zeroPoint));
		assertFalse("Point on border.", testRect.isInside(new Point2D(3.0d,6.0d)));
		assertTrue("Point inside rectangle.", testRect.isInside(new Point2D(4.0d,6.0d)));
	}

	@Test
	public final void testIsInsideBorder() {
		assertFalse("Point not inside rectangle.", testRect.isInsideBorder(zeroPoint));
		assertTrue("Point on border.", testRect.isInsideBorder(new Point2D(3.0d,6.0d)));
		assertTrue("Point inside rectangle.", testRect.isInsideBorder(new Point2D(4.0d,6.0d)));
	}

	@Test
	public final void testRayCast() {
		// Static RayCast tests
		assertEquals("Ray doesn't intersect.", Double.POSITIVE_INFINITY, testRect.rayCast(new Ray2D(1.0d,1.0d,-7.0d,-8.0d)), 0.000005d);
		assertEquals("Ray intersects.", Math.sqrt(2), testRect.rayCast(new Ray2D(2.0d,3.0d,3.0d,4.0d)), 0.000005d);
	
		// Serial RayCast tests
		Rect2D randomRect;
		Line2D currentSide;
		Point2D randomPointOnSide;
		Point2D randomPoint;
		int i = 100;
		do {
			randomRect = new Rect2D(Util.generateRandomDoubleBetween(-1000.0d, 1000.0d),Util.generateRandomDoubleBetween(-1000.0d, 1000.0d),Util.generateRandomDoubleBetween(-1000.0d, 1000.0d),Util.generateRandomDoubleBetween(-1000.0d, 1000.0d));
			int j = 50;
			do {
				currentSide = new Line2D(randomRect.getUpperLeft(),randomRect.getUpperRight());
				randomPointOnSide = currentSide.randomPoint();
				randomPoint = new Point2D(Util.generateRandomDoubleBetween(-1000.0d, 1000.0d),Util.generateRandomDoubleBetween(randomPointOnSide.getY(), 1000.0d));
				assertEquals("Serial rayCast test for Rect2D, upper side.", randomPoint.distance(randomPointOnSide), randomRect.rayCast(new Ray2D(randomPoint, randomPoint.vec(randomPointOnSide))), 0.000005d);
				currentSide = new Line2D(randomRect.getLowerLeft(),randomRect.getLowerRight());
				randomPointOnSide = currentSide.randomPoint();
				randomPoint = new Point2D(Util.generateRandomDoubleBetween(-1000.0d, 1000.0d),Util.generateRandomDoubleBetween(-1000.0d, randomPointOnSide.getY()));
				assertEquals("Serial rayCast test for Rect2D, lower side.", randomPoint.distance(randomPointOnSide), randomRect.rayCast(new Ray2D(randomPoint, randomPoint.vec(randomPointOnSide))), 0.000005d);
				currentSide = new Line2D(randomRect.getLowerLeft(),randomRect.getUpperLeft());
				randomPointOnSide = currentSide.randomPoint();
				randomPoint = new Point2D(Util.generateRandomDoubleBetween(-1000.0d, randomPointOnSide.getX()),Util.generateRandomDoubleBetween(-1000.0d, 1000.0d));
				assertEquals("Serial rayCast test for Rect2D, left side.", randomPoint.distance(randomPointOnSide), randomRect.rayCast(new Ray2D(randomPoint, randomPoint.vec(randomPointOnSide))), 0.000005d);
				currentSide = new Line2D(randomRect.getLowerRight(),randomRect.getUpperRight());
				randomPointOnSide = currentSide.randomPoint();
				randomPoint = new Point2D(Util.generateRandomDoubleBetween(randomPointOnSide.getX(), 1000.0d),Util.generateRandomDoubleBetween(-1000.0d, 1000.0d));
				assertEquals("Serial rayCast test for Rect2D, right side.", randomPoint.distance(randomPointOnSide), randomRect.rayCast(new Ray2D(randomPoint, randomPoint.vec(randomPointOnSide))), 0.000005d);	
				j -= 1;
			} while (j > 0);
			i -= 1;
		} while (i > 0);
		
	}

	@Test
	public final void testGetBounds() {
		assertNotEquals("Not the bounding rectangle.", 1.0d, testRect.getBounds().getUpperLeft().getX(), 0.000005d);
		assertNotEquals("Not the bounding rectangle.", 6.0d, testRect.getBounds().getUpperLeft().getY(), 0.000005d);
		assertNotEquals("Not the bounding rectangle.", 6.0d, testRect.getBounds().getLowerRight().getX(), 0.000005d);
		assertNotEquals("Not the bounding rectangle.", 1.0d, testRect.getBounds().getLowerRight().getY(), 0.000005d);
		assertEquals("The bounding rectangle.", 3.0d, testRect.getBounds().getUpperLeft().getX(), 0.000005d);
		assertEquals("The bounding rectangle.", 8.0d, testRect.getBounds().getUpperLeft().getY(), 0.000005d);
		assertEquals("The bounding rectangle.", 5.0d, testRect.getBounds().getLowerRight().getX(), 0.000005d);
		assertEquals("The bounding rectangle.", 4.0d, testRect.getBounds().getLowerRight().getY(), 0.000005d);
	}

	@Test
	public final void testTransform() {
		assertEquals("Transformation by identity matrix.", ((Rect2D)testRect.transform(TransformMatrix2D.UNITY_MATRIX)).getUpperLeft().getX(), testRect.getUpperLeft().getX(), 0.000005d);
		assertEquals("Transformation by identity matrix.", ((Rect2D)testRect.transform(TransformMatrix2D.UNITY_MATRIX)).getUpperLeft().getY(), testRect.getUpperLeft().getY(), 0.000005d);
		assertEquals("Transformation by identity matrix.", ((Rect2D)testRect.transform(TransformMatrix2D.UNITY_MATRIX)).getLowerRight().getX(), testRect.getLowerRight().getX(), 0.000005d);
		assertEquals("Transformation by identity matrix.", ((Rect2D)testRect.transform(TransformMatrix2D.UNITY_MATRIX)).getLowerRight().getY(), testRect.getLowerRight().getY(), 0.000005d);	
		assertEquals("Translating rectangle: ULX.", ((Rect2D)testRect.transform(TransformMatrix2D.translate(3.0d,5.0d))).getUpperLeft().getX(), 6.0d, 0.000005d);
		assertEquals("Translating rectangle: ULY.", ((Rect2D)testRect.transform(TransformMatrix2D.translate(3.0d,5.0d))).getUpperLeft().getY(), 13.0d, 0.000005d);
		assertEquals("Translating rectangle: LRX.", ((Rect2D)testRect.transform(TransformMatrix2D.translate(3.0d,5.0d))).getLowerRight().getX(), 8.0d, 0.000005d);
		assertEquals("Translating rectangle: LRY.", ((Rect2D)testRect.transform(TransformMatrix2D.translate(3.0d,5.0d))).getLowerRight().getY(), 9.0d, 0.000005d);
		assertEquals("Scaling rectangle: ULX.", ((Rect2D)testRect.transform(TransformMatrix2D.scale(2.0d,4.0d))).getUpperLeft().getX(), 6.0d, 0.000005d);
		assertEquals("Scaling rectangle: ULY.", ((Rect2D)testRect.transform(TransformMatrix2D.scale(2.0d,4.0d))).getUpperLeft().getY(), 32.0d, 0.000005d);
		assertEquals("Scaling rectangle: ULX.", ((Rect2D)testRect.transform(TransformMatrix2D.scale(2.0d,4.0d))).getLowerRight().getX(), 10.0d, 0.000005d);
		assertEquals("Scaling rectangle: ULY.", ((Rect2D)testRect.transform(TransformMatrix2D.scale(2.0d,4.0d))).getLowerRight().getY(), 16.0d, 0.000005d);
	}

}
