package aima.gui.applications.search.map;

import java.util.ArrayList;
import java.util.List;

import aima.basic.Agent;
import aima.search.map.Map;
import aima.search.map.Scenario;

/**
 * Provides a ready-to-use implementation of a model for route planning agent
 * applications. It is based on a scenario description and a list of
 * destinations. Some features are not used. Subclasses can support those
 * features by overriding the methods {@link #hasInfos(String)}, and
 * {@link #hasObjects(String)}.
 * 
 * @author R. Lunde
 */
public class MapAgentModel extends AbstractMapAgentModel {
	/** A scenario. */
	protected Scenario scenario;
	/** A list of location names, possibly null. */
	protected List<String> destinations;

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
	@Override
	public boolean isEmpty() {
		return scenario == null;
	}

	/**
	 * Returns all location names mentioned in the environment map or in the
	 * agent map.
	 */
	@Override
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

	@Override
	public Map getAgentMap() {
		return scenario.getAgentMap();
	}

	@Override
	public Map getEnvMap() {
		return scenario.getEnvMap();
	}

	@Override
	public Agent getAgent() {
		return (Agent) scenario.getEnv().getAgents().get(0);
	}

	@Override
	public boolean isStart(String loc) {
		return scenario.getInitAgentLocation() == loc;
	}

	@Override
	public boolean isDestination(String loc) {
		return destinations != null && destinations.contains(loc);
	}

	@Override
	public double[] getLocCoords(String loc) {
		return scenario.getEnvMap().getXY(loc);
	}

	/** Always returns false. */
	@Override
	public boolean hasInfos(String loc) {
		return false; // not implemented yet
	}

	/** Always returns false. */
	@Override
	public boolean hasObjects(String loc) {
		return false; // not implemented yet
	}
}
