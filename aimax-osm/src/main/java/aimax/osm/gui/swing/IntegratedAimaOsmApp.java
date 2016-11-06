package aimax.osm.gui.swing;

import java.util.Locale;

import aima.gui.swing.applications.IntegratedAimaApp;
import aima.gui.swing.applications.AimaDemoFrame;
import aimax.osm.gui.swing.applications.OsmAgentApp;
import aimax.osm.gui.swing.applications.RoutePlannerApp;
import aimax.osm.gui.swing.applications.SearchDemoOsmAgentApp;

/**
 * The all-in-one demo application including all apps and
 * demos from the aima-gui project and some of the apps from
 * the aimax-osm project.
 * 
 * @author Ruediger Lunde
 */
public class IntegratedAimaOsmApp {

	/** Registers agent applications and command line demos. */
	public static void defineContent(AimaDemoFrame frame) {
		frame.addApp(RoutePlannerApp.class);
		frame.addApp(OsmAgentApp.class);
		frame.addApp(SearchDemoOsmAgentApp.class);
	}
	
	/** Starts the application. */
	public static void main(String[] args) {
		Locale.setDefault(Locale.US);
		AimaDemoFrame frame = new AimaDemoFrame();
		frame.setTitle("Integrated AIMA3e OSM App");
		IntegratedAimaApp.defineContent(frame);
		defineContent(frame);
		frame.setSize(1000, 700);
		frame.setVisible(true);
	}
}
