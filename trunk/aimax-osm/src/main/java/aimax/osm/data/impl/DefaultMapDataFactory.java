package aimax.osm.data.impl;

import aimax.osm.data.MapDataStorage;
import aimax.osm.data.MapDataFactory;
import aimax.osm.data.entities.MapNode;
import aimax.osm.data.entities.MapWay;
import aimax.osm.data.entities.Track;

public class DefaultMapDataFactory extends MapDataFactory {
	public MapDataStorage createMapDataStorage() {
		return new DefaultMapDataStorage();
	}
	
	public MapNode createMapNode(long id) {
		return new DefaultMapNode(id);
	}
	
	public MapWay createMapWay(long id) {
		return new DefaultMapWay(id);
	}
	
	public Track createTrack(long id, String name, String trackType) {
		return new DefaultTrack(id, name, trackType);
	}
	
}
