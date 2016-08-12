package aima.core.robotics.datatypes;

/**
 * A pose consists of a position in the environment and a heading in which the pose is facing.<br/>
 * In a two-dimensional environment this would be an position represented through a point with {@code (x,y)} and an angle.<br/>
 * In an n-dimensional environment the angle is represented through a vector.<br/>
 * 
 * @author Arno von Borries
 * @author Jan Phillip Kretzschmar
 * @author Andreas Walscheid
 * 
 * @param <P> the class that is implementing this interface.
 * @param <V> an n-1-dimensional vector implementing {@link IMclVector}, where n is the dimensionality of the environment. This vector describes the angle between two rays in the environment.
 * @param <M> a movement (or sequence of movements) of the robot, implementing {@link IMclMove}.
 */
public interface IMclPose<P extends IMclPose<P,V,M>,V extends IMclVector,M extends IMclMove<M>> extends Cloneable {
	/**
	 * Moves a pose according to a given move.<br/>
	 * This function should return a new object to prevent unpredictable behavior through more than one usage of the same pose.
	 * @param move the move to be added onto the pose.
	 * @return a new pose that has been moved.
	 */
	P applyMovement(M move);
	/**
	 * Rotates a pose by a given {@link IMclVector}.<br/>
	 * This function should return a new object to prevent unpredictable behavior through more than one usage of the same pose.
	 * @param angle the angle by which the pose should be rotated.
	 * @return a new pose that has been rotated.
	 */
	P addAngle(V angle);
	/**
	 * Clones an object according to {@link Cloneable}.
	 * @return a new pose with the same position and heading.
	 */
	P clone();
	/**
	 * Calculates the length of the straight line between this pose and another pose.<br/>
	 * {@code x.distanceTo(y)} has to return the same value as {@code y.distanceTo(x)}.
	 * @param pose another pose to which the distance is calculated.
	 * @return the distance between the two poses.
	 */
	double distanceTo(P pose);
}