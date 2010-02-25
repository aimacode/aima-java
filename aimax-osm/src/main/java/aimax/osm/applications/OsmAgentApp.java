package aimax.osm.applications;

import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import aima.gui.framework.AgentAppController;
import aima.gui.framework.AgentAppFrame;
import aima.gui.framework.SimpleAgentApp;
import aimax.osm.data.DataResource;
import aimax.osm.data.MapDataStore;
import aimax.osm.reader.MapReader;
import aimax.osm.reader.OsmReader;
import aimax.osm.routing.mapagent.OsmAgentController;
import aimax.osm.routing.mapagent.OsmAgentFrame;
import aimax.osm.routing.mapagent.OsmAgentView;
import aimax.osm.routing.mapagent.OsmMap;

/**
 * Demonstrates, how the OSM map viewer can be integrated into a graphical
 * agent application.
 * @author R. Lunde
 */
public class OsmAgentApp extends SimpleAgentApp {

	protected OsmMap map;
	
	/** Reads a map from the specified file and stores it in {@link #map}. */
	public OsmAgentApp(InputStream osmFile) {
		MapDataStore mapData = new MapDataStore();
		MapReader mapReader = new OsmReader();
		mapReader.readMap(osmFile, mapData);
		map = new OsmMap(mapData);
	}
	
	/** Creates an <code>OsmAgentView</code>. */
	public OsmAgentView createEnvironmentView() {
		return new OsmAgentView(map.getMapData());
	}

	/** Factory method, which creates and configures a <code>OsmMapAgentFrame</code>. */
	@Override
	public AgentAppFrame createFrame() {
		return new OsmAgentFrame(map);
	}

	/** Factory method, which creates a <code>OsmMapAgentController</code>. */
	@Override
	public AgentAppController createController() {
		return new OsmAgentController(map);
	}

	
	/////////////////////////////////////////////////////////////////
	// starter method

	/** Application starter. */
	public static void main(String args[]) {
		Logger.getLogger("aimax.osm").setLevel(Level.FINEST);
		Logger.getLogger("").getHandlers()[0].setLevel(Level.FINE);
		
		OsmAgentApp demo = new OsmAgentApp(DataResource.getULMFileResource());
		demo.startApplication();
	}
}
