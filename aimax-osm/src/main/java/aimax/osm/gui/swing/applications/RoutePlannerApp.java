package aimax.osm.gui.swing.applications;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JToolBar;

import aimax.osm.data.DataResource;
import aimax.osm.data.MapEvent;
import aimax.osm.data.MapEventListener;
import aimax.osm.data.OsmMap;
import aimax.osm.data.Position;
import aimax.osm.routing.RouteCalculator;
import aimax.osm.gui.swing.viewer.MapViewFrame;

/**
 * Implements a simple route planning tool. It extends the OSM map viewer by a
 * little search engine for shortest paths using the A* algorithm. Set two
 * markers (mouse left) before starting route calculation!
 * 
 * @author Ruediger Lunde
 */
public class RoutePlannerApp implements ActionListener {

	public final static String ROUTE_TRACK_NAME = "Route";

	protected MapViewFrame frame;
	protected JComboBox<String> taskSelection;
	protected JButton calcButton;
	protected RouteCalculator routeCalculator;

	public RoutePlannerApp() {
		this(new String[0]);
	}

	public RoutePlannerApp(String[] args) {
		frame = new MapViewFrame(args);
		frame.setTitle("OSM Route Planner");
		routeCalculator = createRouteCalculator();
		JToolBar toolbar = frame.getToolbar();
		toolbar.addSeparator();
		taskSelection = new JComboBox<String>(
				routeCalculator.getTaskSelectionOptions());
		toolbar.add(taskSelection);
		toolbar.addSeparator();
		calcButton = new JButton("Calculate Route");
		calcButton.addActionListener(this);
		toolbar.add(calcButton);

		frame.getMap().addMapDataEventListener(new MapDataEventHandler());
		updateEnabledState();
	}

	/**
	 * Factory method for the routing component. Subclasses can override it and
	 * provide more advanced routing algorithms.
	 */
	protected RouteCalculator createRouteCalculator() {
		return new RouteCalculator();
	}

	public MapViewFrame getFrame() {
		return frame;
	}

	public void showFrame() {
		frame.setSize(800, 600);
		frame.setVisible(true);
	}

	/**
	 * Loads the default map if necessary and returns the frame (useful for
	 * integration into an AIMA demo application).
	 */
	public MapViewFrame constructApplicationFrame() {
		if (frame.getMap().isEmpty())
			frame.readMap(DataResource.getUlmFileResource());
		return frame;
	}

	/** Starts route generation after the calculate button has been pressed. */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == calcButton) {
			OsmMap map = frame.getMap();
			List<Position> positions = routeCalculator.calculateRoute(
					map.getMarkers(), map, taskSelection.getSelectedIndex());
			frame.getMap().createTrack(ROUTE_TRACK_NAME, positions);
		}
	}

	/**
	 * Enables the Calculate button if at least two markers are set.
	 */
	protected void updateEnabledState() {
		calcButton.setEnabled(frame.getMap().getMarkers().size() > 1);
	}
	
	/**
	 * Updates the enabled state of the GUI.
	 * 
	 * @author Ruediger Lunde
	 */
	class MapDataEventHandler implements MapEventListener {
		@Override
		public void eventHappened(MapEvent event) {
			updateEnabledState();
		}
	}

	/**
	 * Start application with program arg <code>-screenwidth=xx</code> (with xx
	 * the width in cm) or <code>-screensize=yy</code> (with yy measured
	 * diagonally in inch).
	 */
	public static void main(String[] args) {
		// indicates progress when reading large maps (for testing only)
		// Logger.getLogger("aimax.osm").setLevel(Level.FINEST);
		// Logger.getLogger("").getHandlers()[0].setLevel(Level.FINE);

		Locale.setDefault(Locale.US);
		RoutePlannerApp demo = new RoutePlannerApp(args);
		demo.getFrame().readMap(DataResource.getUlmFileResource());
		demo.showFrame();
	}
}
