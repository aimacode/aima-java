package aimax.osm.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Logger;

import aimax.osm.data.entities.EntityAttribute;
import aimax.osm.data.entities.EntityViewInfo;
import aimax.osm.data.entities.MapEntity;
import aimax.osm.data.entities.MapNode;
import aimax.osm.data.entities.MapWay;
import aimax.osm.data.entities.Track;
import aimax.osm.data.entities.WayRef;


/**
 * Central container for OSM map data. It is responsible for storing loaded
 * map data and also for data preparation to support efficient routing and
 * map viewing. Data preparation is based on two fundamental tree structures:
 * 
 * <p>The first is an entity classifier. It is used to attach viewing information
 * to map entities based on attribute value checks. Renderers can store whatever
 * they need in those view information objects. However, the details are not visible
 * to this layer. The data store only sees the minimal scale in which
 * the entity shall be visible.</p>
 * 
 * <p>The second is a kd-tree (see {@link aimax.osm.data.KDTree}).</p>
 * 
 * <p>The map data store is used as model for the viewer.</p>
 * @author R. Lunde
 */
public class MapDataStore implements MapDataConsumer {
	private Logger LOG = Logger.getLogger("aimax.osm");
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
	/** Maintains marks (not part of the original map). */
	private ArrayList<MapNode> marks;
	/** Maintains tracks (not part of the original map). */
	private ArrayList<Track> tracks;
	private long nextTrackId;
	
	private BoundingBox boundingBox;
	private EntityClassifier<EntityViewInfo> entityClassifier;
	private KDTree entityTree;

	private ArrayList<MapDataEventListener> listeners;
	
	public MapDataStore() {
		nodes = new Hashtable<Long, MapNode>();
		ways = new Hashtable<Long, MapWay>();
		pois = new ArrayList<MapNode>();
		marks = new ArrayList<MapNode>();
		tracks = new ArrayList<Track>();
		listeners = new ArrayList<MapDataEventListener>();
		//EntityAttributeManager.instance().ignoreAttKeys(new String[]{
		//"created_by", "source", "history", "copyright", "fire_hydrant"}, true);
		EntityAttributeManager.instance().ignoreAttKeys(new String[]{
		"created_by", "source", "history", "copyright"}, false);
	}
	
	/** Resets everything. No data available after this reset. */
	public void clearAll() {
		EntityAttributeManager.instance().clearHash();
		nodes.clear();
		ways.clear();
		pois.clear();
		marks.clear();
		tracks.clear();
		entityTree = null;
		boundingBox = null;
		fireMapDataEvent(new MapDataEvent
				(this, MapDataEvent.Type.MAP_CLEARED));
	}
	
	/** Resets only mark and track information. */
	public void clearMarksAndTracks() {
		marks.clear();
		tracks.clear();
		fireMapDataEvent(new MapDataEvent
				(this, MapDataEvent.Type.MAP_MODIFIED));
	}
	
	/** Removes the specified track. */
	public void clearTrack(String trackName) {
		Track track = getTrack(trackName);
		if (track != null) {
			tracks.remove(track);
			fireMapDataEvent(new MapDataEvent
					(this, MapDataEvent.Type.MAP_MODIFIED));
		}
	}
	
	/** Checks whether data is available. */
	public boolean isEmpty() {
		return nodes.isEmpty() && ways.isEmpty() && pois.isEmpty()
		&& marks.isEmpty() && tracks.isEmpty();
	}
	
	/**
	 * Adds a new map node (way node as well as point of interest)
	 * to the container.
	 */
	public void addNode(MapNode node) {
		if (node.getAttributeValue("mark") != null) {
			addMark(node.getLat(), node.getLon());
		} else {
			nodes.put(node.getId(), node);
			if (nodes.size() % 500000 == 0)
				LOG.fine("Nodes: " + nodes.size());
		}
	}

	/** Adds a new map way to the container. */
	public void addWay(MapWay way) {
		ways.put(way.getId(), way);
		if (ways.size() % 50000 == 0)
			LOG.fine("Ways: " + ways.size());
	}
	
	/** Adds a new mark at the specified position. */
	public void addMark(float lat, float lon) {
		long id = 1;
		for (MapNode node : marks)
			if (node.getId() >= id)
				id = node.getId()+1;
		MapNode node = new MapNode(id, lat, lon);
		List<EntityAttribute> atts = new ArrayList<EntityAttribute>(1);
		atts.add(new EntityAttribute("mark", "yes"));
		node.setAttributes(atts);
		node.setName(Long.toString(id));
		updateEntityViewInfo(node, false);
		marks.add(node);
		fireMapDataEvent(new MapDataEvent
				(this, MapDataEvent.Type.MARK_ADDED, node.getId()));
	}
	
	/**
	 * Creates a new track and adds it to the list of tracks. Possibly
	 * exiting tracks with the same name are removed.
	 */
	public void createTrack(String trackName, List<Position> positions) {
		clearTrack(trackName);
		Track track  =  new Track(nextTrackId++, trackName, trackName);
		updateEntityViewInfo(track, false);
		tracks.add(track);
		for (Position pos : positions)
			track.addTrkPt(pos);
		fireMapDataEvent(new MapDataEvent
				(this, MapDataEvent.Type.TRACK_MODIFIED, track.getId()));
	}
	
	/**
	 * Adds a new point at the end of a specified track.
	 * If a track with the specified name does not exist, a new track
	 * is created. 
	 */
	public void addToTrack(String trackName, Position pos) {
		Track track = getTrack(trackName);
		if (track == null) {
			track  =  new Track(nextTrackId++, trackName, trackName);
			updateEntityViewInfo(track, false);
			tracks.add(track);
		}
		track.addTrkPt(pos);
		fireMapDataEvent(new MapDataEvent
				(this, MapDataEvent.Type.TRACK_MODIFIED, track.getId()));
	}
	
	/**
	 * Provides the data store with an entity classifier. The classifier strongly
	 * influences the generation of the entity tree.
	 */
	public void setEntityClassifier(EntityClassifier<EntityViewInfo> classifier) {
		entityClassifier = classifier;
		if (entityTree != null) {
			applyClassifierAndUpdateTree();
			fireMapDataEvent(new MapDataEvent(this, MapDataEvent.Type.MAP_MODIFIED));
		}
	}
	
	/**
	 * Separates way nodes from points of interests, cleans
	 * up useless garbage and creates a kd-tree for the
	 * remaining entities. Always call this method before
	 * using using the container for viewing.  
	 */
	public void compileData() {
		ArrayList<Long> toDelete = new ArrayList<Long>();
		for (MapNode node : nodes.values()) {
			if (node.getWayRefs().isEmpty())
				toDelete.add(node.getId());
			if (node.getName() != null || node.getAttributes().length > 0)
				pois.add(node);
		}
		for (long id : toDelete) {
			nodes.remove(id);
		}
		LOG.fine("Ways: " + ways.size());
		LOG.fine("WayNodes: " + nodes.size());
		LOG.fine("POIs: " + pois.size());
			
		boundingBox = new BoundingBox();
		boundingBox.adjust(nodes.values());
		boundingBox.adjust(pois);
		applyClassifierAndUpdateTree();
		fireMapDataEvent(new MapDataEvent(this, MapDataEvent.Type.MAP_NEW));
	}
	
	/**
	 * Applies the current entity classifier to all currently maintained map
	 * entities and creates a new entity tree with all relevant ways and
	 * points of interest.
	 */
	protected void applyClassifierAndUpdateTree() {
		entityTree = new KDTree(boundingBox, 8000, 60);
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
	 * Updates the view information of a given entity by means of the
	 * current entity classifier. If suitable viewing information was found and
	 * <code>addToTree</code> is true, the entity is added to the entity tree.
	 */
	private void updateEntityViewInfo(MapEntity entity, boolean addToTree) {
		EntityViewInfo info = null;
		if (entityClassifier != null)
			info = entityClassifier.classify(entity);
		entity.setViewInfo(info);
		if (addToTree && info != null)
			entityTree.insertEntity(entity);
	}
	
	/** Looks up a way for a given id (fast). */
	public MapWay getWay(long id) {
		return ways.get(id);
	}
	
	/** Returns all map ways. */
	public Collection<MapWay> getWays() {
		return ways.values();
	}
	
	/**
	 * Returns a map node for the given id. Points of interest
	 * are ignored here.
	 */
	public MapNode getWayNode(long id) {
		return nodes.get(id);
	}
	
	/** Returns all nodes, which are part of at least one way. */
	public Collection<MapNode> getWayNodes() {
		return nodes.values();
	}
	
	/** Returns all points of interest. */
	public List<MapNode> getPOIs() {
		return pois;
	}
	
	/** Returns all maintained marks. */
	public List<MapNode> getMarks() {
		return marks;
	}
	
	/** Returns all maintained tracks. */
	public List<Track> getTracks() {
		return tracks;
	}
	
	/** Checks whether a mark is set at the specified position. */
	public boolean isMarked(double lat, double lon) {
		for (MapNode node : marks)
			if (node.getLat() == lat && node.getLon() == lon)
				return true;
		return false;
	}
	
	/** Returns the track with the specified id. */
	public Track getTrack(long trackId) {
		for (Track track : tracks)
			if (track.getId() == trackId)
				return track;
		return null;
	}
	
	/** Returns the track with the specified name. */
	public Track getTrack(String trackName) {
		for (Track track : tracks)
			if (track.getName().equals(trackName))
				return track;
		return null;
	}
	
	/** Returns all marks and tracks, which are visible in the specified scale. */
	public List<MapEntity> getVisibleMarksAndTracks(float scale) {
		List<MapEntity> result = new ArrayList<MapEntity>();
		for (MapNode mark : marks)
			if (mark.getViewInfo() != null
					&& mark.getViewInfo().getMinVisibleScale() <= scale)
				result.add(mark);
		for (Track track : tracks)
			if (track.getViewInfo() != null
					&& track.getViewInfo().getMinVisibleScale() <= scale)
				result.add(track);
		return result;
	}
	
	/** Returns a kd-tree with all entities. */
	public KDTree getEntityTree() {
		return entityTree;
	}

	/**
	 * Returns a bounding box for all entities,
	 * which are maintained in this container.
	 */
	public BoundingBox getBoundingBox() {
		return boundingBox;
	}
	
	/**
	 * Returns the corresponding way, if both nodes are part of the
	 * same way, otherwise returns null.
	 */
	public MapWay getWay(MapNode node1, MapNode node2) {
		for (WayRef wr1 : node1.getWayRefs()) {
			for (WayRef wr2 : node2.getWayRefs()) {
				if (wr1.getWay() == wr2.getWay()) {
					return wr1.getWay();
				}
			}
		}
		return null;
	}
	
	/**
	 * Searches for a node containing <code>namepart</code> in its name and returns it.
	 * Nodes with name equal to <code>namepart</code> are preferred. If a position is
	 * given, nodes in the vicinity of position are preferred. 
	 * @param pattern Search pattern.
	 * @param position Possibly null.
	 * @return A node or null.
	 */
	public MapNode findNode(String pattern, Position position,
			boolean includePOIs, boolean includeWayNodes,
			boolean excludeMarked) {
		MapNode result = null;
		ArrayList<MapNode> foundNodes = new ArrayList<MapNode>();
		BestMatchFinder bmf = new BestMatchFinder(pattern);
		if (includePOIs) {
			for (MapNode node : pois) {
				int match = bmf.checkMatchQuality(node);
				if (match >= 0) {
					if (!excludeMarked || !isMarked(node.getLat(), node.getLon())) {
						if (match > 0) {
							foundNodes.clear();
							bmf.useAsReference(node);
						}
						foundNodes.add(node);
					}
				}
			}
		}
		if (includeWayNodes) {
			for (MapWay way : ways.values()) {
				int match = bmf.checkMatchQuality(way);
				if (match >= 0) {
					MapNode node = way.getNodes().get(0);
					if (!excludeMarked || !isMarked(node.getLat(), node.getLon())) {
						if (match > 0) {
							foundNodes.clear();
							bmf.useAsReference(way);
						}
						foundNodes.add(node);
					}
				}
			}
		}
		if (!foundNodes.isEmpty()) {
			if (position != null)
				result = position.selectNearest(foundNodes, null);
			else
				result = foundNodes.get(0);
		}
		return result;
	}
	
	public void addMapDataEventListener(MapDataEventListener listener) {
		listeners.add(listener);
	}
	
	public void removeMapDataEventListener(MapDataEventListener listener) {
		listeners.remove(listener);
	}
	
	/** Informs all interested listeners about map changes. */
	public void fireMapDataEvent(MapDataEvent event) {
		for (MapDataEventListener listener : listeners)
			listener.eventHappened(event);
	}
	
	
	/**
	 * Helper class which is used to find the best match when searching for
	 * special entities. 
	 * @author R. Lunde
	 *
	 */
	private static class BestMatchFinder {
		/** String, specifying the entity to be searched for. */
		String searchPattern;
		/**
		 * Value between 1 and 5 which classifies the reference match level.
		 * 1: reference entity name equal to pattern;
		 * 2: reference entity has attribute value which is identical with the pattern;
		 * 3: reference entity has attribute name equal to pattern;
		 * 4: reference entity name contains pattern;
		 * 5: no match found.
		 */
		int currMatchLevel = 5;
		/** contains the render data of the reference entity. */
		EntityViewInfo currViewInfo = null;
		
		/** Creates a match finder for a given search pattern. */
		protected BestMatchFinder(String pattern) {
			searchPattern = pattern;
		}
		
		/**
		 * Compares whether a given entity matches the search pattern better
		 * or worse than the previously defined reference entity.
		 * @return Match level (-1: forget new entity,
		 *                       0: new entity match quality equal to reference entity,
		 *                       1: new entity matches better then reference entity)
		 */
		protected int checkMatchQuality(MapEntity entity) {
			int matchLevel = getMatchLevel(entity);
			int result = -1;
			if (matchLevel < 5) {
				if (matchLevel < currMatchLevel)
					result = 1;
				else if (matchLevel == currMatchLevel) {
					if (matchLevel == 1 || matchLevel == 4)
						result = compareRenderData(entity.getViewInfo());
					else
						result = 0;
				}
			}
			return result;
		}
		
		/** Defines a new reference entity for search pattern match checks. */
		protected void useAsReference(MapEntity entity) {
			currMatchLevel = getMatchLevel(entity);
			currViewInfo = entity.getViewInfo();
		}
		
		private int getMatchLevel(MapEntity entity) {
			String name = entity.getName();
			if (name != null && entity.getName().equals(searchPattern))
					return 1;
			if (currMatchLevel >= 2) {
				for (EntityAttribute att : entity.getAttributes())
					if (att.getValue().equals(searchPattern))
						return 2;
			}
			if (currMatchLevel >= 3) {
				for (EntityAttribute att : entity.getAttributes())
					if (att.getKey().equals(searchPattern))
						return 3;
			}
			if (name != null && currMatchLevel >= 4 && entity.getName().contains(searchPattern))
				return 4;
			return 5;
		}
		
		/** Prefers entities which are already visible in small scale. */
		private int compareRenderData(EntityViewInfo newData) {
			if (currViewInfo == null)
				if (newData == null)
					return 0;
				else
					return 1;
			else
				if (newData == null)
					return -1;
				else if (newData.getMinVisibleScale() < currViewInfo.getMinVisibleScale())
					return 1;
				else if (newData.getMinVisibleScale() > currViewInfo.getMinVisibleScale())
					return -1;
				else
					return 0;
		}
	}
}
