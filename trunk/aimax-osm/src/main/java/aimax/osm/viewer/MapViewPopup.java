package aimax.osm.viewer;

import java.awt.Component;
import java.awt.Dimension;
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
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.filechooser.FileFilter;

import aimax.osm.data.MapDataEvent;
import aimax.osm.data.Position;
import aimax.osm.data.entities.EntityAttribute;
import aimax.osm.data.entities.MapEntity;
import aimax.osm.data.entities.MapNode;

/**
 * Useful popup menu for the <code>MapViewPane</code>.
 * @author R. Lunde
 */
public class MapViewPopup extends JPopupMenu implements ActionListener {
	private MapViewPane pane;
	private JFileChooser fileChooser;
	
	private JMenuItem infoMenuItem;
	private JMenuItem removeMarkMenuItem;
	private JMenuItem loadMarksMenuItem;
	private JMenuItem saveMarksMenuItem;
	private JMenuItem clearMenuItem;
	private JCheckBoxMenuItem debugMenuItem;
	
	private int x;
	private int y;
	
	public MapViewPopup() {
	    infoMenuItem = new JMenuItem("Info");
	    infoMenuItem.addActionListener(this);
	    add(infoMenuItem);
	    
	    JMenu markMenu = new JMenu("Mark");
	    add(markMenu);
	    removeMarkMenuItem = new JMenuItem("Remove");
	    removeMarkMenuItem.addActionListener(this);
	    markMenu.add(removeMarkMenuItem);
	    loadMarksMenuItem = new JMenuItem("Load All");
	    loadMarksMenuItem.addActionListener(this);
	    markMenu.add(loadMarksMenuItem);
	    saveMarksMenuItem = new JMenuItem("Save All");
	    saveMarksMenuItem.addActionListener(this);
	    markMenu.add(saveMarksMenuItem);
	    
	    clearMenuItem = new JMenuItem("Clear M&T");
	    clearMenuItem.addActionListener(this);
	    add(clearMenuItem);
	    
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
		debugMenuItem.setState(pane.getRenderer().isDebugModeEnabled());
		super.show(invoker, x, y);
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == infoMenuItem) {
			MapNode mNode = pane.getRenderer().getNextNode(x, y);
			List<MapEntity> entities = new ArrayList<MapEntity>();
			for (int i = -1; i < mNode.getWays().size(); i++) {
				MapEntity me;
				if (i == -1)
					me = mNode;
				else
					me = pane.getModel().getWay(mNode.getWays().get(i).wayId);
				if (me.getName() != null || me.getAttributes().length>0
						|| debugMenuItem.isSelected())
					entities.add(me);
					
			}
			boolean done = false;
			for (int i=0; i < entities.size() && !done; i++) {
				MapEntity me = entities.get(i);
				Object[] content = new Object[] {"", ""};
				String text = (me.getName() != null) ? me.getName() : "";
				if (debugMenuItem.isSelected())
					text += " (" + ((me instanceof MapNode) ? "Node " : "Way ")
					+ me.getId() + ")";
				content[0] = text;
				if (me.getAttributes().length > 0) {
					EntityAttribute[] atts = me.getAttributes();
					String[][] attTexts = new String[atts.length][2];
					for (int j = 0; j < atts.length; j++) {
						attTexts[j][0] = atts[j].getName();
						attTexts[j][1] = atts[j].getValue();
					}
					JTable table = new JTable(attTexts, new String[]{"Name", "Value"});
					JScrollPane spane = new JScrollPane(table);
					spane.setPreferredSize(new Dimension(300, 100));
					content[1] = spane;
				}
				Object[] options;
				if (i < entities.size()-1)
					options = new String[]{"OK", "Next"};
				else
					options = new String[]{"OK"};
				if (JOptionPane.showOptionDialog(pane, content,
						"Map Entity Info", 
						JOptionPane.DEFAULT_OPTION,
						JOptionPane.INFORMATION_MESSAGE,
						null, options, options[0]) != 1)
					done = true;
			}
		} else if (ae.getSource() == clearMenuItem) {
			pane.getModel().clearMarksAndTracks();
			pane.fireMapViewEvent(new MapViewEvent
					(pane, MapViewEvent.Type.TMP_NODES_REMOVED));
		} else if (ae.getSource() == removeMarkMenuItem) {
			List<MapNode> marks = pane.getModel().getMarks();
			float lat = pane.getTransformer().lat(y);
			float lon = pane.getTransformer().lon(x);
			MapNode mark = new Position(lat, lon).selectNearest(marks, null);
			if (mark != null)
				marks.remove(mark);
			pane.getModel().fireMapDataEvent(new MapDataEvent
					(pane.getModel(), MapDataEvent.Type.MAP_MODIFIED));
			pane.fireMapViewEvent(new MapViewEvent
					(pane, MapViewEvent.Type.TMP_NODES_REMOVED));
		} else if (ae.getSource() == loadMarksMenuItem) {
			XMLDecoder decoder = null;
		    try {
		    	File xmlFile = null;
		    	if (getFileChooser().showDialog(pane, "Load Marks")
			    		== JFileChooser.APPROVE_OPTION) {
		    		xmlFile = getFileChooser().getSelectedFile();
		    		if (!xmlFile.getPath().contains("."))
			    		xmlFile = new File(xmlFile.getPath() + ".xml");
		    	}
		    	if (xmlFile != null && xmlFile.exists()) {
		    		pane.getModel().clearMarksAndTracks();
		    		decoder = new XMLDecoder(new BufferedInputStream
		    				(new FileInputStream(xmlFile)));
		            int size = (Integer) decoder.readObject();
		            for (int  i = 0; i < size; i++) {
		            	MapNode node = (MapNode) decoder.readObject();
		            	pane.getModel().addMark(node.getLat(), node.getLon());
		            }
		            pane.fireMapViewEvent
					(new MapViewEvent(pane, MapViewEvent.Type.MARK_ADDED));
		    	}
		    } catch (IOException e) {
			      e.printStackTrace();
		    } finally {
		    	if (decoder != null)
		    		decoder.close();
		    }
		} else if (ae.getSource() == saveMarksMenuItem) {
		    XMLEncoder encoder = null;
		    try {
		    	File xmlFile = null;
		    	if (getFileChooser().showDialog(pane, "Load Marks")
			    		== JFileChooser.APPROVE_OPTION) {
		    		xmlFile = getFileChooser().getSelectedFile();
		    		if (!xmlFile.getPath().contains("."))
			    		xmlFile = new File(xmlFile.getPath() + ".xml");
		    		encoder = new XMLEncoder(new BufferedOutputStream(
		    				new FileOutputStream(xmlFile)));
		    		encoder.writeObject(pane.getModel().getMarks().size());
		    		for (MapNode node : pane.getModel().getMarks())
		    			encoder.writeObject(node);
		    	}
		    } catch (IOException e) {
		      e.printStackTrace();
		    } finally {
		      if (encoder != null )
		        encoder.close();
		    }
		} else if (ae.getSource() == debugMenuItem) {
			pane.getRenderer().enableDebugMode(debugMenuItem.isSelected());
			pane.repaint();
		}
	}
	
	private JFileChooser getFileChooser() {
		if (fileChooser == null) {
			fileChooser = new JFileChooser();
			FileFilter filter = new javax.swing.filechooser.FileNameExtensionFilter
			("Mark Data (xml)", "xml");
			fileChooser.setFileFilter(filter);
		}
		return fileChooser;
	}
}
