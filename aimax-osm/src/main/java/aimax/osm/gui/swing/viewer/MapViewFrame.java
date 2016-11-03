package aimax.osm.gui.swing.viewer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.InputStream;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import aimax.osm.data.BoundingBox;
import aimax.osm.data.EntityClassifier;
import aimax.osm.data.MapBuilder;
import aimax.osm.data.OsmMap;
import aimax.osm.data.entities.EntityViewInfo;
import aimax.osm.data.entities.MapNode;
import aimax.osm.data.impl.DefaultMap;
import aimax.osm.reader.Bz2OsmReader;
import aimax.osm.reader.MapReader;
import aimax.osm.viewer.MapStyleFactory;
import aimax.osm.writer.Bz2OsmWriter;
import aimax.osm.writer.MapWriter;

/**
 * Implements a simple frame with a toolbar, a sidebar, and a map view. The
 * toolbar provides buttons for map loading, map saving, and informations about
 * recent events. The sidebar contains a tab for entity finding. The frame
 * serves as base class for all non-agent applications of this library and can
 * be extended in various ways.
 * 
 * @author Ruediger Lunde
 */
public class MapViewFrame extends JFrame implements ActionListener {
	private static final long serialVersionUID = 1L;
	protected static Logger LOG = Logger.getLogger("aimax.osm");

	protected MapViewPane view;
	protected JSplitPane splitter;
	protected JToolBar toolbar;
	protected JTabbedPane sidebar;
	protected EntityClassifier<EntityViewInfo> viewInfo;
	protected MapReader mapReader;
	protected MapWriter mapWriter;

	private JFileChooser fileChooser;
	private JButton loadButton;
	private JButton saveButton;
	private JButton statisticsButton;
	private JCheckBox sidebarCheckBox;

	protected JTextField infoField;

	/**
	 * Tries to find an argument starting with <code>-screenwidth=</code> or
	 * <code>-screensize=</code> and passes the corresponding number to the view
	 * for scale computation.
	 */
	public MapViewFrame(String[] args) {
		this();
		for (String arg : args) {
			try {
				if (arg.startsWith("-screenwidth=")) {
					view.setScreenWidthInCentimeter(Double.parseDouble(arg
							.substring(13)));
					break;
				} else if (arg.startsWith("-screensize=")) {
					view.setScreenSizeInInch(Double.parseDouble(arg
							.substring(12)));
					break;
				}
			} catch (NumberFormatException e) {
				// ignore the argument...
			}
		}
	}

	public MapViewFrame() {
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent arg0) {
				onExit();
				System.exit(0);
			}
		});
		fileChooser = new JFileChooser();
		setMapReader(new Bz2OsmReader());
		setMapWriter(new Bz2OsmWriter());

		JPanel contentPanel = new JPanel();
		contentPanel.setLayout(new BorderLayout());
		getContentPane().add(contentPanel, BorderLayout.CENTER);

		toolbar = new JToolBar();
		contentPanel.add(toolbar, BorderLayout.NORTH);

		splitter = new JSplitPane();
		// splitter.setOneTouchExpandable(true);
		contentPanel.add(splitter, BorderLayout.CENTER);

		view = new MapViewPane();
		splitter.add(view, JSplitPane.RIGHT);

		sidebar = new JTabbedPane();
		splitter.add(sidebar, JSplitPane.LEFT);

		initMapAndClassifier();
		initToolbar();
		initSidebar();
	}

	/** Closes the map. */
	protected void onExit() {
		if (getMap() != null)
			getMap().close();
	}

	/**
	 * Creates the map, provides it to the view, and creates a corresponding
	 * entity classifier which is used by default when reading maps.
	 */
	protected void initMapAndClassifier() {
		view.setMap(new DefaultMap());
		viewInfo = new MapStyleFactory().createDefaultClassifier();
	}

	/**
	 * Defines the functionality of the toolbar by adding components to it.
	 * Override to customize!
	 */
	protected void initToolbar() {
		toolbar.setFloatable(false);
		loadButton = new JButton("Load");
		loadButton
				.setToolTipText("Load Map (<ctrl> bounding box mode, <shift> overview mode)");
		loadButton.addActionListener(this);
		toolbar.add(loadButton);
		saveButton = new JButton("Save");
		saveButton.setToolTipText("Save Visible Map");
		saveButton.addActionListener(this);
		toolbar.add(saveButton);
		statisticsButton = new JButton("Statistics");
		statisticsButton.setToolTipText("Show Map Statistics");
		statisticsButton.addActionListener(this);
		toolbar.add(statisticsButton);
		sidebarCheckBox = new JCheckBox("Sidebar");
		sidebarCheckBox.addActionListener(this);
		sidebarCheckBox.setSelected(false);
		showSidebar(false);
		toolbar.add(sidebarCheckBox);
		InfoField infoField = new InfoField(view, getMap());
		view.addMapViewEventListener(infoField.getMapViewEventListener());
		getMap().addMapDataEventListener(infoField.getMapDataEventListener());
		toolbar.add(infoField);
	}

	/**
	 * Defines the functionality of the sidebar by adding components to it.
	 * Override to customize!
	 */
	protected void initSidebar() {
		// sidebar.setMinimumSize(new Dimension(0, 0));

		// gives an example how to add functionality to the sidebar
		FindPanel findPane = new FindPanel(view);
		getMap().addMapDataEventListener(findPane);
		sidebar.addTab("Find", findPane);
	}

	public void showSidebar(boolean b) {
		if (b) {
			splitter.setDividerSize(4);
			sidebar.setVisible(true);
			splitter.setDividerLocation(splitter.getLastDividerLocation());

		} else {
			splitter.setLastDividerLocation(splitter.getDividerLocation());
			splitter.setDividerSize(0);
			sidebar.setVisible(false);
		}
	}

	public void setVisible(boolean b) {
		super.setVisible(b);
		if (b && !getMap().isEmpty())
			view.adjustToFit();
	}

	public MapViewPane getView() {
		return view;
	}

	public OsmMap getMap() {
		return view.getMap();
	}

	public JToolBar getToolbar() {
		return toolbar;
	}

	public JButton getLoadButton() {
		return loadButton;
	}

	public JButton getSaveButton() {
		return saveButton;
	}

	public void setMapReader(MapReader mapReader) {
		this.mapReader = mapReader;
		for (int i = fileChooser.getChoosableFileFilters().length - 1; i > 0; i--)
			fileChooser.removeChoosableFileFilter(fileChooser
					.getChoosableFileFilters()[i]);
		String[] exts = mapReader.fileFormatDescriptions();
		for (int i = 0; i < exts.length; i++) {
			FileFilter filter = new FileNameExtensionFilter(exts[i], mapReader
					.fileFormatExtensions()[i]);
			fileChooser.addChoosableFileFilter(filter);
		}
		fileChooser.setFileFilter(fileChooser.getChoosableFileFilters()[0]);
		fileChooser.setSelectedFile(new File(""));
	}

	public void setMapWriter(MapWriter mapWriter) {
		this.mapWriter = mapWriter;
	}

	public void readMap(InputStream stream) {
		if (stream != null) {
			MapBuilder builder = getMap().getBuilder();
			builder.setEntityClassifier(viewInfo);
			mapReader.readMap(stream, builder);
			builder.buildMap();
		} else {
			LOG
					.warning("Map reading failed because input stream does not exist.");
		}
	}

	public void readMap(File file) {
		MapBuilder builder = getMap().getBuilder();
		builder.setEntityClassifier(viewInfo);
		mapReader.readMap(file, builder);
		builder.buildMap();
		fileChooser.setSelectedFile(file.getAbsoluteFile());
	}

	/**
	 * Defines what happens when a button is pressed.
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == loadButton) {
			String title = "Load OSM Data";
			if ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)
				title += " (Bounding Box Mode)";
			if ((e.getModifiers() & KeyEvent.SHIFT_MASK) != 0)
				title += " (Overview Mode)";
			fileChooser.setDialogTitle(title);
			int returnVal = fileChooser.showDialog(this, "Load");
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				if ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0) {
					// ctrl+load -> ask the user for a bounding box.
					BoundingBox bb = askForBoundingBox();
					if (bb != null)
						mapReader.setFilter(bb);
					else
						return;
				}
				if ((e.getModifiers() & KeyEvent.SHIFT_MASK) != 0) {
					EntityClassifier<Boolean> filter = createOverviewFilter();
					mapReader.setFilter(filter);
				}
				readMap(fileChooser.getSelectedFile());
			}
		} else if (e.getSource() == saveButton) {
			JFileChooser fc = new JFileChooser();
			String[] exts = mapWriter.fileFormatDescriptions();
			for (int i = 0; i < exts.length; i++) {
				FileFilter filter = new FileNameExtensionFilter(exts[i],
						mapWriter.fileFormatExtensions()[i]);
				fc.addChoosableFileFilter(filter);
			}
			fc.setFileFilter(fc.getChoosableFileFilters()[0]);
			fc.setCurrentDirectory(fileChooser.getCurrentDirectory());
			int returnVal = fc.showSaveDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION
					&& (!fc.getSelectedFile().exists() || JOptionPane
							.showConfirmDialog(this, "File exists, overwrite?",
									"Confirm", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION)) {
				mapWriter.writeMap(fc.getSelectedFile(), getMap(), view
						.getBoundingBox());
			}
		} else if (e.getSource() == statisticsButton) {
			Object[][] data = getMap().getStatistics();
			JTable table = new JTable(data,
					new String[] { "Attribute", "Value" });
			JScrollPane scroller = new JScrollPane(table);
			scroller.setPreferredSize(new Dimension(250, 300));
			JOptionPane
					.showConfirmDialog(this, scroller, "Map Statistics",
							JOptionPane.DEFAULT_OPTION,
							JOptionPane.INFORMATION_MESSAGE);
		} else if (e.getSource() == sidebarCheckBox) {
			showSidebar(sidebarCheckBox.isSelected());
		}
	}

	protected BoundingBox askForBoundingBox() {
		BoundingBox result = null;
		JTextField minLat = new JTextField("-90");
		JTextField minLon = new JTextField("-180");
		JTextField maxLat = new JTextField("90");
		JTextField maxLon = new JTextField("180");
		if (getMap().getMarkers().size() == 2) {
			MapNode m1 = getMap().getMarkers().get(0);
			MapNode m2 = getMap().getMarkers().get(1);
			minLat.setText(Float.toString(Math.min(m1.getLat(), m2.getLat())));
			minLon.setText(Float.toString(Math.min(m1.getLon(), m2.getLon())));
			maxLat.setText(Float.toString(Math.max(m1.getLat(), m2.getLat())));
			maxLon.setText(Float.toString(Math.max(m1.getLon(), m2.getLon())));
		}
		Object[] content = new Object[] { "Min Latitude:", minLat,
				"Min Longitude:", minLon, "Max Latitude:", maxLat,
				"Max Longitude:", maxLon, };
		boolean done;
		do {
			done = true;
			if (JOptionPane.showConfirmDialog(this, content,
					"Specify Bounding Box", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
				try {
					result = new BoundingBox(
							Float.parseFloat(minLat.getText()), Float
									.parseFloat(minLon.getText()), Float
									.parseFloat(maxLat.getText()), Float
									.parseFloat(maxLon.getText()));
				} catch (NumberFormatException e) {
					done = false;
				}
			}
		} while (!done);
		return result;
	}

	protected EntityClassifier<Boolean> createOverviewFilter() {
		EntityClassifier<Boolean> result = new EntityClassifier<Boolean>();
		EntityClassifier<Boolean> sc;
		result.addRule("natural", "coastline", Boolean.TRUE);
		result.addRule("waterway", "river", Boolean.TRUE);
		result.addRule("highway", "motorway", Boolean.TRUE);
		result.addRule("highway", "motorway_link", Boolean.TRUE);
		result.addRule("highway", "trunk", Boolean.TRUE);
		result.addRule("highway", "trunk_link", Boolean.TRUE);
		sc = result.addRule("boundary", null, null);
		sc.addRule("admin_level", "1", Boolean.TRUE);
		sc.addRule("admin_level", "2", Boolean.TRUE);
		sc.addRule("admin_level", "3", Boolean.TRUE);
		sc.addRule("admin_level", "4", Boolean.TRUE);
		result.addRule("place", "city", Boolean.TRUE);
		result.addRule("place", "town", Boolean.TRUE);
		result.addRule("place", "village", Boolean.TRUE);
		return result;
	}
}
