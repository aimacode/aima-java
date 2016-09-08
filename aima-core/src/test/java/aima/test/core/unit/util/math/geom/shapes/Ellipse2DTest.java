package aima.test.core.unit.util.math.geom.shapes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import aima.core.util.Util;
import aima.core.util.math.geom.shapes.Ellipse2D;
import aima.core.util.math.geom.shapes.IGeometric2D;
import aima.core.util.math.geom.shapes.Point2D;
import aima.core.util.math.geom.shapes.Ray2D;
import aima.core.util.math.geom.shapes.TransformMatrix2D;

/**
 * Test case for the {@code aima.core.util.math.geom} package.
 * Tests valid implementation of the {@link IGeometric2D} interface by {@link Ellipse2D}.
 * 
 * @author Arno v. Borries
 * @author Jan Phillip Kretzschmar
 * @author Andreas Walscheid
 *
 */
@SuppressWarnings("javadoc")
public class Ellipse2DTest {
	private Ellipse2D testEllipse;
	private Point2D center;
	private Point2D zeroPoint;
	
	@Before
	public void setUp() {
		center = new Point2D(12.0d, 14.0d);
		testEllipse = new Ellipse2D(center, 10.0d, 5.0d);
		zeroPoint = new Point2D(0.0d,0.0d);
	}

	@Test
	public final void testRandomPoint() {
		for (int i = 0; i < 1000; i++){
			assertTrue("Random point in ellipse", testEllipse.isInsideBorder(testEllipse.randomPoint()));
		}
	}
	
	@Test
	public final void testIsInside() {
		assertFalse("Point not inside ellipse.", testEllipse.isInside(zeroPoint));
		assertFalse("Point on border.", testEllipse.isInside(new Point2D(12.0d,9.0d)));
		assertTrue("Point inside ellipse.", testEllipse.isInside(new Point2D(10.0d,12.0d)));
	}

	@Test
	public final void testIsInsideBorder() {
		assertFalse("Point not inside ellipse.", testEllipse.isInsideBorder(zeroPoint));
		assertTrue("Point on border.", testEllipse.isInsideBorder(new Point2D(12.0d,9.0d)));
		assertTrue("Point inside ellipse.", testEllipse.isInsideBorder(new Point2D(10.0d,12.0d)));
	}

	@Test
	public final void testRayCast() {
		// static tests
		assertEquals("Ray doesn't intersect.", Double.POSITIVE_INFINITY, testEllipse.rayCast(new Ray2D(0.0d,0.0d,0.0d,2.0d)), 0.000005d);
		assertEquals("Ray intersects.", 2.0d, testEllipse.rayCast(new Ray2D(0.0d,14.0d,12.0d,14.0d)), 0.000005d);		
	
		// serial tests
				Ellipse2D randomEllipse;
				Point2D randomPointOnEllipse;
				Point2D randomPoint;
				double currentXRadius;
				double currentYRadius;
				double xvalue;
				double yvalue;
				double randomAngle;
				int sector;
				int counter = 1000;
				
				do {
					randomEllipse = new Ellipse2D(new Point2D(Util.generateRandomDoubleBetween(-500.0d, 500.0d),Util.generateRandomDoubleBetween(-500.0d, 500.0d)),Util.generateRandomDoubleBetween(0.0d, 200.0d),Util.generateRandomDoubleBetween(0.0d, 200.0d));
					currentXRadius = randomEllipse.getHorizontalLength();
					currentYRadius = randomEllipse.getVerticalLength();
					xvalue = Util.generateRandomDoubleBetween(0.0d, currentXRadius);
					yvalue = (currentYRadius * Math.sqrt(currentXRadius * currentXRadius - xvalue * xvalue)) / currentXRadius;
					sector = Util.randomNumberBetween(1, 4);
					switch(sector){
						case 2: {
							yvalue = -yvalue;
							randomPoint = new Point2D(Util.generateRandomDoubleBetween(randomEllipse.getCenter().getX()+xvalue, 1000.0d), Util.generateRandomDoubleBetween(-1000.0d,randomEllipse.getCenter().getY()+yvalue));
							break;
						}
						case 3: {
							xvalue = -xvalue;
							yvalue = -yvalue;
							randomPoint = new Point2D(Util.generateRandomDoubleBetween(-1000.0d,randomEllipse.getCenter().getX()+xvalue), Util.generateRandomDoubleBetween(-1000.0d,randomEllipse.getCenter().getY()+yvalue));
							break;
						}
						case 4: {
							xvalue = -xvalue;
							randomPoint = new Point2D(Util.generateRandomDoubleBetween(-1000.0d,randomEllipse.getCenter().getX()+xvalue), Util.generateRandomDoubleBetween(randomEllipse.getCenter().getY()+yvalue, 1000.0d));
							break;
						}
						default: {
							randomPoint = new Point2D(Util.generateRandomDoubleBetween(randomEllipse.getCenter().getX()+xvalue, 1000.0d), Util.generateRandomDoubleBetween(randomEllipse.getCenter().getY()+yvalue, 1000.0d));
							break; 
						}
					}
					randomPointOnEllipse = new Point2D(randomEllipse.getCenter().getX()+xvalue,randomEllipse.getCenter().getY()+yvalue);
					
					randomAngle = Util.generateRandomDoubleBetween(-Math.PI/2, Math.PI/2);
					randomEllipse = (Ellipse2D)(randomEllipse.transform(TransformMatrix2D.rotate(randomAngle)));
					randomPoint = TransformMatrix2D.rotate(randomAngle).multiply(randomPoint);
					randomPointOnEllipse = TransformMatrix2D.rotate(randomAngle).multiply(randomPointOnEllipse);
					// System.out.printf("RayCast No. %d: Ellipse at (%.2f,%.2f), radii: (%.2f,%.2f). Rotation angle: %.2f, original angle: %.2f, point on ellipse: (%.2f,%.2f), outside point: (%.2f,%.2f), distance: %.2f.\n", 1000-counter, randomEllipse.getCenter().getX(), randomEllipse.getCenter().getY(), randomEllipse.getHorizontalLength(), randomEllipse.getVerticalLength(), randomEllipse.getAngle(), randomAngle, randomPointOnEllipse.getX(), randomPointOnEllipse.getY(), randomPoint.getX(), randomPoint.getY(), randomPoint.distance(randomPointOnEllipse));
					
					assertEquals("Serial rayCast test for Circle2D.", randomPoint.distance(randomPointOnEllipse), randomEllipse.rayCast(new Ray2D(randomPoint,randomPoint.vec(randomPointOnEllipse))), 0.000005d);
					counter -= 1;
				} while (counter > 0);
	
	}

	@Test
	public final void testGetBounds() {
		assertNotEquals("Not the bounding rectangle ULX.", 1.0d, testEllipse.getBounds().getUpperLeft().getX(), 0.000005d);
		assertNotEquals("Not the bounding rectangle ULY.", 6.0d, testEllipse.getBounds().getUpperLeft().getY(), 0.000005d);
		assertNotEquals("Not the bounding rectangle LRX.", 6.0d, testEllipse.getBounds().getLowerRight().getX(), 0.000005d);
		assertNotEquals("Not the bounding rectangle LRY.", 1.0d, testEllipse.getBounds().getLowerRight().getY(), 0.000005d);
		assertEquals("The bounding rectangle ULX.", 2.0d, testEllipse.getBounds().getUpperLeft().getX(), 0.000005d);
		assertEquals("The bounding rectangle ULY.", 19.0d, testEllipse.getBounds().getUpperLeft().getY(), 0.000005d);
		assertEquals("The bounding rectangle LRX.", 22.0d, testEllipse.getBounds().getLowerRight().getX(), 0.000005d);
		assertEquals("The bounding rectangle LRY.", 9.0d, testEllipse.getBounds().getLowerRight().getY(), 0.000005d);
	}

	
	@Test
	public final void testTransform() {
		assertEquals("Transformation by identity matrix: X-value.", ((Ellipse2D)testEllipse.transform(TransformMatrix2D.UNITY_MATRIX)).getCenter().getX(), testEllipse.getCenter().getX(), 0.000005d);
		assertEquals("Transformation by identity matrix: Y-value.", ((Ellipse2D)testEllipse.transform(TransformMatrix2D.UNITY_MATRIX)).getCenter().getY(), testEllipse.getCenter().getY(), 0.000005d);
		assertEquals("Transformation by identity matrix: X-radius.", ((Ellipse2D)testEllipse.transform(TransformMatrix2D.UNITY_MATRIX)).getHorizontalLength(), testEllipse.getHorizontalLength(), 0.000005d);
		assertEquals("Transformation by identity matrix: Y-radius.", ((Ellipse2D)testEllipse.transform(TransformMatrix2D.UNITY_MATRIX)).getVerticalLength(), testEllipse.getVerticalLength(), 0.000005d);
		assertEquals("Transformation by identity matrix: angle.", ((Ellipse2D)testEllipse.transform(TransformMatrix2D.UNITY_MATRIX)).getAngle(), testEllipse.getAngle(), 0.000005d);
		assertEquals("Translating Ellipse: X-Value.", ((Ellipse2D)testEllipse.transform(TransformMatrix2D.translate(4.0d, 5.0d))).getCenter().getX(), 16.0d, 0.000005d);
		assertEquals("Translating Ellipse: Y-Value.", ((Ellipse2D)testEllipse.transform(TransformMatrix2D.translate(4.0d, 5.0d))).getCenter().getY(), 19.0d, 0.000005d);
		assertEquals("Scaling Ellipse: X-Value.", ((Ellipse2D)testEllipse.transform(TransformMatrix2D.scale(0.5d, 0.))).getHorizontalLength(), 5.0d, 0.000005d);
		assertEquals("Scaling Ellipse: Y-Value.", ((Ellipse2D)testEllipse.transform(TransformMatrix2D.scale(0, 2.0d))).getVerticalLength(), 10.0d, 0.000005d);
		}

}
