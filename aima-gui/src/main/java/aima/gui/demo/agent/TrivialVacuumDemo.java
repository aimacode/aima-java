package aima.gui.demo.agent;

import aima.core.agent.*;
import aima.core.agent.impl.DynamicAction;
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
		Environment<Percept, Action> env = new VacuumEnvironment();
		EnvironmentView<Object, Object> view = new SimpleEnvironmentView();
		env.addEnvironmentView(view);
		
		Agent<Percept, Action> a = null;
		a = new ModelBasedReflexVacuumAgent();
		// a = new ReflexVacuumAgent();
		// a = new SimpleReflexVacuumAgent();
		// a = new TableDrivenVacuumAgent();
		
		env.addAgent(a);
		env.step(16);
		env.notifyViews("Performance=" + env.getPerformanceMeasure(a));
	}
}
