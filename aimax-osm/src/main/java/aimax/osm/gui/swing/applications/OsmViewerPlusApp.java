package aimax.osm.gui.swing.applications;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import aimax.osm.data.DataResource;
import aimax.osm.data.EntityClassifier;
import aimax.osm.data.entities.EntityViewInfo;
import aimax.osm.viewer.DefaultEntityRenderer;
import aimax.osm.viewer.MapStyleFactory;
import aimax.osm.viewer.UColor;
import aimax.osm.gui.swing.viewer.MapViewFrame;
import aimax.osm.gui.swing.viewer.MapViewPane;


/**
 * Adds a night view mode to the map viewer. This class demonstrates, how
 * additional functionality can be added to the <code>MapViewFrame</code>
 * even without subclassing.
 * @author Ruediger Lunde
 */
public class OsmViewerPlusApp implements ActionListener {
	protected MapViewFrame frame;
	private JButton zoomInButton;
	private JButton zoomOutButton;
	private JButton leftButton;
	private JButton upButton;
	private JButton downButton;
	private JButton rightButton;
	protected JToggleButton nightButton;
	
	public OsmViewerPlusApp(String[] args) {
		frame = new MapViewFrame(args);
		frame.setTitle("OSM Viewer+");
		
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
	}
	
	public MapViewFrame getFrame() {
		return frame;
	}
	
	public void showFrame() {
		frame.setSize(800, 600);
		frame.setVisible(true);
	}
	
	/** Returns the default renderer without change. */
	protected DefaultEntityRenderer createDayRenderer() {
		return new DefaultEntityRenderer();
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
			if (nightButton.isSelected()) {
				EntityClassifier<EntityViewInfo> eClassifier = 
					new MapStyleFactory().createNightViewClassifier();
				frame.getView().getRenderer().setBackgroundColor(UColor.BLACK);
				frame.getMap().setEntityClassifier(eClassifier);
			} else {
				EntityClassifier<EntityViewInfo> eClassifier =
					new MapStyleFactory().createDefaultClassifier();
				frame.getView().getRenderer().setBackgroundColor(UColor.WHITE);
				frame.getMap().setEntityClassifier(eClassifier);
			}		
		}
	}

	/**
	 * Start application with program arg <code>-screenwidth=xx</code>
	 * (with xx the width in cm)
	 * or <code>-screensize=yy</code> (with yy measured diagonally in inch).
	 */
	public static void main(String[] args) {
		// indicates progress when reading large maps (for testing only)
		// Logger.getLogger("aimax.osm").setLevel(Level.FINEST);
		// Logger.getLogger("").getHandlers()[0].setLevel(Level.FINE);
		
		Locale.setDefault(Locale.US);
		OsmViewerPlusApp demo = new OsmViewerPlusApp(args);
		demo.getFrame().readMap(DataResource.getUlmFileResource());
		demo.showFrame();
	}
}
