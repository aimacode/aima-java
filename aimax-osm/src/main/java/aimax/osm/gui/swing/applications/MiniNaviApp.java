package aimax.osm.gui.swing.applications;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import aima.core.util.CancellableThread;
import aimax.osm.data.DataResource;
import aimax.osm.data.EntityClassifier;
import aimax.osm.data.OsmMap;
import aimax.osm.data.Position;
import aimax.osm.data.entities.EntityViewInfo;
import aimax.osm.data.entities.MapNode;
import aimax.osm.data.entities.Track;
import aimax.osm.data.impl.DefaultMap;
import aimax.osm.gps.GpsFix;
import aimax.osm.gps.GpsLocator;
import aimax.osm.gps.GpsPositionListener;
import aimax.osm.gps.NmeaReader.MessageToFileListener;
import aimax.osm.routing.RouteCalculator;
import aimax.osm.viewer.AbstractEntityRenderer;
import aimax.osm.viewer.MapStyleFactory;
import aimax.osm.gui.swing.viewer.MapViewFrame;

/**
 * Configurable application which shows a map and location information from a
 * GPS connection, and additionally provides routing functionality.
 * <p>
 * This implementation provides a simple reflection-based mechanism which allows
 * to replace the standard implementations of all fundamental components by
 * other implementations. For example, to replace the route calculator just
 * create a class <code>x.y.ZRouteCalculator</code> as subclass of
 * <code>RouteCalculator</code> and add in the main method the line
 * <code>System.setProperty(MiniNaviApp.MAP_CLASS_PROPERTY, "x.y.ZRouteCalculator</code>
 * . Analogously, renderer, entity classifier, and even the map representation
 * itself can be replaced. Note, that system properties can also be set by VM
 * argument (-Dpropertyname=value).
 * </p>
 * <p>
 * To enable the GPS interface, download the rs232 serial port library from
 * http://www.rxtx.org, install it correctly, and rename file
 * <code>NmeaSerialPortReader.txt</code> in package <code>aimax.osm.gps</code>
 * to <code>NmeaSerialPortReader.java</code>. One possible choice for
 * installation is, to add the jar-file to the class path (project properties,
 * add jar), to store the DDL locally (e.g. in project-root/lib), and start the
 * application with VM argument <code>-Djava.library.path=lib -Xmx1500M</code>.
 * </p>
 * 
 * @author Ruediger Lunde
 */
public class MiniNaviApp implements ActionListener {

	protected static Logger LOG = Logger.getLogger("aimax.osm");

	public final static String MAP_CLASS_PROPERTY = "aimax.osm.navi.mapclass";
	public final static String CLASSIFIER_CLASS_PROPERTY = "aimax.osm.navi.classifierclass";
	public final static String RENDERER_CLASS_PROPERTY = "aimax.osm.navi.rendererclass";
	public final static String ROUTECALCULATOR_CLASS_PROPERTY = "aimax.osm.navi.routecalculatorclass";

	public final static String ROUTE_TRACK_NAME = "Route";
	public final static String GPS_TRACK_NAME = "GPS";

	protected MapViewFrame frame;
	protected GpsLocator locator;
	protected MessageToFileListener nmeaLogger;
	protected RouteCalculator routeCalculator;
	protected RoutingThread routingThread;

	protected JComboBox<String> gpsCombo;
	protected JFileChooser gpsFileChooser;
	protected JComboBox<String> waySelection;
	protected JButton calcButton;

	public MiniNaviApp(String[] args) {
		initFrame(args);
		locator = new GpsLocator();
		locator.addGpsPositionListener(new MyGpsPositionListener());
		routeCalculator = (RouteCalculator) createComponent(
				ROUTECALCULATOR_CLASS_PROPERTY, RouteCalculator.class);
		if (routeCalculator == null)
			routeCalculator = new RouteCalculator();

		JToolBar toolbar = frame.getToolbar();
		gpsCombo = new JComboBox<String>(new String[] { "GPS Off", "GPS On",
				"GPS Center", "GPS Cen+Log", "Read Log" });
		gpsCombo.addActionListener(this);
		toolbar.addSeparator();
		toolbar.add(gpsCombo);

		waySelection = new JComboBox<String>(
				routeCalculator.getTaskSelectionOptions());
		toolbar.add(waySelection);
		toolbar.addSeparator();
		calcButton = new JButton("Calculate Route");
		toolbar.add(calcButton);
		calcButton.addActionListener(this);
	}

	protected void initFrame(String[] args) {
		frame = new ConfigurableMapViewFrame(args);
		frame.setTitle("MiniNavi");
	}

	public MapViewFrame getFrame() {
		return frame;
	}

	public OsmMap getMap() {
		return frame.getMap();
	}

	public void showFrame() {
		frame.setSize(800, 600);
		frame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == gpsCombo) {
			getMap().clearTrack(GPS_TRACK_NAME);
			try {
				if (gpsFileChooser == null)
					gpsFileChooser = new JFileChooser();
				if (nmeaLogger != null) {
					nmeaLogger.closeFile();
					locator.getNmeaReader().removeListener(nmeaLogger);
					nmeaLogger = null;
				}
				if (gpsCombo.getSelectedIndex() < 1) { // GPS Off
					locator.closeConnection();
				} else if (gpsCombo.getSelectedIndex() < 4) { // GPS On
					locator.openSerialPortConnection();
					if (locator.isConnected()) {
						if (gpsCombo.getSelectedIndex() == 3
								&& gpsFileChooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
							nmeaLogger = new MessageToFileListener();
							locator.getNmeaReader().addListener(nmeaLogger);
							nmeaLogger.openFile(gpsFileChooser
									.getSelectedFile());
						}
					} else { // Establishing connection to GPS failed
						gpsCombo.setSelectedIndex(0);
					}
				} else if (gpsFileChooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) { // startExperiment
					// GPS
					locator.openFileConnection(gpsFileChooser.getSelectedFile());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (event.getSource() == calcButton) {
			if (routingThread != null) {
				routingThread.cancel();
			} else {
				List<MapNode> markers = getMap().getMarkers();
				if (!markers.isEmpty()) {
					List<MapNode> routeMarkers = new ArrayList<MapNode>();
					Track gpsTrack = getMap().getTrack(GPS_TRACK_NAME);
					if (gpsTrack != null) {
						routeMarkers.add(gpsTrack.getLastNode());
						routeMarkers.add(markers.get(0));
					} else {
						routeMarkers.addAll(markers);
					}
					routingThread = new RoutingThread(routeMarkers);
					updateEnabledState();
					routingThread.start();
				}
			}
		}
	}

	protected void updateEnabledState() {
		frame.getLoadButton().setEnabled(routingThread == null);
		frame.getSaveButton().setEnabled(routingThread == null);
		calcButton.setText(routingThread == null ? "Calculate Route"
				: "Cancel Calculation");
	}

	protected static Object createComponent(String property, Class<?> oclass) {
		Object result = null;
		String className = System.getProperty(property);
		if (className != null) {
			try {
				Class<?> c = Class.forName(className);
				result = c.newInstance();
				if (!oclass.isInstance(result)) {
					LOG.warning("Component instantiation failed. Class "
							+ className + " is not of type "
							+ oclass.getCanonicalName() + ".");
				}
			} catch (Exception e) {
				LOG.warning("Component instantiation failed. Class "
						+ className + " could not be loaded correctly.");
			}
		}
		return result;
	}

	// ///////////////////////////////////////////////////////////////
	// inner classes

	class RoutingThread extends CancellableThread {
		List<MapNode> routeMarkers;
		List<Position> positions;

		public RoutingThread(List<MapNode> routeMarkers) {
			this.routeMarkers = routeMarkers;
		}

		@Override
		public void interrupt() {
			cancel();
			super.interrupt();
		}

		@Override
		public void run() {
			try {
				positions = routeCalculator.calculateRoute(routeMarkers,
						frame.getMap(), waySelection.getSelectedIndex());
			} catch (Exception e) {
				e.printStackTrace(); // for debugging
			}
			try {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						frame.getMap().createTrack(ROUTE_TRACK_NAME, positions);
						routingThread = null;
						updateEnabledState();
					}
				});
			} catch (Exception e) {
				e.printStackTrace(); // for debugging
			}
		}
	}

	class MyGpsPositionListener implements GpsPositionListener {
		@Override
		public void positionUpdated(GpsFix pos) {
			if (pos.isPosOk()) {
				OsmMap mapData = frame.getMap();
				Track track = mapData.getTrack(GPS_TRACK_NAME);
				MapNode node = null;
				if (track != null)
					node = track.getLastNode();
				if (node == null || pos.getDistKM(node) > 0.01) {
					mapData.addToTrack(GPS_TRACK_NAME, pos);
					if (gpsCombo.getSelectedIndex() == 2
							|| gpsCombo.getSelectedIndex() == 3)
						frame.getView().adjustToCenter(pos.getLat(),
								pos.getLon());
				}
			}
		}
	}

	static class ConfigurableMapViewFrame extends MapViewFrame {
		private static final long serialVersionUID = 1L;

		ConfigurableMapViewFrame(String[] args) {
			super(args);
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void initMapAndClassifier() {
			OsmMap map = (OsmMap) createComponent(MAP_CLASS_PROPERTY,
					OsmMap.class);
			if (map == null)
				map = new DefaultMap();
			view.setMap(map);
			viewInfo = (EntityClassifier<EntityViewInfo>) createComponent(
					CLASSIFIER_CLASS_PROPERTY, EntityClassifier.class);
			if (viewInfo == null)
				viewInfo = new MapStyleFactory().createDefaultClassifier();
			AbstractEntityRenderer renderer = (AbstractEntityRenderer) createComponent(
					RENDERER_CLASS_PROPERTY, AbstractEntityRenderer.class);
			if (renderer != null)
				view.setRenderer(renderer);
		}
	}

	// ///////////////////////////////////////////////////////////////
	// application starter

	/**
	 * Start application with VM arg <code>-Djava.library.path=lib</code> and
	 * program arg <code>-screenwidth=xx</code> (with xx the width in cm) or
	 * <code>-screensize=yy</code> (with yy measured diagonally in inch).
	 */
	public static void main(String[] args) {
		// indicates progress when reading large maps (for testing only)
		Logger.getLogger("aimax.osm").setLevel(Level.FINEST);
		Logger.getLogger("").getHandlers()[0].setLevel(Level.FINE);

		Locale.setDefault(Locale.US);
		// System.setProperty(MiniNaviApp.MAP_CLASS_PROPERTY,
		// "rl.osm.data.perst.PerstMap");
		MiniNaviApp demo = new MiniNaviApp(args);
		demo.getFrame().readMap(DataResource.getUlmFileResource());
		// demo.getFrame().readMap(new File("maps/ulm.osm"));
		demo.showFrame();
	}
}
