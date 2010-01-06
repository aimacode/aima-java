package aimax.osm.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.logging.Logger;

import aimax.osm.data.entities.EntityAttribute;
import aimax.osm.data.entities.MapEntity;
import aimax.osm.data.entities.MapNode;
import aimax.osm.data.entities.MapWay;
import aimax.osm.data.entities.Track;
import aimax.osm.data.entities.MapNode.WayRef;


/**
 * Central container for OSM map data. It is used as model for the viewer.
 * @author R. Lunde
 *
 */
public class MapDataStore implements MapDataConsumer {
	private Logger LOG = Logger.getLogger("aimax.osm");
	
	private Hashtable<Long, MapNode> nodes = new Hashtable<Long, MapNode>();
	private Hashtable<Long, MapWay> ways = new Hashtable<Long, MapWay>();
	private ArrayList<MapNode> pois = new ArrayList<MapNode>();
	private ArrayList<Track> tracks = new ArrayList<Track>();
	private ArrayList<MapNode> marks = new ArrayList<MapNode>();
	private long nextTrackId = 0;
	
	private BoundingBox boundingBox;
	private KDTree entityTree;

	private ArrayList<MapDataEventListener> listeners = new ArrayList<MapDataEventListener>();
	
	/** Resets everything. No data available after this reset. */
	public void clearAll() {
		EntityAttributeManager.instance().clear();
		EntityAttributeManager.instance().ignoreAttNames(new String[]{
		"created_by", "source", "history", "copyright", "fire_hydrant"}, true);
		nodes.clear();
		ways.clear();
		pois.clear();
		tracks.clear();
		marks.clear();
		entityTree = null;
		boundingBox = null;
		fireMapDataEvent(new MapDataEvent
				(this, MapDataEvent.Type.MAP_CLEARED));
	}
	
	/** Resets only mark and track information. */
	public void clearMarksAndTracks() {
		tracks.clear();
		marks.clear();
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
		&& tracks.isEmpty() && marks.isEmpty();
	}
	
	/**
	 * Adds a new map node (way node as well as point of interest)
	 * to the container.
	 */
	public void addNode(MapNode node) {
		nodes.put(node.getId(), node);
		if (nodes.size() % 500000 == 0)
			LOG.fine("Nodes: " + nodes.size());
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
		List<EntityAttribute> atts = new ArrayList<EntityAttribute>();
		atts.add(new EntityAttribute("mark", "yes"));
		node.setAttributes(atts);
		node.setName(Long.toString(id));
		marks.add(node);
		fireMapDataEvent(new MapDataEvent
				(this, MapDataEvent.Type.MARK_ADDED, node.getId()));
	}
	
	public void addToTrack(String trackName, float lat, float lon) {
		Track track = getTrack(trackName);
		if (track == null) {
			track  =  new Track(nextTrackId++, trackName);
			tracks.add(track);
		}
		MapNode node = new MapNode(-1, lat, lon);
		node.setName("" + (track.getTrkPts().size()+1));
		track.addTrkPt(node);
		fireMapDataEvent(new MapDataEvent
				(this, MapDataEvent.Type.TRACK_MODIFIED, track.getId()));
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
			if (node.getWays().isEmpty())
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
		
		entityTree = new KDTree(boundingBox, 1000, 60);
		for (MapWay way : ways.values())
			entityTree.insertEntity(way);
		
		//System.out.println(entityTree.depth());
		
		for (MapNode poi : pois)
			entityTree.insertEntity(poi);
		fireMapDataEvent(new MapDataEvent
				(this, MapDataEvent.Type.MAP_NEW));
	}
	
	public void clearRenderData() {
		if (entityTree != null)
			entityTree.clearRenderData();
		for (MapNode mark : marks)
			mark.setRenderData(null);
		for (Track track : tracks)
			track.setRenderData(null);
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
	
	/** Returns all maintained tracks. */
	public List<Track> getTracks() {
		return tracks;
	}
	
	public Track getTrack(long trackId) {
		for (Track track : tracks)
			if (track.getId() == trackId)
				return track;
		return null;
	}
	
	public Track getTrack(String trackName) {
		for (Track track : tracks)
			if (track.getName().equals(trackName))
				return track;
		return null;
	}
	
	/** Returns all maintained marks. */
	public List<MapNode> getMarks() {
		return marks;
	}
	
	/** Checks whether a mark is set at the specified position. */
	public boolean isMarked(double lat, double lon) {
		for (MapNode node : marks)
			if (node.getLat() == lat && node.getLon() == lon)
				return true;
		return false;
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
		for (WayRef wr1 : node1.getWays()) {
			for (WayRef wr2 : node2.getWays()) {
				if (wr1.wayId == wr2.wayId) {
					return getWay(wr1.wayId);
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
		int matchLevel = 4;
		if (includePOIs) {
			for (MapNode node : pois) {
				int match = match(node, pattern, matchLevel);
				if (match > 0) {
					if (!excludeMarked || !isMarked(node.getLat(), node.getLon())) {
						if (match < matchLevel) {
							foundNodes.clear();
							matchLevel = match;
						}
						foundNodes.add(node);
					}
				}
			}
		}
		if (includeWayNodes) {
			for (MapWay way : ways.values()) {
				int match = match(way, pattern, matchLevel);
				if (match > 0) {
					MapNode node = (position == null)
					? way.getNodes().get(0) : position.selectNearest(way.getNodes(), null);
					if (!excludeMarked || !isMarked(node.getLat(), node.getLon())) {
						if (match < matchLevel) {
							foundNodes.clear();
							matchLevel = match;
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
	
	/**
	 * Checks whether an entity matches the search pattern.
	 * @param level Maximal match level to be checked. Value between
	 *        1 (name identical) and 4 (has attribute name equal to pattern).
	 * @return match level between 1 and 4 or 0 (no match).
	 */
	private int match(MapEntity entity, String pattern, int level) {
		if (entity.getName() != null) {
			if (entity.getName().equals(pattern))
				return 1;
			else if (level >= 2 && entity.getName().contains(pattern))
				return 2;
		}
		if (level >= 3) {
			for (EntityAttribute att : entity.getAttributes())
				if (att.getValue().equals(pattern))
					return 3;
		}
		if (level >= 4) {
			for (EntityAttribute att : entity.getAttributes())
				if (att.getName().equals(pattern))
					return 4;
		}
		return 0;
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
	
}
