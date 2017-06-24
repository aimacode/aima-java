package aima.gui.swing.applications.agent;

import aima.core.agent.Action;
import aima.core.agent.impl.AbstractAgent;
import aima.core.environment.vacuum.*;
import aima.core.search.agent.NondeterministicSearchAgent;
import aima.core.search.nondeterministic.NondeterministicProblem;
import aima.gui.swing.framework.AgentAppController;
import aima.gui.swing.framework.AgentAppFrame;
import aima.gui.swing.framework.MessageLogger;
import aima.gui.swing.framework.SimulationThread;

/**
 * Defines how to react on user button events.
 * 
 * @author Ruediger Lunde
 */
public class VacuumController extends AgentAppController {
	
	protected VacuumEnvironment env = null;
	protected AbstractAgent agent = null;
	protected boolean isPrepared = false;
	
	/** Prepares next simulation if that makes sense. */
	@Override
	public void clear() {
		if (!isPrepared())
		prepare(null);
	}

	/**
	 * Creates a vacuum environment and a corresponding agent based on the
	 * state of the selectors and finally passes the environment to the viewer.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void prepare(String changedSelector) {
		AgentAppFrame.SelectionState selState = frame.getSelection();
		env = null;
		switch (selState.getIndex(VacuumFrame.ENV_SEL)) {
		case 0:
			env = new VacuumEnvironment();
			break;
		case 1:
			env = new NondeterministicVacuumEnvironment();
			break;
		}
		agent = null;
		switch (selState.getIndex(VacuumFrame.AGENT_SEL)) {
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
		case 4:
			agent = new NondeterministicSearchAgent<>(percept -> (VacuumEnvironmentState) percept, env);
			break;
		}
		if (env != null && agent != null) {
			frame.getEnvView().setEnvironment(env);
			env.addAgent(agent);
			if (agent instanceof NondeterministicSearchAgent) {
				NondeterministicProblem<VacuumEnvironmentState, Action> problem =
						new NondeterministicProblem<>((VacuumEnvironmentState) env.getCurrentState(),
								VacuumWorldFunctions::getActions, VacuumWorldFunctions.createResultsFunction(agent),
								VacuumWorldFunctions::testGoal, (s, a, sPrimed) -> 1.0);
				// Set the problem now for this kind of agent
				((NondeterministicSearchAgent<VacuumEnvironmentState, Action>) agent).makePlan(problem);
			}
			isPrepared = true;
		}
	}
	
	/** Checks whether simulation can be started. */
	@Override
	public boolean isPrepared() {
		return isPrepared && !env.isDone();
	}

	/** Starts simulation. */
	@Override
	public void run(MessageLogger logger) {
		logger.log("<simulation-log>");
		try {
			while (!env.isDone() && !frame.simulationPaused()) {
				Thread.sleep(500);
				env.step();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		logger.log("Performance: "
				+ env.getPerformanceMeasure(agent));
		logger.log("</simulation-log>\n");
	}

	/** Executes one simulation step. */
	@Override
	public void step(MessageLogger logger) {
		env.step();
	}

	/** Updates the status of the frame after simulation has finished. */
	public void update(SimulationThread simulationThread) {
		if (simulationThread.isCancelled()) {
			frame.setStatus("Task canceled.");
			isPrepared = false;
		} else if (frame.simulationPaused()){
			frame.setStatus("Task paused.");
		} else {
			frame.setStatus("Task completed.");
		}
	}
}

