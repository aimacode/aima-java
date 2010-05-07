package aimax.osm.applications;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JToolBar;

import aimax.osm.data.DataResource;
import aimax.osm.data.MapDataEvent;
import aimax.osm.data.MapDataEventListener;
import aimax.osm.data.MapDataStore;
import aimax.osm.data.Position;
import aimax.osm.reader.OsmBz2Reader;
import aimax.osm.reader.MapReader;
import aimax.osm.routing.RouteCalculator;
import aimax.osm.viewer.MapViewFrame;

/**
 * Implements a simple route planning tool. It extends the OSM map viewer
 * by a little search engine for shortest paths using the A* algorithm.
 * Set two marks (mouse left) before starting route calculation!
 * @author R. Lunde
 */
public class RoutePlannerApp implements ActionListener {
	
	public final static String ROUTE_TRACK_NAME = "Route";
	
	protected MapViewFrame frame;
	protected JComboBox waySelection;
	protected JButton calcButton;
	protected RouteCalculator routeCalculator;
	
	public RoutePlannerApp() {
		MapReader mapReader = new OsmBz2Reader();
		frame = new MapViewFrame(mapReader);
		frame.setTitle("OSM Route Planner");
		routeCalculator = createRouteCalculator();
		JToolBar toolbar = frame.getToolbar();
		toolbar.addSeparator();
		waySelection = new JComboBox(routeCalculator.getWaySelectionOptions());
		toolbar.add(waySelection);
		toolbar.addSeparator();
		calcButton = new JButton("Calculate Route");
		calcButton.setEnabled(frame.getMapData().getMarks().size() >= 2);
		calcButton.addActionListener(this);
		toolbar.add(calcButton);
		
		frame.getMapData().addMapDataEventListener(new MapDataEventHandler());
	}
	
	/**
	 * Factory method for the routing component.
	 * Subclasses can override it and provide more advanced routing algorithms.
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

	/** Starts route generation after the calculate button has been pressed. */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == calcButton) {
			MapDataStore mapData = frame.getMapData();
			List<Position> positions = routeCalculator.calculateRoute
			(mapData.getMarks(), mapData, waySelection.getSelectedIndex());
			frame.getMapData().createTrack(ROUTE_TRACK_NAME, positions);
		}
	}
	
	/**
	 * Updates the info field based on events sent by the MapViewPane. 
	 * @author R. Lunde
	 */
	class MapDataEventHandler implements MapDataEventListener {
		@Override
		public void eventHappened(MapDataEvent event) {
			calcButton.setEnabled(frame.getMapData().getMarks().size() > 1);
		}
	}
	
	
	public static void main(String[] args) {
		// indicates progress when reading large maps (for testing only)
		// Logger.getLogger("aimax.osm").setLevel(Level.FINEST);
		// Logger.getLogger("").getHandlers()[0].setLevel(Level.FINE);
		
		RoutePlannerApp demo = new RoutePlannerApp();
		demo.getFrame().readMap(DataResource.getULMFileResource());
		demo.showFrame();
	}
}
