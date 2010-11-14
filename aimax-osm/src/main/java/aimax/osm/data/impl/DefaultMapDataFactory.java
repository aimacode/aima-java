package aimax.osm.data.impl;

import aimax.osm.data.MapDataStorage;
import aimax.osm.data.MapDataFactory;
import aimax.osm.data.entities.MapNode;
import aimax.osm.data.entities.MapWay;
import aimax.osm.data.entities.Track;

/**
 * Default map data factory implementation, which is used, if
 * no other factory is specified by system property before the first
 * call of one of the factory methods.
 * @author Ruediger Lunde
 */
public class DefaultMapDataFactory extends MapDataFactory {
	
	/** {@inheritDoc} */
	public MapDataStorage createMapDataStorage() {
		return new DefaultMapDataStorage();
	}
	
	/** {@inheritDoc} */
	public MapNode createMapNode(long id) {
		return new DefaultMapNode(id);
	}
	
	/** {@inheritDoc} */
	public MapWay createMapWay(long id) {
		return new DefaultMapWay(id);
	}
	
	/** {@inheritDoc} */
	public Track createTrack(long id, String name, String trackType) {
		return new DefaultTrack(id, name, trackType);
	}
	
}
