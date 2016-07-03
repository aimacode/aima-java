package aima.core.environment.wumpusworld.action;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): page ???.<br>
 * <br>
 * The action Grab can be used to pick up the gold if it is in the same square
 * as the agent.
 * 
 * @author Ciaran O'Reilly
 * @author Anurag Rai
 */
public class Grab extends WWAction {
	public static final String GRAB_ACTION_NAME = "Grab";

	public Grab() {
		super(GRAB_ACTION_NAME);
	}
}
