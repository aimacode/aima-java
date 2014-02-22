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
 */
public class AgentPercept implements Percept {
	private boolean stench;
	private boolean breeze;
	private boolean glitter;
	private boolean bump;
	private boolean scream;

	/**
	 * Default Constructor. All sensor inputs are considered false.
	 */
	public AgentPercept() {
		setStench(false);
		setBreeze(false);
		setGlitter(false);
		setBump(false);
		setScream(false);
	}

	/**
	 * Constructor with all 5 sensor inputs explicitly set.
	 * 
	 * @param stench
	 * @param breeze
	 * @param glitter
	 * @param bump
	 * @param scream
	 */
	public AgentPercept(boolean stench, boolean breeze, boolean glitter,
			boolean bump, boolean scream) {
		setStench(stench);
		setBreeze(breeze);
		setGlitter(glitter);
		setBump(bump);
		setScream(scream);
	}

	public boolean isStench() {
		return stench;
	}

	public void setStench(boolean stench) {
		this.stench = stench;
	}

	public boolean isBreeze() {
		return breeze;
	}

	public void setBreeze(boolean breeze) {
		this.breeze = breeze;
	}

	public boolean isGlitter() {
		return glitter;
	}

	public void setGlitter(boolean glitter) {
		this.glitter = glitter;
	}

	public boolean isBump() {
		return bump;
	}

	public void setBump(boolean bump) {
		this.bump = bump;
	}

	public boolean isScream() {
		return scream;
	}

	public void setScream(boolean scream) {
		this.scream = scream;
	}
	
	@Override
	public String toString() {
		return "["+ ((stench) ? "Stench" : "None") + ", "
				  + ((breeze) ? "Breeze" : "None") + ", "
				  + ((glitter) ? "Glitter" : "None") + ", "
				  + ((bump) ? "Bump" : "None") + ", "
				  + ((scream) ? "Scream" : "None") + "]";
	}
}
