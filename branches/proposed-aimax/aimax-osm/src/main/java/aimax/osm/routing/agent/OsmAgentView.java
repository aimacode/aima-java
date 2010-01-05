package aimax.osm.routing.agent;

import java.awt.BorderLayout;

import aima.core.agent.Action;
import aima.core.agent.Agent;
import aima.core.agent.Environment;
import aima.core.agent.EnvironmentState;
import aima.core.environment.map.MapEnvironment;
import aima.core.environment.map.MoveToAction;
import aima.gui.framework.AgentAppEnvironmentView;
import aimax.osm.data.MapDataStore;
import aimax.osm.data.entities.MapNode;
import aimax.osm.viewer.MapViewPane;

public class OsmAgentView extends AgentAppEnvironmentView {

	MapViewPane mapViewPane;
	
	public OsmAgentView(MapDataStore mapData) {
		mapViewPane = new MapViewPane();
		mapViewPane.setModel(mapData);
		this.setLayout(new BorderLayout());
		add(mapViewPane, BorderLayout.CENTER);
	}
	
	public MapViewPane getMapViewPane() {
		return mapViewPane;
	}
	
	protected MapEnvironment getMapEnv() {
		return (MapEnvironment) env;
	}
	
	@Override
	public void setEnvironment(Environment env) {
		mapViewPane.getModel().clearTrack("Agent Track");
		super.setEnvironment(env);
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
		OsmMap map = (OsmMap) getMapEnv().getMap();
		if (command.isNoOp() || map.getPosition
				(((MoveToAction) command).getToLocation()) != null) {
			String loc = getMapEnv().getAgentLocation(agent);
			MapNode node = map.getWayNode(loc);
			map.getMapData().addToTrack("Agent Track", node.getLat(), node.getLon());
			notify(command.toString());
		}
	}

	@Override
	public void agentAdded(Agent agent, EnvironmentState resultingState) {
		OsmMap map = (OsmMap) getMapEnv().getMap();
		String loc = getMapEnv().getAgentLocation(agent);
		MapNode node = map.getWayNode(loc);
		map.getMapData().addToTrack("Agent Track", node.getLat(), node.getLon());
	}
}
