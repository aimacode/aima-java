package aima.core.agent;

import java.util.Set;

public interface Environment {
	Set<Agent> getAgents();
	void addAgent(Agent a);
	void removeAgent(Agent a);
	Set<EnvironmentObject> getEnvironmentObjects();
	void addEnvironmentObject(EnvironmentObject eo);
	void removeEnvironmentObject(EnvironmentObject eo);
	void step();
	void step(int n);
	void stepUntilDone();
	boolean isDone();
	double getPerformanceMeasure(Agent forAgent);
	void updatePerformanceMeasure(Agent forAgent, double addTo);
	void addEnvironmentView(EnvironmentView ev);
	void removeEnvironmentView(EnvironmentView ev);
	void notifyViews(String msg);
}
