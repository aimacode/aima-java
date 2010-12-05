package aimax.osm.viewer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import aimax.osm.data.BoundingBox;
import aimax.osm.data.MapEvent;
import aimax.osm.data.MapEventListener;
import aimax.osm.data.OsmMap;
import aimax.osm.data.Position;
import aimax.osm.data.entities.EntityAttribute;
import aimax.osm.data.entities.MapEntity;
import aimax.osm.data.entities.MapNode;
import aimax.osm.data.entities.WayRef;
import aimax.osm.data.impl.DefaultMap;

/**
 * Provides a panel which shows map data. As model, a
 * {@link aimax.osm.data.OsmMap} is used.
 * <p>
 * Hint for using the viewer: Try Mouse-Left, Mouse-Right, Mouse-Drag,
 * Ctrl-Mouse-Left, Plus, Minus, Ctrl-Plus, Ctrl-Minus, arrow buttons, and also
 * the Mouse-Wheel for navigation, mark setting, and track definition.
 * 
 * @author Ruediger Lunde
 */
public class MapViewPane extends JComponent implements MapEventListener {

	private static final long serialVersionUID = 1L;
	// private Logger LOG = Logger.getLogger("aimax.osm");

	/**
	 * Maintains a reference to the model which provides the data to be
	 * displayed.
	 */
	protected OsmMap map;
	protected CoordTransformer transformer;
	private AbstractEntityRenderer renderer;
	private ArrayList<MapViewEventListener> eventListeners;
	protected boolean isAdjusted;
	protected JPopupMenu popup;

	public MapViewPane() {
		transformer = new CoordTransformer();
		transformer.setScreenResolution(Toolkit.getDefaultToolkit()
				.getScreenResolution()); // doesn't work...
		renderer = new DefaultEntityRenderer();
		eventListeners = new ArrayList<MapViewEventListener>();
		isAdjusted = false;
		popup = new MapViewPopup();
		MyMouseListener mouseListener = new MyMouseListener();
		addMouseListener(mouseListener);
		addMouseWheelListener(mouseListener);
		addKeyListener(new MyKeyListener());
		this.setFocusable(true);
	}

	public OsmMap getMap() {
		return map;
	}

	/** Sets the map as model of this pane and initiates painting. */
	public void setMap(OsmMap map) {
		if (this.map != null)
			this.map.removeMapDataEventListener(this);
		this.map = map;
		if (map != null) {
			map.addMapDataEventListener(this);
			isAdjusted = false;
		}
		fireMapViewEvent(new MapViewEvent(this, MapViewEvent.Type.MAP_NEW));
	}

	public AbstractEntityRenderer getRenderer() {
		return renderer;
	}

	/** Allows to replace the renderer. */
	public void setRenderer(AbstractEntityRenderer renderer) {
		this.renderer = renderer;
		repaint();
	}

	/** Returns the component responsible for coordinate transformation. */
	public CoordTransformer getTransformer() {
		return transformer;
	}

	/** Changes the pop-up menu shown for mouse-right. */
	public void setPopupMenu(JPopupMenu popup) {
		this.popup = popup;
	}

	/**
	 * Provides the true width of the screen to the transformer. This is
	 * necessary to get correct scale values.
	 * 
	 * @param cm
	 *            Screen width in centimeters.
	 */
	public void setScreenWidthInCentimeter(double cm) {
		double dotsPerCm = Toolkit.getDefaultToolkit().getScreenSize()
				.getWidth()
				/ cm;
		transformer.setScreenResolution((int) (dotsPerCm * 2.54));
	}

	/**
	 * Provides the true size of the screen to the transformer. This is
	 * necessary to get correct scale values.
	 * 
	 * @param inch
	 *            Screen size in inch.
	 */
	public void setScreenSizeInInch(double inch) {
		double width = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		double height = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		double dotsPerInch = Math.sqrt(width * width + height * height) / inch;
		transformer.setScreenResolution((int) dotsPerInch);
	}

	/**
	 * Multiples the current scale with the specified factor and adjusts the
	 * view so that the objects shown at the specified view focus keep at their
	 * position.
	 */
	public void zoom(float factor, int focusX, int focusY) {
		transformer.zoom(factor, focusX, focusY);
		repaint();
		fireMapViewEvent(new MapViewEvent(this, MapViewEvent.Type.ZOOM));
	}

	public void multiplyDisplayFactorWith(float fac) {
		renderer.setDisplayFactor(renderer.getDisplayFactor() * fac);
		repaint();
		fireMapViewEvent(new MapViewEvent(this, MapViewEvent.Type.ZOOM));
	}

	/**
	 * Adjusts the view.
	 * 
	 * @param dx
	 *            Number of pixels for horizontal shift.
	 * @param dy
	 *            Number of pixels for vertical shift.
	 */
	public void adjust(int dx, int dy) {
		transformer.adjust(dx, dy);
		repaint();
		fireMapViewEvent(new MapViewEvent(this, MapViewEvent.Type.ADJUST));
	}

	/**
	 * Initiates a reset of all coordinate transformation parameters.
	 */
	public void adjustToFit() {
		isAdjusted = false;
		repaint();
		fireMapViewEvent(new MapViewEvent(this, MapViewEvent.Type.ADJUST));
	}

	/**
	 * Puts a position given in world coordinates into the center of the view.
	 * 
	 * @param lat
	 *            Latitude
	 * @param lon
	 *            Longitude
	 */
	public void adjustToCenter(double lat, double lon) {
		int dx = getWidth() / 2 - transformer.x(lon);
		int dy = getHeight() / 2 - transformer.y(lat);
		adjust(dx, dy);
	}

	/**
	 * Returns the world coordinates, which are currently shown in the center.
	 */
	public Position getCenterPosition() {
		float lat = transformer.lat(getHeight() / 2);
		float lon = transformer.lon(getWidth() / 2);
		return new Position(lat, lon);
	}

	/** Returns a bounding box describing the currently visible area. */
	public BoundingBox getBoundingBox() {
		float latMin = transformer.lat(getHeight());
		float lonMin = transformer.lon(0);
		float latMax = transformer.lat(0);
		float lonMax = transformer.lon(getWidth());
		return new BoundingBox(latMin, lonMin, latMax, lonMax);
	}

	/**
	 * Removes the mark which is the nearest with respect to the given view
	 * coordinates.
	 */
	public void removeNearestMarker(int x, int y) {
		List<MapNode> marks = map.getMarkers();
		float lat = getTransformer().lat(y);
		float lon = getTransformer().lon(x);
		MapNode mark = new Position(lat, lon).selectNearest(marks, null);
		if (mark != null)
			marks.remove(mark);
		map.fireMapDataEvent(new MapEvent(map,
				MapEvent.Type.MAP_MODIFIED));
		fireMapViewEvent(new MapViewEvent(this,
				MapViewEvent.Type.TMP_NODES_REMOVED));
	}

	/**
	 * Finds the visible entity next to the specified view coordinates and shows
	 * informations about it.
	 * 
	 * @param debug
	 *            Enables a more detailed view.
	 */
	public void showMapEntityInfoDialog(MapEntity entity, boolean debug) {
		List<MapEntity> entities = new ArrayList<MapEntity>();
		if (entity.getName() != null || entity.getAttributes().length > 0
				|| debug)
			entities.add(entity);
		if (entity instanceof MapNode) {
			MapNode mNode = (MapNode) entity;
			for (WayRef ref : mNode.getWayRefs()) {
				MapEntity me = ref.getWay();
				if (me.getName() != null || me.getAttributes().length > 0
						|| debug)
					entities.add(me);
			}
		}
		boolean done = false;
		for (int i = 0; i < entities.size() && !done; i++) {
			MapEntity me = entities.get(i);
			Object[] content = new Object[] { "", "", "" };
			String text = (me.getName() != null) ? me.getName() : "";
			if (debug)
				text += " (" + ((me instanceof MapNode) ? "Node " : "Way ")
						+ me.getId() + ")";
			content[0] = text;
			if (me instanceof MapNode) {
				PositionPanel pos = new PositionPanel();
				pos.setPosition(((MapNode) me).getLat(), ((MapNode) me)
						.getLon());
				pos.setEnabled(false);
				content[1] = pos;
			}
			if (me.getAttributes().length > 0) {
				EntityAttribute[] atts = me.getAttributes();
				String[][] attTexts = new String[atts.length][2];
				for (int j = 0; j < atts.length; j++) {
					attTexts[j][0] = atts[j].getKey();
					attTexts[j][1] = atts[j].getValue();
				}
				JTable table = new JTable(attTexts, new String[] { "Name",
						"Value" });
				JScrollPane spane = new JScrollPane(table);
				spane.setPreferredSize(new Dimension(300, 150));
				content[2] = spane;
			}
			Object[] options;
			if (i < entities.size() - 1)
				options = new String[] { "OK", "Next" };
			else
				options = new String[] { "OK" };
			if (JOptionPane.showOptionDialog(this, content, "Map Entity Info",
					JOptionPane.DEFAULT_OPTION,
					JOptionPane.INFORMATION_MESSAGE, null, options, options[0]) != 1)
				done = true;
		}
	}

	/**
	 * Shows a graphical representation of the provided map data.
	 */
	public void paint(Graphics g) {
		Graphics2D g2 = (java.awt.Graphics2D) g;
		g2.setBackground(renderer.getBackgroundColor());
		g2.clearRect(0, 0, getWidth(), getHeight());
		if (getWidth() > 0 && map != null) {
			if (!isAdjusted) {
				transformer.adjustTransformation(map.getBoundingBox(),
						getWidth(), getHeight());
				isAdjusted = true;
			}
			float latMin = transformer.lat(getHeight());
			float lonMin = transformer.lon(0);
			float latMax = transformer.lat(0);
			float lonMax = transformer.lon(getWidth());
			float scale = transformer.computeScale();
			BoundingBox vbox = new BoundingBox(latMin, lonMin, latMax, lonMax);
			float viewScale = scale / renderer.getDisplayFactor();
			renderer.initForRendering(g2, transformer, map);
			map.visitEntities(renderer, vbox, viewScale);
			for (MapEntity entity : map.getVisibleMarkersAndTracks(viewScale))
				entity.accept(renderer);
			renderer.printBufferedObjects();
			if (renderer.isDebugModeEnabled()
					&& map instanceof DefaultMap) {
				List<double[]> splits = ((DefaultMap) map)
						.getEntityTree().getSplitCoords();
				g2.setColor(Color.LIGHT_GRAY);
				g2.setStroke(new BasicStroke(1f));
				for (double[] split : splits)
					g2.drawLine(renderer.transformer.x(split[1]),
							renderer.transformer.y(split[0]),
							renderer.transformer.x(split[3]),
							renderer.transformer.y(split[2]));
			}
		}
	}

	public void addMapViewEventListener(MapViewEventListener listener) {
		eventListeners.add(listener);
	}

	/**
	 * Informs all interested listener about view events such as mouse events
	 * and data changes.
	 */
	public void fireMapViewEvent(MapViewEvent e) {
		for (MapViewEventListener listener : eventListeners)
			listener.eventHappened(e);
	}

	/**
	 * Defines, how to react on model events (new, modifications).
	 */
	@Override
	public void eventHappened(MapEvent event) {
		if (event.getType() == MapEvent.Type.MAP_NEW) {
			adjustToFit();
			fireMapViewEvent(new MapViewEvent(this, MapViewEvent.Type.MAP_NEW));
		} else {
			repaint();
		}
	}

	// ////////////////////////////////////////////////////////////////////
	// some inner classes...

	/**
	 * Defines reactions on mouse events including navigation, mark setting and
	 * track creation.
	 * 
	 * @author R. Lunde
	 * 
	 */
	private class MyMouseListener implements MouseListener, MouseWheelListener {
		int xp;
		int yp;
		MapNode mark;

		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				float lat = transformer.lat(e.getY());
				float lon = transformer.lon(e.getX());
				if ((e.getModifiers() & MouseEvent.CTRL_MASK) != 0) {
					// mouse left button + ctrl -> add track point
					map.addToTrack("Mouse Track",
							new Position(lat, lon));
					fireMapViewEvent(new MapViewEvent(MapViewPane.this,
							MapViewEvent.Type.TRK_PT_ADDED));
				} else if ((e.getModifiers() & MouseEvent.SHIFT_MASK) != 0) {
					// mouse left button + shift -> add track point
					removeNearestMarker(e.getX(), e.getY());
				} else if (e.getClickCount() == 1) {
					// mouse left button -> add mark
					mark = map.addMarker(lat, lon);
					fireMapViewEvent(new MapViewEvent(MapViewPane.this,
							MapViewEvent.Type.MARKER_ADDED));
				} else { // double click
					map.removeMarker(mark);
					MapNode mNode = getRenderer().getNextNode(e.getX(), e.getY());
					if (mNode != null)
						showMapEntityInfoDialog(mNode, renderer.isDebugModeEnabled());
				}
			} else if (popup != null) {
				popup.show(MapViewPane.this, e.getX(), e.getY());
			} else {
				// mouse right button -> clear marks and tracks
				map.clearMarkersAndTracks();
				fireMapViewEvent(new MapViewEvent(MapViewPane.this,
						MapViewEvent.Type.TMP_NODES_REMOVED));
			}
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			MapViewPane.this.requestFocusInWindow();
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
		}

		@Override
		public void mousePressed(MouseEvent e) {
			xp = e.getX();
			yp = e.getY();
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				int xr = e.getX();
				int yr = e.getY();
				if (xr != xp || yr != yp) {
					// mouse drag -> adjust view
					adjust(xr - xp, yr - yp);
				} 
			}
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			int rot = e.getWheelRotation();
			int x = e.getX();
			int y = e.getY();
			float fac = ((e.getModifiers() & KeyEvent.SHIFT_MASK) != 0) ? 1.1f
					: 1.5f;
			if (rot == -1) {
				if ((e.getModifiers() & KeyEvent.ALT_MASK) != 0) {
					multiplyDisplayFactorWith(fac);
				} else
					zoom(fac, x, y);
			} else if (rot == 1) {
				if ((e.getModifiers() & KeyEvent.ALT_MASK) != 0) {
					multiplyDisplayFactorWith(1f / fac);
				} else
					zoom(1 / fac, x, y);
			}
		}
	}

	/**
	 * Enables map navigation (zooming, adjustment) with the keyboard.
	 * 
	 * @author R. Lunde
	 */
	private class MyKeyListener implements KeyListener {

		@Override
		public void keyPressed(KeyEvent e) {
			float zfac = ((e.getModifiers() & KeyEvent.SHIFT_MASK) != 0) ? 1.1f
					: 1.5f;
			float afac = ((e.getModifiers() & KeyEvent.SHIFT_MASK) != 0) ? 0.1f
					: 0.3f;
			switch (e.getKeyCode()) {
			case KeyEvent.VK_PLUS:
				if ((e.getModifiers() & KeyEvent.ALT_MASK) != 0)
					multiplyDisplayFactorWith(zfac);
				else
					zoom(zfac, getWidth() / 2, getHeight() / 2);
				break;
			case KeyEvent.VK_MINUS:
				if ((e.getModifiers() & KeyEvent.ALT_MASK) != 0)
					multiplyDisplayFactorWith(1f / zfac);
				else
					zoom(1 / zfac, getWidth() / 2, getHeight() / 2);
				break;
			case KeyEvent.VK_SPACE:
				if ((e.getModifiers() & KeyEvent.CTRL_MASK) == 0)
					zoom(zfac, getWidth() / 2, getHeight() / 2);
				else
					zoom(1 / zfac, getWidth() / 2, getHeight() / 2);
				break;
			case KeyEvent.VK_LEFT:
				adjust((int) (afac * getWidth()), 0);
				break;
			case KeyEvent.VK_RIGHT:
				adjust((int) (-afac * getWidth()), 0);
				break;
			case KeyEvent.VK_UP:
				adjust(0, (int) (afac * getHeight()));
				break;
			case KeyEvent.VK_DOWN:
				adjust(0, (int) (-afac * getHeight()));
				break;
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
		}

		@Override
		public void keyTyped(KeyEvent e) {
		}
	}
}
