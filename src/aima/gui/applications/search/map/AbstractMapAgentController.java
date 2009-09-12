package aima.gui.applications.search.map;

import java.util.List;

import aima.basic.Agent;
import aima.gui.framework.AgentAppController;
import aima.search.framework.SearchFactory;
import aima.search.map.AdaptableHeuristicFunction;
import aima.search.map.DynAttributeNames;
import aima.search.map.Scenario;


/**
 * Provides a useful base class for agent application controller
 * implementations in the context of route planning agent application
 * development.
 * To get it ready to work, all you need to do is, to provide implementations
 * for the four abstract methods.
 * See {@link RoutePlanningAgentAppDemo} for an example.
 * @author R. Lunde
 */
public abstract class AbstractMapAgentController extends AgentAppController {
	/** A scenario. */
	protected Scenario scenario;
	/**
	 * Some location names. For routing problems, only one location should
	 * be specified.
	 */
	protected List<String> destinations;
	/** Search method to be used. */
	protected aima.search.framework.Search search;
	/** Heuristic function to be used when performing informed search. */
	protected AdaptableHeuristicFunction heuristic;
	
	/** Clears the model's tour history. */
	public void clearAgent() {
		((AbstractMapAgentModel) model).clearTourHistory();
		frame.modelChanged();
	}
	
	/** 
	 * Performs necessary preparations for running the agent. The behavior is
	 * strongly influenced by the abstract methods {@link #selectScenarioAndDest(int, int)},
	 * {@link #prepareModel()} and {@link #createHeuristic(int)}.
	 */
	public void prepareAgent() {
		MapAgentFrame.SelectionState state = frame.getSelection();
		selectScenarioAndDest(
				state.getValue(MapAgentFrame.SCENARIO_SEL),
				state.getValue(MapAgentFrame.DESTINATION_SEL));
		prepareModel();
		search = SearchFactory.getInstance().createSearch(
				state.getValue(MapAgentFrame.SEARCH_SEL),
				state.getValue(MapAgentFrame.SEARCH_MODE_SEL));
		heuristic = createHeuristic(
				state.getValue(MapAgentFrame.HEURISTIC_SEL));
		scenario.getEnv().registerView(model);
	}
	
	/** Starts the agent and updates the status bar of the frame. */
	public void runAgent() {
		startAgent();
		List agents = scenario.getEnv().getAgents();
		if (agents.size() == 1) {
			Agent agent = (Agent) agents.get(0);
			String status =
				(String) agent.getAttribute(DynAttributeNames.AGENT_STATUS);
			Integer travelDistance =
				(Integer) agent.getAttribute(DynAttributeNames.AGENT_TRAVEL_DISTANCE);
			if (status != null && travelDistance != null)
				frame.setStatus("Task " + status + "; travel distance " + travelDistance);
			else
				frame.setStatus("Task completed.");
		}
	}
	
	
	/////////////////////////////////////////////////////////////////
	// abstract methods
	
	/**
	 * Template method, responsible for assigning values
	 * to attributes {@link #scenario} and {@link #destinations}.
	 */
	abstract protected void selectScenarioAndDest(int scenarioIdx, int destIdx);
	
	/**
	 * Template method, responsible for preparing the model.
	 * Scenario and destinations are already selected when
	 * this method is called.
	 */
	abstract protected void prepareModel();
	
	/**
	 * Factory method, responsible for creating a heuristic function.
	 */
	abstract protected AdaptableHeuristicFunction createHeuristic(int heuIdx);

	/**
	 * Template method, responsible for creating and starting the agent.
	 * Scenario, destinations are selected before as well
	 * as search method and search heuristic.
	 */
	protected abstract void startAgent();
	
}