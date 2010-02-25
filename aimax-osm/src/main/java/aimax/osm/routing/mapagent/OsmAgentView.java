package aimax.osm.routing.mapagent;

import java.awt.BorderLayout;
import java.awt.Color;

import aima.core.agent.Action;
import aima.core.agent.Agent;
import aima.core.agent.Environment;
import aima.core.agent.EnvironmentState;
import aima.core.environment.map.MapEnvironment;
import aima.core.environment.map.MoveToAction;
import aima.gui.framework.AgentAppEnvironmentView;
import aimax.osm.data.MapDataStore;
import aimax.osm.data.entities.MapNode;
import aimax.osm.viewer.DefaultMapEntityRenderer;
import aimax.osm.viewer.MapViewPane;

/**
 * Visualizes agent positions and movements in an OSM map. It is
 * assumed that agents are only added but never removed from
 * an environment.
 * @author R. Lunde
 *
 */
public class OsmAgentView extends AgentAppEnvironmentView {

	public final static String TRACK_NAME = "Track";
	
	MapViewPane mapViewPane;
	
	public OsmAgentView(MapDataStore mapData) {
		mapViewPane = new MapViewPane();
		mapViewPane.setModel(mapData);
		this.setLayout(new BorderLayout());
		add(mapViewPane, BorderLayout.CENTER);
		
		DefaultMapEntityRenderer renderer = 
		(DefaultMapEntityRenderer) mapViewPane.getRenderer();
		renderer.setTrackInfo(TRACK_NAME+0, 0, Color.RED, DefaultMapEntityRenderer.LIGHT_GRAY_TRANS);
		renderer.setTrackInfo(TRACK_NAME+1, 0, Color.GREEN, DefaultMapEntityRenderer.LIGHT_GRAY_TRANS);
		renderer.setTrackInfo(TRACK_NAME+2, 0, Color.BLUE, DefaultMapEntityRenderer.LIGHT_GRAY_TRANS);
	}
	
	public MapViewPane getMapViewPane() {
		return mapViewPane;
	}
	
	protected MapEnvironment getMapEnv() {
		return (MapEnvironment) env;
	}
	
	/**
	 * Clears history and starts listening to changes within the given
	 * environment.
	 */
	@Override
	public void setEnvironment(Environment env) {
		mapViewPane.getModel().getTracks().clear();
		super.setEnvironment(env);
	}

	/**
	 * Reacts on environment changes and updates the tracks.
	 */
	@Override
	public void agentAdded(Agent agent, EnvironmentState resultingState) {
		String loc = getMapEnv().getAgentLocation(agent);
		updateTrack(agent, loc);
	}
	
	/**
	 * Reacts on environment changes and updates the tracks.
	 */
	@Override
	public void agentActed(Agent agent, Action command, EnvironmentState state) {
		String msg = "";
		if (env.getAgents().size() > 1)
			msg = "A" + env.getAgents().indexOf(agent) + ": ";
		notify(msg + command.toString());
		if (command instanceof MoveToAction) {
			updateTrack(agent, ((MoveToAction) command).getToLocation());
		}
	}
	

	private void updateTrack(Agent agent, String location) {
		OsmMap map = (OsmMap) getMapEnv().getMap();
		MapNode node = map.getWayNode(location);
		if (node != null) {
			int aIdx = getMapEnv().getAgents().indexOf(agent);
			map.getMapData().addToTrack
			(TRACK_NAME+aIdx, node.getLat(), node.getLon());
		}
	}
	
}
