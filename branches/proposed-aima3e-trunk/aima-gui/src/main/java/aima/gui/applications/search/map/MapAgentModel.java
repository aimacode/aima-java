package aima.gui.applications.search.map;

import java.util.ArrayList;
import java.util.List;

import aima.core.agent.Action;
import aima.core.agent.Agent;
import aima.core.agent.EnvironmentState;
import aima.core.agent.Percept;
import aima.core.environment.map.Map;
import aima.core.environment.map.MoveToAction;
import aima.core.environment.map.Scenario;
import aima.core.util.datastructure.Point2D;
import aima.gui.framework.AgentAppModel;

/**
 * Provides a ready-to-use implementation of a model for route planning agent
 * applications. It is based on a scenario description and a list of
 * destinations. Some features are not used. Subclasses can support those
 * features by overriding the methods {@link #hasInfos(String)}, and
 * {@link #hasObjects(String)}.
 * 
 * @author R. Lunde
 */
public class MapAgentModel extends AgentAppModel {
	/** A scenario. */
	protected Scenario scenario;
	/** A list of location names, possibly null. */
	protected List<String> destinations;

	/** Stores the locations, the agent has already visited. */
	private final ArrayList<String> tourHistory = new ArrayList<String>();

	/** Returns a list of all already visited agent locations. */
	public List<String> getTourHistory() {
		return tourHistory;
	}

	/** Clears the list of already visited locations. */
	public void clearTourHistory() {
		tourHistory.clear();
	}
	
	@Override
	public void agentAdded(Agent agent, Percept perceives) {
		String loc = scenario.getEnv().getAgentLocation(agent);
		tourHistory.add(loc);
		fireModelChanged();
	}

	/**
	 * Reacts on environment changes and updates the tour history. The command
	 * string is always send to all registered model change listeners. If the
	 * command is a location name (with attached position info) or
	 * {@link aima.core.agent.impl.AbstractAgent#NO_OP}, the agent's current location is
	 * added to the tour history and all listeners are informed about the
	 * change.
	 */
	@Override
	public void agentActed(Agent agent, Action command, EnvironmentState state) {
		for (AgentAppModel.ModelChangedListener listener : listeners) {
			listener.logMessage(command.toString());
		}
		if (command.isNoOp() || getLocCoords(((MoveToAction)command).getToLocation()) != null) {
			String loc = scenario.getEnv().getAgentLocation(agent);
			tourHistory.add(loc);
			fireModelChanged();
		}
	}

	/**
	 * Assigns values to the attributes {@link #scenario} and
	 * {@link #destinations}, clears the history and informs all interested
	 * listeners about the model change.
	 * 
	 * @param s
	 *            A scenario or null.
	 * @param d
	 *            List of location names or null.
	 */
	public void prepare(Scenario s, List<String> d) {
		scenario = s;
		destinations = d;
		clearTourHistory();
		fireModelChanged();
	}

	/** Checks whether there is a scenario available. */
	public boolean isEmpty() {
		return scenario == null;
	}

	/**
	 * Returns all location names mentioned in the environment map or in the
	 * agent map.
	 */
	public List<String> getLocations() {
		List<String> result = scenario.getEnvMap().getLocations();
		if (!result.containsAll(scenario.getAgentMap().getLocations())) {
			result = new ArrayList<String>(result);
			for (String loc : scenario.getAgentMap().getLocations())
				if (!result.contains(loc))
					result.add(loc);
		}
		return result;
	}

	/** Returns the map used by the agent. */
	public Map getAgentMap() {
		return scenario.getAgentMap();
	}

	/** Returns the map used by the environment. */
	public Map getEnvMap() {
		return scenario.getEnvMap();
	}

	/** Returns the agent. */
	public Agent getAgent() {
		return scenario.getEnv().getAgents().iterator().next();
	}

	/** Checks whether a given location is the initial location of the agent. */
	public boolean isStart(String loc) {
		return scenario.getInitAgentLocation() == loc;
	}

	/** Checks whether a given location is one of the specified destinations. */
	public boolean isDestination(String loc) {
		return destinations != null && destinations.contains(loc);
	}

	/**
	 * Returns the coordinates of the specified location.
	 */
	public Point2D getLocCoords(String loc) {
		return scenario.getEnvMap().getPosition(loc);
	}

	/**
	 * Checks whether special informations can be perceived at the location.
	 * This implementation always returns false.
	 */

	public boolean hasInfos(String loc) {
		return false; // not implemented yet
	}

	/**
	 * Checks whether interesting objects can be perceived at the location. This
	 * implementation always returns false.
	 */
	public boolean hasObjects(String loc) {
		return false; // not implemented yet
	}
}
