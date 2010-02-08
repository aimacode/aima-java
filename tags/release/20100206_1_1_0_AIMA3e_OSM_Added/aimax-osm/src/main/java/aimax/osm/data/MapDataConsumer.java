package aimax.osm.data;

import aimax.osm.data.entities.MapNode;
import aimax.osm.data.entities.MapWay;
/**
 * Simple interface to aggregate map data. Its purpose is, to keep osm parsers
 * independent from the concrete structure which is used to store the data.
 * @author R. Lunde
 */
public interface MapDataConsumer {
	public void addNode(MapNode node);
	
	public void addWay(MapWay way);
	
	public MapNode getWayNode(long id);
	
}
