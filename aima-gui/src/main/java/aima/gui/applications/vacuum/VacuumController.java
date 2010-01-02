package aima.gui.applications.vacuum;

import aima.core.agent.impl.AbstractAgent;
import aima.core.environment.vacuum.ModelBasedReflexVacuumAgent;
import aima.core.environment.vacuum.ReflexVacuumAgent;
import aima.core.environment.vacuum.SimpleReflexVacuumAgent;
import aima.core.environment.vacuum.TableDrivenVacuumAgent;
import aima.core.environment.vacuum.VacuumEnvironment;
import aima.gui.framework.AgentAppController;
import aima.gui.framework.AgentAppFrame;
import aima.gui.framework.MessageLogger;

/**
 * Defines how to react on user button events.
 */
public class VacuumController extends AgentAppController {
	
	VacuumEnvironment env = null;
	AbstractAgent agent = null;
	
	/** Does nothing. */
	@Override
	public void clearAgent() {
	}

	/**
	 * Creates a vacuum agent and a corresponding environment based on the
	 * selection state of the selectors and finally updates the model.
	 */
	@Override
	public void prepareAgent() {
		AgentAppFrame.SelectionState selState = frame.getSelection();
		env = null;
		agent = null;
		switch (selState.getValue(VacuumFrame.ENV_SEL)) {
		case 0:
			env = new VacuumEnvironment();
			break;
		}
		switch (selState.getValue(VacuumFrame.AGENT_SEL)) {
		case 0:
			agent = new TableDrivenVacuumAgent();
			break;
		case 1:
			agent = new ReflexVacuumAgent();
			break;
		case 2:
			agent = new SimpleReflexVacuumAgent();
			break;
		case 3:
			agent = new ModelBasedReflexVacuumAgent();
			break;
		}
		if (env != null && agent != null) {
			frame.getEnvView().setEnvironment(env);
			env.addAgent(agent, VacuumEnvironment.Location.A);
		}
	}

	/** Starts the agent and afterwards updates the status of the frame. */
	@Override
	public void runAgent() {
		if (env != null && agent != null) {
			MessageLogger logger = frame.getMessageLogger();
			logger.log("<simulation-log>");
			while (!env.isDone()) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {}
				env.step();
			}
			logger.log("Performance: "
					+ env.getPerformanceMeasure(agent));
			logger.log("</simulation-log>");
			frame.setStatus("Task completed.");
		}
	}
}

