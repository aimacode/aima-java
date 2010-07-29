package aimax.osm.data;

import aimax.osm.data.entities.MapNode;
import aimax.osm.data.entities.MapWay;
/**
 * Simple interface to aggregate map data. Its purpose is, to keep OSM parsers
 * independent from the concrete structure which is used to store the data.
 * @author Ruediger Lunde
 */
public interface MapDataConsumer {
	
	/** Should remove all previously added data. */
	public void clearAll();
	
	/** Should add a node (way node or poi) to the map representation. */
	public void addNode(MapNode node);
	
	/** Should add a way to the map representation. */
	public void addWay(MapWay way);
	
	/**
	 * Should return the node with the specified id, provided that the node
	 * has been added before; otherwise null.
	 */
	public MapNode getWayNode(long id);
	
	/** Should be called after all relevant nodes have been added. */
	public void compileData();
	
}
