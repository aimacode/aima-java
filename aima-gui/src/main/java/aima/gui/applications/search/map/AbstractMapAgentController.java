package aima.gui.applications.search.map;

import java.text.DecimalFormat;
import java.util.List;

import aima.core.agent.Agent;
import aima.core.environment.map.AdaptableHeuristicFunction;
import aima.core.environment.map.MapEnvironment;
import aima.core.environment.map.Scenario;
import aima.gui.framework.AgentAppController;
import aima.gui.framework.MessageLogger;
import aima.gui.framework.SimulationThread;

/**
 * Provides a useful base class for agent application controller implementations
 * in the context of route finding agent application development. To get it
 * ready to work, all you need to do is, to provide implementations for the four
 * abstract methods. See {@link RouteFindingAgentApp} for an example.
 * 
 * @author Ruediger Lunde
 */
public abstract class AbstractMapAgentController extends AgentAppController {
	/** A scenario. */
	protected Scenario scenario;
	/**
	 * Some location names. For route finding problems, only one location
	 * should be specified.
	 */
	protected List<String> destinations;
	/** Search method to be used. */
	protected aima.core.search.framework.Search search;
	/** Heuristic function to be used when performing informed search. */
	protected AdaptableHeuristicFunction heuristic;
	/** Is the scenario up to date? */
	protected boolean isPrepared;
	/** Sleep time between two steps during simulation in msec. */
	protected long sleepTime = 500l;

	/** Clears all tracks and prepares simulation if necessary. */
	@Override
	public void clear() {
		((MapAgentView) frame.getEnvView()).clearTracks();
		if (!isPrepared())
			prepare(null);
	}

	/**
	 * Template method, which performs necessary preparations for running the
	 * agent. The behavior is strongly influenced by the primitive operations
	 * {@link #selectScenarioAndDest(int, int)}, {@link #prepareView()} and
	 * {@link #createHeuristic(int)}.
	 */
	@Override
	public void prepare(String changedSelectors) {
		MapAgentFrame.SelectionState state = frame.getSelection();
		selectScenarioAndDest(state.getValue(MapAgentFrame.SCENARIO_SEL), state
				.getValue(MapAgentFrame.DESTINATION_SEL));
		prepareView();
		heuristic = createHeuristic(state.getValue(MapAgentFrame.HEURISTIC_SEL));
		search = SearchFactory.getInstance().createSearch(
				state.getValue(MapAgentFrame.SEARCH_SEL),
				state.getValue(MapAgentFrame.SEARCH_MODE_SEL), heuristic);
		isPrepared = true;
	}
	
	/**
	 * Checks whether the current scenario contains an environment which is
	 * ready for simulation (no agents or not done).
	 */
	public boolean isPrepared() {
		return isPrepared && (scenario.getEnv().getAgents().isEmpty()
				|| !scenario.getEnv().isDone());
	}
	
	/**
	 * Calls {@link #initAgents(MessageLogger)} if necessary and
	 * then starts simulation until done.
	 */
	public void run(MessageLogger logger) {
		logger.log("<simulation-protocol>");
		logger.log("search: " + search.getClass().getName());
		if (heuristic != null)
			logger.log("heuristic: " + heuristic.getClass().getName());
		MapEnvironment env = scenario.getEnv();
		if (env.getAgents().isEmpty())
			initAgents(logger);
		try {
			while (!env.isDone() && !frame.simulationPaused()) {
				Thread.sleep(sleepTime);
				env.step();
			}
		} catch (InterruptedException e) {}
		logger.log("</simulation-protocol>\n");
	}
	
	/**
	 * Calls {@link #initAgents(MessageLogger)} if necessary and
	 * then executes one simulation step.
	 */
	@Override
	public void step(MessageLogger logger) {
		MapEnvironment env = scenario.getEnv();
		if (env.getAgents().isEmpty())
			initAgents(logger);
		env.step();
	}
		
	/** Updates the status of the frame. */
	public void update(SimulationThread simulationThread) {
		if (simulationThread.isCanceled()) {
			frame.setStatus("Task canceled.");
			isPrepared = false;
		} else if (frame.simulationPaused()){
			frame.setStatus("Task paused.");
		} else {
			StringBuffer statusMsg = new StringBuffer();
			statusMsg.append("Task completed");
			List<Agent> agents = scenario.getEnv().getAgents();
			if (agents.size() == 1) {
				Double travelDistance = scenario.getEnv().getAgentTravelDistance(
						agents.get(0));
				if (travelDistance != null) {
					DecimalFormat f = new DecimalFormat("#0.0");
					statusMsg.append("; travel distance: "
							+ f.format(travelDistance));
				}
			}
			statusMsg.append(".");
			frame.setStatus(statusMsg.toString());
		}
	}

	/////////////////////////////////////////////////////////////////
	// abstract methods

	/**
	 * Primitive operation, responsible for assigning values to attributes
	 * {@link #scenario} and {@link #destinations}.
	 */
	abstract protected void selectScenarioAndDest(int scenarioIdx, int destIdx);

	/**
	 * Primitive operation, responsible for preparing the view. Scenario and
	 * destinations are already selected when this method is called.
	 */
	abstract protected void prepareView();

	/**
	 * Factory method, responsible for creating a heuristic function.
	 */
	abstract protected AdaptableHeuristicFunction createHeuristic(int heuIdx);

	/**
	 * Primitive operation, responsible for creating new agents and adding
	 * them to the current environment.
	 */
	protected abstract void initAgents(MessageLogger logger);
	
}