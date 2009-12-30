package aimax.osm.applications;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JToolBar;

import aimax.osm.data.MapDataStore;
import aimax.osm.data.entities.MapNode;
import aimax.osm.data.entities.Track;
import aimax.osm.gps.GpsFix;
import aimax.osm.gps.GpsLocator;
import aimax.osm.gps.GpsPositionListener;
import aimax.osm.gps.NmeaReader.MessageToFileListener;
import aimax.osm.reader.MapReader;
import aimax.osm.reader.OsmReader;
import aimax.osm.routing.RouteCalculator;
import aimax.osm.viewer.DefaultMapEntityRenderer;
import aimax.osm.viewer.MapViewFrame;

/**
 * Little demo application which shows a map and location information
 * from a GPS connection, and additionally provides routing functionality.
 * <p>To run this class, download the rs232 serial port library from
 * http://www.rxtx.org, install it correctly, and rename file
 * <code>NmeaSerialPortReader.txt</code> in package <code>aimax.osm.gps</code>
 * to <code>NmeaSerialPortReader.class</code>.
 * One possible choice for installation is, to add the jar-file
 * to the class path (project properties, add jar), to store the DDL
 * locally (e.g. in project-root/lib), and start the application with
 * VM argument <code>-Djava.library.path=lib -Xmx500M</code>.</p>
 * @author R. Lunde
 */
public class MiniNaviApp implements ActionListener {
	
	public final static String GPS_TRACK_NAME = "GPS";
	
	protected MapViewFrame frame;
	private GpsLocator locator;
	private MessageToFileListener nmeaLogger;
	protected RouteCalculator routeCalculator;
	
	private JComboBox gpsCombo;
	private JFileChooser fileChooser;
	protected JComboBox waySelection;
	protected JButton calcButton;
	
	public MiniNaviApp(File file) {
		MapReader mapReader = new OsmReader();
		frame = new MapViewFrame(mapReader, file);
		locator = new GpsLocator();
		locator.addGpsPositionListener(new MyGpsPositionListener());
		routeCalculator = new RouteCalculator();
		
		frame.setTitle("MiniNavi");
		((DefaultMapEntityRenderer) frame.getView().getRenderer()).
		setTrackInfo("GPS", 0, Color.GREEN,
				DefaultMapEntityRenderer.GRAY_TRANS);
		JToolBar toolbar = frame.getToolbar();
		
		gpsCombo = new JComboBox
		(new String[]{"GPS Off", "GPS On", "GPS Center", "GPS Cen+Log", "Read Log"});
		gpsCombo.addActionListener(this);
		toolbar.addSeparator();
		toolbar.add(gpsCombo);
		
		waySelection = new JComboBox(new String[]{
				"Distance", "Distance (Car)", "Distance (Bike)"});
		waySelection.setSelectedIndex(1);
		toolbar.add(waySelection);
		toolbar.addSeparator();
		calcButton = new JButton("Calculate Route");
		//calcButton.setEnabled(frame.getMapData().getMarks().size() >= 2);
		toolbar.add(calcButton);
		calcButton.addActionListener(this);
	}
	
	public void showFrame() {
		frame.setSize(800, 600);
		frame.setVisible(true);
	}
	
	public MapDataStore getMapData() {
		return frame.getMapData();
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
			MapDataStore mapData = frame.getMapData();
			List<MapNode> marks = mapData.getMarks();
			if (!marks.isEmpty()) {
				List<MapNode> routeMarks = new ArrayList<MapNode>();
				Track gpsTrack = mapData.getTrack(GPS_TRACK_NAME);
				if (gpsTrack != null) {
					routeMarks.add(gpsTrack.getLastTrkPt());
					routeMarks.add(marks.get(marks.size()-1));
				} else {
					routeMarks.addAll(marks);
				}
				routeCalculator.calculateRoute(routeMarks, mapData,
						waySelection.getSelectedIndex());
			}
		}
	}
	
	
	/////////////////////////////////////////////////////////////////
	// inner classes

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
					mapData.addToTrack
					("GPS", pos.getLat(), pos.getLon());
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
	
	public static void main(String[] args) {
		// start with -Djava.library.path=lib
		
		// indicates progress when reading large maps (for testing only)
		// Logger.getLogger("aimax.osm").setLevel(Level.FINEST);
		// Logger.getLogger("").getHandlers()[0].setLevel(Level.FINE);
		
		File file = new File("src/main/resource/maps/ulm.osm");
		MiniNaviApp demo = new MiniNaviApp(file);
		demo.showFrame();
	}
}
