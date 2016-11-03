package aimax.osm.gui.swing.viewer;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.filechooser.FileFilter;

import aimax.osm.data.Position;
import aimax.osm.data.entities.MapNode;
import aimax.osm.viewer.MapViewEvent;

/**
 * Useful popup menu for the <code>MapViewPane</code>.
 * 
 * @author Ruediger Lunde
 */
public class MapViewPopup extends JPopupMenu implements ActionListener {

	private static final long serialVersionUID = 1L;
	protected MapViewPane pane;
	private JFileChooser fileChooser;

	private JMenuItem entityInfoMenuItem;
	private JMenuItem createMarkerMenuItem;
	private JMenuItem removeMarkerMenuItem;
	private JMenuItem loadMarkersMenuItem;
	private JMenuItem saveMarkersMenuItem;
	private JMenuItem clearMenuItem;
	private JMenuItem functionsMenuItem;
	private JCheckBoxMenuItem debugMenuItem;

	private int x;
	private int y;

	public MapViewPopup() {
		entityInfoMenuItem = new JMenuItem("Entity Info");
		entityInfoMenuItem.addActionListener(this);
		add(entityInfoMenuItem);

		JMenu markerMenu = new JMenu("Marker");
		add(markerMenu);
		createMarkerMenuItem = new JMenuItem("Create");
		createMarkerMenuItem.addActionListener(this);
		markerMenu.add(createMarkerMenuItem);
		removeMarkerMenuItem = new JMenuItem("Remove");
		removeMarkerMenuItem.addActionListener(this);
		markerMenu.add(removeMarkerMenuItem);
		loadMarkersMenuItem = new JMenuItem("Load All");
		loadMarkersMenuItem.addActionListener(this);
		markerMenu.add(loadMarkersMenuItem);
		saveMarkersMenuItem = new JMenuItem("Save All");
		saveMarkersMenuItem.addActionListener(this);
		markerMenu.add(saveMarkersMenuItem);

		clearMenuItem = new JMenuItem("Clear M&T");
		clearMenuItem.addActionListener(this);
		add(clearMenuItem);

		functionsMenuItem = new JMenuItem("Functions");
		functionsMenuItem.addActionListener(this);
		add(functionsMenuItem);

		addSeparator();
		debugMenuItem = new JCheckBoxMenuItem("Debug Mode");
		debugMenuItem.addActionListener(this);
		add(debugMenuItem);
	}

	/**
	 * This implementation assumes, that invoker is of type
	 * <code>MapViewPane</code>.
	 */
	@Override
	public void show(Component invoker, int x, int y) {
		pane = (MapViewPane) invoker;
		this.x = x;
		this.y = y;
		debugMenuItem.setState(pane.isDebugModeEnabled());
		super.show(invoker, x, y);
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == entityInfoMenuItem) {
			MapNode mNode = pane.getRenderer().getNextNode(x, y);
			if (mNode != null)
				pane.showMapEntityInfoDialog(mNode, pane.isDebugModeEnabled());
		} else if (ae.getSource() == clearMenuItem) {
			pane.getMap().clearMarkersAndTracks();
		} else if (ae.getSource() == createMarkerMenuItem) {
			PositionPanel panel = new PositionPanel();
			int res = JOptionPane.showConfirmDialog(pane, panel,
					"Specify a Position", JOptionPane.OK_CANCEL_OPTION);
			if (res == JOptionPane.OK_OPTION) {
				float lat = panel.getLat();
				float lon = panel.getLon();
				if (!Float.isNaN(lat) && !Float.isNaN(lon)) {
					pane.getMap().addMarker(lat, lon);
					pane.adjustToCenter(lat, lon);
				}
			}
		} else if (ae.getSource() == removeMarkerMenuItem) {
			pane.removeNearestMarker(x, y);
		} else if (ae.getSource() == loadMarkersMenuItem) {
			XMLDecoder decoder = null;
			try {
				File xmlFile = null;
				if (getFileChooser().showDialog(pane, "Load Markers") == JFileChooser.APPROVE_OPTION) {
					xmlFile = getFileChooser().getSelectedFile();
					if (!xmlFile.getPath().contains("."))
						xmlFile = new File(xmlFile.getPath() + ".xml");
				}
				if (xmlFile != null && xmlFile.exists()) {
					pane.getMap().clearMarkersAndTracks();
					decoder = new XMLDecoder(new BufferedInputStream(
							new FileInputStream(xmlFile)));
					int size = (Integer) decoder.readObject();
					for (int i = 0; i < size; i++) {
						WritablePosition pos = (WritablePosition) decoder
								.readObject();
						pane.getMap().addMarker(pos.getLat(), pos.getLon());
					}
					pane.fireMapViewEvent(new MapViewEvent(pane,
							MapViewEvent.Type.MARKER_ADDED));
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (decoder != null)
					decoder.close();
			}
		} else if (ae.getSource() == saveMarkersMenuItem) {
			XMLEncoder encoder = null;
			try {
				File xmlFile = null;
				if (getFileChooser().showDialog(pane, "Save Markers") == JFileChooser.APPROVE_OPTION) {
					xmlFile = getFileChooser().getSelectedFile();
					if (!xmlFile.getPath().contains("."))
						xmlFile = new File(xmlFile.getPath() + ".xml");
					encoder = new XMLEncoder(new BufferedOutputStream(
							new FileOutputStream(xmlFile)));
					encoder.writeObject(pane.getMap().getMarkers().size());
					for (MapNode node : pane.getMap().getMarkers())
						encoder.writeObject(new WritablePosition(node));
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (encoder != null)
					encoder.close();
			}
		} else if (ae.getSource() == functionsMenuItem) {
			JOptionPane.showMessageDialog(pane,
					MapViewPane.FUNCTION_DESCRIPTION.split("\\|"),
					"Function Description", JOptionPane.INFORMATION_MESSAGE);
		} else if (ae.getSource() == debugMenuItem) {
			pane.enableDebugMode(debugMenuItem.isSelected());
		}
	}

	private JFileChooser getFileChooser() {
		if (fileChooser == null) {
			fileChooser = new JFileChooser();
			FileFilter filter = new javax.swing.filechooser.FileNameExtensionFilter(
					"Marker Data (xml)", "xml");
			fileChooser.setFileFilter(filter);
		}
		return fileChooser;
	}

	/**
	 * Provides a position implementation with full java bean interface which is
	 * suitable for xml serialization.
	 * 
	 * @author Ruediger Lunde
	 */
	public static class WritablePosition extends Position {
		public WritablePosition() {
			super(0f, 0f);
		}

		public WritablePosition(MapNode node) {
			super(node);
		}

		public void setLat(float lat) {
			this.lat = lat;
		}

		public void setLon(float lon) {
			this.lon = lon;
		}
	}
}
