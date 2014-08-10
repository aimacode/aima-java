package aima.core.environment.wumpusworld.action;

import aima.core.agent.impl.DynamicAction;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 237.<br>
 * <br>
 * The action Grab can be used to pick up the gold if it is in the same square
 * as the agent.
 * 
 * @author Ciaran O'Reilly
 */
public class Grab extends DynamicAction {
	public static final String GRAB_ACTION_NAME = "Grab";

	public Grab() {
		super(GRAB_ACTION_NAME);
	}
}
