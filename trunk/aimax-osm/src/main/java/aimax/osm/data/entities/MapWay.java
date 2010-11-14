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
	
	public boolean isOneway();
	
	public boolean isArea();
	
	/**
	 * Returns an unmodifiable list of nodes describing the way.
	 */
	public List<MapNode> getNodes();
	
	/**
	 * Computes the smallest box containing all way nodes (zoom level 0).
	 */
	public BoundingBox computeBoundingBox();
	
	/**
	 * Returns the sum of the side lengths of the bounding box.
	 * A fast size measure is needed by the renderer for area sorting.
	 */
	public float getBoundingBoxSize();
}
