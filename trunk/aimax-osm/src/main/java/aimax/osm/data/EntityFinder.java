package aimax.osm.data;

import java.util.List;

import aimax.osm.data.entities.MapEntity;
import aimax.osm.data.entities.MapNode;

public interface EntityFinder {
	void setMinRadius(int km);
	void setMaxRadius(int km);
	void findEntity(String pattern, Position pos);
	void findNode(String pattern, Position pos);
	void findWay(String pattern, Position pos, MapWayFilter filter);
	void findAddress(String address, Position pos);
	
	void findMore();
	
	Position getRefPosition();
	
	List<MapEntity> getIntermediateResults();
	void selectIntermediateResult(MapNode entity);
	
	List<MapEntity> getResults();
}
