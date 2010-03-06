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
	protected boolean isPrepared = false;
	
	/** Prepare next simulation. */
	@Override
	public void clear() {
		if (!isPrepared())
		prepare(null);
	}

	/**
	 * Creates a vacuum agent and a corresponding environment based on the
	 * selection state of the selectors and finally passes the
	 * environment to the viewer.
	 */
	@Override
	public void prepare(String changedSelector) {
		AgentAppFrame.SelectionState selState = frame.getSelection();
		env = null;
		switch (selState.getValue(VacuumFrame.ENV_SEL)) {
		case 0:
			env = new VacuumEnvironment();
			break;
		}
		agent = null;
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
			isPrepared = true;
		}
	}
	
	/** Checks whether a step can be executed. */
	@Override
	public boolean isPrepared() {
		return isPrepared && !env.isDone();
	}

	/** Starts the agent. */
	@Override
	public void run(MessageLogger logger) {
		logger.log("<simulation-log>");
		try {
			while (!env.isDone()) {
				Thread.sleep(500);
				env.step();
			}
		} catch (InterruptedException e) {}
		logger.log("Performance: "
				+ env.getPerformanceMeasure(agent));
		logger.log("</simulation-log>\n");
	}

	/** Executes one step. */
	@Override
	public void step(MessageLogger logger) {
		env.step();
	}

	/** Updates the status of the frame after the agent has finished its work. */
	public void update(AgentThread agentThread) {
		if (agentThread.isCanceled()) {
			frame.setStatus("Task canceled.");
			isPrepared = false;
		} else {
			frame.setStatus("Task completed.");
		}
	}
}

