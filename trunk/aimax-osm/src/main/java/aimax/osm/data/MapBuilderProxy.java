package aimax.osm.data;

import java.util.List;

import aimax.osm.data.entities.MapNode;
import aimax.osm.data.entities.MapWay;

/**
 * This map builder proxy delegates every call to the original builder.
 * @author Ruediger Lunde
 */
public class MapBuilderProxy implements MapBuilder {
	protected MapBuilder builder;
	protected int counter;
	
	public MapBuilderProxy(MapBuilder consumer) {
		this.builder = consumer;
		counter = 0;
	}
	
	public void incrementCounter() {
		counter++;
	}
	
	@Override
	public void prepareForNewData() {
		builder.prepareForNewData();
	}
	@Override
	public void setBoundingBox(BoundingBox bb) {
		builder.setBoundingBox(bb);
	}
	@Override
	public void addNode(MapNode node) {
		builder.addNode(node);
	}
	@Override
	public void addWay(MapWay way, List<MapNode> wayNodes) {
		builder.addWay(way, wayNodes);
	}
	@Override
	public MapNode getNode(long id) {
		return builder.getNode(id);
	}
	@Override
	public MapWay getWay(long id) {
		return builder.getWay(id);
	}
	@Override
	public boolean nodesWithoutPositionAdded() {
		return builder.nodesWithoutPositionAdded();
	}
	@Override
	public void compileResults() {
		builder.compileResults();
	}
}