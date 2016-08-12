package aima.test.core.unit.robotics.impl.simple;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import aima.core.robotics.impl.datatypes.Angle;
import aima.core.robotics.impl.datatypes.IPose2D;
import aima.core.robotics.impl.simple.SimpleMove;
import aima.core.robotics.impl.simple.SimplePose;
import aima.core.util.Util;
import aima.core.util.math.geom.shapes.Point2D;
import aima.core.util.math.geom.shapes.Vector2D;

/**
 * Test case for the {@code aima.core.robotics.impl.simple} package.
 * Tests valid implementation of the {@link IPose2D} interface by {@link SimplePose}.
 * 
 * @author Arno v. Borries
 * @author Jan Phillip Kretzschmar
 * @author Andreas Walscheid
 *
 */
@SuppressWarnings("javadoc")
public class SimplePoseTest {
	
	private static final int TEST_COUNT = 1000;

	@Test
	public void testApplyMovement() {
		for(int i=0; i < TEST_COUNT; i++) {
			final SimplePose startPose = new SimplePose(new Point2D(Util.generateRandomDoubleBetween(-10000.0d, +10000.0d),Util.generateRandomDoubleBetween(-10000.0d, +10000.0d)),Util.generateRandomDoubleBetween(0, 2*Math.PI));
			final SimplePose endPose = new SimplePose(new Point2D(Util.generateRandomDoubleBetween(-10000.0d, +10000.0d),Util.generateRandomDoubleBetween(-10000.0d, +10000.0d)),Util.generateRandomDoubleBetween(0, 2*Math.PI));;
			final Vector2D vector = startPose.getPosition().vec(endPose.getPosition());
			final double vectorAngle = vector.angleTo(Vector2D.X_VECTOR);
			final double firstRotation = vectorAngle - startPose.getHeading();
			final double lastRotation = endPose.getHeading() - vectorAngle;
			final SimpleMove move = new SimpleMove(firstRotation,vector.length(),lastRotation);
			assertEquals("The X coordinate of the pose. Cycle: " + i, endPose.getX(), startPose.applyMovement(move).getX(), 0.000005d);
			assertEquals("The Y coordinate of the pose. Cycle: " + i, endPose.getY(), startPose.applyMovement(move).getY(), 0.000005d);
			assertEquals("The heading of the pose. Cycle: " + i, endPose.getHeading(), startPose.applyMovement(move).getHeading(), 0.000005d);
		}
	}

	@Test
	public void testAddAngle() {
		for(int i=0; i < TEST_COUNT; i++) {
			final SimplePose pose = new SimplePose(new Point2D(Util.generateRandomDoubleBetween(-10000.0d, +10000.0d),Util.generateRandomDoubleBetween(-10000.0d, +10000.0d)),Util.generateRandomDoubleBetween(0, 2*Math.PI));
			final Angle angle = new Angle(Util.generateRandomDoubleBetween(0, 2*Math.PI));
			
			assertEquals("The X coordinate of the pose. Cycle: " + i, pose.getX(), pose.addAngle(angle).getX(), 0.000005d);
			assertEquals("The Y coordinate of the pose. Cycle: " + i, pose.getY(), pose.addAngle(angle).getY(), 0.000005d);
			assertEquals("The heading of the pose. Cycle: " + i, (pose.getHeading() + angle.getValue()) % (2*Math.PI), pose.addAngle(angle).getHeading(), 0.000005d);
		}
	}

}
