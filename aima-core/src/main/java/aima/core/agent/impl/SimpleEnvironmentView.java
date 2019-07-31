package aima.core.agent.impl;

import aima.core.agent.Agent;
import aima.core.agent.Environment;
import aima.core.agent.EnvironmentListener;

/**
 * Simple environment listener which uses the standard output stream to inform about
 * relevant events.
 * 
 * @author Ruediger Lunde
 */
public class SimpleEnvironmentView implements EnvironmentListener<Object, Object> {
	@Override
	public void notify(String msg) {
		System.out.println("Message: " + msg);
	}

	@Override
	public void agentAdded(Agent<?, ?> agent, Environment<?, ?> source) {
		int agentId = source.getAgents().indexOf(agent) + 1;
		System.out.println("Agent " + agentId + " added.");
	}

	@Override
	public void agentActed(Agent<?, ?> agent, Object percept, Object action, Environment<?, ?> source) {
		StringBuilder builder = new StringBuilder();
		int agentId = source.getAgents().indexOf(agent) + 1;
		builder.append("Agent ").append(agentId).append(" acted.");
		builder.append("\n   Percept: ").append(percept.toString());
		builder.append("\n   Action: ").append(action.toString());
		System.out.println(builder.toString());
	}
}
