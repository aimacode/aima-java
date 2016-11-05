package aima.gui.swing.applications.robotics.simple;

import aima.core.robotics.impl.datatypes.Angle;
import aima.core.robotics.impl.datatypes.IPose2D;
import aima.core.util.math.geom.shapes.Point2D;
import aima.core.util.math.geom.shapes.Vector2D;

/**
 * Implementation of {@link IPose2D} for {@link SimpleMove}.<br/>
 * This class is used in the context of the virtual robot. 
 * 
 * @author Arno von Borries
 * @author Jan Phillip Kretzschmar
 * @author Andreas Walscheid
 *
 */
public final class SimplePose implements IPose2D<SimplePose,SimpleMove> {

	private final Point2D position;
	private final double heading;
	
	/**
	 * @param point the position of the pose.
	 * @param heading the direction in which the pose is facing. 0 means parallel to the X axis, {@code Math.PI/2} means parallel to the Y axis.
	 */
	public SimplePose(Point2D point, double heading) {
		this.position = point;
		this.heading = heading % (2*Math.PI);
	}
	
	/**
	 * @param x the X coordinate of the position of the pose.
	 * @param y the Y coordinate of the position of the pose.
	 * @param heading the direction in which the pose is facing. 0 means parallel to the X axis, {@code Math.PI/2} means parallel to the Y axis.
	 */
	public SimplePose(double x, double y, double heading) {
		this.position = new Point2D(x, y);
		this.heading = heading % (2*Math.PI);
	}
	
	/**
	 * Returns the position of the pose.
	 * @return the position of the pose.
	 */
	public Point2D getPosition() {
		return position;
	}
	
	@Override
	public SimplePose applyMovement(SimpleMove move) {
		final double headingNew = heading + move.getFirstRotation();
		Point2D positionNew = position.add(Vector2D.calculateFromPolar(move.getForward(),-headingNew));
		return new SimplePose(positionNew,headingNew + move.getLastRotation());
	}

	@Override
	public SimplePose addAngle(Angle angle) {
		return new SimplePose(position.clone(),heading + angle.getValue());
	}

	@Override
	public SimplePose clone() {
		return new SimplePose(position.clone(),heading);
	}

	@Override
	public double distanceTo(SimplePose pose) {
		return this.position.distance(pose.getPosition());
	}

	@Override
	public double getX() {
		return position.getX();
	}

	@Override
	public double getY() {
		return position.getY();
	}

	@Override
	public double getHeading() {
		return heading;
	}
}
