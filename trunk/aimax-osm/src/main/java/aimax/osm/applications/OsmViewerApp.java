package aimax.osm.applications;

import aimax.osm.data.DataResource;
import aimax.osm.reader.MapReader;
import aimax.osm.viewer.MapViewFrame;

/**
 * Opens a <code>MapViewFrame</code> and initializes it with a
 * map of Ulm, provided that a file with name ulm.osm is found
 * in the resource/maps path.
 * @author R. Lunde
 */
public class OsmViewerApp {
	public static void main(String[] args) {
		// indicates progress when reading large maps (for testing only)
		// Logger.getLogger("aimax.osm").setLevel(Level.FINEST);
		// Logger.getLogger("").getHandlers()[0].setLevel(Level.FINE);
		
		MapReader mapReader = new aimax.osm.reader.OsmReader();
		MapViewFrame frame = new MapViewFrame(mapReader, DataResource.getULMFileResource());
		frame.setTitle("OSM Viewer");
		frame.setSize(800, 600);
		frame.setVisible(true);
	}
}
