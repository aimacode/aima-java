package aima.test.core.unit.agent.impl;

import aima.core.agent.AgentProgram;
import aima.core.agent.impl.AbstractAgent;

/**
 * @author Ravi Mohan
 * @author Ruediger Lunde
 * 
 */
public class MockAgent<P, A> extends AbstractAgent<P, A> {
	public MockAgent() { }

	public MockAgent(AgentProgram<P, A> agent) {
		super(agent);
	}
}