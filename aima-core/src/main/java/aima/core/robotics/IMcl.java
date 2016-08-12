package aima.core.robotics;

import aima.core.robotics.datatypes.IMclMove;
import aima.core.robotics.datatypes.IMclPose;
import aima.core.robotics.datatypes.IMclRangeReading;
import aima.core.robotics.datatypes.IMclVector;

/**
 * This interface defines the basic operations of the Monte-Carlo-Localization algorithm.<br/>
 * 
 * The algorithm can be used through the function {@code localize()}.<br/>
 * Otherwise the algorithm can be used step by step using these functions:
 * <ol>
 * <li>{@code applyMove(IMclRobot.performMove())}</li>
 * <li>{@code weightParticles(IMclRobot.getRangeReadings())}</li>
 * <li>{@code reselectParticles()}</li>
 * <li>{@code getPose()}</li>
 * <li>If 4. returns {@code null}: Repeat at 1.</li>
 * </ol>
 * 
 * @author Arno von Borries
 * @author Jan Phillip Kretzschmar
 * @author Andreas Walscheid
 *
 * @param <P> a pose implementing {@link IMclPose}.
 * @param <V> an n-1-dimensional vector implementing {@link IMclVector}, where n is the dimensionality of the environment. This vector describes the angle between two rays in the environment.
 * @param <M> a movement (or sequence of movements) of the robot, implementing {@link IMclMove}.
 * @param <R> a range measurement, implementing {@link IMclRangeReading}.
 */
public interface IMcl<P extends IMclPose<P,V,M>, V extends IMclVector, M extends IMclMove<M>, R extends IMclRangeReading<R,V>> {
	/**
	 * Generates a new cloud of particles.
	 */
	public void generateCloud();
	/**
	 * Applies a movement (or sequence of movements) which the robot performed to every particle in the cloud. 
	 * @param move the move the robot performed.
	 */
	public void applyMove(M move);
	/**
	 * Weights every particle in the cloud based on a range noise model as specified in {@link IMclRobot}. 
	 * @param rangeReadings a set of range readings by which the particles will be weighted.
	 */
	public void weightParticles(R[] rangeReadings);
	/**
	 * As specified by the AIMA 3rd edition MCL algorithm, this always re-samples the particles in the cloud using the Weighted-Sample-With-Replacement method. 
	 */
	public void resampleParticles();
	/**
	 * Verifies whether a sufficiently accurate pose has been established. 
	 * @return the found pose. {code null} if none was found.  
	 */
	public P getPose();
	/**
	 * Runs the MCL algorithm looped until the pose of the robot has been established. 
	 * @return the found pose. {@code null} if none was found.
	 */
	public P localize();
}
