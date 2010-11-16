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

import aimax.osm.data.entities.EntityAttribute;
import aimax.osm.data.entities.MapEntity;
import aimax.osm.data.entities.MapNode;
import aimax.osm.data.entities.WayRef;

/**
 * Useful popup menu for the <code>MapViewPane</code>.
 * @author Ruediger Lunde
 */
public class MapViewPopup extends JPopupMenu implements ActionListener {
	
	private static final long serialVersionUID = 1L;
	protected MapViewPane pane;
	private JFileChooser fileChooser;
	
	private JMenuItem infoMenuItem;
	private JMenuItem createMarkMenuItem;
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
	    createMarkMenuItem = new JMenuItem("Create");
	    createMarkMenuItem.addActionListener(this);
	    markMenu.add(createMarkMenuItem);
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
			pane.showMapEntityInfoDialog(mNode, debugMenuItem.isSelected());
		} else if (ae.getSource() == clearMenuItem) {
			pane.getModel().clearMarksAndTracks();
			pane.fireMapViewEvent(new MapViewEvent
					(pane, MapViewEvent.Type.TMP_NODES_REMOVED));
		} else if (ae.getSource() == createMarkMenuItem) {
			PositionPanel panel = new PositionPanel();
			int res = JOptionPane.showConfirmDialog(pane, panel, "Specify a Position", JOptionPane.OK_CANCEL_OPTION);
			if (res == JOptionPane.OK_OPTION) {
				float lat = panel.getLat();
				float lon = panel.getLon();
				if (!Float.isNaN(lat) && !Float.isNaN(lon)) {
					pane.getModel().addMark(lat, lon);
					pane.adjustToCenter(lat, lon);
				}
			}
		} else if (ae.getSource() == removeMarkMenuItem) {
			pane.removeNearestMark(x, y);
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
