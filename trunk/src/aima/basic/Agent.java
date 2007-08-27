package aima.basic;

import java.util.Hashtable;

/**
 * Artificial Intelligence A Modern Approach (2nd Edition): Figure 2.1, page 33.
 * 
 * Figure 2.1 Agents interact with environments through sensors and actuators.
 */

/**
 * @author Ravi Mohan
 * 
 */
public abstract class Agent extends ObjectWithDynamicAttributes {
	protected AgentProgram program;

	protected boolean isAlive;

	protected Hashtable enviromentSpecificAttributes;

	protected Agent() {
		live();

	}

	public Agent(AgentProgram aProgram) {
		this();

		program = aProgram;
	}

	public String execute(Percept p) {
		return program.execute(p);
	}

	public void live() {
		isAlive = true;
	}

	public void die() {
		isAlive = false;
	}

	public boolean isAlive() {
		return isAlive;
	}

}