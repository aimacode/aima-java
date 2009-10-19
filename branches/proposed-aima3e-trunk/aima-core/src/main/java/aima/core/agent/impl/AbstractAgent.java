package aima.core.agent.impl;

import aima.core.agent.Action;
import aima.core.agent.Agent;
import aima.core.agent.AgentProgram;
import aima.core.agent.Percept;

/**
 * Artificial Intelligence A Modern Approach (2nd Edition): Figure 2.1, page 33.
 * 
 * Figure 2.1 Agents interact with environments through sensors and actuators.
 */

/**
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 */
public abstract class AbstractAgent implements Agent {

	protected AgentProgram program;
	private boolean alive = true;

	public AbstractAgent() {
		
	}
	
	public AbstractAgent(AgentProgram aProgram) {
		program = aProgram;
	}

	//
	// START-Agent
	public Action execute(Percept p) {
		if (null != program) {
			return program.execute(p);
		}
		return NoOpAction.NO_OP;
	}
	
	public boolean isAlive() {
		return alive;
	}
	
	public void setAlive(boolean alive) {
		this.alive = alive;
	}
	
	// END-Agent
	//
}