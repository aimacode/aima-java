package aima.core.agent;

public interface EnvironmentView {
	void notify(String msg);
	void envChanged(Action action, EnvironmentState state);
}
