package aima.test.core.unit.util.math.geom.shapes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import aima.core.util.Util;
import aima.core.util.math.geom.shapes.Circle2D;
import aima.core.util.math.geom.shapes.Ellipse2D;
import aima.core.util.math.geom.shapes.IGeometric2D;
import aima.core.util.math.geom.shapes.Point2D;
import aima.core.util.math.geom.shapes.Ray2D;
import aima.core.util.math.geom.shapes.TransformMatrix2D;

/**
 * Test case for the {@code aima.core.util.math.geom} package.
 * Tests valid implementation of the {@link IGeometric2D} interface by {@link Circle2D}.
 * 
 * @author Arno v. Borries
 * @author Jan Phillip Kretzschmar
 * @author Andreas Walscheid
 *
 */
@SuppressWarnings("javadoc")
public class Circle2DTest {
	
	private Circle2D testCircle;
	private Point2D center;
	private Point2D zeroPoint;
	
	@Before
	public void setUp() {
		center = new Point2D(12.0d, 14.0d);
		testCircle = new Circle2D(center, 10.0d);
		zeroPoint = new Point2D(0.0d,0.0d);
	}

	@Test
	public final void testRandomPoint() {
		for (int i = 0; i < 1000; i++){
			assertTrue("Random point in circle", testCircle.isInsideBorder(testCircle.randomPoint()));
		}
	}

	@Test
	public final void testIsInside() {
		assertFalse("Point not inside circle.", testCircle.isInside(zeroPoint));
		assertFalse("Point on border.", testCircle.isInside(new Point2D(12.0d,4.0d)));
		assertTrue("Point inside circle.", testCircle.isInside(new Point2D(10.0d,8.0d)));
	}

	@Test
	public final void testIsInsideBorder() {
		assertFalse("Point not inside circle.", testCircle.isInsideBorder(zeroPoint));
		assertTrue("Point on border.", testCircle.isInsideBorder(new Point2D(12.0d,4.0d)));
		assertTrue("Point inside circle.", testCircle.isInsideBorder(new Point2D(10.0d,8.0d)));
	}

	@Test
	public final void testRayCast() {
	
		// static tests
		assertEquals("Ray doesn't intersect.", Double.POSITIVE_INFINITY, testCircle.rayCast(new Ray2D(1.0d,1.0d,0.0d,2.0d)), 0.000005d);
		assertEquals("Ray intersects.", Math.sqrt(2), testCircle.rayCast(new Ray2D(11.0d,3.0d,12.0d,4.0d)), 0.000005d);
	
		// serial tests
		Circle2D randomCircle;
		Point2D randomPointOnCircle;
		Point2D randomPoint;
		double currentRadius;
		double xvalue;
		double yvalue;
		int sector;
		int counter = 1000;
		
		do {
			randomCircle = new Circle2D(new Point2D(Util.generateRandomDoubleBetween(-500.0d, 500.0d),Util.generateRandomDoubleBetween(-500.0d, 500.0d)),Util.generateRandomDoubleBetween(0.0d, 200.0d));
			currentRadius = randomCircle.getRadius();
			xvalue = Util.generateRandomDoubleBetween(0.0d,currentRadius);
			yvalue = Math.sqrt(currentRadius * currentRadius - xvalue * xvalue);
			sector = Util.randomNumberBetween(1, 4);
			switch(sector){
				case 2: {
					yvalue = -yvalue;
					randomPoint = new Point2D(Util.generateRandomDoubleBetween(randomCircle.getCenter().getX()+xvalue, 1000.0d), Util.generateRandomDoubleBetween(-1000.0d,randomCircle.getCenter().getY()+yvalue));
					break;
				}
				case 3: {
					xvalue = -xvalue;
					yvalue = -yvalue;
					randomPoint = new Point2D(Util.generateRandomDoubleBetween(-1000.0d,randomCircle.getCenter().getX()+xvalue), Util.generateRandomDoubleBetween(-1000.0d,randomCircle.getCenter().getY()+yvalue));
					break;
				}
				case 4: {
					xvalue = -xvalue;
					randomPoint = new Point2D(Util.generateRandomDoubleBetween(-1000.0d,randomCircle.getCenter().getX()+xvalue), Util.generateRandomDoubleBetween(randomCircle.getCenter().getY()+yvalue, 1000.0d));
					break;
				}
				default: {
					randomPoint = new Point2D(Util.generateRandomDoubleBetween(randomCircle.getCenter().getX()+xvalue, 1000.0d), Util.generateRandomDoubleBetween(randomCircle.getCenter().getY()+yvalue, 1000.0d));
					break; 
				}
			}
			randomPointOnCircle = new Point2D(randomCircle.getCenter().getX()+xvalue,randomCircle.getCenter().getY()+yvalue);
			// System.out.printf("Circle at (%.2f,%.2f), Radius %.2f. Point on Circle: (%.2f,%.2f). Outside Point: (%.2f,%.2f). Distance: %.2f.\n", randomCircle.getCenter().getX(), randomCircle.getCenter().getY(), randomCircle.getRadius(), randomPointOnCircle.getX(), randomPointOnCircle.getY(), randomPoint.getX(), randomPoint.getY(), randomPoint.distance(randomPointOnCircle));
			assertEquals("Serial rayCast test for Circle2D.", randomPoint.distance(randomPointOnCircle), randomCircle.rayCast(new Ray2D(randomPoint,randomPoint.vec(randomPointOnCircle))), 0.000005d);
			counter -= 1;
		} while (counter > 0);
	}

	@Test
	public final void testGetBounds() {
		assertNotEquals("Not the bounding rectangle ULX.", 1.0d, testCircle.getBounds().getUpperLeft().getX(), 0.000005d);
		assertNotEquals("Not the bounding rectangle ULY.", 6.0d, testCircle.getBounds().getUpperLeft().getY(), 0.000005d);
		assertNotEquals("Not the bounding rectangle LRX.", 6.0d, testCircle.getBounds().getLowerRight().getX(), 0.000005d);
		assertNotEquals("Not the bounding rectangle LRY.", 1.0d, testCircle.getBounds().getLowerRight().getY(), 0.000005d);
		assertEquals("The bounding rectangle ULX.", 2.0d, testCircle.getBounds().getUpperLeft().getX(), 0.000005d);
		assertEquals("The bounding rectangle ULY.", 24.0d, testCircle.getBounds().getUpperLeft().getY(), 0.000005d);
		assertEquals("The bounding rectangle LRX.", 22.0d, testCircle.getBounds().getLowerRight().getX(), 0.000005d);
		assertEquals("The bounding rectangle LRY.", 4.0d, testCircle.getBounds().getLowerRight().getY(), 0.000005d);
	}

	@Test
	public final void testTransform() {
		assertEquals("Transformation by identity matrix: X-value.", ((Circle2D)testCircle.transform(TransformMatrix2D.UNITY_MATRIX)).getCenter().getX(), testCircle.getCenter().getX(), 0.000005d);
		assertEquals("Transformation by identity matrix: Y-value.", ((Circle2D)testCircle.transform(TransformMatrix2D.UNITY_MATRIX)).getCenter().getY(), testCircle.getCenter().getY(), 0.000005d);
		assertEquals("Transformation by identity matrix: radius.", ((Circle2D)testCircle.transform(TransformMatrix2D.UNITY_MATRIX)).getRadius(), testCircle.getRadius(), 0.000005d);
		assertEquals("Translating circle: X-Value.", ((Circle2D)testCircle.transform(TransformMatrix2D.translate(4.0d, 5.0d))).getCenter().getX(), 16.0d, 0.000005d);
		assertEquals("Translating circle: Y-Value.", ((Circle2D)testCircle.transform(TransformMatrix2D.translate(4.0d, 5.0d))).getCenter().getY(), 19.0d, 0.000005d);
		assertEquals("Scaling circle into ellipse: X-Value.", ((Ellipse2D)testCircle.transform(TransformMatrix2D.scale(2.0d, 0.))).getHorizontalLength(), 20.0d, 0.000005d);
		assertEquals("Scaling circle into ellipse: Y-Value.", ((Ellipse2D)testCircle.transform(TransformMatrix2D.scale(0, 0.5d))).getVerticalLength(), 5.0d, 0.000005d);
	}		

}
