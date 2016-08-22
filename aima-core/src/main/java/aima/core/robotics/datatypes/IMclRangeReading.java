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
	V getAngle();
	/**
	 * The range sensor noise model.
	 * Calculates a weight between 0 and 1 that specifies how similar the given range readings is to this range reading.
	 * @param secondRange the second range to be weighted against.
	 * @return a weight between 0 and 1.
	 */
	double calculateWeight(R secondRange);
}
