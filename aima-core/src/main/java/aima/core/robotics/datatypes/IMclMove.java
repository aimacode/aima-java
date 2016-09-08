package aima.core.robotics.datatypes;

/**
 * This interface represents a movement or a sequence of movements that the robot performed.<br/>
 * In addition it describes a method for using a movement noise model on the move.
 * 
 * @author Arno von Borries
 * @author Jan Phillip Kretzschmar
 * @author Andreas Walscheid
 * 
 * @param <M> the class that is implementing this interface.
 */
public interface IMclMove<M extends IMclMove<M>> {
	/**
	 * Generates noise onto the move to mask errors in measuring the performed movements and to localize successfully with a smaller number of particles than without noise.
	 * @return a new move onto that noise has been added.
	 */
	M generateNoise();
}
