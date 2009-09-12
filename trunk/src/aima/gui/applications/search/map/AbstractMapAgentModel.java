package aima.gui.applications.search.map;

import java.util.ArrayList;
import java.util.List;

import aima.basic.Agent;
import aima.gui.framework.AgentAppModel;
import aima.search.map.DynAttributeNames;
import aima.search.map.Map;

/**
 * Abstract base class which defines a universal interface for route planning
 * agent application models.
 * 
 * @author R. Lunde
 */
public abstract class AbstractMapAgentModel extends AgentAppModel {
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

	/**
	 * Reacts on environment changes and updates the tour history. The command
	 * string is always send to all registered model change listeners. If the
	 * command is a location name (with attached position info) or
	 * {@link aima.basic.Agent#NO_OP} or {@link aima.basic.Agent#DIE}, the
	 * agent's current location is added to the tour history and all listeners
	 * are informed about the change.
	 */
	@Override
	public void envChanged(String command) {
		for (AgentAppModel.ModelChangedListener listener : listeners)
			listener.logMessage(command);
		if (getLocCoords(command) != null || command.equals(Agent.NO_OP)
				|| command.equals(Agent.DIE)) {
			String loc = (String) getAgent().getAttribute(
					DynAttributeNames.AGENT_LOCATION);
			tourHistory.add(loc);
			fireModelChanged();
		}
	}

	/** Checks, whether the model contains useful data at all. */
	public abstract boolean isEmpty();

	/** Returns all locations of the map which is used by the environment. */
	public abstract List<String> getLocations();

	/** Returns the map used by the agent. */
	public abstract Map getAgentMap();

	/** Returns the map used by the environment. */
	public abstract Map getEnvMap();

	/** Returns the agent. */
	public abstract Agent getAgent();

	/** Checks whether a given location is the initial location of the agent. */
	public abstract boolean isStart(String loc);

	/** Checks whether a given location is one of the specified destinations. */
	public abstract boolean isDestination(String loc);

	/**
	 * Creates an array of size 2 with the coordinates of the specified
	 * location.
	 */
	public abstract double[] getLocCoords(String loc);

	/** Checks whether special informations can be perceived at the location. */
	public abstract boolean hasInfos(String loc);

	/** Checks whether interesting objects can be perceived at the location. */
	public abstract boolean hasObjects(String loc);
}
