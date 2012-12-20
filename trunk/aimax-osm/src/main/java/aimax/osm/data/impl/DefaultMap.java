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
import aimax.osm.data.MapBuilder;
import aimax.osm.data.MapEvent;
import aimax.osm.data.MapEventListener;
import aimax.osm.data.MapWayFilter;
import aimax.osm.data.OsmMap;
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
 * viewing. All map data is kept in RAM in this implementation. Data preparation
 * is based on two fundamental tree structures:
 * 
 * <p>
 * The first is an entity classifier. It is used to attach viewing information
 * to map entities based on attribute value checks. Renderers can store whatever
 * they need in those view information objects. However, the details are not
 * visible to this layer. The map only sees the minimal scale in which
 * the entity shall be visible.
 * </p>
 * 
 * <p>
 * The second is a kd-tree (see {@link aimax.osm.data.impl.KDTree}).
 * </p>
 * 
 * <p>
 * The map is used as model for the viewer.
 * </p>
 * 
 * @author Ruediger Lunde
 */
public class DefaultMap implements OsmMap {
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
	/** Maintains markers (not part of the original map). */
	private ArrayList<MapNode> markers;
	/** Maintains tracks (not part of the original map). */
	private ArrayList<Track> tracks;
	private long nextTrackId;

	private EntityClassifier<EntityViewInfo> entityClassifier;
	private KDTree entityTree;

	private ArrayList<MapEventListener> listeners;

	public DefaultMap() {
		nodes = new Hashtable<Long, MapNode>();
		ways = new Hashtable<Long, MapWay>();
		pois = new ArrayList<MapNode>();
		markers = new ArrayList<MapNode>();
		tracks = new ArrayList<Track>();
		listeners = new ArrayList<MapEventListener>();
		// EntityAttributeManager.instance().ignoreAttKeys(new String[]{
		// "created_by", "source", "history", "copyright", "fire_hydrant"},
		// true);
		EntityAttributeManager.instance()
				.ignoreAttKeys(
						new String[] { "created_by", "source", "history",
								"copyright" }, false);
	}

	/** No data available after this reset. */
	protected void clear() {
		EntityAttributeManager.instance().clearHash();
		nodes.clear();
		ways.clear();
		pois.clear();
		markers.clear();
		tracks.clear();
		entityTree = null;
		boundingBox = null;
		fireMapDataEvent(new MapEvent(this, MapEvent.Type.MAP_CLEARED));
	}

	/** {@inheritDoc} */
	@Override
	public boolean isEmpty() {
		return nodes.isEmpty() && ways.isEmpty() && pois.isEmpty()
				&& markers.isEmpty() && tracks.isEmpty();
	}

	/** {@inheritDoc} */
	@Override
	public MapBuilder getBuilder() {
		return new DefaultMapBuilder(this);
	}

	/** Does nothing */
	@Override
	public void close() {
	}

	/**
	 * Provides the data store with an entity classifier. The classifier
	 * strongly influences the generation of the entity tree.
	 */
	@Override
	public void setEntityClassifier(EntityClassifier<EntityViewInfo> classifier) {
		entityClassifier = classifier;
		if (entityTree != null) {
			applyClassifierAndUpdateTree(entityTree.getBoundingBox());
			fireMapDataEvent(new MapEvent(this, MapEvent.Type.MAP_MODIFIED));
		}
	}

	/** {@inheritDoc} */
	@Override
	public BoundingBox getBoundingBox() {
		return boundingBox;
	}

	/** Defines a bounding box for the map. */
	protected void setBoundingBox(BoundingBox bb) {
		boundingBox = bb;
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

	/** Adds a node to the central node hashtable. */
	protected void addNode(DefaultMapNode node) {
		nodes.put(node.getId(), node);
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

	/** Adds a way to the central way hashtable. */
	protected void addWay(DefaultMapWay way) {
		ways.put(way.getId(), way);
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
	 * Separates way nodes from points of interests, cleans up useless garbage
	 * and creates a kd-tree for the remaining entities. Always call this method
	 * before using using the container for viewing.
	 */
	public void compile() {
		ArrayList<Long> toDelete = new ArrayList<Long>();
		for (MapNode node : nodes.values()) {
			if (node.hasPosition()) {
				if (node.getName() != null || node.getAttributes().length > 0)
					pois.add(node);
				else if (node.getWayRefs().isEmpty())
					toDelete.add(node.getId());
			} else {
				LOG.warning("No definition found for referenced node " + node.getId() + ".");
				toDelete.add(node.getId());
			}
		}
		for (long id : toDelete) {
			nodes.remove(id);
		}
		BoundingBox bbAllNodes = new BoundingBox();
		bbAllNodes.adjust(nodes.values());
		bbAllNodes.adjust(pois);
		if (boundingBox == null)
			boundingBox = bbAllNodes;
		else
			boundingBox.intersectWith(bbAllNodes);
		applyClassifierAndUpdateTree(bbAllNodes);
		fireMapDataEvent(new MapEvent(this, MapEvent.Type.MAP_NEW));
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
		for (MapNode marker : markers)
			updateEntityViewInfo(marker, false);
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

	/** {@inheritDoc} */
	@Override
	public List<MapNode> getPois(BoundingBox bb) {
		// not really efficient but ok for small maps...
		List<MapNode> result = new ArrayList<MapNode>();
		for (MapNode poi : pois)
			if (bb.isInside(poi.getLat(), poi.getLon()))
				result.add(poi);
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public List<MapNode> getPlaces(String name) {
		String pattern = name.toLowerCase();
		List<MapNode> results = new ArrayList<MapNode>();
		for (MapNode node : pois) {
			if (node.getAttributeValue("place") != null
					&& node.getName() != null
					&& node.getName().toLowerCase().startsWith(pattern))
				results.add(node);
		}
		return results;
	}

	/** {@inheritDoc} */
	@Override
	public void clearMarkersAndTracks() {
		markers.clear();
		tracks.clear();
		fireMapDataEvent(new MapEvent(this, MapEvent.Type.MAP_MODIFIED));
	}

	/** {@inheritDoc} */
	@Override
	public List<MapEntity> getVisibleMarkersAndTracks(float scale) {
		List<MapEntity> result = new ArrayList<MapEntity>();
		for (MapNode marker : markers)
			if (marker.getViewInfo() != null
					&& marker.getViewInfo().getMinVisibleScale() <= scale)
				result.add(marker);
		for (Track track : tracks)
			if (track.getViewInfo() != null
					&& track.getViewInfo().getMinVisibleScale() <= scale)
				result.add(track);
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public MapNode addMarker(float lat, float lon) {
		long id = 1;
		for (MapNode node : markers)
			if (node.getId() >= id)
				id = node.getId() + 1;
		MapNode node = new DefaultMapNode(id);
		node.setName(Long.toString(id));
		List<EntityAttribute> atts = new ArrayList<EntityAttribute>(1);
		atts.add(new EntityAttribute("marker", "yes"));
		node.setAttributes(atts);
		node.setPosition(lat, lon);
		updateEntityViewInfo(node, false);
		markers.add(node);
		fireMapDataEvent(new MapEvent(this, MapEvent.Type.MARKER_ADDED, node
				.getId()));
		return node;
	}

	/** {@inheritDoc} */
	@Override
	public void removeMarker(MapNode marker) {
		markers.remove(marker);
		fireMapDataEvent(new MapEvent(this, MapEvent.Type.MARKER_REMOVED,
				marker.getId()));
	}

	/** {@inheritDoc} */
	@Override
	public List<MapNode> getMarkers() {
		return markers;
	}

	/** {@inheritDoc} */
	@Override
	public void clearTrack(String trackName) {
		Track track = getTrack(trackName);
		if (track != null) {
			tracks.remove(track);
			fireMapDataEvent(new MapEvent(this, MapEvent.Type.MAP_MODIFIED));
		}
	}

	/** {@inheritDoc} */
	@Override
	public void createTrack(String trackName, List<Position> positions) {
		clearTrack(trackName);
		Track track = new DefaultTrack(nextTrackId++, trackName, trackName);
		updateEntityViewInfo(track, false);
		tracks.add(track);
		for (Position pos : positions)
			track.addNode(pos);
		fireMapDataEvent(new MapEvent(this, MapEvent.Type.TRACK_MODIFIED, track
				.getId()));
	}

	/** {@inheritDoc} */
	@Override
	public void addToTrack(String trackName, Position pos) {
		Track track = getTrack(trackName);
		if (track == null) {
			track = new DefaultTrack(nextTrackId++, trackName, trackName);
			updateEntityViewInfo(track, false);
			tracks.add(track);
		}
		track.addNode(pos);
		fireMapDataEvent(new MapEvent(this, MapEvent.Type.TRACK_MODIFIED, track
				.getId()));
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
	public EntityFinder getEntityFinder() {
		EntityFinder result = new DefaultEntityFinder(this);
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public Object[][] getStatistics() {
		Object[][] result = new Object[3][2];
		result[0][0] = "Nodes";
		result[0][1] = nodes.size();
		result[1][0] = "Ways";
		result[1][1] = ways.size();
		result[2][0] = "POIs";
		result[2][1] = pois.size();
		return result;
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

	/** {@inheritDoc} */
	@Override
	public void addMapDataEventListener(MapEventListener listener) {
		listeners.add(listener);
	}

	/** {@inheritDoc} */
	@Override
	public void removeMapDataEventListener(MapEventListener listener) {
		listeners.remove(listener);
	}

	/** {@inheritDoc} */
	@Override
	public void fireMapDataEvent(MapEvent event) {
		for (MapEventListener listener : listeners)
			listener.eventHappened(event);
	}
}