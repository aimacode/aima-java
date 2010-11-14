package aimax.osm.data;

import java.util.List;

import aimax.osm.data.entities.MapNode;
import aimax.osm.data.entities.MapWay;
/**
 * Simple interface to aggregate map data. Its purpose is, to keep OSM parsers
 * independent from the concrete structure which is used to store the data.
 * To be able to read only parts of a given map (e.g. defined by a bounding
 * box or an entity filter), it is important, that ways can be processed before
 * everything about the nodes defining the way is known. Hence, implementations
 * should allow to add nodes without position and accept later refinement.
 * @author Ruediger Lunde
 */
public interface MapContentBuilder {
	
	/** Must be called before adding new map data. */
	public void prepareForNewData();
	
	/** Defines the region in which complete the map data is available. */
	public void setBoundingBox(BoundingBox bb);
	
	/**
	 * Adds a new map node (way node as well as point of interest)
	 * to the container. When calling this method from outside, latitude
	 * and longitude values should be available. Before, it should be
	 * checked, whether a node with the same id already exists.
	 */
	public void addNode(MapNode node);

	/**
	 * Returns a node for the given id.
	 */
	public MapNode getNode(long id);
	
	/**
	 * Adds a new map way to the container, stores a list of way nodes
	 * describing the way if they have not been added before, and
	 * additionally adds a way reference to each node. The way nodes define
	 * the representation of the way at zoom level 0). The positions of the
	 * nodes may be set later on. It should be checked before, whether a node
	 * with the same id already exists.
	 */
	public void addWay(MapWay way, List<MapNode> wayNodes);
	
	/**
	 * Returns a way for the given id.
	 */
	public MapWay getWay(long id);
	
	/**
	 * Checks whether nodes without position (latitude/longitude)
	 * have been added since the last call.
	 */
	public boolean nodesWithoutPositionAdded();
	
	/** Must be called when all map entities have been added. */
	public void compileResults();
}
