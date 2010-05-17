package aimax.osm.viewer;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import aimax.osm.data.BoundingBox;
import aimax.osm.data.EntityClassifier;
import aimax.osm.data.MapDataEvent;
import aimax.osm.data.MapDataEventListener;
import aimax.osm.data.MapDataStore;
import aimax.osm.data.Position;
import aimax.osm.data.entities.MapNode;
import aimax.osm.data.entities.MapWay;
import aimax.osm.data.entities.Track;
import aimax.osm.data.entities.WayRef;
import aimax.osm.reader.Bz2OsmReader;
import aimax.osm.reader.MapReader;
import aimax.osm.writer.MapWriter;
import aimax.osm.writer.OsmBz2Writer;

/**
 * Implements a simple frame with a toolbar and a map view. The
 * toolbar provides buttons for map loading, map saving, and entity finding.
 * Additionally, a text field is included which shows informations like positions,
 * track length, and POI names. 
 * @author R. Lunde
 */
public class MapViewFrame extends JFrame implements ActionListener {
	protected MapViewPane view;
	protected MapDataStore mapData;
	protected MapReader mapReader;
	protected MapWriter mapWriter;
	protected JToolBar toolbar;
	
	private JFileChooser fileChooser;
	private JButton loadButton;
	private JButton saveButton;
	private JButton findButton;
	private JTextField findField;
	protected JTextField infoField;
	
	public MapViewFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fileChooser = new JFileChooser();
		toolbar = new JToolBar();
		loadButton = new JButton("Load");
		loadButton.setToolTipText("Load Map (<ctrl> bounding box mode, <shift> overview mode)");
		loadButton.addActionListener(this);
		toolbar.add(loadButton);
		saveButton = new JButton("Save");
		saveButton.setToolTipText("Save Map");
		saveButton.addActionListener(this);
		toolbar.add(saveButton);
		toolbar.addSeparator();
		findField = new JTextField();
		toolbar.add(findField);
		findField.addKeyListener(new KeyListener() {
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER)
					find(findField.getText());
			}
			public void keyReleased(KeyEvent e) {}
			public void keyTyped(KeyEvent e) {}});
		findButton = new JButton("Find");
		findButton.setToolTipText("Find Entity by Name or Attribute");
		findButton.addActionListener(this);
		toolbar.add(findButton);
		toolbar.addSeparator();
		infoField = new JTextField(20);
		infoField.setEditable(false);
		toolbar.add(infoField);
		getContentPane().add(toolbar, BorderLayout.NORTH);
		
		mapData = new MapDataStore();
		setDefaultEntityClassifier();
		setMapReader(new Bz2OsmReader());
		setMapWriter(new OsmBz2Writer());
		view = new MapViewPane();
		view.setModel(mapData);
		getContentPane().add(view, BorderLayout.CENTER);
		MapEventHandler eventHandler = new MapEventHandler();
		mapData.addMapDataEventListener(eventHandler);
		view.addMapViewEventListener(eventHandler);
	}
	
	/**
	 * Tries to find an argument starting with <code>-screenwidth=</code> or
	 * <code>-screensize=</code> and passes the corresponding number to the
	 * view for scale computation.
	 */
	public MapViewFrame(String[] args) {
		this();
		for (String arg : args) {
			try {
				if (arg.startsWith("-screenwidth=")) {
						view.setScreenWidthInCentimeter(Double.parseDouble(arg.substring(13)));
						break;
				} else if (arg.startsWith("-screensize=")) {
					view.setScreenSizeInInch(Double.parseDouble(arg.substring(12)));
					break;
				}
			} catch (NumberFormatException e) {
				// ignore the argument...
			}
		}
	}
	
	protected void setDefaultEntityClassifier() {
		mapData.setEntityClassifier(new MapStyleFactory().createDefaultClassifier());
	}
	
	public void setVisible(boolean b) {
		super.setVisible(b);
		if (b && !mapData.isEmpty())
			view.adjustToFit();
	}
	
	public MapViewPane getView() {
		return view;
	}
	
	public MapDataStore getMapData() {
		return mapData;
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
		for (int i = fileChooser.getChoosableFileFilters().length-1; i>0; i--)
			fileChooser.removeChoosableFileFilter
			(fileChooser.getChoosableFileFilters()[i]);
		String[] exts = mapReader.fileFormatDescriptions();
		for (int i = 0 ; i < exts.length; i++) {
			FileFilter filter = new FileNameExtensionFilter
			(exts[i], mapReader.fileFormatExtensions()[i]);
			fileChooser.addChoosableFileFilter(filter);
		}
		fileChooser.setSelectedFile(new File(""));
	}
	
	public void setMapWriter(MapWriter mapWriter) {
		this.mapWriter = mapWriter;
	}
	
	public void readMap(InputStream map) {
		if (map != null)
			mapReader.readMap(map, mapData);
		else
			System.err.println("Error: Map reading failed because input stream does not exist.");
	}
	
	public void readMap(File map) {
		mapReader.readMap(map, mapData);
		fileChooser.setSelectedFile(map.getAbsoluteFile());	
	}
	
	public void find(String namepart) {
		Position pos = view.getCenterPosition();
		MapNode node = mapData.findNode
		(findField.getText(), pos, true, true, true);
		if (node != null) {
			mapData.addMark(node.getLat(), node.getLon());
			view.adjustToCenter(node.getLat(), node.getLon());
			String name = node.getName();
			if (name == null) {
				name = "";
				for (WayRef wr : node.getWayRefs()) {
					MapWay way = wr.getWay();
					String wayName = way.getName();
					if (wayName != null && !name.contains(wayName)) {
						if (!name.isEmpty())
							name = name + " / ";
						name = name + way.getName();
					}
				}
			}
			infoField.setText("Found: " + name);
		} else {
			infoField.setText("");
		}
	}
	
	/**
	 * Defines what happens when a button is pressed. 
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == loadButton) {
			int returnVal = fileChooser.showOpenDialog(this);
		    if(returnVal == JFileChooser.APPROVE_OPTION) {
		    	if ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0) {
		    		//ctrl+load -> ask the user for a bounding box.
		    		BoundingBox bb = askForBoundingBox();
		    		if (bb != null)
		    			mapReader.setBoundingBox(bb);
		    		else
		    			return;
		    	}
		    	if ((e.getModifiers() & KeyEvent.SHIFT_MASK) != 0) {
		    		EntityClassifier<Boolean> filter = new EntityClassifier<Boolean>();
		    		filter.addRule("place", "city", Boolean.TRUE);
		    		filter.addRule("place", "town", Boolean.TRUE);
		    		filter.addRule("place", "village", Boolean.TRUE);
		    		filter.addRule("highway", "motorway", Boolean.TRUE);
		    		filter.addRule("highway", "motorway_link", Boolean.TRUE);
		    		filter.addRule("highway", "trunk", Boolean.TRUE);
		    		filter.addRule("highway", "trunk_link", Boolean.TRUE);
		    		mapReader.setAttFilter(filter);
		    	}
		    	mapReader.readMap(fileChooser.getSelectedFile(), mapData);
		    }    
		} else if (e.getSource() == saveButton) {
			JFileChooser fc = new JFileChooser();
			String[] exts = mapReader.fileFormatDescriptions();
			for (int i = 0 ; i < exts.length; i++) {
				FileFilter filter = new FileNameExtensionFilter
				(exts[i], mapReader.fileFormatExtensions()[i]);
				fc.addChoosableFileFilter(filter);
			}
			fc.setCurrentDirectory(fileChooser.getCurrentDirectory());
			int returnVal = fc.showSaveDialog(this);
		    if(returnVal == JFileChooser.APPROVE_OPTION &&
		    		(!fc.getSelectedFile().exists() ||
		    				JOptionPane.showConfirmDialog(this,
		    						"File exists, overwrite?", "Confirm",
		    						JOptionPane.OK_CANCEL_OPTION)
		    						== JOptionPane.OK_OPTION)) {
			    	mapWriter.writeMap(fc.getSelectedFile(), mapData);
		    }
		} else if (e.getSource() == findButton) {
			find(findField.getText());
		}
	}
	
	protected BoundingBox askForBoundingBox() {
		BoundingBox result = null;
		JTextField minLat = new JTextField("-90");
		JTextField minLon = new JTextField("-180");
		JTextField maxLat = new JTextField("90");
		JTextField maxLon = new JTextField("180");
		Object[] content = new Object[] {
				"Min Latitude:", minLat, "Min Longitude:", minLon,
				"Max Latitude:", maxLat, "Max Longitude:", maxLon,
		};
		boolean done;
		do {
			done = true;
			if (JOptionPane.showConfirmDialog(this,
			    						content, "Specify Bounding Box",
			    						JOptionPane.OK_CANCEL_OPTION)
			    						== JOptionPane.OK_OPTION) {
				try {
					result = new BoundingBox(
							Float.parseFloat(minLat.getText()),
							Float.parseFloat(minLon.getText()),
							Float.parseFloat(maxLat.getText()),
							Float.parseFloat(maxLon.getText()));
				} catch (NumberFormatException e) {
					done = false;
				}
			}
		} while (!done);
		return result;
	}
	
	/**
	 * Updates the info field based on events sent by the MapViewPane. 
	 * @author R. Lunde
	 */
	class MapEventHandler implements MapDataEventListener, MapViewEventListener {

		@Override
		public void eventHappened(MapDataEvent event) {
			if (event.getType() == MapDataEvent.Type.MAP_NEW) {
				infoField.setText("Ways: " + mapData.getWays().size() + ", Way Nodes: "
						+ mapData.getWayNodes().size() + ", POIs: " + mapData.getPOIs().size());
			} else if (event.getType() == MapDataEvent.Type.MARK_ADDED) {
				List<MapNode> nodes = mapData.getMarks();
				DecimalFormat f1 = new DecimalFormat("#0.00");
				MapNode mark = nodes.get(nodes.size()-1);
				infoField.setText("Mark " + mark.getName() + ": Lat "
						+ f1.format(mark.getLat()) + "; Lon "
						+ f1.format(mark.getLon()));
			} else if (event.getType() == MapDataEvent.Type.TRACK_MODIFIED) {
				Track track = mapData.getTrack(event.getObjId());
				if (track != null) {
					List<MapNode> nodes = track.getTrkPts();
					DecimalFormat f1 = new DecimalFormat("#0.00");
					double km = Position.getTrackLengthKM(nodes);
					String info = track.getName() + ": Length " + f1.format(km) + " km";
					if (nodes.size() > 1) {
						DecimalFormat f2 = new DecimalFormat("#000");
						MapNode m1 = nodes.get(nodes.size() - 2);
						MapNode m2 = nodes.get(nodes.size() - 1);
						int course = new Position(m1).getCourseTo(m2);
						info += "; Course " + f2.format(course);
					}
					infoField.setText(info);
				}
			} else {
				infoField.setText("");
			}
			
		}

		@Override
		public void eventHappened(MapViewEvent event) {
			if (event.getType() == MapViewEvent.Type.ZOOM) {
				if (mapData.getMarks().isEmpty()) {
					DecimalFormat f = new DecimalFormat("#0.0");
					double scale = view.getTransformer().computeScale();
					String text = "Scale: 1 / ";
					if (scale <= 1e-4)
						text += (int) (0.001f/scale) + " 000";
					else
						text += (int) (1f/scale) + "";
					text += "  Display Factor: "
						+ f.format(view.getRenderer().getDisplayFactor());
					//text += "  (" + (int) view.getTransformer().getDotsPerDeg() + ")";
					infoField.setText(text);
				}
			}
		}
	}
}
