package aima.core.agent;

public interface Agent extends EnvironmentObject {
	 Action execute(Percept p);
	 boolean isAlive();
	 void setAlive(boolean alive);
}
