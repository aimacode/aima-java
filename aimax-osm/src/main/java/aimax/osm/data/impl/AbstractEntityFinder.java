package aimax.osm.data.impl;

import java.util.ArrayList;
import java.util.List;

import aimax.osm.data.EntityFinder;
import aimax.osm.data.MapDataStorage;
import aimax.osm.data.MapWayFilter;
import aimax.osm.data.Position;
import aimax.osm.data.entities.MapEntity;
import aimax.osm.data.entities.MapNode;
import aimax.osm.data.entities.MapWay;

public abstract class AbstractEntityFinder implements EntityFinder {

	protected static enum Mode {
		ENTITY, NODE, WAY, ADDRESS
	}

	private final MapDataStorage storage;

	protected Mode mode;
	protected String pattern;
	protected Position position;
	protected MapWayFilter wayFilter;
	private int minRadius;
	private int maxRadius;

	private List<MapEntity> intermediateResults;
	private List<MapEntity> results;

	public AbstractEntityFinder(MapDataStorage storage) {
		this.storage = storage;
		minRadius = 2;
		maxRadius = 100;
		intermediateResults = new ArrayList<MapEntity>();
		results = new ArrayList<MapEntity>();
	}
	
	@Override
	public void setMinRadius(int km) {
		minRadius = km;
	}
	
	@Override
	public void setMaxRadius(int km) {
		maxRadius = km;
	}
	
	@Override
	public void findEntity(String pattern, Position pos) {
		mode = Mode.ENTITY;
		this.pattern = pattern;
		position = pos;
		wayFilter = null;
		clearResults();
		find(false);
	}

	@Override
	public void findNode(String pattern, Position pos) {
		mode = Mode.NODE;
		this.pattern = pattern;
		position = pos;
		clearResults();
		find(false);
	}

	@Override
	public void findWay(String pattern, Position pos, MapWayFilter filter) {
		mode = Mode.WAY;
		this.pattern = pattern;
		position = pos;
		wayFilter = filter;
		clearResults();
		find(false);
	}

	@Override
	public void findAddress(String pattern, Position pos) {
		mode = Mode.ADDRESS;
		this.pattern = pattern;
		position = pos;
		wayFilter = null;
		clearResults();
		find(false);
	}

	@Override
	public void findMore() {
		find(true);
	}

	protected abstract void find(boolean findMore);

	public Position getRefPosition() {
		return position;
	}
	
	@Override
	public List<MapEntity> getIntermediateResults() {
		return intermediateResults;
	}
	
	@Override
	public List<MapEntity> getResults() {
		return results;
	}

	@Override
	public void selectIntermediateResult(MapNode node) {
		intermediateResults.clear();
		intermediateResults.add(node);
	}

	private void clearResults() {
		intermediateResults.clear();
		results.clear();
	}
	
	protected MapDataStorage getStorage() {
		return storage;
	}
	
	protected int getMinRadius() {
		return minRadius;
	}

	protected int getMaxRadius() {
		return maxRadius;
	}
}
