package aima.core.environment.wumpusworld.action;

/**
 * Artificial Intelligence A Modern Approach (4th Edition): page ???.<br>
 * <br>
 * The action Shoot can be used to fire an arrow in a straight line in the
 * direction the agent is facing. The arrow continues until it either hits (and
 * hence kills) the wumpus or hits a wall. The agent has only one arrow, so only
 * the first Shoot action has any effect.
 * 
 * @author Federico Baron
 * @author Alessandro Daniele
 * @author Ciaran O'Reilly
 * @author Anurag Rai
 */
public class Shoot extends WWAction {

	public static final String SHOOT_ACTION_NAME = "Shoot";

	public Shoot() {
		super(SHOOT_ACTION_NAME);
	}
}
