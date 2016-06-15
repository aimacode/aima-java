
package aima.core.environment.vacuum;

/**
 * @author Ciaran O'Reilly
 */
public class VacuumEnvironment {
	//
	// The 3 allowable actions in the Simple Vacuum World Environment
	// (others in more complex worlds could be 'Up' and 'Down'.
	public static final String ACTION_LEFT = "Left";
	public static final String ACTION_RIGHT = "Right";
	public static final String ACTION_SUCK = "Suck";
	//
	// Two default locations mentioned in examples given in AIMA4e
	public static final String LOCATION_A = "A";
	public static final String LOCATION_B = "B";

	//
	// The status of a location in the environment
	public enum Status {
		Clean, Dirty
	}
}