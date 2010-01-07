package aima.gui.applications.vacuum;

import aima.core.agent.impl.AbstractAgent;
import aima.core.environment.vacuum.ModelBasedReflexVacuumAgent;
import aima.core.environment.vacuum.ReflexVacuumAgent;
import aima.core.environment.vacuum.SimpleReflexVacuumAgent;
import aima.core.environment.vacuum.TableDrivenVacuumAgent;
import aima.core.environment.vacuum.VacuumEnvironment;
import aima.gui.framework.AgentAppController;
import aima.gui.framework.AgentAppFrame;
import aima.gui.framework.AgentThread;
import aima.gui.framework.MessageLogger;

/**
 * Defines how to react on user button events.
 */
public class VacuumController extends AgentAppController {
	
	protected VacuumEnvironment env = null;
	protected AbstractAgent agent = null;
	
	/** Does nothing. */
	@Override
	public void clear() {
	}

	/**
	 * Creates a vacuum agent and a corresponding environment based on the
	 * selection state of the selectors and finally passes the
	 * environment to the viewer.
	 */
	@Override
	public void prepare() {
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
			env.addAgent(agent);
		}
	}

	/** Starts the agent. */
	@Override
	public void run(MessageLogger logger) {
		if (env != null && agent != null) {
			logger.log("<simulation-log>");
			try {
				while (!env.isDone()) {
					Thread.sleep(500);
					env.step();
				}
			} catch (InterruptedException e) {}
			logger.log("Performance: "
					+ env.getPerformanceMeasure(agent));
			logger.log("</simulation-log>");
		}
	}
	
	/** Updates the status of the frame after the agent has finished its work. */
	public void update(AgentThread agentThread) {
		if (agentThread.isCanceled())
			frame.setStatus("Task cancelled.");
		else
			frame.setStatus("Task completed.");
	}
}

