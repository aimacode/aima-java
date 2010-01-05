package aima.gui.applications.search.map;

import java.text.DecimalFormat;
import java.util.List;

import aima.core.agent.Agent;
import aima.core.environment.map.AdaptableHeuristicFunction;
import aima.core.environment.map.Scenario;
import aima.gui.applications.search.SearchFactory;
import aima.gui.framework.AgentAppController;
import aima.gui.framework.AgentThread;
import aima.gui.framework.MessageLogger;

/**
 * Provides a useful base class for agent application controller implementations
 * in the context of route finding agent application development. To get it
 * ready to work, all you need to do is, to provide implementations for the four
 * abstract methods. See {@link RouteFindingAgentApp} for an example.
 * 
 * @author R. Lunde
 */
public abstract class AbstractMapAgentController extends AgentAppController {
	/** A scenario. */
	protected Scenario scenario;
	/**
	 * Some location names. For route planning problems, only one location
	 * should be specified.
	 */
	protected List<String> destinations;
	/** Search method to be used. */
	protected aima.core.search.framework.Search search;
	/** Heuristic function to be used when performing informed search. */
	protected AdaptableHeuristicFunction heuristic;

	/** Clears the model's tour history. */
	@Override
	public void clear() {
		((MapAgentView) frame.getEnvView()).clearTracks();
	}

	/**
	 * Template method, which performs necessary preparations for running the
	 * agent. The behavior is strongly influenced by the primitive operations
	 * {@link #selectScenarioAndDest(int, int)}, {@link #prepareModel()} and
	 * {@link #createHeuristic(int)}.
	 */
	@Override
	public void prepare() {
		MapAgentFrame.SelectionState state = frame.getSelection();
		selectScenarioAndDest(state.getValue(MapAgentFrame.SCENARIO_SEL), state
				.getValue(MapAgentFrame.DESTINATION_SEL));
		prepareView();
		heuristic = createHeuristic(state.getValue(MapAgentFrame.HEURISTIC_SEL));
		search = SearchFactory.getInstance().createSearch(
				state.getValue(MapAgentFrame.SEARCH_SEL),
				state.getValue(MapAgentFrame.SEARCH_MODE_SEL), heuristic);
	}
		
	/** Updates the status of the frame. */
	public void update(AgentThread agentThread) {
		if (agentThread.isCancelled()) {
			frame.setStatus("Task cancelled.");
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
	 * Responsible for creating and starting the agent.
	 * Scenario, destinations are selected before as well as search method and
	 * search heuristic.
	 */
	public abstract void run(MessageLogger logger);
}