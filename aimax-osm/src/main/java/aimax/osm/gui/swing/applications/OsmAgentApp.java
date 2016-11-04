package aimax.osm.gui.swing.applications;

import java.io.File;
import java.io.InputStream;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import aima.gui.swing.framework.AgentAppController;
import aima.gui.swing.framework.AgentAppFrame;
import aima.gui.swing.framework.SimpleAgentApp;
import aimax.osm.data.DataResource;
import aimax.osm.data.MapBuilder;
import aimax.osm.data.impl.DefaultMapBuilder;
import aimax.osm.reader.Bz2OsmReader;
import aimax.osm.reader.MapReader;
import aimax.osm.gui.swing.viewer.agent.OsmAgentController;
import aimax.osm.gui.swing.viewer.agent.OsmAgentFrame;
import aimax.osm.gui.swing.viewer.agent.OsmAgentView;
import aimax.osm.routing.MapAdapter;

/**
 * Demonstrates, how the OSM map viewer can be integrated into a graphical
 * agent application.
 * @author Ruediger Lunde
 */
public class OsmAgentApp extends SimpleAgentApp {

	protected static Logger LOG = Logger.getLogger("aimax.osm");
	
	protected MapAdapter map;
	
	/** Sets the default language (for file choosers etc.) to US. */
	public OsmAgentApp() {
		Locale.setDefault(Locale.US);
	}
	
	/** Reads a map from the specified stream and stores it in {@link #map}. */
	public void readMap(InputStream stream) {
		if (stream != null) {
			MapReader mapReader = new Bz2OsmReader();
			MapBuilder mapBuilder = new DefaultMapBuilder();
			mapReader.readMap(stream, mapBuilder);
			map = new MapAdapter(mapBuilder.buildMap());
		}
		else
			LOG.warning("Map reading failed because input stream does not exist.");
	}
	
	/** Reads a map from the specified file and stores it in {@link #map}. */
	public void readMap(File file) {
		MapReader mapReader = new Bz2OsmReader();
		MapBuilder mapBuilder = new DefaultMapBuilder();
		mapReader.readMap(file, mapBuilder);
		map = new MapAdapter(mapBuilder.buildMap());
	}
	
	/**
	 * Reads the default map showing the city of Ulm if no map
	 * has been loaded yet and calls the super class implementation.
	 */
	@Override
	public AgentAppFrame constructApplicationFrame() {
		if (map == null)
			readMap(DataResource.getULMFileResource());
		return super.constructApplicationFrame();
	}
	
	/** Factory method, which creates an <code>OsmAgentView</code>. */
	@Override
	public OsmAgentView createEnvironmentView() {
		return new OsmAgentView(map.getOsmMap());
	}

	/** Factory method, which creates an <code>OsmAgentFrame</code>. */
	@Override
	public AgentAppFrame createFrame() {
		return new OsmAgentFrame();
	}

	/** Factory method, which creates an <code>OsmAgentController</code>. */
	@Override
	public AgentAppController createController() {
		return new OsmAgentController(map);
	}

	
	/////////////////////////////////////////////////////////////////
	// starter method

	/**
	 * Application starter.
	 */
	public static void main(String args[]) {
		// Start with program arg -screenwidth=xx (with xx the width in cm).
		Logger.getLogger("aimax.osm").setLevel(Level.FINEST);
		Logger.getLogger("").getHandlers()[0].setLevel(Level.FINE);
		
		OsmAgentApp demo = new OsmAgentApp();
		// demo.readMap(new File("maps/Ulm.osm"));
		demo.startApplication();
	}
}
