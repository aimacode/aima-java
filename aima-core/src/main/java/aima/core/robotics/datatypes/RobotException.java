package aima.core.robotics.datatypes;

import aima.core.robotics.IMclRobot;

/**
 * A {@code RobotException} may be thrown by a class implementing {@link IMclRobot} during any actions invoked on the robot in case something has gone wrong and the localization should be halted.
 * 
 * @author Arno von Borries
 * @author Jan Phillip Kretzschmar
 * @author Andreas Walscheid
 *
 */
public class RobotException extends Exception {

	private static final long serialVersionUID = 1L;
}
