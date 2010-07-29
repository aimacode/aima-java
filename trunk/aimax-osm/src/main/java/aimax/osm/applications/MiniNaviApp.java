package aimax.osm.applications;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;

import aima.core.util.CancelableThread;
import aimax.osm.data.DataResource;
import aimax.osm.data.MapDataStore;
import aimax.osm.data.Position;
import aimax.osm.data.entities.MapNode;
import aimax.osm.data.entities.Track;
import aimax.osm.gps.GpsFix;
import aimax.osm.gps.GpsLocator;
import aimax.osm.gps.GpsPositionListener;
import aimax.osm.gps.NmeaReader.MessageToFileListener;
import aimax.osm.routing.RouteCalculator;
import aimax.osm.viewer.MapViewFrame;

/**
 * Little demo application which shows a map and location information
 * from a GPS connection, and additionally provides routing functionality.
 * <p>To run this class, download the rs232 serial port library from
 * http://www.rxtx.org, install it correctly, and rename file
 * <code>NmeaSerialPortReader.txt</code> in package <code>aimax.osm.gps</code>
 * to <code>NmeaSerialPortReader.java</code>.
 * One possible choice for installation is, to add the jar-file
 * to the class path (project properties, add jar), to store the DDL
 * locally (e.g. in project-root/lib), and start the application with
 * VM argument <code>-Djava.library.path=lib -Xmx500M</code>.</p>
 * @author Ruediger Lunde
 */
public class MiniNaviApp implements ActionListener {
	
	public final static String ROUTE_TRACK_NAME = "Route";
	public final static String GPS_TRACK_NAME = "GPS";
	
	protected MapViewFrame frame;
	protected GpsLocator locator;
	protected MessageToFileListener nmeaLogger;
	protected RouteCalculator routeCalculator;
	protected RoutingThread routingThread;
	
	protected JComboBox gpsCombo;
	protected JFileChooser fileChooser;
	protected JComboBox waySelection;
	protected JButton calcButton;
	
	public MiniNaviApp(String[] args) {
		frame = new MapViewFrame(args);
		locator = new GpsLocator();
		locator.addGpsPositionListener(new MyGpsPositionListener());
		routeCalculator = createRouteCalculator();
		
		frame.setTitle("MiniNavi");
		JToolBar toolbar = frame.getToolbar();
		
		gpsCombo = new JComboBox(new String[]{
				"GPS Off", "GPS On", "GPS Center", "GPS Cen+Log", "Read Log"}); 
		gpsCombo.addActionListener(this);
		toolbar.addSeparator();
		toolbar.add(gpsCombo);
		
		waySelection = new JComboBox(routeCalculator.getWaySelectionOptions());
		waySelection.setSelectedIndex(1);
		toolbar.add(waySelection);
		toolbar.addSeparator();
		calcButton = new JButton("Calculate Route");
		toolbar.add(calcButton);
		calcButton.addActionListener(this);
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
	
	public MapDataStore getMapData() {
		return frame.getMapData();
	}
	
	public void showFrame() {
		frame.setSize(800, 600);
		frame.setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == gpsCombo) {
			getMapData().clearTrack(GPS_TRACK_NAME);
			try {
				if (fileChooser == null)
					fileChooser = new JFileChooser();
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
						if (gpsCombo.getSelectedIndex() == 3 &&
								fileChooser.showSaveDialog(frame)
								== JFileChooser.APPROVE_OPTION) {
							nmeaLogger = new MessageToFileListener();
							locator.getNmeaReader().addListener(nmeaLogger);
							nmeaLogger.openFile(fileChooser.getSelectedFile());
						}
					} else { // Establishing connection to GPS failed
						gpsCombo.setSelectedIndex(0);
					}
				} else if (fileChooser.showOpenDialog(frame)
						== JFileChooser.APPROVE_OPTION) { // simulate GPS
					locator.openFileConnection(fileChooser.getSelectedFile());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (event.getSource() == calcButton) {
			if (routingThread != null) {
				routingThread.cancel();
			} else {
				MapDataStore mapData = frame.getMapData();
				List<MapNode> marks = mapData.getMarks();
				if (!marks.isEmpty()) {
					List<MapNode> routeMarks = new ArrayList<MapNode>();
					Track gpsTrack = mapData.getTrack(GPS_TRACK_NAME);
					if (gpsTrack != null) {
						routeMarks.add(gpsTrack.getLastTrkPt());
						routeMarks.add(marks.get(0));
					} else {
						routeMarks.addAll(marks);
					}
					routingThread = new RoutingThread(routeMarks);
					updateEnableState();
					routingThread.start();
				}
			}
		}
	}
	
	protected void updateEnableState() {
		frame.getLoadButton().setEnabled(routingThread == null);
		frame.getSaveButton().setEnabled(routingThread == null);
		calcButton.setText
		(routingThread == null ? "Calculate Route" : "Cancel Calculation");
	}
	
	/////////////////////////////////////////////////////////////////
	// inner classes

	class RoutingThread extends CancelableThread {
		List<MapNode> routeMarks;
		List<Position> positions;

		public RoutingThread(List<MapNode> routeMarks) {
			this.routeMarks = routeMarks;
		}
		
		@Override
		public void interrupt() {
			cancel();
			super.interrupt();
		}
		
		@Override
		public void run() {
			try {
				positions = routeCalculator.calculateRoute
				(routeMarks, frame.getMapData(), waySelection.getSelectedIndex());
			} catch (Exception e) {
				e.printStackTrace(); // for debugging
			}
			try {
				SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						frame.getMapData().createTrack(ROUTE_TRACK_NAME, positions);
						routingThread = null;
						updateEnableState();
					}
				});
			} catch(Exception e) {
				e.printStackTrace(); // for debugging
			}
		}
	}
		
	
	class MyGpsPositionListener implements GpsPositionListener {
		@Override
		public void positionUpdated(GpsFix pos) {
			if (pos.isPosOk()) {
				MapDataStore mapData = frame.getMapData();
				Track track = mapData.getTrack(GPS_TRACK_NAME);
				MapNode node = null;
				if (track != null)
					node = track.getLastTrkPt();
				if (node == null || pos.getDistKM(node) > 0.01) {
					mapData.addToTrack(GPS_TRACK_NAME, pos);
					if (gpsCombo.getSelectedIndex() == 2
							|| gpsCombo.getSelectedIndex() == 3)
						frame.getView().adjustToCenter
						(pos.getLat(), pos.getLon());
				}
			}
		}
	}
	
	
	/////////////////////////////////////////////////////////////////
	// application starter
	
	/**
	 * Start application with VM arg <code>-Djava.library.path=lib</code>
	 * and program arg <code>-screenwidth=xx</code> (with xx the width in cm)
	 * or <code>-screensize=yy</code> (with yy measured diagonally in inch).
	 */
	public static void main(String[] args) {
		// indicates progress when reading large maps (for testing only)
		// Logger.getLogger("aimax.osm").setLevel(Level.FINEST);
		// Logger.getLogger("").getHandlers()[0].setLevel(Level.FINE);
		
		Locale.setDefault(Locale.US);
		MiniNaviApp demo = new MiniNaviApp(args);
		demo.getFrame().readMap(DataResource.getULMFileResource());
		demo.showFrame();
	}
}
