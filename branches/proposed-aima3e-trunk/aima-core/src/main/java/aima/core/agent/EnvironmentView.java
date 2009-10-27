package aima.core.agent;

public interface EnvironmentView {
	void notify(String msg);
	void envChanged(Agent agent, Action action, EnvironmentState state);
}
