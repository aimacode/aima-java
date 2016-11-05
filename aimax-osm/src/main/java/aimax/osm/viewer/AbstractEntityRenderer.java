package aimax.osm.viewer;

import aimax.osm.data.EntityVisitor;
import aimax.osm.data.WayNodeProvider;
import aimax.osm.data.entities.MapNode;
import aimax.osm.data.entities.MapWay;
import aimax.osm.data.entities.Track;

/**
 * Provides a base for defining renderers for the OSM viewer. A renderer knows
 * how to display entities and how to transform world into view coordinates. If
 * the order in which the entities are provided shall not be identical to the
 * order of drawing, the renderer should use entity buffers and sort them before
 * printing.
 * 
 * @author Ruediger Lunde
 * 
 */
public abstract class AbstractEntityRenderer implements EntityVisitor {
	protected UnifiedImageBuilder<?> imageBdr;
	/** Is responsible for world to view coordinate transformations. */
	protected CoordTransformer transformer;
	protected WayNodeProvider wnProvider;
	private UColor backgroundColor = UColor.WHITE;
	protected boolean debugMode;
	/**
	 * Controls the size of symbols, line widths, and texts. Value two doubles
	 * the original size.
	 */
	protected float displayFactor = 1.0f;

	public CoordTransformer getTransformer() {
		return transformer;
	}
	
	public UColor getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(UColor color) {
		backgroundColor = color;
	}

	public void enableDebugMode(boolean state) {
		debugMode = state;
	}

	public boolean isDebugModeEnabled() {
		return debugMode;
	}

	public float getDisplayFactor() {
		return displayFactor;
	}

	public void setDisplayFactor(float factor) {
		displayFactor = factor;
	}

	/** Resets the renderer. */
	public void initForRendering(UnifiedImageBuilder<?> imageBdr, CoordTransformer transformer,
			WayNodeProvider wnProvider) {
		this.imageBdr = imageBdr;
		this.transformer = transformer;
		this.wnProvider = wnProvider;
		imageBdr.setColor(backgroundColor);
		imageBdr.setAreaFilled(true);
		imageBdr.drawRect(0, 0, imageBdr.getWidth(), imageBdr.getHeight());
	}

	/**
	 * Abstract method, returns a visible map entity in the vicinity of the
	 * specified view coordinates.
	 */
	public abstract MapNode getNextNode(int x, int y);

	/** Abstract method, responsible for completing the rendering task. */
	public abstract void printBufferedObjects();

	/** Abstract method, responsible for rendering a map node (especially POIs). */
	@Override
	public abstract void visitMapNode(MapNode node);

	/** Abstract method, responsible for rendering a map way. */
	@Override
	public abstract void visitMapWay(MapWay way);

	/** Abstract method, responsible for rendering a track. */
	@Override
	public abstract void visitTrack(Track track);
}
