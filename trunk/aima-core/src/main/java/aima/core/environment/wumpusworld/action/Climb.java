package aima.core.environment.wumpusworld.action;

import aima.core.agent.impl.DynamicAction;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 237.<br>
 * <br>
 * The action Climb can be used to climb out of the cave, but only from square
 * [1,1].
 * 
 * @author Ciaran O'Reilly
 */
public class Climb extends DynamicAction {
	public static final String CLIMB_ACTION_NAME = "Climb";

	public Climb() {
		super(CLIMB_ACTION_NAME);
	}
}
