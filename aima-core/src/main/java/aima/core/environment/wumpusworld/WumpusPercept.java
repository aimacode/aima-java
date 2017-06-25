package aima.core.environment.wumpusworld;

import aima.core.agent.Percept;

/**
 * Artificial Intelligence A Modern Approach (3rd Edition): page 237.<br>
 * <br>
 * The agent has five sensors, each of which gives a single bit of information:
 * <ul>
 * <li>In the square containing the wumpus and in the directly (not diagonally)
 * adjacent squares, the agent will perceive a Stench.</li>
 * <li>In the squares directly adjacent to a pit, the agent will perceive a
 * Breeze.</li>
 * <li>In the square where the gold is, the agent will perceive a Glitter.</li>
 * <li>When an agent walks into a wall, it will perceive a Bump.</li>
 * <li>When the wumpus is killed, it emits a woeful Scream that can be perceived
 * anywhere in the cave.</li>
 * </ul>
 * 
 * @author Federico Baron
 * @author Alessandro Daniele
 * @author Ciaran O'Reilly
 * @author Ruediger Lunde
 */
public class WumpusPercept implements Percept {
	private boolean stench;
	private boolean breeze;
	private boolean glitter;
	private boolean bump;
	private boolean scream;

	public WumpusPercept setStench() {
		stench = true;
		return this;
	}

	public WumpusPercept setBreeze() {
		breeze = true;
		return this;
	}

	public WumpusPercept setGlitter() {
		glitter = true;
		return this;
	}

	public WumpusPercept setBump() {
		bump = true;
		return this;
	}

	public WumpusPercept setScream() {
		scream = true;
		return this;
	}

	public boolean isStench() {
		return stench;
	}

	public boolean isBreeze() {
		return breeze;
	}

	public boolean isGlitter() {
		return glitter;
	}

	public boolean isBump() {
		return bump;
	}

	public boolean isScream() {
		return scream;
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder("{");
		if (stench)
			result.append("Stench, ");
		if (breeze)
			result.append("Breeze, ");
		if (glitter)
			result.append("Glitter, ");
		if (bump)
			result.append("Bump, ");
		if (scream)
			result.append("Scream, ");
		if (result.length() > 1)
			result.delete(result.length() - 2, result.length());
		result.append("}");
		return result.toString();
	}
}
