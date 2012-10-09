package aimax.osm.data;

import java.util.List;

import aimax.osm.data.entities.EntityAttribute;
import aimax.osm.data.entities.EntityViewInfo;

/**
 * This map builder proxy delegates every call to the original builder.
 * 
 * @author Ruediger Lunde
 */
public class MapBuilderProxy implements MapBuilder {
	protected MapBuilder builder;
	protected int counter;

	public MapBuilderProxy(MapBuilder builder) {
		this.builder = builder;
		counter = 0;
	}

	public void incrementCounter() {
		counter++;
	}

	@Override
	public void setEntityClassifier(EntityClassifier<EntityViewInfo> classifier) {
		builder.setEntityClassifier(classifier);
	}

	@Override
	public void setBoundingBox(BoundingBox bb) {
		builder.setBoundingBox(bb);
	}

	@Override
	public boolean isNodeDefined(long id, BoundingBox bb) {
		return builder.isNodeDefined(id, bb);
	}
	
	@Override
	public boolean isNodeReferenced(long id) {
		return builder.isNodeReferenced(id);
	}

	@Override
	public void addNode(long id, String name, List<EntityAttribute> atts,
			float lat, float lon) {
		builder.addNode(id, name, atts, lat, lon);
	}

	@Override
	public boolean isWayDefined(long id) {
		return builder.isWayDefined(id);
	}

	@Override
	public void addWay(long id, String name, List<EntityAttribute> atts,
			List<Long> wayNodeIds) {
		builder.addWay(id, name, atts, wayNodeIds);
	}

	@Override
	public boolean nodeRefsWithoutDefsAdded() {
		return builder.nodeRefsWithoutDefsAdded();
	}

	@Override
	public OsmMap buildMap() {
		return builder.buildMap();
	}
}