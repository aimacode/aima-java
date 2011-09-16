package aimax.osm.applications;

import java.util.Locale;

import aima.gui.applications.AimaDemoApp;
import aima.gui.applications.AimaDemoFrame;

/**
 * The all-in-one demo application including all apps and
 * demos from the aima-gui project and some of the apps from
 * the aimax-osm project.
 * 
 * @author Ruediger Lunde
 */
public class OsmAimaDemoApp {

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
		AimaDemoApp.registerDemos(frame);
		registerDemos(frame);
		frame.setSize(800, 600);
		frame.setVisible(true);
	}
}
