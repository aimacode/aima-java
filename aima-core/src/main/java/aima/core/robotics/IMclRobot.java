package aima.core.robotics;

import aima.core.robotics.datatypes.IMclMove;
import aima.core.robotics.datatypes.IMclRangeReading;
import aima.core.robotics.datatypes.IMclVector;
import aima.core.robotics.datatypes.RobotException;
import aima.core.robotics.impl.MonteCarloLocalization;
/**
 * This interface defines functionality a robotic agent has to implement in order to localize itself via {@link MonteCarloLocalization}. 
 * 
 * @author Arno von Borries
 * @author Jan Phillip Kretzschmar
 * @author Andreas Walscheid
 *
 * @param <V> an n-1-dimensional vector implementing {@link IMclVector}, where n is the dimensionality of the environment.
 * @param <M> a movement (or sequence of movements) of the robot, implementing {@link IMclMove}. 
 * @param <R> a range measurement, implementing {@link IMclRangeReading}.
 */
public interface IMclRobot<V extends IMclVector, M extends IMclMove<M>, R extends IMclRangeReading<R,V>> {
	/**
	 * Causes a series of sensor measurements to determine the distance to various obstacles within the environment.
	 * @return an array of range measurements {@code <R>}. 
	 * @throws RobotException thrown if the range reading was not successful.
	 */
	R[] getRangeReadings() throws RobotException;
	/**
	 * Calculates a weight between 0 and 1 that specifies how similar two given range readings are to each other.
	 * @param firstRange the first range to be weighted against.
	 * @param secondRange the second range to be weighted against.
	 * @return a weight between 0 and 1.
	 */
	float calculateWeight(R firstRange, R secondRange);
	/**
	 * Causes the robot to perform a movement.
	 * @return the move the robot performed.
	 * @throws RobotException thrown if the move was not successful.
	 */
	M performMove() throws RobotException;
}
