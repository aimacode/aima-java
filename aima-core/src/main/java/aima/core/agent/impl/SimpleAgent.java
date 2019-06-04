package aima.core.agent.impl;

import aima.core.agent.Agent;
import aima.core.agent.AgentProgram;

import java.util.Optional;

/**
 * @param <P> Type which is used to represent percepts
 * @param <A> Type which is used to represent actions
 * @author Ravi Mohan
 * @author Ciaran O'Reilly
 * @author Mike Stampone
 * @author Ruediger Lunde
 */
public class SimpleAgent<P, A> implements Agent<P, A> {

	protected AgentProgram<P, A> program;
	private boolean alive = true;

	public SimpleAgent() { }

	/**
	 * Constructs an Agent with the specified AgentProgram.
	 * 
	 * @param program
	 *            the Agent's program, which maps any given percept sequences to
	 *            an action.
	 */
	public SimpleAgent(AgentProgram<P, A> program) {
		this.program = program;
	}

	public Optional<A> act(P percept) {
		return (null != program) ? program.apply(percept) : Optional.empty();
	}

	public boolean isAlive() {
		return alive;
	}

	public void setAlive(boolean alive) {
		this.alive = alive;
	}
}