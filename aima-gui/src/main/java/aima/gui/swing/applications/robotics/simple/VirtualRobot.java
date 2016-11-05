package aima.gui.swing.applications.robotics.simple;

import aima.core.robotics.IMclRobot;
import aima.core.robotics.impl.datatypes.AbstractRangeReading;
import aima.core.robotics.impl.datatypes.Angle;
import aima.core.robotics.impl.map.MclCartesianPlot2D;
import aima.core.util.math.geom.shapes.Vector2D;

/**
 * This class implements functionality for a robot in a virtual environment.<br/>
 * It implements {@link IMclRobot}.
 * 
 * @author Arno von Borries
 * @author Jan Phillip Kretzschmar
 * @author Andreas Walscheid
 *
 */
public final class VirtualRobot implements IMclRobot<Angle,SimpleMove,AbstractRangeReading> {

	private double maxMoveDistance; 
	private double minMoveDistance;
	private Angle[] rangeReadingAngles;
	private MclCartesianPlot2D<SimplePose,SimpleMove,AbstractRangeReading> map;
	private SimplePose pose;
	
	/**
	 * @param map the virtual environment in which the robot "moves" and performs range readings.
	 */
	public VirtualRobot(MclCartesianPlot2D<SimplePose, SimpleMove, AbstractRangeReading> map) {
		this.map = map;
	}
	
	/**
	 * Set the angles at which range readings will be performed.
	 * @param angles the array of angles to read ranges at.
	 */
	public void setRangeReadingAngles(Angle[] angles) {
		this.rangeReadingAngles = angles;
	}
	
	/**
	 * Sets the maximum distance that the robot moves in one move.
	 * @param distance the distance that the robot should move at max.
	 */
	public void setMaxMoveDistance(double distance) {
		this.maxMoveDistance = distance;
	}
	
	/**
	 * Sets the minimum distance that the robot moves in one move.
	 * @param distance the distance that the robot should move at least.
	 */
	public void setMinMoveDistance(double distance) {
		this.minMoveDistance = distance;
	}
	
	/**
	 * Moves the robot to a new random position on the map.
	 */
	public void setRandomPose() {
		pose = map.randomPose(); 
	}
	
	/**
	 * Gets the current pose of the robot.
	 * @return the current pose of the robot.
	 */
	public SimplePose getPose() {
		return pose;
	}
	
	@Override
	public AbstractRangeReading[] getRangeReadings() {
		AbstractRangeReading[] ranges = new SimpleRangeReading[rangeReadingAngles.length];
		for(int i=0; i<rangeReadingAngles.length;i++) {
			SimplePose rangePose = pose.addAngle(rangeReadingAngles[i]);
			ranges[i] = map.rayCast(rangePose);
			ranges[i] = new SimpleRangeReading(ranges[i].getValue(), rangeReadingAngles[i]);
		}
		return ranges;
	}
	
	/**
	 * Lets the robot perform a move.<br/>
	 * As it is hard for a virtual robot to see which of its movements is valid without additional functionality in the map class,<br/>
	 * a randomly new pose on the map is selected. The rotation and the vector to the pose form the move.
	 */
	@Override
	public SimpleMove performMove() {
		SimplePose newPose;
		Vector2D vector;
		do {
			newPose = map.randomPose();
			vector = pose.getPosition().vec(newPose.getPosition());
		} while(vector.length() > maxMoveDistance || vector.length() < minMoveDistance);
		final double vectorAngle = vector.angleTo(Vector2D.X_VECTOR);
		final double firstRotation = vectorAngle - pose.getHeading();
		final double lastRotation = newPose.getHeading() - vectorAngle;
		pose = newPose;
		return new SimpleMove(firstRotation,vector.length(),lastRotation);
	}
}
