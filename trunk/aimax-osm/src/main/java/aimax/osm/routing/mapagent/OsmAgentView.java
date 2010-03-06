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
import aimax.osm.data.EntityClassifier;
import aimax.osm.data.MapDataStore;
import aimax.osm.data.Position;
import aimax.osm.data.entities.EntityViewInfo;
import aimax.osm.data.entities.MapNode;
import aimax.osm.viewer.MapStyleFactory;
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
	
	/**
	 * Creates an agent view which displays agent positions within a map using
	 * the given map data. This implementation assumes that for the same
	 * <code>mapData</code> instance only one agent view is created.
	 */ 
	public OsmAgentView(MapDataStore mapData) {
		EntityClassifier<EntityViewInfo> eClassifier = MapStyleFactory.createDefaultClassifier();
		eClassifier.addRule("track_type", TRACK_NAME+0, MapStyleFactory.createTrackInfo(Color.RED));
		eClassifier.addRule("track_type", TRACK_NAME+1, MapStyleFactory.createTrackInfo(Color.GREEN));
		eClassifier.addRule("track_type", TRACK_NAME+2, MapStyleFactory.createTrackInfo(Color.BLUE));
		mapData.setEntityClassifier(eClassifier);
		
		mapViewPane = new MapViewPane();
		mapViewPane.setModel(mapData);
		setLayout(new BorderLayout());
		add(mapViewPane, BorderLayout.CENTER);
	}
	
	public MapViewPane getMapViewPane() {
		return mapViewPane;
	}
	
	protected MapEnvironment getMapEnv() {
		return (MapEnvironment) env;
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
		OsmMapAdapter map = (OsmMapAdapter) getMapEnv().getMap();
		MapNode node = map.getWayNode(location);
		if (node != null) {
			int aIdx = getMapEnv().getAgents().indexOf(agent);
			map.getMapData().addToTrack
			(TRACK_NAME+aIdx, new Position(node.getLat(), node.getLon()));
		}
	}
}
