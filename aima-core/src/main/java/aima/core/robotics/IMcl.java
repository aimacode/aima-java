package aima.core.robotics;

import java.util.Set;

import aima.core.robotics.datatypes.IMclMove;
import aima.core.robotics.datatypes.IMclPose;
import aima.core.robotics.datatypes.IMclRangeReading;
import aima.core.robotics.datatypes.IMclVector;

/**
 * This interface defines the basic operations of the Monte-Carlo-Localization algorithm.<br/>
 *  TODO
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
	 * Generates a new cloud of samples. TODO
	 */
	public Set<P> generateCloud(int sampleCount);
	
	/**
	 * Runs the MCL algorithm. TODO
	 * @return the set of samples for the next time step.
	 */
	public Set<P> localize(Set<P> samples, M move, R[] rangeReadings);
}
