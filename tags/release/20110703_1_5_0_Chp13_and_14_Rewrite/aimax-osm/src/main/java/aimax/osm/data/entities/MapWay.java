package aimax.osm.data.entities;

import java.util.List;

import aimax.osm.data.BoundingBox;

/**
 * Represents a way within a map. Ways are defined by lists of nodes.
 * A special dynamic attribute called "oneway" marks ways, which can only
 * be traveled in ascending node index order.
 * 
 * @author Ruediger Lunde 
 */
public interface MapWay extends MapEntity {
	/** Indicates that the way can only be traveled in one direction. */
	public boolean isOneway();
	/** Indicates, that the way represents an area. */
	public boolean isArea();
	
	/**
	 * Returns an unmodifiable list of nodes describing the way. This
	 * representation should always be complete - no abstraction from
	 * original data with respect to some zooming level.
	 */
	public List<MapNode> getNodes();
	
	/**
	 * Computes the smallest box containing all way nodes.
	 */
	public BoundingBox computeBoundingBox();
	
	/**
	 * Returns the sum of the side lengths of the bounding box.
	 * A fast size measure is needed by the renderer for area sorting.
	 */
	public float getBoundingBoxSize();
}
