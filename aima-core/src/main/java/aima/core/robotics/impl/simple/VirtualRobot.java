package aima.core.robotics.impl.simple;

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
	private double badDelta;
	private double sensorRange;
	private Angle[] rangeReadingAngles;
	private MclCartesianPlot2D<SimplePose,SimpleMove,AbstractRangeReading> map;
	private SimplePose pose;
	
	/**
	 * @param map the virtual environment in which the robot "moves" and performs range readings.
	 */
	public VirtualRobot(MclCartesianPlot2D<SimplePose, SimpleMove, AbstractRangeReading> map) {
		this.map = map;
	}
	
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
	 * Sets the delta between two range readings, above which a weight of zero is calculated.
	 * @param badDelta the worst acceptable delta.
	 */
	public void setBadDelta(double badDelta) {
		this.badDelta = badDelta;
	}
	
	/**
	 * Sets the sensor range that the virtual robot can measure at most.
	 * @param sensorRange the range of the robot's sensor.
	 */
	public void setSensorRange(double sensorRange) {
		this.sensorRange = sensorRange;
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

	@Override
	public float calculateWeight(AbstractRangeReading robotRange, AbstractRangeReading mapRange) {
		if(robotRange.getValue() < 0 || mapRange.getValue() < 0) return 0;
		if(Double.isInfinite(robotRange.getValue()) && Double.isInfinite(mapRange.getValue())) return 1;
		final double robotValue;
		if(Double.isInfinite(robotRange.getValue()) || robotRange.getValue() > sensorRange) robotValue = sensorRange;
		else robotValue = robotRange.getValue();
		final double mapValue;
		if(Double.isInfinite(mapRange.getValue()) || mapRange.getValue() > sensorRange) mapValue = sensorRange;
		else mapValue = mapRange.getValue();
		final double delta = Math.abs(robotValue - mapValue);
		if(delta > badDelta) return 0.0f;
		return (float) (1.0d - delta / badDelta);
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
