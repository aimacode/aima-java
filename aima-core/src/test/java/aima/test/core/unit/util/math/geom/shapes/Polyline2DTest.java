package aima.test.core.unit.util.math.geom.shapes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import aima.core.util.math.geom.shapes.IGeometric2D;
import aima.core.util.math.geom.shapes.Point2D;
import aima.core.util.math.geom.shapes.Polyline2D;
import aima.core.util.math.geom.shapes.Ray2D;
import aima.core.util.math.geom.shapes.TransformMatrix2D;

/**
 * Test case for the {@code aima.core.util.math.geom} package.
 * Tests valid implementation of the {@link IGeometric2D} interface by {@link Polyline2D}.
 * 
 * @author Arno v. Borries
 * @author Jan Phillip Kretzschmar
 * @author Andreas Walscheid
 *
 */
@SuppressWarnings("javadoc")
public class Polyline2DTest {
	
	private Point2D[] testVertices = {new Point2D(2.0d,2.0d), new Point2D(5.0d,7.0d), new Point2D(6.0d,4.0d), new Point2D(6.0d, -3.0d)};
	private Polyline2D testPolylineOpen;
	private Polyline2D testPolylineClosed;
	private Point2D zeroPoint;
	
	@Before
	public void setUp() throws Exception {
		testPolylineOpen = new Polyline2D(testVertices, false);
		testPolylineClosed = new Polyline2D(testVertices, true);
		zeroPoint = new Point2D(0.0d,0.0d);
	}

	@Test
	public final void testRandomPoint() {
		Point2D randomPoint = testPolylineOpen.randomPoint();
		for (int i = 0; i < 1000; i++){
			randomPoint = testPolylineOpen.randomPoint();
			assertTrue("Random point on polyline.", testPolylineOpen.isInsideBorder(randomPoint));
		}
		for (int i = 0; i < 1000; i++){
			randomPoint = testPolylineClosed.randomPoint();
			assertTrue("Random point in polygon.", testPolylineClosed.isInsideBorder(testPolylineClosed.randomPoint()));	
		}
		
		
	}

	@Test
	public final void testIsInside() {
		assertFalse("Point cannot be inside polyline.", testPolylineOpen.isInside(new Point2D(3.0d,3.0d)));
		assertFalse("Point on border of polyline.", testPolylineOpen.isInside(new Point2D(6.0d,2.0d)));
		assertFalse("Point outside polygon.", testPolylineClosed.isInside(zeroPoint));
		assertFalse("Point on border of polygon.", testPolylineClosed.isInside(new Point2D(6.0d,2.0d)));
		assertTrue("Point inside polygon.", testPolylineClosed.isInside(new Point2D(3.0d,3.0d)));
		}

	@Test
	public final void testIsInsideBorder() {
		assertFalse("Point cannot be inside polyline.", testPolylineOpen.isInsideBorder(new Point2D(3.0d,3.0d)));
		assertTrue("Point on border of polyline.", testPolylineOpen.isInsideBorder(new Point2D(6.0d,2.0d)));
		assertFalse("Point outside polygon.", testPolylineClosed.isInsideBorder(zeroPoint));
		assertTrue("Point on border of polygon.", testPolylineClosed.isInsideBorder(new Point2D(6.0d,2.0d)));
		assertTrue("Point inside polygon.", testPolylineClosed.isInsideBorder(new Point2D(3.0d,3.0d)));
	}

	@Test
	public final void testRayCast() {
		
		// Static RayCast tests
		assertEquals("Ray doesn't intersect with polyline.", Double.POSITIVE_INFINITY, testPolylineOpen.rayCast(new Ray2D(1.0d,1.0d,-7.0d,-8.0d)), 0.000005d);
		assertEquals("Ray intersects with polyline.", Math.sqrt(2), testPolylineOpen.rayCast(new Ray2D(1.0d,1.0d,4.0d,4.0d)), 0.000005d);
		assertEquals("Ray doesn't intersect with polygon.", Double.POSITIVE_INFINITY, testPolylineClosed.rayCast(new Ray2D(1.0d,1.0d,-7.0d,-8.0d)), 0.000005d);
		assertEquals("Ray intersects with polygon.", Math.sqrt(2), testPolylineClosed.rayCast(new Ray2D(1.0d,1.0d,4.0d,4.0d)), 0.000005d);
		
		// Serial RayCast tests	
		/*Point2D randomPoint;
		Point2D randomPointOnEdge;
		Line2D currentEdge;
		int counter = 500;
		do {
			for (int i = 1; i < testVertices.length; i++){
				currentEdge = new Line2D(testVertices[i], testVertices[i-1]);
				randomPointOnEdge = currentEdge.randomPoint();
				randomPoint = new Point2D(Util.generateRandomDoubleBetween(-1000.0d, 1000.0d), Util.generateRandomDoubleBetween(-1000.0d, 1000.0d));
				assertEquals("Serial rayCast test for Polyline2D (open).", randomPoint.distance(randomPointOnEdge), testPolylineOpen.rayCast(new Ray2D(randomPoint,randomPoint.vec(randomPointOnEdge))), 0.000005d);
			}
			counter -= 1;	
		} while (counter > 0);*/
	}
	
	@Test
	public final void testGetBounds() {
		assertNotEquals("Not the bounding rectangle.", 1.0d, testPolylineOpen.getBounds().getUpperLeft().getX(), 0.000005d);
		assertNotEquals("Not the bounding rectangle.", 8.0d, testPolylineOpen.getBounds().getUpperLeft().getY(), 0.000005d);
		assertNotEquals("Not the bounding rectangle.", 8.0d, testPolylineOpen.getBounds().getLowerRight().getX(), 0.000005d);
		assertNotEquals("Not the bounding rectangle.", 1.0d, testPolylineOpen.getBounds().getLowerRight().getY(), 0.000005d);
		assertEquals("The bounding rectangle.", 2.0d, testPolylineOpen.getBounds().getUpperLeft().getX(), 0.000005d);
		assertEquals("The bounding rectangle.", 7.0d, testPolylineOpen.getBounds().getUpperLeft().getY(), 0.000005d);
		assertEquals("The bounding rectangle.", 6.0d, testPolylineOpen.getBounds().getLowerRight().getX(), 0.000005d);
		assertEquals("The bounding rectangle.", -3.0d, testPolylineOpen.getBounds().getLowerRight().getY(), 0.000005d);
		assertNotEquals("Not the bounding rectangle.", 1.0d, testPolylineClosed.getBounds().getUpperLeft().getX(), 0.000005d);
		assertNotEquals("Not the bounding rectangle.", 8.0d, testPolylineClosed.getBounds().getUpperLeft().getY(), 0.000005d);
		assertNotEquals("Not the bounding rectangle.", 8.0d, testPolylineClosed.getBounds().getLowerRight().getX(), 0.000005d);
		assertNotEquals("Not the bounding rectangle.", 1.0d, testPolylineClosed.getBounds().getLowerRight().getY(), 0.000005d);
		assertEquals("The bounding rectangle.", 2.0d, testPolylineClosed.getBounds().getUpperLeft().getX(), 0.000005d);
		assertEquals("The bounding rectangle.", 7.0d, testPolylineClosed.getBounds().getUpperLeft().getY(), 0.000005d);
		assertEquals("The bounding rectangle.", 6.0d, testPolylineClosed.getBounds().getLowerRight().getX(), 0.000005d);
		assertEquals("The bounding rectangle.", -3.0d, testPolylineClosed.getBounds().getLowerRight().getY(), 0.000005d);
	}

	@Test
	public final void testTransform() {
		for (int i = 0; i < testPolylineOpen.getVertexes().length; i++){
			assertEquals ("Transformation by identity matrix", testPolylineOpen.transform(TransformMatrix2D.UNITY_MATRIX).getVertexes()[i].getX(), testPolylineOpen.getVertexes()[i].getX(), 0.000005d);
			assertEquals ("Transformation by identity matrix", testPolylineOpen.transform(TransformMatrix2D.UNITY_MATRIX).getVertexes()[i].getY(), testPolylineOpen.getVertexes()[i].getY(), 0.000005d);
		}
		for (int i = 0; i < testPolylineClosed.getVertexes().length; i++){
			assertEquals ("Transformation by identity matrix", testPolylineClosed.transform(TransformMatrix2D.UNITY_MATRIX).getVertexes()[i].getX(), testPolylineClosed.getVertexes()[i].getX(), 0.000005d);
			assertEquals ("Transformation by identity matrix", testPolylineClosed.transform(TransformMatrix2D.UNITY_MATRIX).getVertexes()[i].getY(), testPolylineClosed.getVertexes()[i].getY(), 0.000005d);
		}
	}

}
