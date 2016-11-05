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

	/** Registers agent applications and console program demos. */
	public static void registerDemos(AimaDemoFrame frame) {
		frame.addApp(RoutePlannerApp.class);
		frame.addApp(OsmAgentApp.class);
		frame.addApp(SearchDemoOsmAgentApp.class);
	}
	
	/** Starts the demo. */
	public static void main(String[] args) {
		Locale.setDefault(Locale.US);
		AimaDemoFrame frame = new AimaDemoFrame();
		frame.setTitle("AIMA3e Java Demos with OSM");
		IntegratedAimaApp.registerDemos(frame);
		registerDemos(frame);
		frame.setSize(1000, 700);
		frame.setVisible(true);
	}
}
