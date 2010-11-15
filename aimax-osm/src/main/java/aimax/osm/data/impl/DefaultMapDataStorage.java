package aimax.osm.data.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Logger;

import aimax.osm.data.BoundingBox;
import aimax.osm.data.EntityAttributeManager;
import aimax.osm.data.EntityClassifier;
import aimax.osm.data.EntityFinder;
import aimax.osm.data.EntityVisitor;
import aimax.osm.data.MapContentBuilder;
import aimax.osm.data.MapDataEvent;
import aimax.osm.data.MapDataEventListener;
import aimax.osm.data.MapDataFactory;
import aimax.osm.data.MapDataStorage;
import aimax.osm.data.MapWayFilter;
import aimax.osm.data.Position;
import aimax.osm.data.entities.EntityAttribute;
import aimax.osm.data.entities.EntityViewInfo;
import aimax.osm.data.entities.MapEntity;
import aimax.osm.data.entities.MapNode;
import aimax.osm.data.entities.MapWay;
import aimax.osm.data.entities.Track;

/**
 * Central container for OSM map data. It is responsible for storing loaded map
 * data and also for data preparation to support efficient routing and map
 * viewing. All map data is kept in RAM in this implementation.
 * Data preparation is based on two fundamental tree structures:
 * 
 * <p>
 * The first is an entity classifier. It is used to attach viewing information
 * to map entities based on attribute value checks. Renderers can store whatever
 * they need in those view information objects. However, the details are not
 * visible to this layer. The data storage only sees the minimal scale in which
 * the entity shall be visible.
 * </p>
 * 
 * <p>
 * The second is a kd-tree (see {@link aimax.osm.data.impl.KDTree}).
 * </p>
 * 
 * <p>
 * The map data storage is used as model for the viewer.
 * </p>
 * 
 * @author Ruediger Lunde
 */
public class DefaultMapDataStorage implements MapDataStorage, MapContentBuilder {
	private static Logger LOG = Logger.getLogger("aimax.osm");
	private BoundingBox boundingBox;
	/**
	 * Maintains all map nodes during map loading; after compilation only the
	 * way nodes remain. IDs are used as keys.
	 */
	private Hashtable<Long, MapNode> nodes;
	/** Maintains all way nodes. IDs are used as keys. */
	private Hashtable<Long, MapWay> ways;
	/**
	 * Maintains after compilation all nodes which have a name or at least one
	 * attribute.
	 */
	private ArrayList<MapNode> pois;
	private boolean nodeWithoutPositionAdded;
	/** Maintains marks (not part of the original map). */
	private ArrayList<MapNode> marks;
	/** Maintains tracks (not part of the original map). */
	private ArrayList<Track> tracks;
	private long nextTrackId;

	private EntityClassifier<EntityViewInfo> entityClassifier;
	private KDTree entityTree;

	private ArrayList<MapDataEventListener> listeners;

	public DefaultMapDataStorage() {
		nodes = new Hashtable<Long, MapNode>();
		ways = new Hashtable<Long, MapWay>();
		pois = new ArrayList<MapNode>();
		marks = new ArrayList<MapNode>();
		tracks = new ArrayList<Track>();
		listeners = new ArrayList<MapDataEventListener>();
		// EntityAttributeManager.instance().ignoreAttKeys(new String[]{
		// "created_by", "source", "history", "copyright", "fire_hydrant"},
		// true);
		EntityAttributeManager.instance()
				.ignoreAttKeys(
						new String[] { "created_by", "source", "history",
								"copyright" }, false);
	}

	/** {@inheritDoc} */
	@Override
	public boolean isEmpty() {
		return nodes.isEmpty() && ways.isEmpty() && pois.isEmpty()
				&& marks.isEmpty() && tracks.isEmpty();
	}

	/** {@inheritDoc} */
	@Override
	public MapContentBuilder getContentBuilder() {
		return this;
	}

	/** Does nothing */
	@Override
	public void close() {
	}

	// ///////////////////////////////////////////////////////////////
	// MapContentBuilder interface

	/** {@inheritDoc} No data available after this reset. */
	@Override
	public void prepareForNewData() {
		EntityAttributeManager.instance().clearHash();
		nodes.clear();
		ways.clear();
		pois.clear();
		marks.clear();
		tracks.clear();
		entityTree = null;
		boundingBox = null;
		nodeWithoutPositionAdded = false;
		fireMapDataEvent(new MapDataEvent(this, MapDataEvent.Type.MAP_CLEARED));
	}

	/** {@inheritDoc} */
	@Override
	public void setBoundingBox(BoundingBox bb) {
		boundingBox = bb;
	}

	/** {@inheritDoc} */
	@Override
	public void addNode(MapNode node) {
		if (!node.hasPosition())
			nodeWithoutPositionAdded = true;
		nodes.put(node.getId(), node);
		if (nodes.size() % 500000 == 0)
			LOG.fine("Nodes: " + nodes.size());
	}

	/**
	 * {@inheritDoc} <code>DefaultMapWay</code> and <code>DefaultMapNode</code>
	 * instances are expected. Ways with less than two way nodes are ignored.
	 */
	@Override
	public void addWay(MapWay way, List<MapNode> wayNodes) {
		if (wayNodes.size() > 1) {
			List<MapNode> newWayNodes = new ArrayList<MapNode>(wayNodes.size());
			int i = 0;
			for (MapNode node : wayNodes) {
				MapNode existingNode = getNode(node.getId());
				if (existingNode == null)
					addNode(node);
				else if (existingNode != node)
					// node occurs twice in the way - use same representation!
					node = existingNode;
				node.addWayRef(way, i++);
				newWayNodes.add(node);
			}
			((DefaultMapWay) way).setNodes(newWayNodes);
			ways.put(way.getId(), way);
			if (ways.size() % 50000 == 0)
				LOG.fine("Ways: " + ways.size());
		}
	}

	/** {@inheritDoc} */
	@Override
	public boolean nodesWithoutPositionAdded() {
		boolean result = nodeWithoutPositionAdded;
		nodeWithoutPositionAdded = false;
		return result;
	}

	/**
	 * Separates way nodes from points of interests, cleans up useless garbage
	 * and creates a kd-tree for the remaining entities. Always call this method
	 * before using using the container for viewing.
	 */
	@Override
	public void compileResults() {
		ArrayList<Long> toDelete = new ArrayList<Long>();
		for (MapNode node : nodes.values()) {
			if (node.getName() != null || node.getAttributes().length > 0)
				pois.add(node);
			else if (node.getWayRefs().isEmpty())
				toDelete.add(node.getId());
		}
		for (long id : toDelete) {
			nodes.remove(id);
		}
		LOG.fine("Ways: " + getWayCount());
		LOG.fine("Nodes: " + getNodeCount());
		LOG.fine("POIs: " + getPoiCount());

		BoundingBox bbAllNodes = new BoundingBox();
		bbAllNodes.adjust(nodes.values());
		bbAllNodes.adjust(pois);
		if (boundingBox == null)
			boundingBox = bbAllNodes;
		else
			boundingBox.intersectWith(bbAllNodes);
		applyClassifierAndUpdateTree(bbAllNodes);
		fireMapDataEvent(new MapDataEvent(this, MapDataEvent.Type.MAP_NEW));
	}

	// ///////////////////////////////////////////////////////////////
	// MapDataStorage interface and other methods

	/**
	 * Provides the data store with an entity classifier. The classifier
	 * strongly influences the generation of the entity tree.
	 */
	@Override
	public void setEntityClassifier(EntityClassifier<EntityViewInfo> classifier) {
		entityClassifier = classifier;
		if (entityTree != null) {
			applyClassifierAndUpdateTree(entityTree.getBoundingBox());
			fireMapDataEvent(new MapDataEvent(this,
					MapDataEvent.Type.MAP_MODIFIED));
		}
	}

	/**
	 * Applies the current entity classifier to all currently maintained map
	 * entities and creates a new entity tree with all relevant ways and points
	 * of interest.
	 */
	protected void applyClassifierAndUpdateTree(BoundingBox bb) {
		entityTree = new KDTree(bb, 8000, 60);
		for (MapWay way : ways.values())
			updateEntityViewInfo(way, true);
		for (MapNode poi : pois)
			updateEntityViewInfo(poi, true);
		for (MapNode mark : marks)
			updateEntityViewInfo(mark, false);
		for (Track track : tracks)
			updateEntityViewInfo(track, false);
	}

	/**
	 * Updates the view information of a given entity by means of the current
	 * entity classifier. If suitable viewing information was found and
	 * <code>addToTree</code> is true, the entity is added to the entity tree.
	 */
	private void updateEntityViewInfo(MapEntity entity, boolean addToTree) {
		EntityViewInfo info = null;
		if (entityClassifier != null)
			info = entityClassifier.classify(entity);
		entity.setViewInfo(info);
		if (addToTree && info != null)
			entityTree.insertEntity((DefaultMapEntity) entity);
	}

	/** {@inheritDoc} */
	@Override
	public BoundingBox getBoundingBox() {
		return boundingBox;
	}

	/** {@inheritDoc} */
	@Override
	public int getNodeCount() {
		return nodes.size();
	}

	/** {@inheritDoc} */
	@Override
	public MapNode getNode(long id) {
		return nodes.get(id);
	}

	/** {@inheritDoc} */
	@Override
	public int getWayCount() {
		return ways.size();
	}

	/** {@inheritDoc} */
	@Override
	public MapWay getWay(long id) {
		return ways.get(id);
	}

	/**
	 * Returns all map ways which intersect the specified bounding box.
	 */
	@Override
	public Collection<MapWay> getWays(BoundingBox bb) {
		// not really efficient but OK for small maps...
		List<MapWay> result = new ArrayList<MapWay>();
		for (MapWay way : ways.values())
			if (way.computeBoundingBox().intersectsWith(bb))
				result.add(way);
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public MapNode getNearestWayNode(Position pos, MapWayFilter filter) {
		MapNode node = pos.selectNearest(nodes.values(), filter);
		return node;
	}
	
	/** Reduces the level of detail by selecting some of the given nodes. */
	@Override
	public List<MapNode> getWayNodes(MapWay way, float scale) {
		List<MapNode> wayNodes = way.getNodes();
		int zoomLevel;
		if (scale <= 1f / 10000000)
			zoomLevel = 3;
		else if (scale <= 1f / 750000)
			zoomLevel = 2;
		else if (scale <= 1f / 350000)
			zoomLevel = 1;
		else
			zoomLevel = 0;
		if (zoomLevel > 0) {
			int size = wayNodes.size();
			List<MapNode> result = new ArrayList<MapNode>(size / zoomLevel + 2);
			int i = 0;
			for (MapNode node : wayNodes) {
				if (i == 0 || i == size - 1
						|| node.getId() % (4 * zoomLevel) == 0)
					result.add(node);
				i++;
			}
			if (wayNodes.get(0) == wayNodes.get(size - 1) && result.size() < 4)
				result.clear();
			return result;
		} else {
			return Collections.unmodifiableList(wayNodes);
		}
	}

	/** {@inheritDoc} */
	@Override
	public int getPoiCount() {
		return pois.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<MapNode> getPois(BoundingBox bb) {
		// not really efficient but ok for small maps...
		List<MapNode> result = new ArrayList<MapNode>();
		for (MapNode poi : pois)
			if (bb.isInside(poi.getLat(), poi.getLon()))
				result.add(poi);
		return result;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<MapNode> getPlaces(String name) {
		String pattern = name.toLowerCase();
		List<MapNode> results = new ArrayList<MapNode>();
		for (MapNode node : nodes.values()) {
			if (node.getAttributeValue("place") != null
					&& node.getName() != null
					&& node.getName().toLowerCase().startsWith(pattern))
				results.add(node);
		}
		return results;
	}

	/** {@inheritDoc} */
	@Override
	public void clearMarksAndTracks() {
		marks.clear();
		tracks.clear();
		fireMapDataEvent(new MapDataEvent(this, MapDataEvent.Type.MAP_MODIFIED));
	}

	/** {@inheritDoc} */
	@Override
	public List<MapEntity> getVisibleMarksAndTracks(float scale) {
		List<MapEntity> result = new ArrayList<MapEntity>();
		for (MapNode mark : marks)
			if (mark.getViewInfo() != null
					&& mark.getViewInfo().getMaxVisibleScale() <= scale)
				result.add(mark);
		for (Track track : tracks)
			if (track.getViewInfo() != null
					&& track.getViewInfo().getMaxVisibleScale() <= scale)
				result.add(track);
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public MapNode addMark(float lat, float lon) {
		long id = 1;
		for (MapNode node : marks)
			if (node.getId() >= id)
				id = node.getId() + 1;
		MapNode node = MapDataFactory.instance().createMapNode(id);
		node.setLat(lat);
		node.setLon(lon);
		List<EntityAttribute> atts = new ArrayList<EntityAttribute>(1);
		atts.add(new EntityAttribute("mark", "yes"));
		node.setAttributes(atts);
		node.setName(Long.toString(id));
		updateEntityViewInfo(node, false);
		marks.add(node);
		fireMapDataEvent(new MapDataEvent(this, MapDataEvent.Type.MARK_ADDED,
				node.getId()));
		return node;
	}

	/** {@inheritDoc} */
	@Override
	public void removeMark(MapNode mark) {
		marks.remove(mark);
		fireMapDataEvent(new MapDataEvent(this, MapDataEvent.Type.MARK_REMOVED,
				mark.getId()));
	}

	/** {@inheritDoc} */
	@Override
	public List<MapNode> getMarks() {
		return marks;
	}

	/** {@inheritDoc} */
	@Override
	public boolean isMarked(double lat, double lon) {
		for (MapNode node : marks)
			if (node.getLat() == lat && node.getLon() == lon)
				return true;
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public void clearTrack(String trackName) {
		Track track = getTrack(trackName);
		if (track != null) {
			tracks.remove(track);
			fireMapDataEvent(new MapDataEvent(this,
					MapDataEvent.Type.MAP_MODIFIED));
		}
	}

	/** {@inheritDoc} */
	@Override
	public void createTrack(String trackName, List<Position> positions) {
		clearTrack(trackName);
		Track track = MapDataFactory.instance().createTrack(nextTrackId++,
				trackName, trackName);
		updateEntityViewInfo(track, false);
		tracks.add(track);
		for (Position pos : positions)
			track.addNode(pos);
		fireMapDataEvent(new MapDataEvent(this,
				MapDataEvent.Type.TRACK_MODIFIED, track.getId()));
	}

	/** {@inheritDoc} */
	@Override
	public void addToTrack(String trackName, Position pos) {
		Track track = getTrack(trackName);
		if (track == null) {
			track = MapDataFactory.instance().createTrack(nextTrackId++,
					trackName, trackName);
			updateEntityViewInfo(track, false);
			tracks.add(track);
		}
		track.addNode(pos);
		fireMapDataEvent(new MapDataEvent(this,
				MapDataEvent.Type.TRACK_MODIFIED, track.getId()));
	}

	/** {@inheritDoc} */
	@Override
	public List<Track> getTracks() {
		return tracks;
	}

	/** {@inheritDoc} */
	@Override
	public Track getTrack(long trackId) {
		for (Track track : tracks)
			if (track.getId() == trackId)
				return track;
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public Track getTrack(String trackName) {
		for (Track track : tracks)
			if (track.getName().equals(trackName))
				return track;
		return null;
	}

	/** {@inheritDoc} */
	@Override
	public void visitEntities(EntityVisitor visitor, BoundingBox vbox,
			float scale) {
		if (entityTree != null)
			entityTree.visitEntities(visitor, vbox, scale);
	}

	/** Returns a kd-tree with all entities. */
	public KDTree getEntityTree() {
		return entityTree;
	}

	public EntityFinder getEntityFinder() {
		EntityFinder result = new DefaultEntityFinder(this);
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public void addMapDataEventListener(MapDataEventListener listener) {
		listeners.add(listener);
	}

	/** {@inheritDoc} */
	@Override
	public void removeMapDataEventListener(MapDataEventListener listener) {
		listeners.remove(listener);
	}

	/** {@inheritDoc} */
	@Override
	public void fireMapDataEvent(MapDataEvent event) {
		for (MapDataEventListener listener : listeners)
			listener.eventHappened(event);
	}
}