package aima.gui.demo.agent;

import aima.core.agent.Agent;
import aima.core.agent.EnvironmentView;
import aima.core.agent.impl.SimpleEnvironmentView;
import aima.core.environment.vacuum.ModelBasedReflexVacuumAgent;
import aima.core.environment.vacuum.VacuumEnvironment;

/**
 * Demonstrates the use of the VaccumEnvironment and different vacuum agents.
 * 
 * @author Ruediger Lunde
 */
public class TrivialVacuumDemo {
	public static void main(String[] args) {
		// create environment with random state of cleaning.
		VacuumEnvironment env = new VacuumEnvironment();
		EnvironmentView view = new SimpleEnvironmentView();
		env.addEnvironmentView(view);
		
		Agent a = null;
		a = new ModelBasedReflexVacuumAgent();
		// a = new ReflexVacuumAgent();
		// a = new SimpleReflexVacuumAgent();
		// a = new TableDrivenVacuumAgent();
		
		env.addAgent(a);
		env.step(16);
		env.notifyViews("Performance=" + env.getPerformanceMeasure(a));
	}
}
