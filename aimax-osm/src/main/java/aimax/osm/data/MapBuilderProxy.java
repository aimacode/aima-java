package aimax.osm.data;

import java.util.List;

import aimax.osm.data.entities.EntityAttribute;
import aimax.osm.data.entities.EntityViewInfo;
import aimax.osm.data.entities.MapNode;
import aimax.osm.data.entities.MapWay;

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
	public MapNode getNode(long id) {
		return builder.getNode(id);
	}

	@Override
	public void addNode(long id, String name, List<EntityAttribute> atts,
			float lat, float lon) {
		builder.addNode(id, name, atts, lat, lon);
	}

	@Override
	public MapWay getWay(long id) {
		return builder.getWay(id);
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