package aimax.osm.gui.swing.viewer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
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
import aimax.osm.viewer.*;

/**
 * Provides a panel which visualizes map data. As model, an
 * {@link OsmMap} is used. The panel reacts on user events
 * and visualizes currently visible map entities as image. For details about
 * image creation see method {@link #updateOffScreenImage()}.
 * <p>
 * Hint for using the viewer: Try Mouse-Left, Mouse-Right, Mouse-Drag,
 * Ctrl-Mouse-Left, Plus, Minus, Ctrl-Plus, Ctrl-Minus, arrow buttons, and also
 * the Mouse-Wheel for navigation, marker placement, and track definition.
 *
 * @author Ruediger Lunde
 */
public class MapViewPane extends JComponent implements MapEventListener {

	private static final long serialVersionUID = 1L;
	// private Logger LOG = Logger.getLogger("aimax.osm");

	public static final String FUNCTION_DESCRIPTION = ""
			+ "Zoom: Mouse-Wheel; Buttons Plus, Minus, Space, Ctrl-Space"
			+ "|Adjust symbol size: Ctrl-Mouse-Wheel; Buttons Ctrl+Plus, Ctrl-Minus"
			+ "|Pan: Mouse-Drag; Buttons Up, Down, Left, Right"
			+ "|Reduce step size during zoom, adjust, pan: Additionally Shift"
			+ "|Set a marker: Mouse-Left"
			+ "|Remove a marker: Shift-Mouse-Left"
			+ "|Add a node to a path: Ctrl-Mouse-Left"
			+ "|Open a context menu: Mouse-Right";

	private UnifiedMapDrawer<Image> imageUpdater;
	private ArrayList<MapViewEventListener> eventListeners;
	protected boolean isAdjusted;
	protected JPopupMenu popup;
	/** Off-screen image. */
	private Image image;
	private boolean isImageUpToDate;

	public MapViewPane() {
		imageUpdater = new UnifiedMapDrawer<Image>(new AWTImageBuilder());
		imageUpdater.getTransformer().setScreenResolution(Toolkit.getDefaultToolkit()
				.getScreenResolution()); // doesn't work...
		eventListeners = new ArrayList<MapViewEventListener>();
		isAdjusted = false;
		popup = new MapViewPopup();
		MyMouseListener mouseListener = new MyMouseListener();
		addMouseListener(mouseListener);
		addMouseWheelListener(mouseListener);
		addMouseMotionListener(mouseListener);
		addKeyListener(new MyKeyListener());
		this.setFocusable(true);
	}

	public OsmMap getMap() {
		return imageUpdater.getMap();
	}

	/** Sets the map as model of this pane and initiates painting. */
	public void setMap(OsmMap map) {
		OsmMap oldMap = imageUpdater.getMap();
		if (oldMap != null)
			oldMap.removeMapDataEventListener(this);
		imageUpdater.setMap(map);
		if (map != null) {
			map.addMapDataEventListener(this);
			isAdjusted = false;
		}
		viewChanged(MapViewEvent.Type.NEW_MAP);
	}

	public AbstractEntityRenderer getRenderer() {
		return imageUpdater.getRenderer();
	}

	/** Allows to replace the renderer. */
	public void setRenderer(AbstractEntityRenderer renderer) {
		imageUpdater.setRenderer(renderer);
		viewChanged(MapViewEvent.Type.NEW_RENDERER);
	}

	/** Controls whether kd-tree informations, node identifiers etc. are shown. */
	public void enableDebugMode(boolean b) {
		getRenderer().enableDebugMode(b);
		viewChanged(MapViewEvent.Type.NEW_RENDERER);
	}

	public boolean isDebugModeEnabled() {
		return getRenderer().isDebugModeEnabled();
	}

	/** Returns the component responsible for coordinate transformation. */
	public CoordTransformer getTransformer() {
		return imageUpdater.getTransformer();
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
		getTransformer().setScreenResolution((int) (dotsPerCm * 2.54));
		viewChanged(MapViewEvent.Type.ZOOM);
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
		getTransformer().setScreenResolution((int) dotsPerInch);
		viewChanged(MapViewEvent.Type.ZOOM);
	}

	/**
	 * Multiples the current scale with the specified factor and adjusts the
	 * view so that the objects shown at the specified view focus keep at their
	 * position.
	 */
	public void zoom(float factor, int focusX, int focusY) {
		getTransformer().zoom(factor, focusX, focusY);
		paintPreview((int) ((1 - factor) * focusX),
				(int) ((1 - factor) * focusY), factor);
		viewChanged(MapViewEvent.Type.ZOOM);
	}

	public void multiplyDisplayFactorWith(float fac) {
		getRenderer().setDisplayFactor(getRenderer().getDisplayFactor() * fac);
		viewChanged(MapViewEvent.Type.ZOOM);
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
		getTransformer().adjust(dx, dy);
		paintPreview(dx, dy, 1f);
		viewChanged(MapViewEvent.Type.ADJUST);
	}

	/**
	 * Initiates a reset of all coordinate transformation parameters.
	 */
	public void adjustToFit() {
		isAdjusted = false;
		viewChanged(MapViewEvent.Type.ADJUST);
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
		int dx = getWidth() / 2 - getTransformer().x(lon);
		int dy = getHeight() / 2 - getTransformer().y(lat);
		adjust(dx, dy);
	}

	private void viewChanged(MapViewEvent.Type eventType) {
		isImageUpToDate = false;
		repaint();
		fireMapViewEvent(new MapViewEvent(this, eventType));
	}

	/**
	 * Returns the world coordinates, which are currently shown in the center.
	 */
	public Position getCenterPosition() {
		float lat = getTransformer().lat(getHeight() / 2);
		float lon = getTransformer().lon(getWidth() / 2);
		return new Position(lat, lon);
	}

	/** Returns a bounding box describing the currently visible area. */
	public BoundingBox getBoundingBox() {
		float latMin = getTransformer().lat(getHeight());
		float lonMin = getTransformer().lon(0);
		float latMax = getTransformer().lat(0);
		float lonMax = getTransformer().lon(getWidth());
		return new BoundingBox(latMin, lonMin, latMax, lonMax);
	}

	/**
	 * Removes the mark which is the nearest with respect to the given view
	 * coordinates.
	 */
	public void removeNearestMarker(int x, int y) {
		List<MapNode> markers = getMap().getMarkers();
		float lat = getTransformer().lat(y);
		float lon = getTransformer().lon(x);
		MapNode marker = new Position(lat, lon).selectNearest(markers, null);
		if (marker != null)
			markers.remove(marker);
		getMap().fireMapDataEvent(new MapEvent(getMap(), MapEvent.Type.MAP_MODIFIED));
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
				pos.setPosition(((MapNode) me).getLat(),
						((MapNode) me).getLon());
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
	public void paintComponent(Graphics g) {
		if (image == null || image.getWidth(null) != getWidth()
				|| image.getHeight(null) != getHeight()) {
			isImageUpToDate = false;
		}
		if (!isImageUpToDate)
			updateOffScreenImage();
		g.drawImage(image, 0, 0, this);
	}

	/**
	 * Creates a new image for the currently visible part of the map and updates
	 * attribute {@link #image}.
	 */
	protected void updateOffScreenImage() {
		Image image = createImage(getWidth(), getHeight());
		imageUpdater.drawMap(image, !isAdjusted);
		this.image = image;
		if (getWidth() > 0 && getMap() != null)
			isAdjusted = true;
		isImageUpToDate = true;
	}

	/**
	 * Draws the off-screen image if exists at position (dx, dy) scaled by the
	 * specified factor.
	 */
	private void paintPreview(int dx, int dy, float zoomfactor) {
		if (image != null) {
			Graphics2D g2 = (Graphics2D) getGraphics();
			UColor bg = getRenderer().getBackgroundColor();
			g2.setBackground(new Color(bg.getRed(), bg.getGreen(),
					bg.getBlue(), bg.getAlpha()));
			int newWidth = Math.round(image.getWidth(null) * zoomfactor);
			int newHeight = (int) Math
					.round(image.getHeight(null) * zoomfactor);
			g2.drawImage(image, dx, dy, newWidth, newHeight, null);
			if (dx > 0)
				g2.clearRect(0, 0, dx, getHeight());
			else
				g2.clearRect(getWidth() + dx, 0, getWidth(), getHeight());
			if (dy > 0)
				g2.clearRect(0, 0, getWidth(), dy);
			else
				g2.clearRect(0, getHeight() + dy, getWidth(), getHeight());
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
			fireMapViewEvent(new MapViewEvent(this, MapViewEvent.Type.NEW_MAP));
		} else {
			isImageUpToDate = false;
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
	private class MyMouseListener extends MouseAdapter {
		int xp;
		int yp;
		MapNode marker;

		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				float lat = getTransformer().lat(e.getY());
				float lon = getTransformer().lon(e.getX());
				if ((e.getModifiers() & MouseEvent.CTRL_MASK) != 0) {
					// mouse left button + ctrl -> add track point
					getMap().addToTrack("Mouse Track", new Position(lat, lon));
				} else if ((e.getModifiers() & MouseEvent.SHIFT_MASK) != 0) {
					// mouse left button + shift -> add track point
					removeNearestMarker(e.getX(), e.getY());
				} else if (e.getClickCount() == 1) {
					// mouse left button -> add marker
					marker = getMap().addMarker(lat, lon);
					fireMapViewEvent(new MapViewEvent(this,
							MapViewEvent.Type.MARKER_ADDED));
				} else { // double click
					getMap().removeMarker(marker);
					MapNode mNode = getRenderer().getNextNode(e.getX(), e.getY());
					if (mNode != null)
						showMapEntityInfoDialog(mNode,
								getRenderer().isDebugModeEnabled());
				}
			} else if (popup != null) {
				popup.show(MapViewPane.this, e.getX(), e.getY());
			} else {
				// mouse right button -> clear markers and tracks
				getMap().clearMarkersAndTracks();
			}
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			MapViewPane.this.requestFocusInWindow();
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
				if ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0) {
					multiplyDisplayFactorWith(fac);
				} else {
					zoom(fac, x, y);
				}
			} else if (rot == 1) {
				if ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0) {
					multiplyDisplayFactorWith(1f / fac);
				} else {
					zoom(1 / fac, x, y);
				}
			}
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			paintPreview(e.getX() - xp, e.getY() - yp, 1);
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
					if ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)
						multiplyDisplayFactorWith(zfac);
					else
						zoom(zfac, getWidth() / 2, getHeight() / 2);
					break;
				case KeyEvent.VK_MINUS:
					if ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)
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
