package aimax.osm.applications;

import java.io.File;

import aimax.osm.reader.MapReader;
import aimax.osm.viewer.MapViewFrame;

/**
 * Opens a <code>MapViewFrame</code> and initializes it with a
 * map of Ulm, provided that a file with name data/ulm.osm is found.
 * @author R. Lunde
 */
public class OsmViewerApp {
	public static void main(String[] args) {
		// indicates progress when reading large maps (for testing only)
		// Logger.getLogger("aimax.osm").setLevel(Level.FINEST);
		// Logger.getLogger("").getHandlers()[0].setLevel(Level.FINE);
		
		File file = new File("src/main/resource/maps/Ulm.osm");
		MapReader mapReader = new aimax.osm.reader.OsmReader();
		MapViewFrame frame = new MapViewFrame(mapReader, file);
		frame.setTitle("OSM Viewer");
		frame.setSize(800, 600);
		frame.setVisible(true);
	}
}
