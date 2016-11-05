package aima.gui.swing.applications.robotics.simple;

import aima.core.robotics.datatypes.IMclMove;
import aima.core.util.Util;

/**
 * This class implements a basic movement in a two dimensional setting. It implements {@link IMclMove}.<br/>
 * It contains a rotation, a forward distance and another rotation. With these three elements, every pose can be reached from any other pose.
 * 
 * @author Arno von Borries
 * @author Jan Phillip Kretzschmar
 * @author Andreas Walscheid
 *
 */
public final class SimpleMove implements IMclMove<SimpleMove> {

	private static double ROTATION_NOISE;
	private static double MOVEMENT_NOISE;
	
	private final double firstRotation;
	private final double forward;
	private final double lastRotation;
	
	/**
	 * Sets the move noise model for the rotation.
	 * @param value the radians value of the noise.
	 */
	public static void setRotationNoise(double value) {
		ROTATION_NOISE = value;
	}
	
	/**
	 * Sets the move noise model for the distance.
	 * @param value the absolute value of the noise.
	 */
	public static void setMovementNoise(double value) {
		MOVEMENT_NOISE = value;
	}
	
	/**
	 * @param firstRotation the first rotation in radians.
	 * @param forward the distance to move forward.
	 * @param lastRotation the second rotation in radians.
	 */
	public SimpleMove(double firstRotation, double forward, double lastRotation) {
		this.firstRotation = firstRotation;
		this.forward = forward;
		this.lastRotation = lastRotation;
	}
	
	/**
	 * Returns the first rotation.
	 * @return the first rotation angle in radians.
	 */
	public double getFirstRotation() {
		return firstRotation;
	}
	
	/**
	 * Returns the forwarding distance.
	 * @return the distance to move forward.
	 */
	public double getForward() {
		return forward;
	}

	/**
	 * Returns the last rotation.
	 * @return the last rotation angle in radians.
	 */
	public double getLastRotation() {
		return lastRotation;
	}
	
	@Override
	public SimpleMove generateNoise() {
		final double firstRotationNew = Util.generateRandomDoubleBetween(firstRotation-ROTATION_NOISE, firstRotation+ROTATION_NOISE);
		final double forwardNew = Util.generateRandomDoubleBetween(forward-MOVEMENT_NOISE, forward+MOVEMENT_NOISE);
		final double lastRotationNew = Util.generateRandomDoubleBetween(lastRotation-ROTATION_NOISE, lastRotation+ROTATION_NOISE);
		return new SimpleMove(firstRotationNew,forwardNew,lastRotationNew);
	}
	
	@Override
	public String toString() {
		return String.format("(%.2f,%.2f,%.2f)", firstRotation,forward,lastRotation);
	}
}
