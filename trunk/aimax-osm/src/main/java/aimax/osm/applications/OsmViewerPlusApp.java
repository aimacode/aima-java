package aimax.osm.applications;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;

import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import aimax.osm.data.DataResource;
import aimax.osm.data.EntityClassifier;
import aimax.osm.reader.MapReader;
import aimax.osm.reader.OsmReader;
import aimax.osm.viewer.DefaultMapEntityRenderer;
import aimax.osm.viewer.EntityPrintInfo;
import aimax.osm.viewer.EntityIcon;
import aimax.osm.viewer.MapViewFrame;
import aimax.osm.viewer.MapViewPane;


/**
 * Adds a night view mode to the map viewer. This class demonstrates, how
 * additional functionality can be added to the <code>MapViewFrame</code>
 * even without subclassing.
 * @author R. Lunde
 */
public class OsmViewerPlusApp implements ActionListener {
	protected MapViewFrame frame;
	protected DefaultMapEntityRenderer dayRenderer;
	protected DefaultMapEntityRenderer nightRenderer;
	private JButton zoomInButton;
	private JButton zoomOutButton;
	private JButton leftButton;
	private JButton upButton;
	private JButton downButton;
	private JButton rightButton;
	protected JToggleButton nightButton;
	
	public OsmViewerPlusApp(InputStream file) {
		MapReader mapReader = new OsmReader();
		frame = new MapViewFrame(mapReader, file);
		frame.setTitle("OSM Viewer+");
		
		dayRenderer = createDayRenderer();
		nightRenderer = createNightRenderer();
		
		JToolBar toolbar = frame.getToolbar();
		toolbar.addSeparator();
		nightButton = new JToggleButton("Night Mode");
		zoomInButton = new JButton("In");
		zoomInButton.addActionListener(this);
		toolbar.add(zoomInButton);
		zoomOutButton = new JButton("Out");
		zoomOutButton.addActionListener(this);
		toolbar.add(zoomOutButton);
		leftButton = new JButton("Left");
		leftButton.addActionListener(this);
		toolbar.add(leftButton);
		rightButton = new JButton("Right");
		rightButton.addActionListener(this);
		toolbar.add(rightButton);
		upButton = new JButton("Up");
		upButton.addActionListener(this);
		toolbar.add(upButton);
		downButton = new JButton("Down");
		downButton.addActionListener(this);
		toolbar.add(downButton);
		
		toolbar.addSeparator();
		toolbar.add(nightButton);
		nightButton.addActionListener(this);
		frame.getView().setRenderer(dayRenderer);
	}
	
	public void showFrame() {
		frame.setSize(800, 600);
		frame.setVisible(true);
	}
	
	/** Returns the default renderer without change. */
	protected DefaultMapEntityRenderer createDayRenderer() {
		return new DefaultMapEntityRenderer();
	}
	
	/** Returns a renderer with modified color settings. */
	protected DefaultMapEntityRenderer createNightRenderer() {
		DefaultMapEntityRenderer result = new DefaultMapEntityRenderer();
		EntityClassifier<EntityPrintInfo> entityClassifier = result.getEntityClassifier(); 
		// change colors of ways and nodes
		result.setBackgroundColor(Color.BLACK);
		result.setTrackInfo
		("default", 0, Color.WHITE, DefaultMapEntityRenderer.GRAY_TRANS);
	
		entityClassifier.replaceRule("place", "city", new EntityPrintInfo(0, 100, Color.WHITE, null, 30));
	    entityClassifier.replaceRule("place", "town", new EntityPrintInfo(0, 1000, Color.WHITE, null, 29));
	    entityClassifier.replaceRule("place", "village", new EntityPrintInfo(0, 3000, Color.GRAY, null, 29));
	    entityClassifier.replaceRule("place", null, new EntityPrintInfo(0, 10000, Color.GRAY, null, 28));
	     
	    entityClassifier.replaceRule("mark", "yes", new EntityPrintInfo(0, 0, Color.GREEN.darker(), EntityIcon.createCircle(12, Color.GREEN.darker()), 5));

		return result;
	}

	public void actionPerformed(ActionEvent e) {
		MapViewPane view = frame.getView();
		if (e.getSource() == zoomInButton) {
			view.zoom(2, view.getWidth() / 2, view.getHeight() / 2);
		} else if (e.getSource() == zoomOutButton) {
			view.zoom(0.5f, view.getWidth() / 2, view.getHeight() / 2);
		} else if (e.getSource() == leftButton) {
			view.adjust((int) (0.3 * view.getWidth()), 0);
		} else if (e.getSource() == rightButton) {
			view.adjust((int) (-0.3 * view.getWidth()), 0);
		}  else if (e.getSource() == upButton) {
			view.adjust(0, (int) (0.3 * view.getHeight()));
		} else if (e.getSource() == downButton) {
			view.adjust(0, (int) (-0.3 * view.getHeight()));
		} else if (e.getSource() == nightButton) {
			boolean b = nightButton.isSelected();
			frame.getView().setRenderer(b ? nightRenderer : dayRenderer);		
		}
	}

	public static void main(String[] args) {
		// indicates progress when reading large maps (for testing only)
		// Logger.getLogger("aimax.osm").setLevel(Level.FINEST);
		// Logger.getLogger("").getHandlers()[0].setLevel(Level.FINE);
		
		OsmViewerPlusApp demo = new OsmViewerPlusApp(DataResource.getULMFileResource());
		demo.showFrame();
	}
}
