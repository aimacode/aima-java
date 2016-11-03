package aimax.osm.gui.swing.applications;

import java.util.Locale;

import aimax.osm.data.DataResource;
import aimax.osm.gui.swing.viewer.MapViewFrame;

/**
 * Opens a <code>MapViewFrame</code> and initializes it with a
 * map of Ulm, provided that a file with name ulm.osm is found
 * in the resource/maps path.
 * @author Ruediger Lunde
 */
public class OsmViewerApp {
	/**
	 * Start application with program arg <code>-screenwidth=xx</code>
	 * (with xx the width in cm)
	 * or <code>-screensize=yy</code> (with yy measured diagonally in inch).
	 */
	public static void main(String[] args) {
		// indicates progress when reading large maps (for testing only)
		// Logger.getLogger("aimax.osm").setLevel(Level.FINEST);
		// Logger.getLogger("").getHandlers()[0].setLevel(Level.FINE);
		
		Locale.setDefault(Locale.US);
		MapViewFrame frame = new MapViewFrame(args);
		frame.readMap(DataResource.getULMFileResource());
		frame.setTitle("OSM Viewer");
		frame.setSize(800, 600);
		frame.setVisible(true);
	}
}
