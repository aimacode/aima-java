package aima.core.robotics.datatypes;

/**
 * This interface defines a data type for a range measurement.
 * It has to contain an {@link IMclVector} by which the robot was rotated from its original facing direction to receive the measurement.<br/>
 * In addition it describes a method for using a range sensor noise model on the measurement.
 * 
 * @author Arno von Borries
 * @author Jan Phillip Kretzschmar
 * @author Andreas Walscheid
 * 
 * @param <R> the class that is implementing this interface.
 * @param <V> a n-1-dimensional vector implementing {@link IMclVector}, where n is the dimensionality of the environment.
 */
public interface IMclRangeReading<R extends IMclRangeReading<R,V>, V extends IMclVector> {
	/**
	 * Returns the vector by which the robot was rotated for this range reading.
	 * @return the vector by which the robot was rotated for this range reading.
	 */
	public V getAngle();
	/**
	 * Generates noise onto the range reading to mask errors in measuring the range.
	 * @return a new range reading onto which noise has been added.
	 */
	public R addRangeNoise();
}
