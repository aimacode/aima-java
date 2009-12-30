package aimax.osm.routing.agent;

import aima.core.agent.Action;
import aima.core.agent.Agent;
import aima.core.agent.EnvironmentState;
import aima.core.environment.map.MapEnvironment;
import aima.core.environment.map.MoveToAction;
import aima.gui.framework.AgentAppModel;
import aimax.osm.data.MapDataStore;
import aimax.osm.data.entities.MapNode;

/**
 * Provides a simple ready-to-use implementation of a model for OSM agent
 * applications.
 * @author R. Lunde
 */
public class OsmAgentModel extends AgentAppModel {
	
	protected OsmMap map;
	protected MapEnvironment env;

	public OsmAgentModel(OsmMap map) {
		this.map = map;
	}
	
	public MapDataStore getMapData() {
		return (map != null) ? map.getMapData() : null;
	}
	
	public void prepare(MapEnvironment env) {
		this.env = env;
		map.getMapData().getTracks().clear();
		fireModelChanged();
	}
	
	public void agentAdded(Agent agent, EnvironmentState state) {
		fireModelChanged();
	}

	/**
	 * Reacts on environment changes and updates the tour history. The command
	 * string is always send to all registered model change listeners. If the
	 * command is a location name (with attached position info) or
	 * {@link aima.core.agent.impl.AbstractAgent#NO_OP}, the agent's current
	 * location is added to the tour history and all listeners are informed
	 * about the change.
	 */
	@Override
	public void agentActed(Agent agent, Action command, EnvironmentState state) {
		for (AgentAppModel.ModelChangedListener listener : listeners) {
			listener.logMessage(command.toString());
		}
		if (command.isNoOp() || map.getPosition
				(((MoveToAction) command).getToLocation()) != null) {
			String loc = env.getAgentLocation(agent);
			MapNode node = map.getWayNode(loc);
			map.getMapData().addToTrack("Agent Track", node.getLat(), node.getLon());
			fireModelChanged();
		}
	}
}
