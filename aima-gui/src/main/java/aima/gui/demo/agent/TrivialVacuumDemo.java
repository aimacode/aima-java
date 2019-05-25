package aima.gui.demo.agent;

import aima.core.agent.Action;
import aima.core.agent.Agent;
import aima.core.agent.Environment;
import aima.core.agent.EnvironmentView;
import aima.core.agent.impl.DynamicPercept;
import aima.core.agent.impl.SimpleEnvironmentView;
import aima.core.environment.vacuum.ModelBasedReflexVacuumAgent;
import aima.core.environment.vacuum.VacuumEnvironment;

/**
 * Demonstrates, how to set up a simple environment, place an agent in it,
 * and run it. The vacuum world is used as a simple example.
 * 
 * @author Ruediger Lunde
 */
public class TrivialVacuumDemo {
	public static void main(String[] args) {
		// create environment with random state of cleaning.
		Environment<DynamicPercept, Action> env = new VacuumEnvironment();
		EnvironmentView<Object, Object> view = new SimpleEnvironmentView();
		env.addEnvironmentView(view);
		
		Agent<DynamicPercept, Action> agent;
		agent = new ModelBasedReflexVacuumAgent();
		// agent = new ReflexVacuumAgent();
		// agent = new SimpleReflexVacuumAgent();
		// agent = new TableDrivenVacuumAgent();

		env.addAgent(agent);
		env.step(16);
		env.notifyViews("Performance=" + env.getPerformanceMeasure(agent));
	}
}
