package aima.core.environment.wumpusworld.action;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): page ???.<br>
 * <br>
 * The action Climb can be used to climb out of the cave, but only from square
 * [1,1].
 * 
 * @author Ciaran O'Reilly
 * @author Anurag Rai
 */
public class Climb extends WWAction {
	public static final String CLIMB_ACTION_NAME = "Climb";

	public Climb() {
		super(CLIMB_ACTION_NAME);
	}

}
