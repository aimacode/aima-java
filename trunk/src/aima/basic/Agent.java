package aima.basic;

import java.util.Hashtable;

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